package model;

import lombok.Data;

@Data
public class WeatherResponse {
    private Location location;
    private Current current;

    @Data
    public static class Location {
        private String name;
    }

    @Data
    public static class Current {
        private double temp_c;
        private double wind_kph;
        private Condition condition;

        @Data
        public static class Condition {
            private String text;
        }
    }
}