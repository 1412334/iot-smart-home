package com.example.phuc.iot_smart_home_v2.activities;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.phuc.iot_smart_home_v2.R;

public class ACRemoteControlActivity extends AppCompatActivity {

    TextView txtName;
    Spinner spinner;

    ImageButton btnPower;
    ImageButton btnTempUp;
    ImageButton btnTempDown;

    TextView txtTemp;
    Button btnMode;
    Button btnFan;
    Button btnSwing;
    Button btnProfile;

    SharedPreferences sharedPreferences;
    MediaPlayer soundBtn;

    short temperature;

    String mode[] = {"auto", "cool"};
    String fan[] = {"auto", "1", "2", "3"};
    String swing[] = {"auto", "1", "2", "3"};
    String profile[] = {"normal", "quiet", "boost"};
    short modeIndex;
    short fanIndex;
    short swingIndex;
    short profileIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ac_remote_control);

        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        soundBtn = MediaPlayer.create(this, R.raw.buttonsound);

        txtName = findViewById(R.id.txt_user);
        spinner = findViewById(R.id.ac_spinner);
        btnPower = findViewById(R.id.btn_ac_power);

        btnTempUp = findViewById(R.id.btn_temp_up);
        btnTempDown = findViewById(R.id.btn_temp_down);
        txtTemp = findViewById(R.id.txt_temperature);

        btnMode = findViewById(R.id.btn_mode);
        btnFan = findViewById(R.id.btn_fan);
        btnSwing = findViewById(R.id.btn_swing);
        btnProfile = findViewById(R.id.btn_profile);

        String[] productLines = {"Samsung", "Panasonic"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, productLines);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        txtName.setText(sharedPreferences.getString("name", ""));

        temperature = 20;
        txtTemp.setText(String.valueOf(temperature));

        modeIndex = 0;
        fanIndex = 0;
        swingIndex = 0;
        profileIndex = 0;

        btnMode.setText(mode[modeIndex]);
        btnFan.setText(fan[fanIndex]);
        btnSwing.setText(swing[swingIndex]);
        btnProfile.setText(profile[profileIndex]);
    }

    public void powerClick(View view) {
        soundBtn.start();
    }

    public void temperatureUpClick(View view) {
        soundBtn.start();
        temperature += 1;
        txtTemp.setText(String.valueOf(temperature));
    }

    public void temperatureDownClick(View view) {
        soundBtn.start();
        temperature -= 1;
        txtTemp.setText(String.valueOf(temperature));
    }

    public void modeSwitch(View view) {
        soundBtn.start();
        modeIndex = (short) ((modeIndex + 1) % mode.length);
        btnMode.setText(mode[modeIndex]);
    }

    public void fanSwitch(View view) {
        soundBtn.start();
        fanIndex = (short) ((fanIndex + 1) % fan.length);
        btnFan.setText(fan[fanIndex]);
    }

    public void swingSwitch(View view) {
        soundBtn.start();
        swingIndex = (short) ((swingIndex + 1) % swing.length);
        btnSwing.setText(swing[swingIndex]);
    }

    public void profileSwitch(View view) {
        soundBtn.start();
        profileIndex = (short) ((profileIndex + 1) % profile.length);
        btnProfile.setText(profile[profileIndex]);
    }
}
