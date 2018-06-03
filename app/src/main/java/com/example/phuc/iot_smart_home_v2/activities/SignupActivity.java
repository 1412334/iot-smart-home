package com.example.phuc.iot_smart_home_v2.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.phuc.iot_smart_home_v2.R;
import com.example.phuc.iot_smart_home_v2.components.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends Activity {

    @BindView(R.id.txtUsername)
    TextView txtUserName;

    @BindView(R.id.txtPassword)
    TextView txtPassword;

    @BindView(R.id.txtName)
    TextView txtName;

    @BindView(R.id.txtHomeID)
    TextView txtHomeID;

    @BindView(R.id.btnSignup)
    Button btnSignup;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference accountRef = database.getReference("accounts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User userInfo = new User(txtUserName.getText().toString(),
                        txtPassword.getText().toString(), txtName.getText().toString(),
                        txtHomeID.getText().toString());

                accountRef.push().setValue(userInfo);

                Intent gobackSignin = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(gobackSignin);
            }
        });
    }
}
