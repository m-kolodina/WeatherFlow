package business;

import model.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherValidator {
    private static final Logger logger = LoggerFactory.getLogger(WeatherValidator.class);
    private boolean validationPassed = true;

    public void validateWeatherData(WeatherResponse response, String city) {
        validationPassed = true;
        validateCity(response, city);
        validateTemperature(response, city);
        validateWindSpeed(response, city);
        validateCondition(response, city);

        if (validationPassed) {
            logger.info("Все значения погоды для города {} соответствуют ожидаемым", city);
        }
    }

    private void validateCity(WeatherResponse response, String expectedCity) {
        String actualCity = response.getLocation().getName();
        if (!actualCity.equals(expectedCity)) {
            logDiscrepancy("Город", actualCity, expectedCity);
        } else {
            logger.info("Город соответствует: {}", actualCity);
        }
    }

    private void validateTemperature(WeatherResponse response, String city) {
        double actualTemp = response.getCurrent().getTemp_c();
        double expectedTemp = getExpectedTemperature(city);
        if (actualTemp != expectedTemp) {
            logDiscrepancy("Температура", actualTemp, expectedTemp);
        } else {
            logger.info("Температура соответствует: {}", actualTemp);
        }
    }

    private void validateWindSpeed(WeatherResponse response, String city) {
        double actualWind = response.getCurrent().getWind_kph();
        double expectedWind = getExpectedWindSpeed(city);
        if (actualWind != expectedWind) {
            logDiscrepancy("Скорость ветра", actualWind, expectedWind);
        } else {
            logger.info("Скорость ветра соответствует: {}", actualWind);
        }
    }

    private void validateCondition(WeatherResponse response, String city) {
        String actualCondition = response.getCurrent().getCondition().getText();
        String expectedCondition = getExpectedCondition(city);
        if (!actualCondition.equals(expectedCondition)) {
            logDiscrepancy("Состояние погоды", actualCondition, expectedCondition);
        } else {
            logger.info("Состояние погоды соответствует: {}", actualCondition);
        }
    }

    private double getExpectedTemperature(String city) {
        switch (city) {
            case "London": return 15.0;
            case "Paris": return 18.0;
            case "Tokyo": return 25.0;
            case "New York": return 20.0;
            default: throw new IllegalArgumentException("Unknown city: " + city);
        }
    }

    private double getExpectedWindSpeed(String city) {
        switch (city) {
            case "London": return 10.0;
            case "Paris": return 12.0;
            case "Tokyo": return 15.0;
            case "New York": return 8.0;
            default: throw new IllegalArgumentException("Unknown city: " + city);
        }
    }

    private String getExpectedCondition(String city) {
        switch (city) {
            case "London": return "Sunny";
            case "Paris": return "Cloudy";
            case "Tokyo": return "Rainy";
            case "New York": return "Clear";
            default: throw new IllegalArgumentException("Unknown city: " + city);
        }
    }

    private void logDiscrepancy(String field, Object actual, Object expected) {
        logger.warn("Расхождение в {}: Ожидалось {}, Фактически {}", field, expected, actual);
        validationPassed = false;
    }
}