package de.peass.ci;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.dagere.peass.analysis.measurement.ProjectStatistics;
import de.dagere.peass.dependency.analysis.data.TestCase;
import de.dagere.peass.dependency.analysis.data.deserializer.TestcaseKeyDeserializer;
import de.dagere.peass.dependency.analysis.testData.TestMethodCall;
import de.dagere.peass.measurement.statistics.data.TestcaseStatistic;
import de.dagere.peass.utils.Constants;

public class TestStatisticsReading {

   @Test
   public void testTestcaseNaming() throws JsonParseException, JsonMappingException, IOException {
      File statisticsFile = new File("src/test/resources/statistics.json");

      Constants.OBJECTMAPPER.registerModules(new SimpleModule().addKeyDeserializer(TestCase.class, new TestcaseKeyDeserializer()));
      
      ProjectStatistics statistics = Constants.OBJECTMAPPER.readValue(statisticsFile, ProjectStatistics.class);

      Map<TestMethodCall, TestcaseStatistic> testcase = statistics.getStatistics().values().iterator().next();

      for (TestMethodCall test : testcase.keySet()) {
         MatcherAssert.assertThat(test.getClazz(), Matchers.not(Matchers.containsString(" ")));
      }
   }
}
