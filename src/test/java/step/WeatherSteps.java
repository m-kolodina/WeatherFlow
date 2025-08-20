package step;

import business.WeatherValidator;
import model.ErrorResponse;
import model.WeatherResponse;
import stubs.WeatherApiStubs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WeatherSteps {
    private WeatherApiStubs stubs;
    private final WeatherValidator validator = new WeatherValidator();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Response response;
    private String baseURL;

    @Before
    @Step("Инициализация тестового окружения")
    public void setup() {
        try {
            stubs = new WeatherApiStubs();
            baseURL = "http://localhost:" + stubs.getPort() + "/v1";
            Allure.addAttachment("WireMock", "Запущен WireMock сервер на порту: " + stubs.getPort());
        } catch (Exception e) {
            Allure.addAttachment("Ошибка", "text/plain", "Не удалось запустить WireMock: " + e.getMessage());
            throw new RuntimeException("Failed to start WireMock", e);
        }
    }

    @After
    @Step("Очистка тестового окружения")
    public void teardown() {
        if (stubs != null) {
            stubs.stop();
            Allure.addAttachment("WireMock", "Сервер WireMock остановлен");
        }
    }

    @Given("the Weather API is stubbed with valid responses")
    @Step("Настроены валидные ответы API погоды")
    public void setupPositiveStubs() {
        stubs.setupPositiveStubs();
        Allure.addAttachment("Stubs", "Настроены валидные заглушки для API погоды");
    }

    @Given("the Weather API is stubbed to simulate a missing API key")
    @Step("Настроена заглушка для случая отсутствия API ключа")
    public void setupMissingApiKeyStub() {
        stubs.setupMissingApiKeyStub();
        Allure.addAttachment("Stubs", "Настроена заглушка для ошибки отсутствия API ключа");
    }

    @Given("the Weather API is stubbed to simulate an invalid location")
    @Step("Настроена заглушка для случая неверного местоположения")
    public void setupInvalidLocationStub() {
        stubs.setupInvalidLocationStub();
        Allure.addAttachment("Stubs", "Настроена заглушка для ошибки неверного местоположения");
    }

    @Given("the Weather API is stubbed to simulate an invalid API key")
    @Step("Настроена заглушка для случая неверного API ключа")
    public void setupInvalidApiKeyStub() {
        stubs.setupInvalidApiKeyStub();
        Allure.addAttachment("Stubs", "Настроена заглушка для ошибки неверного API ключа");
    }

    @Given("the Weather API is stubbed to simulate a malformed URL")
    @Step("Настроена заглушка для случая неверного URL")
    public void setupMalformedUrlStub() {
        stubs.setupMalformedUrlStub();
        Allure.addAttachment("Stubs", "Настроена заглушка для ошибки неверного URL");
    }

    @When("I request current weather for {string}")
    @Step("Отправлен запрос текущей погоды для города {city}")
    public void requestWeatherForCity(String city) {
        response = RestAssured.given()
                .baseUri(baseURL)
                .queryParam("key", "9869287f978849e484d70517250308")
                .queryParam("q", city)
                .get("/current.json");

        attachRequestDetails(city);
    }

    @When("I request current weather for {string} without an API key")
    @Step("Отправлен запрос погоды для города {city} без API ключа")
    public void requestWeatherWithoutApiKey(String city) {
        response = RestAssured.given()
                .baseUri(baseURL)
                .queryParam("q", city)
                .get("/current.json");

        attachRequestDetails(city);
    }

    @When("I request current weather for {string} with an invalid API key")
    @Step("Отправлен запрос погоды для города {city} с неверным API ключом")
    public void requestWeatherWithInvalidApiKey(String city) {
        response = RestAssured.given()
                .baseUri(baseURL)
                .queryParam("key", "invalid_key")
                .queryParam("q", city)
                .get("/current.json");

        attachRequestDetails(city);
    }

    @When("I request current weather with an invalid URL")
    @Step("Отправлен запрос по неверному URL")
    public void requestWeatherWithInvalidUrl() {
        response = RestAssured.given()
                .baseUri(baseURL)
                .get("/invalid_endpoint");

        Allure.addAttachment("Request", "GET " + baseURL + "/invalid_endpoint");
    }

    @Then("the response contains weather data for {string}")
    @Step("Ответ содержит данные погоды для города {city}")
    public void verifyWeatherData(String city) {
        WeatherResponse weatherResponse = response.as(WeatherResponse.class);
        attachResponseDetails(weatherResponse);
        assertEquals(city, weatherResponse.getLocation().getName());
    }

    @Then("the temperature, wind speed, and condition are logged with any discrepancies")
    @Step("Проверены температура, скорость ветра и состояние погоды")
    public void logWeatherDiscrepancies() {
        WeatherResponse weatherResponse = response.as(WeatherResponse.class);
        validator.validateWeatherData(weatherResponse, weatherResponse.getLocation().getName());
        attachWeatherDetails(weatherResponse);
    }

    @Then("the response returns error code {int} with message {string}")
    @Step("Ответ содержит код ошибки {expectedCode} и сообщение {expectedMessage}")
    public void verifyErrorResponse(int expectedCode, String expectedMessage) throws IOException {
        ErrorResponse errorResponse = objectMapper.readValue(response.getBody().asString(), ErrorResponse.class);
        attachErrorDetails(errorResponse);
        assertEquals(expectedCode, errorResponse.getError().getCode());
        assertEquals(expectedMessage, errorResponse.getError().getMessage());
    }


    @Step("Прикрепление деталей запроса")
    private void attachRequestDetails(String city) {
        try {
            Map<String, String> requestDetails = new HashMap<>();
            requestDetails.put("URL", baseURL + "/current.json");
            requestDetails.put("City", city);
            requestDetails.put("Method", "GET");
            Allure.addAttachment("Request Details", "application/json",
                    objectMapper.writeValueAsString(requestDetails));
        } catch (IOException e) {
            Allure.addAttachment("Request Error", "text/plain",
                    "Failed to serialize request details: " + e.getMessage());
        }
    }

    @Step("Прикрепление деталей ответа")
    private void attachResponseDetails(WeatherResponse response) {
        try {
            Allure.addAttachment("Response", "application/json",
                    objectMapper.writeValueAsString(response));
        } catch (IOException e) {
            Allure.addAttachment("Response Error", "text/plain",
                    "Failed to serialize response: " + e.getMessage());
        }
    }

    @Step("Прикрепление деталей погоды")
    private void attachWeatherDetails(WeatherResponse weather) {
        try {
            Map<String, Object> weatherDetails = new HashMap<>();
            weatherDetails.put("Temperature (C)", weather.getCurrent().getTemp_c());
            weatherDetails.put("Wind Speed (kph)", weather.getCurrent().getWind_kph());
            weatherDetails.put("Condition", weather.getCurrent().getCondition().getText());
            Allure.addAttachment("Weather Details", "application/json",
                    objectMapper.writeValueAsString(weatherDetails));
        } catch (IOException e) {
            Allure.addAttachment("Weather Error", "text/plain",
                    "Failed to serialize weather details: " + e.getMessage());
        }
    }

    @Step("Прикрепление деталей ошибки")
    private void attachErrorDetails(ErrorResponse error) {
        try {
            Allure.addAttachment("Error Details", "application/json",
                    objectMapper.writeValueAsString(error));
        } catch (IOException e) {
            Allure.addAttachment("Error Serialization", "text/plain",
                    "Failed to serialize error: " + e.getMessage());
        }
    }
}