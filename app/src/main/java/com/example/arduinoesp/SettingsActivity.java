package com.example.arduinoesp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    EditText bulbEditText, plugEditText;
    Button saveButton;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bulbEditText = (EditText) findViewById(R.id.bulbIp);
        //plugEditText = (EditText) findViewById(R.id.plugIp);
        saveButton = (Button) findViewById(R.id.saveButton);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        bulbEditText.setText(preferences.getString("bulb", ""));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("bulb", bulbEditText.getText().toString());
                //editor.putString("plug", plugEditText.getText().toString());
                editor.commit();
                Toast.makeText(getApplicationContext(), "Saved successfully", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
