package com.example.phuc.iot_smart_home_v2.activities;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phuc.iot_smart_home_v2.R;
import com.example.phuc.iot_smart_home_v2.components.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    // Username and Password
    User userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
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
                userInfo = new User(userName, password);
                Log.d("Info: ", userInfo.getUserName() + " + " + userInfo.getPassword());
            }
        });

        accountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot user : children) {
                    User u = user.getValue(User.class);
                    Log.d("username: ", u.getUserName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
