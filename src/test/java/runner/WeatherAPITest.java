package runner;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameters({
        @ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "WeatherAPI.step"),
        @ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
                value = "pretty, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm, html:target/cucumber-report/cucumber.html"),
        @ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@positive or @negative")
})
@SuiteDisplayName("Weather API Cucumber Tests")
@ExcludeEngines("junit-jupiter")
public class WeatherAPITest {
}