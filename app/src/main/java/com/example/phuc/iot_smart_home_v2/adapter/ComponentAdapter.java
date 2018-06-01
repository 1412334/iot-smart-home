package com.example.phuc.iot_smart_home_v2.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cengalabs.flatui.views.FlatSeekBar;
import com.example.phuc.iot_smart_home_v2.R;
import com.example.phuc.iot_smart_home_v2.components.Component;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.ViewHolder> {
    ArrayList<Component> listComponents;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ComponentAdapter(ArrayList<Component> listComponents) {
        this.listComponents = listComponents;
    }

    // Configure Adapter Listener events
    private ComponentAdapterListener mListener;

    public void setComponentAdaptListener(ComponentAdapterListener mListener) {
        this.mListener = mListener;
    }

    public interface ComponentAdapterListener {
        void onItemClick(CardView item, Component comp);
        void onToggleButton(CompoundButton toggle, boolean isChecked);
        void onSlideSeekBar(SeekBar opacity,int progress, boolean fromUser);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private Context context;

        @BindView(R.id.icon_component)
        ImageView icon_component;

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

        public void setComp(Component comp) {
            this.comp = comp;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.bind(this, itemView);

            opacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mListener.onSlideSeekBar(seekBar, progress, fromUser);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        @OnClick(R.id.item_cardview)
        public void onItemClick() {
            mListener.onItemClick(item, this.comp);
        }

        @OnCheckedChanged(R.id.swControl)
        public void onToggleButton(CompoundButton button, boolean isChecked) {
            mListener.onToggleButton(button, isChecked);
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



        if (listComponents.get(position).getIcon().equals("light")) {
            holder.icon_component.setImageResource(R.drawable.idea);
            holder.opacity.setProgress(component.getOpacity());
            holder.toggle.setChecked(component.getValue() == 1);
            holder.txtStatus.setText("" + component.getValue());
            holder.txtDescription.setText(component.getDescription());
        }

        ((ViewHolder ) holder).setComp(listComponents.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listComponents.size();
    }
}
