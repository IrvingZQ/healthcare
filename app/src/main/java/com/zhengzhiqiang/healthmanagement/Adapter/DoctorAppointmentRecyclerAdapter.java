package com.zhengzhiqiang.healthmanagement.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhengzhiqiang.healthmanagement.CommonActivity.LoginActivity;
import com.zhengzhiqiang.healthmanagement.Entity.RegisterForm;
import com.zhengzhiqiang.healthmanagement.R;

import java.util.List;
import java.util.PrimitiveIterator;

public class DoctorAppointmentRecyclerAdapter extends RecyclerView.Adapter<DoctorAppointmentRecyclerAdapter.DoctorAppointmentViewHolder> {

    private List<RegisterForm> list;

    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public DoctorAppointmentRecyclerAdapter(List<RegisterForm> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public DoctorAppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient_appointment_record,parent,false);
        return new DoctorAppointmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAppointmentViewHolder holder, int position) {

        RegisterForm appointmentForm = list.get(position);
        holder.tvDate.setText(String.valueOf(appointmentForm.getDate()));
        holder.tvName.setText(appointmentForm.getPatientName());
        holder.tvId.setText(String.valueOf(appointmentForm.getPatientPhone()));
        holder.tvHospital.setText(appointmentForm.getHospital());
        holder.tvDepartment.setText(appointmentForm.getDepartment());
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

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    static  class DoctorAppointmentViewHolder extends RecyclerView.ViewHolder{

        TextView tvDate;
        TextView tvName;
        TextView tvId;
        TextView tvHospital;
        TextView tvDepartment;
        public DoctorAppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.item_tv_patient_appointment_date);
            tvName = itemView.findViewById(R.id.item_tv_patient_appointment_name);
            tvId = itemView.findViewById(R.id.item_tv_patient_appointment_id);
            tvHospital = itemView.findViewById(R.id.item_tv_patient_appointment_hospital);
            tvDepartment = itemView.findViewById(R.id.item_tv_patient_appointment_department);
        }
    }
}
