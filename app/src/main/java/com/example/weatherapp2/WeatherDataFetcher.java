package com.example.weatherapp2;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherDataFetcher {
    @SuppressLint("StaticFieldLeak")
    public void fetchWeatherData(String latitude, String longitude, WeatherDataCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String location = ReverseGeocoding.getLocationName(latitude, longitude);
                    String url = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=" + latitude + "&lon=" + longitude;

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .header("User-Agent", "YourApp/1.0")
                            .build();

                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
                        JsonObject current = jsonObject.getAsJsonObject("properties").getAsJsonArray("timeseries").get(0).getAsJsonObject().getAsJsonObject("data").getAsJsonObject("instant").getAsJsonObject("details");
                        double temperature = current.get("air_temperature").getAsDouble();
                        JsonObject next1Hour = jsonObject.getAsJsonObject("properties").getAsJsonArray("timeseries").get(0).getAsJsonObject().getAsJsonObject("data").getAsJsonObject("next_1_hours").getAsJsonObject("summary");
                        String weatherCondition = next1Hour.has("symbol_code") ? next1Hour.get("symbol_code").getAsString() : "unknown";
                        WeatherData weatherData = new WeatherData(location, temperature, weatherCondition);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onWeatherDataFetched(weatherData);
                            }
                        });
                    }
                    } catch (IOException e) {
                        e.printStackTrace();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onError();
                            }
                        });
                    }
                }
            });
        }
    }