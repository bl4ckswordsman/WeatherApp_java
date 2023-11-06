package com.example.demow4;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ReverseGeocoding {
    public static String getLocationName(String latitude, String longitude) throws IOException, InterruptedException {
        String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + latitude + "&lon=" + longitude;
        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("User-Agent", "Java 17 weather app prototype (amra2101)")
            .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(getRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
            String locationName = jsonObject.get("display_name").getAsString();
            return locationName;
        } else {
            throw new IOException("Failed to retrieve location name. Status code: " + response.statusCode());
        }
    }

    public static void main(String[] args) {
        try {
            String latitude = "60.10";
            String longitude = "9.58";
            String locationName = getLocationName(latitude, longitude);
            System.out.println("Location name: " + locationName);
        } catch (IOException | InterruptedException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}