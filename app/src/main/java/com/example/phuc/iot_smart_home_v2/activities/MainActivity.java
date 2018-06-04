package com.example.phuc.iot_smart_home_v2.activities;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phuc.iot_smart_home_v2.R;
import com.example.phuc.iot_smart_home_v2.components.GlobalSocket;
import com.example.phuc.iot_smart_home_v2.components.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends Activity {

    @BindView(R.id.imgLogo)
    ImageView imgLogo;

    @BindView(R.id.txtUsername)
    TextView txtUsername;

    @BindView(R.id.txtPassword)
    TextView txtPassword;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.btnForgotPassword)
    Button btnFogotPassword;

    @BindView(R.id.signup)
    Button btnSignup;

    // Username and Password
    ArrayList<User> listUsers = new ArrayList<>();

    SharedPreferences sharedPreferences;

    //Socketio
    private Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        GlobalSocket app = (GlobalSocket) getApplication();

        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);

        txtUsername.setText(sharedPreferences.getString("username", ""));
        txtPassword.setText(sharedPreferences.getString("password", ""));

        if (!txtUsername.getText().toString().equals("")) {

            boolean isSuccess = false;
            User userInfo = new User();

            for (User u : listUsers) {
                if (u.getUserName().equals(txtUsername.getText().toString())
                        && u.getPassword().equals(txtPassword.getText().toString())) {
                    isSuccess = true;
                    userInfo = new User(u);
                    break;
                }
            }

            Intent gotoDashboard = new Intent(MainActivity.this, ControlActivity.class);
            gotoDashboard.putExtra("user-info", userInfo);
            startActivity(gotoDashboard);
        }

        // Disable Title bar Android

        //Set image source for logo
        imgLogo.setImageResource(R.drawable.logo);

        // Connect to firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference accountRef = database.getReference("accounts");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                boolean isSuccess = false;
                User userInfo = new User();

                for (User u : listUsers) {
                    if (u.getUserName().equals(userName) && u.getPassword().equals(password)) {
                        isSuccess = true;
                        userInfo = new User(u);
                        break;
                    }
                }

                if (isSuccess) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", userName);
                    editor.putString("password", password);
                    editor.putString("name", userInfo.getName());
                    editor.commit();

                    Intent gotoDashboard = new Intent(MainActivity.this, ControlActivity.class);
                    gotoDashboard.putExtra("user-info", userInfo);
                    startActivity(gotoDashboard);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Incorrect username or password", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoSignup = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(gotoSignup);
            }
        });

        accountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot user : children) {
                    User u = user.getValue(User.class);
                    listUsers.add(u);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
