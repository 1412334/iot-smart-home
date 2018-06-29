package com.example.phuc.iot_smart_home_v2.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cengalabs.flatui.views.FlatSeekBar;
import com.example.phuc.iot_smart_home_v2.R;
import com.example.phuc.iot_smart_home_v2.activities.RemoteControlActivity;
import com.example.phuc.iot_smart_home_v2.components.Component;

import java.util.ArrayList;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.ViewHolder> {
    ArrayList<Component> listComponents;

    Activity activity;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ComponentAdapter(ArrayList<Component> listComponents, Activity activity) {
        this.listComponents = listComponents;
        this.activity = activity;
    }

    // Configure Adapter Listener events
    private ComponentAdapterListener mListener;


    public void setComponentAdaptListener(ComponentAdapterListener mListener) {
        this.mListener = mListener;
    }

    public interface ComponentAdapterListener {
        void onItemClick(CardView item, Component comp, int position);
        void onToggleButton(CompoundButton toggle, boolean isChecked, int position);
        void onSlideSeekBar(SeekBar opacity,int progress, boolean fromUser, int position);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private Context context;

        @BindView(R.id.icon_component)
        ImageButton icon_component;

        @BindView(R.id.tvdesription)
        TextView txtDescription;

        @BindView(R.id.tvstatus)
        TextView txtStatus;

        @BindView(R.id.seekbar)
        SeekBar opacity;

        @BindView(R.id.swControl)
        Switch toggle;

        @BindView(R.id.item_cardview)
        CardView item;

        private Component comp;
        private int position;

        Point p;

        public void setComp(Component comp, int position) {
            this.comp = comp;
            this.position = position;
        }

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);

            opacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mListener.onSlideSeekBar(seekBar, progress, fromUser, position);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            // Set opacity-change animation on touching
            icon_component.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            v.setAlpha(0.5f);
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL: {
                            v.setAlpha(1f);
                            break;
                        }
                    }
                    return false;
                }
            });
        }

        @OnClick(R.id.item_cardview)
        public void onItemClick() {
            mListener.onItemClick(item, this.comp, this.position);
        }

        @OnCheckedChanged(R.id.swControl)
        public void onToggleButton(CompoundButton button, boolean isChecked) {
            mListener.onToggleButton(button, isChecked, this.position);
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ComponentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cardview, parent, false);
        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that elements
        Component component = listComponents.get(position);

        holder.txtStatus.setText(component.getValue() == 1 ? "ON" : "OFF");
        switch (component.getType()) {
            case "light":
                holder.icon_component.setImageResource(R.drawable.light);
                break;
            case "fan":
                holder.icon_component.setImageResource(R.drawable.fan);
                break;
            case "air-conditioner":
                holder.icon_component.setImageResource(R.drawable.air_conditioner);
                break;
            case "tv":
                holder.icon_component.setImageResource(R.drawable.tv);
                holder.icon_component.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent remoteControl = new Intent(activity, RemoteControlActivity.class);
                        activity.startActivity(remoteControl);
                    }
                });
                break;
            default:
                holder.icon_component.setImageResource(R.drawable.light);

        }

        holder.opacity.setProgress(component.getOpacity());
        holder.toggle.setChecked(component.getValue() == 1);
        holder.txtDescription.setText(component.getDescription());

        ((ViewHolder ) holder).setComp(listComponents.get(position), position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listComponents.size();
    }
}
