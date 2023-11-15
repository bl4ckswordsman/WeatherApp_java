package com.example.weatherapp2;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    public WeatherData() {
        this.location = "unknown";
        this.temperature = 0;
        this.weatherCondition = "unknown";
        this.weatherConditionImg = "unknown";
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

    public WeatherData fetch(String latitude, String longitude) throws IOException {
                    String location = ReverseGeocoding.getLocationName(latitude, longitude);
                    String url = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=" + latitude + "&lon=" + longitude;

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .header("User-Agent", "YourApp/1.0")
                            .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Gson gson = new Gson();
                assert response.body() != null;
                JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
                JsonObject current = jsonObject.getAsJsonObject("properties")
                        .getAsJsonArray("timeseries").get(0).getAsJsonObject()
                        .getAsJsonObject("data")
                        .getAsJsonObject("instant").
                        getAsJsonObject("details");
                double temperature = current.get("air_temperature").getAsDouble();
                JsonObject next1Hour = jsonObject.getAsJsonObject("properties")
                        .getAsJsonArray("timeseries").get(0).getAsJsonObject()
                        .getAsJsonObject("data")
                        .getAsJsonObject("next_1_hours")
                        .getAsJsonObject("summary");
                String weatherCondition = next1Hour.has("symbol_code")
                        ? next1Hour.get("symbol_code").getAsString() : "unknown";
                return new WeatherData(location, temperature, weatherCondition);
            }
        }

        return WeatherData.this;
    }
}