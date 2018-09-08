package com.example.snivy.intelligentagriculture;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.LocationClient;

import java.util.List;

/**
 * Created by Snivy on 2018/4/18.
 */

public class oneNodeInfoAdapter extends RecyclerView.Adapter<oneNodeInfoAdapter.ViewHolder>{

    private List<Node> mNodeList;

    private LocationClient mLocationClient;

    private MainActivity mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        TextView info;

        public ViewHolder(View view) {
            super(view);
            time = (TextView) view.findViewById(R.id.item_time);
            info = (TextView) view.findViewById(R.id.item_info);
        }
    }

    public oneNodeInfoAdapter(List<Node> nodeList, LocationClient locationClient, MainActivity context) {
        mNodeList = nodeList;
        mLocationClient = locationClient;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.information_item2, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                mContext.node_clicked(holder.getAdapterPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.time.setText(mNodeList.get(position).getTime());
        holder.info.setText(formatInfo(position));
    }

    @Override
    public int getItemCount(){
        return mNodeList.size();
    }

    private String formatInfo(int i){
        boolean isFirat = true;
        StringBuilder builder = new StringBuilder();
        Node node = mNodeList.get(i);
        if (node.getTemperature() != null) {
            if (isFirat){
                isFirat = false;
            } else {
                builder.append("\n");
            }
            builder.append("温度:").append(node.getTemperature()).append("℃");
        }
        if(node.getHumidity() != null) {
            if (isFirat){
                isFirat = false;
            } else {
                builder.append("\n");
            }
            builder.append("相对湿度:").append(node.getHumidity()).append("%");
        }
        if(node.getPressure() != null) {
            if (isFirat){
                isFirat = false;
            } else {
                builder.append("\n");
            }
            builder.append("气压:").append(node.getPressure()).append("Pa");
        }
        if(node.getPrecipitation() != null) {
            if (isFirat){
                isFirat = false;
            } else {
                builder.append("\n");
            }
            builder.append("降雨量:").append(node.getPrecipitation()).append("ml");
        }
        if(node.getWind_speed() != null) {
            if (isFirat){
                isFirat = false;
            } else {
                builder.append("\n");
            }
            builder.append("风速:").append(node.getWind_speed());
        }
        if(node.getWind_direction() != null) {
            if (isFirat){
                isFirat = false;
            } else {
                builder.append("\n");
            }
            builder.append("风向:").append(node.getWind_direction());
        }
        if(node.getWind_direction() != null) {
            if (isFirat){
                isFirat = false;
            } else {
                builder.append("\n");
            }
            builder.append("风向:").append(node.getWind_direction());
        }
        if(node.getSoil_temperature() != null) {
            if (isFirat){
                isFirat = false;
            } else {
                builder.append("\n");
            }
            builder.append("土壤温度:").append(node.getSoil_temperature()).append("℃");
        }
        if(node.getSoil_water_content() != null) {
            if (isFirat){
                isFirat = false;
            } else {
                builder.append("\n");
            }
            builder.append("土壤含水量:").append(node.getSoil_water_content()).append("g");
        }
        if(node.getLight() != null) {
            if (isFirat){
                isFirat = false;
            } else {
                builder.append("\n");
            }
            builder.append("光照强度:").append(node.getLight()).append("Lx");
        }
        if(node.getDissolved_oxygen() != null) {
            if (isFirat){
                isFirat = false;
            } else {
                builder.append("\n");
            }
            builder.append("溶解氧:").append(node.getDissolved_oxygen());
        }
        if(node.getOxygen_density() != null) {
            if (isFirat){
                isFirat = false;
            } else {
                builder.append("\n");
            }
            builder.append("氧气浓度:").append(node.getOxygen_density());
        }
        if(node.getCO2_density() != null) {
            if (isFirat){
                isFirat = false;
            } else {
                builder.append("\n");
            }
            builder.append("二氧化碳浓度:").append(node.getCO2_density());
        }
        if(node.getWater_level() != null) {
            if (isFirat){
                isFirat = false;
            } else {
                builder.append("\n");
            }
            builder.append("水位:").append(node.getWater_level());
        }
        return builder.toString();
    }
}
