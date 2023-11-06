package com.example.demow4;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class WeatherDataFetcher {
    public WeatherData fetchWeatherData(String latitude, String longitude) throws IOException, InterruptedException {
        String location = ReverseGeocoding.getLocationName(latitude, longitude);
        String url = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=" + latitude + "&lon=" + longitude;
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(getRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
            JsonObject current = jsonObject.getAsJsonObject("properties").getAsJsonArray("timeseries").get(0).getAsJsonObject().getAsJsonObject("data").getAsJsonObject("instant").getAsJsonObject("details");
            double temperature = current.get("air_temperature").getAsDouble();
            JsonObject next1Hour = jsonObject.getAsJsonObject("properties").getAsJsonArray("timeseries").get(0).getAsJsonObject().getAsJsonObject("data").getAsJsonObject("next_1_hours").getAsJsonObject("summary");
            String weatherCondition = next1Hour.has("symbol_code") ? next1Hour.get("symbol_code").getAsString() : "unknown";
            return new WeatherData(location, temperature, weatherCondition);
        } else {
            throw new IOException("Failed to retrieve JSON data. Status code: " + response.statusCode());
        }
    }
}