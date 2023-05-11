package com.course.evremea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class CoordinatesActivity extends AppCompatActivity {

    private EditText etLat;
    private EditText etLon;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinates);

        etLat = findViewById(R.id.et_lat);
        etLon = findViewById(R.id.et_lon);
        tvInfo = findViewById(R.id.tv_info);

        findViewById(R.id.button_request).setOnClickListener((v) -> {
            new Thread(() -> {
                try {
                    URL url = new URL(Settings.getInstance().getCoordinatesEndpoint(etLat.getText().toString(), etLon.getText().toString()));
                    URLConnection conn = url.openConnection();

                    byte[] buffer = new byte[256];
                    int count = conn.getInputStream().read(buffer);
                    String response = new String(buffer, 0, count);
                    JSONObject jsonObject = new JSONObject(response);
                    int temperature = jsonObject.getInt("temperature");
                    int humidity = jsonObject.getInt("humidity");

                    runOnUiThread(() -> {
                        tvInfo.setText("T: " + temperature + "\nH: " + humidity);
                    });
                } catch(IOException | JSONException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
}