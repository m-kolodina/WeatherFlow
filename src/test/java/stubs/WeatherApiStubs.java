package stubs;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WeatherApiStubs {
    private final WireMockServer wireMockServer;
    private final int port;

    public WeatherApiStubs() {
        this.wireMockServer = new WireMockServer(options().dynamicPort());
        this.wireMockServer.start();
        this.port = wireMockServer.port();
    }

    public void stop() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    public int getPort() {
        return port;
    }

    public void setupPositiveStubs() {
        // London
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/current.json"))
                .withQueryParam("key", equalTo("9869287f978849e484d70517250308"))
                .withQueryParam("q", equalTo("London"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"location\":{\"name\":\"London\"},\"current\":{\"temp_c\":15.0,\"wind_kph\":10.0,\"condition\":{\"text\":\"Sunny\"}}}")));

        // Paris
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/current.json"))
                .withQueryParam("key", equalTo("9869287f978849e484d70517250308"))
                .withQueryParam("q", equalTo("Paris"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"location\":{\"name\":\"Paris\"},\"current\":{\"temp_c\":18.0,\"wind_kph\":12.0,\"condition\":{\"text\":\"Cloudy\"}}}")));

        // Tokyo
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/current.json"))
                .withQueryParam("key", equalTo("9869287f978849e484d70517250308"))
                .withQueryParam("q", equalTo("Tokyo"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"location\":{\"name\":\"Tokyo\"},\"current\":{\"temp_c\":25.0,\"wind_kph\":15.0,\"condition\":{\"text\":\"Rainy\"}}}")));

        // New York
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/current.json"))
                .withQueryParam("key", equalTo("9869287f978849e484d70517250308"))
                .withQueryParam("q", equalTo("New York"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"location\":{\"name\":\"New York\"},\"current\":{\"temp_c\":20.0,\"wind_kph\":8.0,\"condition\":{\"text\":\"Clear\"}}}")));
    }

    public void setupCityWeather(String city) {
        switch (city) {
            case "London":
                wireMockServer.stubFor(get(urlPathEqualTo("/v1/current.json"))
                        .withQueryParam("q", equalTo(city))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"location\":{\"name\":\"London\"},\"current\":{\"temp_c\":15.0,\"wind_kph\":10.0,\"condition\":{\"text\":\"Sunny\"}}}")));
                break;
            case "Paris":
                wireMockServer.stubFor(get(urlPathEqualTo("/v1/current.json"))
                        .withQueryParam("q", equalTo(city))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"location\":{\"name\":\"Paris\"},\"current\":{\"temp_c\":18.0,\"wind_kph\":12.0,\"condition\":{\"text\":\"Cloudy\"}}}")));
                break;
            case "Tokyo":
                wireMockServer.stubFor(get(urlPathEqualTo("/v1/current.json"))
                        .withQueryParam("q", equalTo(city))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"location\":{\"name\":\"Tokyo\"},\"current\":{\"temp_c\":25.0,\"wind_kph\":15.0,\"condition\":{\"text\":\"Rainy\"}}}")));
                break;
            case "New York":
                wireMockServer.stubFor(get(urlPathEqualTo("/v1/current.json"))
                        .withQueryParam("q", equalTo(city))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"location\":{\"name\":\"New York\"},\"current\":{\"temp_c\":20.0,\"wind_kph\":8.0,\"condition\":{\"text\":\"Clear\"}}}")));
                break;
            default:
                throw new IllegalArgumentException("Unsupported city: " + city);
        }
    }

    public void setupMissingApiKeyStub() {
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/current.json"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":{\"code\":1002,\"message\":\"API key not provided\"}}")));
    }

    public void setupInvalidLocationStub() {
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/current.json"))
                .withQueryParam("key", equalTo("9869287f978849e484d70517250308"))
                .withQueryParam("q", equalTo("InvalidCity"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":{\"code\":1006,\"message\":\"No location found\"}}")));
    }

    public void setupInvalidApiKeyStub() {
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/current.json"))
                .withQueryParam("key", equalTo("invalid_key"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":{\"code\":2006,\"message\":\"API key provided is invalid\"}}")));
    }

    public void setupMalformedUrlStub() {
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/invalid_endpoint"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":{\"code\":1005,\"message\":\"API request url is invalid\"}}")));
    }

    public void setupDisabledApiKeyStub() {
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/current.json"))
                .withQueryParam("key", equalTo("disabled_key"))
                .willReturn(aResponse()
                        .withStatus(403)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":{\"code\":2007,\"message\":\"API key has been disabled\"}}")));
    }

    public void setupExceededQuotaStub() {
        wireMockServer.stubFor(get(urlPathEqualTo("/v1/current.json"))
                .withQueryParam("key", equalTo("exceeded_key"))
                .willReturn(aResponse()
                        .withStatus(403)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":{\"code\":2008,\"message\":\"API quota exceeded\"}}")));
    }
}