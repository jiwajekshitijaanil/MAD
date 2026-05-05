package com.example.project;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textCity, textState, textMessage;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);
        textCity = findViewById(R.id.textCity);
        textState = findViewById(R.id.textState);
        textMessage = findViewById(R.id.textMessage);

        loadJSON();

        button.setOnClickListener(v -> {
            String pin = editText.getText().toString().trim();

            if (TextUtils.isEmpty(pin) || pin.length() != 6) {
                textMessage.setText("Please enter a valid 6 digit pincode");
                textCity.setText("City: -");
                textState.setText("State: -");
            } else {
                searchPin(pin);
            }
        });
    }

    private void loadJSON() {
        try {
            InputStream is = getAssets().open("pincode.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            jsonArray = new JSONArray(json);

        } catch (Exception e) {
            textMessage.setText("Error loading data");
        }
    }

    private void searchPin(String pin) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                if (obj.getString("pincode").equals(pin)) {

                    textCity.setText("City: " + obj.getString("district"));
                    textState.setText("State: " + obj.getString("state"));
                    textMessage.setText("");
                    return;
                }
            }

            textMessage.setText("Pincode not found");
            textCity.setText("City: -");
            textState.setText("State: -");

        } catch (Exception e) {
            textMessage.setText("Error reading data");
        }
    }
}