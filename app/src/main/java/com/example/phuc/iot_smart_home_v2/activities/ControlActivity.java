package com.example.phuc.iot_smart_home_v2.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cengalabs.flatui.FlatUI;
import com.example.phuc.iot_smart_home_v2.R;
import com.example.phuc.iot_smart_home_v2.adapter.ComponentAdapter;
import com.example.phuc.iot_smart_home_v2.components.Component;
import com.example.phuc.iot_smart_home_v2.components.GlobalSocket;
import com.example.phuc.iot_smart_home_v2.components.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.socket.client.Socket;

public class ControlActivity extends Activity {

    private final int REQ_CODE_SPEECH_INPUT = 100;

    @BindView(R.id.cardView)
    RecyclerView cardView;

    @BindView(R.id.username_dashboard)
    TextView txtUserNameDB;

    @BindView(R.id.btnSetting)
    ImageButton btnSetting;

    @BindView(R.id.value_temp)
    TextView txtValueTemp;

    @BindView(R.id.value_humi)
    TextView txtValueHumi;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.btnIndex)
    ImageButton btnIndex;

    ComponentAdapter mAdapter;
    ArrayList<Component> listComponents = new ArrayList<>();
    ArrayList<Component> listSensors = new ArrayList<>();

    SharedPreferences sharedPreferences;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference homeRef = database.getReference("component");
    DatabaseReference sensorRef = database.getReference("measure");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_control);

        final User userInfo =  (User) getIntent().getSerializableExtra("user-info");
        Toast.makeText(getApplicationContext(), userInfo.getName(), Toast.LENGTH_LONG).show();

        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);

        ButterKnife.bind(this);

        txtUserNameDB.setText(sharedPreferences.getString("name", ""));

//        homeRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
//                int i = 0;
//                ArrayList<Component> list_temp = new ArrayList<>();
//                for (DataSnapshot user : children) {
//                    Component c = user.getValue(Component.class);
//                    if (!c.getType().equals("temperature") && !c.getType().equals("humidity")
//                            && c.getHomeID().equals(userInfo.getHomeID())) {
//                        list_temp.add(c);
//                    }
//                }
//                if (list_temp.size() > listComponents.size()) {
//                    listComponents.add(list_temp.get(list_temp.size() - 1));
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 SharedPreferences.Editor editor = sharedPreferences.edit();
                 editor.remove("username");
                 editor.remove("password");
                 editor.remove("name");
                 editor.commit();

                Intent gotoLogin = new Intent(ControlActivity.this, MainActivity.class);
                startActivity(gotoLogin);
            }
        });

        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                listSensors.clear();
                for (DataSnapshot user : children) {
                    Component c = user.getValue(Component.class);
                        listSensors.add(c);
                }
                txtValueTemp.setText(""+listSensors.get(0).getValue());
                txtValueHumi.setText(""+listSensors.get(1).getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoIndex = new Intent(ControlActivity.this, IndexActivity.class);
                startActivity(gotoIndex);
            }
        });

        homeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                listComponents.clear();
                for (DataSnapshot user : children) {
                    Component c = user.getValue(Component.class);
                    if (!c.getType().equals("temperature") && !c.getType().equals("humidity")) {
                        listComponents.add(c);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cardView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cardView.setLayoutManager(linearLayoutManager);


        mAdapter = new ComponentAdapter(listComponents);
        mAdapter.setComponentAdaptListener(adapterListener);
        cardView.setAdapter(mAdapter);
        Toast.makeText(getApplicationContext(), "size: " + listComponents.size(), Toast.LENGTH_LONG).show();


    }

    private ComponentAdapter.ComponentAdapterListener adapterListener = new ComponentAdapter.ComponentAdapterListener() {
        @Override
        public void onItemClick(CardView item, Component comp, int position) {
            //Toast.makeText(getApplicationContext(), comp.getDescription() + "  " + position, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onToggleButton(CompoundButton toggle, boolean isChecked, int position) {
            homeRef.child(""+(position+1)).child("value").setValue(isChecked ? 1: 0);
            //Toast.makeText(getApplicationContext(), comp.getDescription(), Toast.LENGTH_LONG).show();

            // Log.d("checked", "" + isChecked + comp.getId());
            //Toast.makeText(getApplicationContext(), "progress: " + isChecked + "  " + position, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSlideSeekBar(SeekBar opacity, int progress, boolean fromUser, int position) {
            homeRef.child(""+(position+1)).child("opacity").setValue(progress);
            //Toast.makeText(getApplicationContext(), "progress: " + progress + "  " + position, Toast.LENGTH_LONG).show();
        }
    };

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(getApplicationContext(), result.get(0), Toast.LENGTH_LONG).show();
                    if (result.get(0).toLowerCase().contains("turn on")) {
                        for (int i = 0; i < listComponents.size(); i++) {
                            String temp = result.get(0).substring(9, result.get(0).length() - 1);
                            if (listComponents.get(i).getDescription().toLowerCase().contains(temp)) {
                                homeRef.child(""+(i+1)).child("value").setValue(1);
                            }
                        }
                    } else {
                        for (int i = 0; i < listComponents.size(); i++) {
                            String temp = result.get(0).substring(10, result.get(0).length() - 1);
                            if (listComponents.get(i).getDescription().toLowerCase().contains(temp)) {
                                homeRef.child(""+(i+1)).child("value").setValue(0);
                            }
                        }
                    }

                }
                break;
            }

        }
    }

}
