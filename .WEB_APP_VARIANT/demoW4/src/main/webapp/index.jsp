<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<h1><%= "Hello World!" %></h1>
<br/>
<a href="weather-servlet">Weather (default location)</a>
<br/>
<a href="https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=60.10&lon=9.58">API test</a>
<br/>
<form action="weather-coordinates-servlet" method="get">
    Latitude: <input type="text" name="lat"><br>
    Longitude: <input type="text" name="lon"><br>
    <input type="submit" value="Weather for input coordinates">
</form>
</body>
</html>