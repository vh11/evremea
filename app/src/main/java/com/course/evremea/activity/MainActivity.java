package com.course.evremea.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.course.evremea.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_location).setOnClickListener(view -> {
            Intent intent = new Intent(this, LocationActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button_coordinates).setOnClickListener(view -> {
            Intent intent = new Intent(this, CoordinatesActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button_settings).setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button_graph_example).setOnClickListener(view -> {
            Intent intent = new Intent(this, GraphExampleActivity.class);
            startActivity(intent);
        });
    }
}