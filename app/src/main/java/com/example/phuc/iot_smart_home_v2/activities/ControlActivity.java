package com.example.phuc.iot_smart_home_v2.activities;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.cengalabs.flatui.FlatUI;
import com.example.phuc.iot_smart_home_v2.R;
import com.example.phuc.iot_smart_home_v2.adapter.ComponentAdapter;
import com.example.phuc.iot_smart_home_v2.components.Component;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ControlActivity extends Activity {

    @BindView(R.id.cardView)
    RecyclerView cardView;

    ComponentAdapter mAdapter;
    ArrayList<Component> listComponents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_control);

        ButterKnife.bind(this);

        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.SNOW);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference homeRef = database.getReference("component");

        homeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                listComponents.clear();
                for (DataSnapshot user : children) {
                    Component c = user.getValue(Component.class);
                    listComponents.add(c);
                    Log.d("component:", c.getDescription());
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
    }

    private ComponentAdapter.ComponentAdapterListener adapterListener = new ComponentAdapter.ComponentAdapterListener() {
        @Override
        public void onItemClick(CardView item, Component comp) {
            Toast.makeText(getApplicationContext(), comp.getDescription(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onToggleButton(CompoundButton toggle, boolean isChecked) {
            Log.d("checked", "" + isChecked);
        }

        @Override
        public void onSlideSeekBar(SeekBar opacity, int progress, boolean fromUser) {
            Log.d("value","" + progress);
        }
    };
}
