package de.dagere.peass.ci.persistence;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.dagere.peass.analysis.measurement.ProjectStatistics;
import de.dagere.peass.dependency.analysis.data.TestCase;
import de.dagere.peass.dependency.analysis.testData.TestMethodCall;
import de.dagere.peass.measurement.statistics.data.TestcaseStatistic;

public class BuildMeasurementValues {
   private Map<String, TestMeasurementValues> values = new LinkedHashMap<>();

   public Map<String, TestMeasurementValues> getValues() {
      return values;
   }

   public void setValues(final Map<String, TestMeasurementValues> values) {
      this.values = values;
   }

   @JsonIgnore
   public void addMeasurement(final ProjectStatistics currentVersionStatistics, final int buildNumber) {
      for (Entry<String, Map<TestMethodCall, TestcaseStatistic>> version : currentVersionStatistics.getStatistics().entrySet()) {
         for (Map.Entry<TestMethodCall, TestcaseStatistic> testcase : version.getValue().entrySet()) {
            final String testcaseName = testcase.getKey().toString();
            TestMeasurementValues testcasePersistedValues = values.get(testcaseName);
            if (testcasePersistedValues == null) {
               testcasePersistedValues = new TestMeasurementValues();
               values.put(testcaseName, testcasePersistedValues);
            }
            testcasePersistedValues.getStatistics().put(buildNumber, testcase.getValue());
         }
      }
   }
   
}
