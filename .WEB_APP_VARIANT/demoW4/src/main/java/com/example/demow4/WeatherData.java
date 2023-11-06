package com.example.demow4;

public class WeatherData {
    private final String location;
    private final double temperature;
    private final String weatherCondition;

    private final String weatherConditionImg;

    public WeatherData(String location, double temperature, String weatherCondition) {
        String weatherIconsRepo = "https://raw.githubusercontent.com/metno/weathericons/main/weather/svg";

        this.location = location;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.weatherConditionImg = weatherIconsRepo + "/" + weatherCondition + ".svg";
    }

    // getters and setters
    public String getLocation() {
        return location;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public String getWeatherConditionImg() { return weatherConditionImg; }
}