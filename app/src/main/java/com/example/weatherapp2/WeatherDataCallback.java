package com.example.weatherapp2;

public interface WeatherDataCallback {
    void onWeatherDataFetched(WeatherData weatherData);
    void onError();
}

