package io.femo.bukkit.deploy;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.Properties;

/**
 * Created by felix on 6/28/15.
 */
public class ServerInstaller {

    private static final String[] filesToDelete = new String[] {
            "apache-maven",
            "BuildData",
            "Bukkit",
            "CraftBukkit",
            "Spigot",
            "work",
            "BuildTools.log.txt"
    };

    public static void install(File serverPath, File tmpPath, Log log, String rconPwd) {
        URL buildUrl;
        log.info("Fetching build tools");
        try {
            String response = Jsoup.connect("https://hub.spigotmc.org/jenkins/job/BuildTools/api/json?pretty=false")
                    .ignoreContentType(true).method(Connection.Method.GET).execute().body();
            JsonObject info = new JsonParser().parse(response).getAsJsonObject();
            String baseUrl = info.get("lastStableBuild").getAsJsonObject().get("url").getAsString();
            response = Jsoup.connect(baseUrl + "api/json?pretty=false").ignoreContentType(true)
                    .method(Connection.Method.GET).execute().body();
            JsonArray artifacts = new JsonParser().parse(response).getAsJsonObject().getAsJsonArray("artifacts");
            buildUrl = new URL(baseUrl + "artifact/" + artifacts.get(0).getAsJsonObject().get("relativePath").getAsString());
        } catch (IOException e) {
            log.error("Could not fetch server information", e);
            return;
        }
        try {
            if(!tmpPath.exists()) {
                tmpPath.mkdirs();
            }
            URLConnection urlConnection = buildUrl.openConnection();
            FileOutputStream fileOutputStream = new FileOutputStream(new File(tmpPath, "build_tools.jar"));
            IOUtil.copy(urlConnection.getInputStream(), fileOutputStream);
            fileOutputStream.close();
            urlConnection.getInputStream().close();
        } catch (IOException e) {
            log.error("Error while loading server", e);
            return;
        }
        log.info("Installing craftbukkit and spigot server");
        try {
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new File(tmpPath, "build_tools.jar").toURI().toURL()});
            Class<?> builder = urlClassLoader.loadClass("org.spigotmc.builder.Builder");
            Method main = builder.getDeclaredMethod("main", String[].class);
            main.invoke(null, new Object[]{new String[]{}});
            log.info("Install success. Performing clean up");
            if(!serverPath.exists()) {
                serverPath.mkdirs();
            }
            Properties properties = new Properties();
            properties.setProperty("eula", "true");
            properties.store(new FileOutputStream(new File(serverPath, "eula.txt")), "");
            Properties server = new Properties();
            server.setProperty("enable-query", "true");
            server.setProperty("enable-rcon", "true");
            server.setProperty("rcon.password", rconPwd);
            server.setProperty("enable-command-block", "true");
            server.store(new FileOutputStream(new File(serverPath, "server.properties")), "");
            for(File file : new File(System.getProperty("user.dir")).listFiles()) {
                if(file.getName().startsWith("craftbukkit")) {
                    FileUtils.copyFile(file, new File(serverPath, "craftbukkit.jar"));
                    file.delete();
                } else if (file.getName().startsWith("spigot")) {
                    FileUtils.copyFile(file, new File(serverPath, "spigot.jar"));
                    file.delete();
                }
                for(String fileName : filesToDelete) {
                    if(file.getName().startsWith(fileName)) {
                        if(file.isDirectory()) {
                            FileUtils.deleteDirectory(file);
                        } else {
                            file.delete();
                        }
                    }
                }
            }
            log.info("Cleaning up temp");
            FileUtils.deleteDirectory(tmpPath);
            log.info("Cleanup completed. Server installed.");
        } catch (Throwable t) {
            log.error("Installation failed", t);
        }
    }
}
