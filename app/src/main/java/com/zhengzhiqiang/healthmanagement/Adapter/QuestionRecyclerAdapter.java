package com.zhengzhiqiang.healthmanagement.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhengzhiqiang.healthmanagement.Entity.QuestionItem;
import com.zhengzhiqiang.healthmanagement.Entity.QuestionSet;
import com.zhengzhiqiang.healthmanagement.R;

import java.util.List;

public class QuestionRecyclerAdapter extends RecyclerView.Adapter<QuestionRecyclerAdapter.QuetionViewHolder>{

    private List<QuestionItem> list;

    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public QuestionRecyclerAdapter(List<QuestionItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public QuestionRecyclerAdapter.QuetionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question,parent,false);
        return new QuetionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionRecyclerAdapter.QuetionViewHolder holder, int position) {
        QuestionItem assessmentQuestion =list.get(position);
        holder.tvTitle.setText(assessmentQuestion.getQuestionContent());
        holder.rbOptionOne.setText(assessmentQuestion.getOptionOne());
        holder.rbOptionOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position,assessmentQuestion.getOptionOne());
            }
        });
        holder.rbOptionTwo.setText(assessmentQuestion.getOptionTwo());
        holder.rbOptionTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position,assessmentQuestion.getOptionTwo());
            }
        });
        holder.rbOptionThree.setText(assessmentQuestion.getOptionThree());
        holder.rbOptionThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position,assessmentQuestion.getOptionThree());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static  class QuetionViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;

        RadioButton rbOptionOne;

        RadioButton rbOptionTwo;

        RadioButton rbOptionThree
                ;
        public QuetionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.question_tv_title);

            rbOptionOne = itemView.findViewById(R.id.question_rb_option_one);
            rbOptionTwo = itemView.findViewById(R.id.question_rb_option_two);
            rbOptionThree = itemView.findViewById(R.id.question_rb_option_three);

        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position,String selectedOption);
    }
}
