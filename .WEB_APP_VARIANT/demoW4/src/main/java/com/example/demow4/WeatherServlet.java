package com.example.demow4;

import java.io.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "weatherServlet", value = "/weather-servlet")
public class WeatherServlet extends WeatherServletBase {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String defaultLatitude = "62.392899";
        String defaultLongitude = "17.285322";
        // Fetch weather data
        WeatherDataFetcher fetcher = new WeatherDataFetcher();
        WeatherData weatherData;
        try {
            weatherData = fetcher.fetchWeatherData(defaultLatitude, defaultLongitude);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Display weather data
        PrintWriter out = response.getWriter();
        displayWeatherData(weatherData, out);
    }
}