package com.zhengzhiqiang.healthmanagement.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhengzhiqiang.healthmanagement.Entity.Hospital;
import com.zhengzhiqiang.healthmanagement.R;

import java.util.List;

public class HospitalRecyclerAdapter extends RecyclerView.Adapter<HospitalRecyclerAdapter.HospitalViewHolder> {

    private List<Hospital> list;

    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public HospitalRecyclerAdapter(List<Hospital> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HospitalRecyclerAdapter.HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hospital,parent,false);
        return new HospitalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalRecyclerAdapter.HospitalViewHolder holder, final int position) {
        Hospital hospital = list.get(position);
        holder.tvName.setText(hospital.getName());
        holder.tvGrade.setText(hospital.getGrade());
        holder.tvLocation.setText(hospital.getLocation());
        holder.tvPhone.setText(hospital.getPhone());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HospitalViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        TextView tvGrade;
        TextView tvLocation;
        TextView tvPhone;

        public HospitalViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.hospital_tv_item_name);
            tvGrade = itemView.findViewById(R.id.hospital_tv_item_grade);
            tvLocation = itemView.findViewById(R.id.hospital_tv_item_location);
            tvPhone = itemView.findViewById(R.id.hospital_tv_item_phone);
        }
    }

    public interface OnItemClickListener{

        void onItemClick(int position);
    }
}
