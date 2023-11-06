package com.example.weatherapp2;

public class WeatherData {
    private final String location;
    private final double temperature;
    private final String weatherCondition;

    private final String weatherConditionImg;

    public WeatherData(String location, double temperature, String weatherCondition) {
        String imgFormat = "png"; // "png", "svg", "pdf"
        String weatherIconsRepo = "https://raw.githubusercontent.com/metno/weathericons/main/weather/"
                + imgFormat;

        this.location = location;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.weatherConditionImg = weatherIconsRepo + "/" + weatherCondition + "." + imgFormat;
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