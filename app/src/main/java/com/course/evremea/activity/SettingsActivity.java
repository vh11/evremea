package com.course.evremea.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.course.evremea.R;
import com.course.evremea.store.Settings;

public class SettingsActivity extends AppCompatActivity {

    private EditText etApiHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etApiHost = findViewById(R.id.et_api_host);
        etApiHost.setText(Settings.getInstance().getApiHost() + ":" + Settings.getInstance().getApiPort());

        findViewById(R.id.button_save).setOnClickListener((v) -> {
            String hostWithPort = etApiHost.getText().toString();

            String[] parts = hostWithPort.split(":");
            Settings.getInstance().setApiHost(parts[0]);
            Settings.getInstance().setApiPort(parts.length > 1 ? Integer.parseInt(parts[1]) : 80);

            finish();
        });
    }
}