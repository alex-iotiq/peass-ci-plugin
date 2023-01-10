package de.dagere.peass.ci;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import de.dagere.peass.config.MeasurementConfig;
import de.dagere.peass.config.TestSelectionConfig;
import de.dagere.peass.execution.utils.EnvironmentVariables;

public class PeassProcessConfiguration implements Serializable {
   private static final long serialVersionUID = 5858433989302224348L;

   private final boolean updateSnapshotDependencies;
   private final MeasurementConfig measurementConfig;
   private final TestSelectionConfig dependencyConfig;
   private final EnvironmentVariables envVars;
   private final Pattern pattern;

   private final int importLogSizeInMb;

   private final boolean displayRTSLogs;
   private final boolean displayLogs;
   private final boolean displayRCALogs;

   public PeassProcessConfiguration(final boolean updateSnapshotDependencies, final MeasurementConfig measurementConfig, final TestSelectionConfig dependencyConfig,
         final EnvironmentVariables envVars,
         int importLogSizeInMb, final boolean displayRTSLogs, final boolean displayLogs, final boolean displayRCALogs, final Pattern pattern) {
      this.updateSnapshotDependencies = updateSnapshotDependencies;
      this.measurementConfig = measurementConfig;
      this.dependencyConfig = dependencyConfig;
      this.envVars = envVars;
      this.importLogSizeInMb = importLogSizeInMb;
      this.displayRTSLogs = displayRTSLogs;
      this.displayLogs = displayLogs;
      this.displayRCALogs = displayRCALogs;
      this.pattern = pattern;
   }

   public boolean isUpdateSnapshotDependencies() {
      return updateSnapshotDependencies;
   }

   public MeasurementConfig getMeasurementConfig() {
      return measurementConfig;
   }

   public TestSelectionConfig getDependencyConfig() {
      return dependencyConfig;
   }

   public EnvironmentVariables getEnvVars() {
      return envVars;
   }

   public Pattern getPattern() {
      return pattern;
   }

   public int getImportLogSizeInMb() {
      return importLogSizeInMb;
   }

   public boolean isDisplayRTSLogs() {
      return displayRTSLogs;
   }

   public boolean isDisplayLogs() {
      return displayLogs;
   }

   public boolean isDisplayRCALogs() {
      return displayRCALogs;
   }

   public String getFileText(File file) throws IOException {
      String fileData;
      long filesize = file.length() / (1024 * 1024);
      if (filesize < getImportLogSizeInMb()) {
         fileData = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
      } else {
         fileData = "Could not import " + file + " since its size was " + filesize + " MB but only " + getImportLogSizeInMb() + " MB was allowed.";
      }
      return fileData;
   }

   public boolean isAllowedFilesize(File logfile) {
      long filesize = logfile.length() / (1024 * 1024);
      return filesize < getImportLogSizeInMb();
   }
}
