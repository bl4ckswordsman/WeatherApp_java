package com.example.demow4;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "weatherCoordinatesServlet", value = "/weather-coordinates-servlet")
public class WeatherServletWCoord extends WeatherServletBase {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Fetch latitude and longitude from request parameters
        String latitude = request.getParameter("lat");
        String longitude = request.getParameter("lon");

        // Fetch weather data
        WeatherDataFetcher fetcher = new WeatherDataFetcher();
        WeatherData weatherData;
        try {
            weatherData = fetcher.fetchWeatherData(latitude, longitude);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Display weather data
        PrintWriter out = response.getWriter();
        displayWeatherData(weatherData, out);
    }
}
