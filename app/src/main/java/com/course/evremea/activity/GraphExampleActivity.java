package com.course.evremea.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.course.evremea.R;
import com.course.evremea.store.Reading;
import com.course.evremea.store.Settings;
import com.course.evremea.ui.GraphCanvas;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class GraphExampleActivity extends AppCompatActivity {

    private Spinner spinnerLocationType;
    private EditText etCity;
    private EditText etLat;
    private EditText etLon;
    private Button buttonShow;
    private Spinner spinnerInterval;
    private GraphCanvas graphCanvas;;
    private Calendar now = new GregorianCalendar();
    private Button buttonPrevious;
    private Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_example);

        spinnerLocationType = findViewById(R.id.spinner_location_type);
        etCity = findViewById(R.id.et_city);
        etLat = findViewById(R.id.et_lat);
        etLon = findViewById(R.id.et_lon);
        buttonShow = findViewById(R.id.button_show);
        spinnerInterval = findViewById(R.id.spinner_interval);
        graphCanvas = findViewById(R.id.gc_example);
        buttonPrevious = findViewById(R.id.button_previous);
        buttonNext = findViewById(R.id.button_next);

        String[] options = {"City", "Coordinates"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocationType.setAdapter(adapter);

        String[] optionsInterval = {"Hourly", "Daily", "Weekly"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, optionsInterval);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInterval.setAdapter(adapter);

        buttonShow.setOnClickListener(view -> {
            onShow();
        });

        buttonPrevious.setOnClickListener(view -> {
            if (spinnerInterval.getSelectedItemPosition() == 0) {
                now.roll(Calendar.HOUR, -1);
            } else if (spinnerInterval.getSelectedItemPosition() == 1) {
                now.roll(Calendar.DAY_OF_YEAR, -1);
            } else if (spinnerInterval.getSelectedItemPosition() == 2) {
                now.roll(Calendar.WEEK_OF_YEAR, -1);
            }

            onShow();
        });

        buttonNext.setOnClickListener(view -> {
            if (spinnerInterval.getSelectedItemPosition() == 0) {
                now.roll(Calendar.HOUR, 1);
            } else if (spinnerInterval.getSelectedItemPosition() == 1) {
                now.roll(Calendar.DAY_OF_YEAR, 1);
            } else if (spinnerInterval.getSelectedItemPosition() == 2) {
                now.roll(Calendar.WEEK_OF_YEAR, 1);
            }

            onShow();
        });

    }

    void onShow() {
        new Thread(() -> {
            Calendar from = new GregorianCalendar();
            from.setTime(now.getTime());
            Calendar to = new GregorianCalendar();
            to.setTime(now.getTime());
            from.set(Calendar.MINUTE, 0);
            from.set(Calendar.SECOND, 0);
            to.set(Calendar.MINUTE, 59);
            to.set(Calendar.SECOND, 59);
            int periodInSeconds = 60;
            if (spinnerInterval.getSelectedItemPosition() == 1) {
                periodInSeconds = 60 * 10;
                from.set(Calendar.HOUR_OF_DAY, 0);
                to.set(Calendar.HOUR_OF_DAY, 23);
            } else if (spinnerInterval.getSelectedItemPosition() == 2) {
                periodInSeconds = 60 * 60;
                from.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                to.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            }

            long t0 = from.getTime().getTime();

            List<Reading> readings = new ArrayList<>();
            while (from.before(to)) {
                try {
                    URL url = new URL(Settings.getInstance().getLocationEndpoint(etCity.getText().toString(), from.getTime().getTime()));
                    URLConnection conn = url.openConnection();

                    byte[] buffer = new byte[256];
                    int count = conn.getInputStream().read(buffer);
                    String response = new String(buffer, 0, count);
                    JSONObject jsonObject = new JSONObject(response);

                    Reading reading = new Reading();
                    reading.timestamp = (int) (from.getTime().getTime() - t0) / 1000;
                    reading.temperature = jsonObject.getInt("temperature");
                    reading.humidity = jsonObject.getInt("humidity");

                    readings.add(reading);

                    from.add(Calendar.SECOND, periodInSeconds);
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            runOnUiThread(() -> {
                graphCanvas.setReadings(readings);
            });
        }).start();
    }
}