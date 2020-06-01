package com.zhengzhiqiang.healthmanagement.PatientActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.tv.TvContentRating;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.zhengzhiqiang.healthmanagement.Entity.Department;
import com.zhengzhiqiang.healthmanagement.Entity.Doctor;
import com.zhengzhiqiang.healthmanagement.Entity.Hospital;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OnlineInquirySecondActivity extends AppCompatActivity {

    private TextView tvBack;
    private ImageView ivHospitalPicture;
    private TextView tvHospitalName;
    private TextView tvHospitalLocation;
    private TextView tvHospitalPhone;
    private TextView tvHospitalGrade;

    private TextView tvChooseDepartment;
    private TextView tvChooseDoctor;

    private ViewStub viewStub;
    private ImageView ivDoctorPhoto;
    private TextView tvDoctorName;
    private TextView tvDoctorPosition;
    private TextView tvDoctorExpertise;
    private TextView tvDoctorIntroduction;
    private Button btnTextInquiry;

    private Department selectedDepartment;
    private Doctor selectedDoctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_online_inquiry_second);
        initView();
    }

    private void initView(){
        //前一个页面传过来的医院对象
        Hospital hospital = (Hospital) getIntent().getSerializableExtra("hospital");

        tvBack = findViewById(R.id.online_inquiry_tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });

        ivHospitalPicture = findViewById(R.id.online_inquiry_iv_hospital_picture);
        Picasso.with(OnlineInquirySecondActivity.this)
                .load(MyApplication.Images_Url+hospital.getPhoto())
                .into(ivHospitalPicture);

        tvHospitalName =findViewById(R.id.online_inquiry_hospital_tv_name);
        tvHospitalName.setText(hospital.getName());

        tvHospitalLocation = findViewById(R.id.online_inquiry_hospital_tv_location);
        tvHospitalLocation.setText(hospital.getLocation());

        tvHospitalPhone = findViewById(R.id.online_inquiry_hospital_tv_phone);
        tvHospitalPhone.setText(hospital.getPhone());

        tvHospitalGrade = findViewById(R.id.online_inquiry_hospital_tv_grade);
        tvHospitalGrade.setText(hospital.getGrade());

        tvChooseDepartment = findViewById(R.id.online_inquiry_tv_choose_department);
        tvChooseDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtils.getDepartments(hospital.getName(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(OnlineInquirySecondActivity.this, "网络出错！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String departmentJson = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {
                                    if (departmentJson.equals("[]")){
                                        Toast.makeText(OnlineInquirySecondActivity.this,"部门信息还没完善哦！",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Gson gson = new Gson();
                                    List<Department> departments = gson.fromJson(departmentJson, new TypeToken<List<Department>>() {
                                    }.getType());
                                    List<String> departmentNames = new ArrayList<>(departments.size());
                                    for (Department department : departments) {
                                        departmentNames.add(department.getName());
                                    }
                                    OptionsPickerView optionsPicker = new OptionsPickerBuilder(OnlineInquirySecondActivity.this, new OnOptionsSelectListener() {
                                        @SuppressLint("SetTextI18n")
                                        @Override
                                        public void onOptionsSelect(int options1, int options2, int options3, View v) {

                                            selectedDepartment = departments.get(options1);
                                            tvChooseDepartment.setText(departmentNames.get(options1)+">");
                                        }
                                    })
                                            .setTitleText("选择科室")
                                            .setDividerColor(Color.BLACK)
                                            .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                                            .setContentTextSize(20)
                                            .build();

                                    optionsPicker.setPicker(departmentNames);//一级选择器
                                    optionsPicker.show();
                                }
                            }
                        });
                    }
                });

            }
        });

        tvChooseDoctor = findViewById(R.id.online_inquiry_tv_choose_doctor);
        tvChooseDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDepartment == null){
                 Toast.makeText(OnlineInquirySecondActivity.this,"请先选择科室",Toast.LENGTH_SHORT).show();
                 return;
                }
                HttpUtils.getDoctors(hospital.getName(), selectedDepartment.getName(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(OnlineInquirySecondActivity.this, "网络出错！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String doctorsJson = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {
                                    if (doctorsJson.equals("[]")){
                                        Toast.makeText(OnlineInquirySecondActivity.this,"还没有医生入驻哦！",Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    Gson gson = new Gson();
                                    List<Doctor> doctors = gson.fromJson(doctorsJson, new TypeToken<List<Doctor>>() {
                                    }.getType());
                                    List<String> doctorsNames = new ArrayList<>(doctors.size());
                                    for (Doctor doctor : doctors) {
                                        doctorsNames.add(doctor.getName());
                                    }
                                    OptionsPickerView optionsPicker = new OptionsPickerBuilder(OnlineInquirySecondActivity.this, new OnOptionsSelectListener() {
                                        @Override
                                        public void onOptionsSelect(int options1, int options2, int options3, View v) {

                                            tvChooseDoctor.setText(doctorsNames.get(options1));
                                            //选中的医生的phone
                                            selectedDoctor = doctors.get(options1);
                                            tvChooseDoctor.setText(selectedDoctor.getName()+">");
                                            showDoctorDetailArea();
                                        }
                                    })
                                            .setTitleText("选择科室")
                                            .setDividerColor(Color.BLACK)
                                            .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                                            .setContentTextSize(20)
                                            .build();

                                    optionsPicker.setPicker(doctorsNames);//一级选择器
                                    optionsPicker.show();
                                }
                            }
                        });
                    }
                });

            }
        });

    }

    private void showDoctorDetailArea(){

        if (viewStub == null){
            viewStub = findViewById(R.id.online_inquiry_view_stub);
            viewStub.inflate();
        }
            ivDoctorPhoto = findViewById(R.id.online_inquiry_doctor_iv_head_image);
            Picasso.with(OnlineInquirySecondActivity.this)
                    .load(MyApplication.Images_Url+selectedDoctor.getPhoto())
                    .into(ivDoctorPhoto);
            tvDoctorName = findViewById(R.id.online_inquiry_doctor_tv_name);
            tvDoctorName.setText(selectedDoctor.getName());
            tvDoctorPosition = findViewById(R.id.online_inquiry_doctor_tv_position);
            tvDoctorPosition.setText(selectedDoctor.getPosition());
            tvDoctorExpertise = findViewById(R.id.online_inquiry_doctor_tv_expertise);
            tvDoctorExpertise.setText(selectedDoctor.getExpertise());
            tvDoctorIntroduction = findViewById(R.id.online_inquiry_doctor_introduction);
            tvDoctorIntroduction.setText(selectedDoctor.getIntroduction());

            btnTextInquiry = findViewById(R.id.online_inquiry_btn_text_inquiry);
            btnTextInquiry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OnlineInquirySecondActivity.this,"跳到聊天界面",Toast.LENGTH_SHORT).show();
                }
            });
        }
}
