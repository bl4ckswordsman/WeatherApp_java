package com.example.weatherapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity implements WeatherDataCallback {

    private TextView locationTextView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private ImageView conditionImageView;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        AtomicReference<WeatherDataFetcher> weatherDataFetcher = new AtomicReference<>(new WeatherDataFetcher());
        weatherDataFetcher.get().fetchWeatherData(defaultLatitude, defaultLongitude, this);

        MaterialToolbar toolbar = findViewById(R.id.materialToolbar2);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        // Initialize the SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Set an OnRefreshListener to handle the refresh action
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //String defaultLatitude = "40";
                //String defaultLongitude = "20";
                // Recreate the main activity
                WeatherDataFetcher weatherDataFetcher = new WeatherDataFetcher();
                weatherDataFetcher.fetchWeatherData(defaultLatitude, defaultLongitude, MainActivity.this);
            }
        });

        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> {
            // Recreate the main activity
            weatherDataFetcher.set(new WeatherDataFetcher());
            weatherDataFetcher.get().fetchWeatherData(defaultLatitude, defaultLongitude, MainActivity.this);
        });

        Button getWeatherButton = findViewById(R.id.getWeatherButton);
        getWeatherButton.setOnClickListener(v -> {
            String latitude = ((TextInputEditText) findViewById(R.id.latInput)).getText().toString();
            String longitude = ((TextInputEditText) findViewById(R.id.longInput)).getText().toString();

            // Check if latitude and longitude are not empty before fetching data
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                weatherDataFetcher.set(new WeatherDataFetcher());
                weatherDataFetcher.get().fetchWeatherData(latitude, longitude, MainActivity.this);
            } else {
                Toast.makeText(this, "Please enter latitude and longitude", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onWeatherDataFetched(WeatherData weatherData) {
        if (weatherData != null) {
            locationTextView.setText(weatherData.getLocation());
            temperatureTextView.setText(String.format(Locale.getDefault(), "%.1f°C", weatherData.getTemperature()));
            conditionTextView.setText(weatherData.getWeatherCondition());

            // Load weather condition image using a library like Glide or Picasso
            String imageUrl = weatherData.getWeatherConditionImg();

            // Using Glide for image loading
            Glide.with(this)
                    .asBitmap() // Treat the resource as a static image
                    .load(imageUrl)
                    .into(conditionImageView);
            // Stop the swipe refresh animation
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, "Weather data fetched!", Toast.LENGTH_SHORT).show();
        } else {
            // Handle error case if needed
        }
    }

    @Override
    public void onError() {
        // Handle error case if needed
        swipeRefreshLayout.setRefreshing(false);
    }
}
