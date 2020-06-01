package com.zhengzhiqiang.healthmanagement.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhengzhiqiang.healthmanagement.Entity.RegisterForm;
import com.zhengzhiqiang.healthmanagement.R;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class AppointmentRecyclerAdapter extends RecyclerView.Adapter<AppointmentRecyclerAdapter.AppointmentViewHolder> {

    private List<RegisterForm>  list;

    private OnItemClickListener onItemClickListener;

    public AppointmentRecyclerAdapter(List<RegisterForm> list) {
        this.list = list;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AppointmentRecyclerAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment,parent,false);
        return new AppointmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentRecyclerAdapter.AppointmentViewHolder holder, final int position) {
        RegisterForm registerForm = list.get(position);
        holder.tvFee.setText(String.valueOf(registerForm.getFee())+"元");
        holder.tvHospital.setText(registerForm.getHospital());
        holder.tvDepartment.setText(registerForm.getDepartment());
        holder.tvDoctor.setText(registerForm.getDoctorName());
        holder.tvPatient.setText(registerForm.getPatientName());
        Timestamp date = registerForm.getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String startTime = simpleDateFormat.format(date);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
        String endTime = simpleDateFormat1.format(new Date(date.getTime()+30*60*1000));
        holder.tvDate.setText(startTime+"~"+endTime);
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

    static class AppointmentViewHolder extends RecyclerView.ViewHolder{
        TextView tvFee;
        TextView tvHospital;
        TextView tvDepartment;
        TextView tvDoctor;
        TextView tvDate;
        TextView tvPatient;
        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFee = itemView.findViewById(R.id.appointment_tv_item_fee);
            tvHospital = itemView.findViewById(R.id.appointment_tv_item_hospital);
            tvDepartment = itemView.findViewById(R.id.appointment_tv_item_department);
            tvDoctor = itemView.findViewById(R.id.appointment_tv_item_doctor);
            tvDate = itemView.findViewById(R.id.appointment_tv_item_date);
            tvPatient = itemView.findViewById(R.id.appointment_tv_item_patient);
        }
    }
}
