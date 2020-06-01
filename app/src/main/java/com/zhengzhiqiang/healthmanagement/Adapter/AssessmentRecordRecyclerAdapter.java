package com.zhengzhiqiang.healthmanagement.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhengzhiqiang.healthmanagement.Entity.AssessmentRecord;
import com.zhengzhiqiang.healthmanagement.Entity.QuestionItem;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;

import java.text.SimpleDateFormat;
import java.util.List;

public class AssessmentRecordRecyclerAdapter extends RecyclerView.Adapter<AssessmentRecordRecyclerAdapter.AssessmentRecordViewHolder> {

    private List<AssessmentRecord> list;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public AssessmentRecordRecyclerAdapter(List<AssessmentRecord> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AssessmentRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assessment_record,parent,false);
        return new AssessmentRecordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentRecordViewHolder holder, int position) {
        AssessmentRecord assessmentRecord =list.get(position);
        String type ="生理评估";
        if (assessmentRecord.getType() == MyApplication.ASSESSMENT_TYPE_PSYCHOLOGY){
            type="心理评估";
        }
        holder.tvType.setText(type);
        holder.tvDate.setText(simpleDateFormat.format(assessmentRecord.getDate()));
        holder.tvResult.setText(assessmentRecord.getResult());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static  class AssessmentRecordViewHolder extends RecyclerView.ViewHolder{

        TextView tvType;
        TextView tvDate;
        TextView tvResult;

        public AssessmentRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.assessment_record_item_tv_type);
            tvDate = itemView.findViewById(R.id.assessment_record_item_tv_date);
            tvResult = itemView.findViewById(R.id.assessment_record_item_tv_result);

        }
    }

}
