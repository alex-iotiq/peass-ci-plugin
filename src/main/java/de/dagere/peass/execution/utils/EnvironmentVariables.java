package de.dagere.peass.execution.utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * Try to overwrite Peass' own EnvironmentVariables, so no multiple calls are necessary
 * @author DaGeRe
 *
 */
public class EnvironmentVariables implements Serializable {

   private static final long serialVersionUID = 6287969989033845334L;

   private final Map<String, String> environmentVariables = new TreeMap<>();

   private final String properties;

   public EnvironmentVariables(final String properties) {
      if (properties != null) {
         this.properties = properties;
      } else {
         this.properties = "";
      }
   }

   public EnvironmentVariables() {
      properties = "";
   }

   public Map<String, String> getEnvironmentVariables() {
      return environmentVariables;
   }

   public String getProperties() {
      return properties;
   }

   public String fetchMavenCall(File projectFolder) {
      System.out.println("Using fetchMavenCall from peass-ci EnvironmentVariables");
      if (projectFolder != null) {
         if (!isWindows()) {
            File potentialWrapper = new File(projectFolder, "mvnw");
            if (potentialWrapper.exists() && potentialWrapper.canExecute()) {
               return "./mvnw";
            }
         } else {
            File potentialWrapper = new File(projectFolder, "mvnw.cmd");
            if (potentialWrapper.exists()) {
               try {
                  return potentialWrapper.getCanonicalPath();
               } catch (IOException e) {
                  System.err.println("Was not able to get canonical path");
                  e.printStackTrace();
                  return potentialWrapper.getAbsolutePath();
               }
            }
         }
      }

      return fetchMavenCall();
   }

   private String fetchMavenCall() {
      String mvnCall;
      if (environmentVariables.containsKey("MVN_CMD")) {
         mvnCall = environmentVariables.get("MVN_CMD");
      } else if (!isWindows()) {
         mvnCall = "mvn";
      } else {
         mvnCall = "mvn.cmd";
      }
      return mvnCall;
   }

   public static String fetchMavenCallGeneric() {
      String mvnCall;
      if (!isWindows()) {
         mvnCall = "mvn";
      } else {
         mvnCall = "mvn.cmd";
      }
      return mvnCall;
   }

   public static String fetchGradleCall() {
      String gradleCall;
      if (!isWindows()) {
         gradleCall = "./gradlew";
      } else {
         gradleCall = "gradlew.bat";
      }
      return gradleCall;
   }

   public static String fetchAdbCall() {
      String adbCall;
      adbCall = new File(System.getenv("ANDROID_SDK_ROOT"), "platform-tools/adb").getAbsolutePath();
      return adbCall;
   }

   public static boolean isWindows() {
      return System.getProperty("os.name").startsWith("Windows");
   }

   public static boolean isLinux() {
      return !System.getProperty("os.name").startsWith("Windows") && !System.getProperty("os.name").startsWith("Mac");
   }
}
