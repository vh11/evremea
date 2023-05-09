package com.course.evremea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private TextView tvHello;
    private Button buttonGo;
    private Button buttonNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvHello = findViewById(R.id.tv_hello);
        buttonGo = findViewById(R.id.button_go);
        buttonNew = findViewById(R.id.button_new);

        buttonGo.setOnClickListener(view -> {
            tvHello.setText("Hello");

            new Thread(() -> {
                try {
                    URLConnection conn = new URL("http://172.19.171.153:4567/api/v1/weather/cities/city/Baia%20Mare").openConnection();

                    byte[] buffer = new byte[256];
                    int count = conn.getInputStream().read(buffer);
                    String response = new String(buffer, 0, count);
                    JSONObject jsonObject = new JSONObject(response);
                    int temperature = jsonObject.getInt("temperature");

                    runOnUiThread(() -> {
                        tvHello.setText("" + temperature);
                    });
                } catch(IOException | JSONException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        buttonNew.setOnClickListener(view -> {
            Intent intent = new Intent(this, ExampleActivity.class);
            startActivity(intent);
        });
    }
}