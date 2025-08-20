Feature: Weather API Testing
  @positive
  Scenario Outline: Get current weather for multiple cities
    Given the Weather API is stubbed with valid responses
    When I request current weather for "<city>"
    Then the response contains weather data for "<city>"
    And the temperature, wind speed, and condition are logged with any discrepancies

    Examples:
      | city      |
      | London    |
      | Paris     |
      | Tokyo     |
      | New York  |

  @negative
  Scenario: Handle missing API key
    Given the Weather API is stubbed to simulate a missing API key
    When I request current weather for "London" without an API key
    Then the response returns error code 1002 with message "API key not provided"

  @negative
  Scenario: Handle invalid location
    Given the Weather API is stubbed to simulate an invalid location
    When I request current weather for "InvalidCity"
    Then the response returns error code 1006 with message "No location found"

  @negative
  Scenario: Handle invalid API key
    Given the Weather API is stubbed to simulate an invalid API key
    When I request current weather for "London" with an invalid API key
    Then the response returns error code 2006 with message "API key provided is invalid"

  @negative
  Scenario: Handle malformed request URL
    Given the Weather API is stubbed to simulate a malformed URL
    When I request current weather with an invalid URL
    Then the response returns error code 1005 with message "API request url is invalid"