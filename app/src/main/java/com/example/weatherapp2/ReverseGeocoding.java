package com.example.weatherapp2;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReverseGeocoding {
    public static String getLocationName(String latitude, String longitude) throws IOException {
        String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + latitude + "&lon=" + longitude;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "AndroidWeatherApp prototype/0.1")  // Add your app's user agent
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
                String wantedParam = "name"; // "display_name", "name", "country", "city" etc.
                String locationName;
                if (jsonObject.get(wantedParam) == null) { locationName = "Reverse geocoding failed" +
                        " (parameter not found)"; }
                else { locationName = jsonObject.get(wantedParam).getAsString(); }
                return locationName;
            } else {
                throw new IOException("Failed to retrieve location name. Status code: " + response.code());
            }
        }
    }

    public static void main(String[] args) {
        try {
            String latitude = "60.10";
            String longitude = "9.58";
            String locationName = getLocationName(latitude, longitude);
            System.out.println("Location name: " + locationName);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
