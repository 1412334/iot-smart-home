package com.example.phuc.iot_smart_home_v2.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.CartesianSeriesLine;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Line3d;
import com.anychart.anychart.Stroke;
import com.anychart.anychart.TooltipPositionMode;
import com.anychart.anychart.ValueDataEntry;
import com.example.phuc.iot_smart_home_v2.R;
import com.example.phuc.iot_smart_home_v2.components.Index;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IndexActivity extends Activity {

    // Connect to firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference indexRef = database.getReference("index");

    SharedPreferences sharedPreferences;

    @BindView(R.id.chart)
    AnyChartView chartModel;

    @BindView(R.id.username_dashboard)
    TextView txtName;

    CartesianSeriesLine series1;

    List<DataEntry> seriesData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_index);

        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);

        txtName.setText(sharedPreferences.getString("name", ""));

        final Cartesian chart = AnyChart.line();
        chart.setAnimation(true);
        chart.setPadding(10d, 20d, 5d, 20d);

        chart.getCrosshair().setEnabled(true);
        chart.getCrosshair()
                .setYLabel(true)
                .setYStroke((Stroke) null, null, null, null, null);

        chart.getTooltip().setPositionMode(TooltipPositionMode.POINT);

        chart.getYAxis().setTitle("Chart of Electric Consumption in 7 days");
        chart.getXAxis().getLabels().setPadding(5d, 5d, 5d, 5d);

        indexRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Index i = data.getValue(Index.class);
                    seriesData.add(new ValueDataEntry(i.getDate(), i.getValue()));
                }

                chart.line(seriesData);
                chartModel.setChart(chart);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
