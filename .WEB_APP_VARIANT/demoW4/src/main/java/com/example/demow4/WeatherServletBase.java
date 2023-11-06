package com.example.demow4;

import java.io.*;
import jakarta.servlet.http.*;

public class WeatherServletBase extends HttpServlet {
    protected void displayWeatherData(WeatherData weatherData, PrintWriter out) {
        out.println("<html><head>" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\"><body>");
        out.println("<h1>Weather</h1>");
        out.println("<img src=\"" + weatherData.getWeatherConditionImg()
                + "\" alt=\"Weather condition icon\" width=\"200\">");
        out.println("<p>Location: " + weatherData.getLocation() + "</p>");
        out.println("<p>Current temperature: " + weatherData.getTemperature() + "°C</p>");
        out.println("<p>Weather condition: " + weatherData.getWeatherCondition() + "</p>");
        out.println("</body></html>");
    }
}

