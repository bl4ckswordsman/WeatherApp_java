package com.example.weatherapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextView locationTextView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private ImageView conditionImageView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationTextView = findViewById(R.id.locationTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        conditionTextView = findViewById(R.id.conditionTextView);
        conditionImageView = findViewById(R.id.conditionImageView);

        String defaultLatitude = "62.392899";
        String defaultLongitude = "17.285322";

        fetchWeatherData(defaultLatitude, defaultLongitude);

        MaterialToolbar toolbar = findViewById(R.id.materialToolbar2);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(() -> fetchWeatherData(defaultLatitude, defaultLongitude));

        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> fetchWeatherData(defaultLatitude, defaultLongitude));

        Button getWeatherButton = findViewById(R.id.getWeatherButton);
        getWeatherButton.setOnClickListener(v -> {
            String latitude = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.latInput)).getText()).toString();
            String longitude = Objects.requireNonNull(((TextInputEditText) findViewById(R.id.longInput)).getText()).toString();

            if (!latitude.isEmpty() && !longitude.isEmpty()) {
                fetchWeatherData(latitude, longitude);
            } else {
                Toast.makeText(this, "Please enter latitude and longitude", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchWeatherData(String latitude, String longitude) {
        executorService.execute(() -> {
            try {
                WeatherData weatherData = new WeatherData().fetch(latitude, longitude);
                handler.post(() -> updateUI(weatherData));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateUI(WeatherData weatherData) {
        if (weatherData != null) {
            locationTextView.setText(weatherData.getLocation());
            temperatureTextView.setText(String.format(Locale.getDefault(), "%.1f°C", weatherData.getTemperature()));
            conditionTextView.setText(weatherData.getWeatherCondition());

            Glide.with(this)
                    .asBitmap()
                    .load(weatherData.getWeatherConditionImg())
                    .into(conditionImageView);

            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, "Weather data fetched!", Toast.LENGTH_SHORT).show();
        }
    }
}