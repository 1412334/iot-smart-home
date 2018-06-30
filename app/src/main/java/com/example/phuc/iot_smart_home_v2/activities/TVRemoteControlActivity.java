package com.example.phuc.iot_smart_home_v2.activities;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.phuc.iot_smart_home_v2.R;

public class TVRemoteControlActivity extends AppCompatActivity {

    TextView txtName;
    Spinner spinner;
    ImageButton btnPower;
    ImageButton btnChannelUp;
    ImageButton btnChannelDown;
    ImageButton btnVolumeUp;
    ImageButton btnVolumeDown;

    MediaPlayer soundBtn;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tv_remote_control);

        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);

        soundBtn = MediaPlayer.create(this, R.raw.buttonsound);

        txtName = findViewById(R.id.txt_username);
        spinner = findViewById(R.id.spinner);
        btnPower = findViewById(R.id.btn_power);
        btnChannelUp = findViewById(R.id.btn_channel_up);
        btnChannelDown = findViewById(R.id.btn_channel_down);
        btnVolumeUp = findViewById(R.id.btn_volume_up);
        btnVolumeDown = findViewById(R.id.btn_volume_down);

        String[] productLines = {"Samsung", "LG"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, productLines);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        txtName.setText(sharedPreferences.getString("name", ""));
    }

    public void powerClick(View v) {
        soundBtn.start();
    }

    public void channelUpClick(View v) {
        soundBtn.start();
    }

    public void channelDownClick(View v) {
        soundBtn.start();
    }

    public void volumeUpClick(View v) {
        soundBtn.start();
    }

    public void volumeDownClick(View v) {
        soundBtn.start();
    }
}
