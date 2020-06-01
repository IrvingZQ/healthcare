package com.zhengzhiqiang.healthmanagement.DoctorActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhengzhiqiang.healthmanagement.Entity.Department;
import com.zhengzhiqiang.healthmanagement.Entity.Doctor;
import com.zhengzhiqiang.healthmanagement.Entity.Hospital;
import com.zhengzhiqiang.healthmanagement.PatientActivity.MakeAnAppointmentActivity;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CareerInformationActivity extends Activity {

    private ImageView ivBack;
    private TextView tvSave;
    private EditText etPosition;
    private EditText etFee;
    private TextView tvDepartment;
    private TextView tvHospital;
    private EditText etExpertise;
    private EditText etIntroduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_information);
        initView();
    }

    private void initView(){
        ivBack = findViewById(R.id.career_information_iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });

        tvSave = findViewById(R.id.career_information_tv_save_edit);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Doctor doctor = new Doctor();
                doctor.setPhone(MyApplication.doctor.getPhone());
                doctor.setPosition(etPosition.getText().toString().trim());
                doctor.setDepartment(tvDepartment.getText().toString().trim());
                doctor.setHospital(tvHospital.getText().toString().trim());
                doctor.setExpertise(etExpertise.getText().toString().trim());
                doctor.setIntroduction(etIntroduction.getText().toString().trim());
                try {
                    double fee= Double.valueOf(etFee.getText().toString().trim());
                    doctor.setRegisterFee(fee);
                    HttpUtils.updateDoctor(doctor, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("CareerInformation","updateDoctorInformation请求失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (response.code() == 200 && result.equals("1")){
                                        Toast.makeText(CareerInformationActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                        MyApplication.doctor.setPosition(doctor.getPosition());
                                        MyApplication.doctor.setDepartment(doctor.getDepartment());
                                        MyApplication.doctor.setHospital(doctor.getHospital());
                                        MyApplication.doctor.setRegisterFee(doctor.getRegisterFee());
                                        MyApplication.doctor.setExpertise(doctor.getExpertise());
                                        MyApplication.doctor.setIntroduction(doctor.getIntroduction());
                                        finishAndRemoveTask();
                                    }else{
                                        Toast.makeText(CareerInformationActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }catch (Exception e){
                    Log.e("CareerInformation","挂号费输入错误！");
                    Toast.makeText(CareerInformationActivity.this,"挂号费输入包含非数字符号",Toast.LENGTH_SHORT).show();
                }
            }
        });

        etPosition = findViewById(R.id.career_information_et_position);
        etPosition.setText(MyApplication.doctor.getPosition());

        etFee = findViewById(R.id.career_information_et_fee);
        if (MyApplication.doctor.getRegisterFee() !=null)
        etFee.setText(MyApplication.doctor.getRegisterFee().toString());

        tvDepartment = findViewById(R.id.career_information_tv_department);
        tvDepartment.setText(MyApplication.doctor.getDepartment());
        tvDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvHospital.getText().toString().trim().equals("")) {
                    Toast.makeText(CareerInformationActivity.this, "请先选择医院", Toast.LENGTH_SHORT).show();
                    return;
                }
                chooseDepartment();
            }
        });

        tvHospital = findViewById(R.id.career_information_tv_hospital);
        tvHospital.setText(MyApplication.doctor.getHospital());
        tvHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseHospital();
            }
        });

        etExpertise = findViewById(R.id.career_information_et_expertise);
        etExpertise.setText(MyApplication.doctor.getExpertise());

        etIntroduction = findViewById(R.id.career_information_et_introduction);
        etIntroduction.setText(MyApplication.doctor.getIntroduction());

    }

    private void chooseHospital(){
        HttpUtils.getHospitals(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("CareerInformation","chooseHospital网络出错！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String hospitalJson = response.body().string();
                Log.d("Appointment", "hospitalJson:" + hospitalJson);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200 && !hospitalJson.equals("")) {
                            Gson gson = new Gson();
                            List<Hospital> hospitals = gson.fromJson(hospitalJson, new TypeToken<List<Hospital>>() {
                            }.getType());
                            List<String> hospitalNames = new ArrayList<>(hospitals.size());
                            for (int i = 0; i < hospitals.size(); i++) {
                                hospitalNames.add(hospitals.get(i).getName());
                            }
                            OptionsPickerView optionsPicker = new OptionsPickerBuilder(CareerInformationActivity.this, new OnOptionsSelectListener() {
                                @Override
                                public void onOptionsSelect(int options1, int options2, int options3, View v) {

                                    tvHospital.setText(hospitalNames.get(options1));
                                }
                            })
                                    .setTitleText("选择医院")
                                    .setDividerColor(Color.BLACK)
                                    .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                                    .setContentTextSize(20)
                                    .build();

                            optionsPicker.setPicker(hospitalNames);//一级选择器
                            optionsPicker.show();
                        } else {
                            Toast.makeText(CareerInformationActivity.this, "请求出错！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void chooseDepartment(){
        HttpUtils.getDepartments(tvHospital.getText().toString().trim(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(CareerInformationActivity.this, "网络出错！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String departmentJson = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200 && !departmentJson.equals("")) {
                            Gson gson = new Gson();
                            List<Department> departments = gson.fromJson(departmentJson, new TypeToken<List<Department>>() {
                            }.getType());
                            List<String> departmentNames = new ArrayList<>(departments.size());
                            for (Department department : departments) {
                                departmentNames.add(department.getName());
                            }
                            OptionsPickerView optionsPicker = new OptionsPickerBuilder(CareerInformationActivity.this, new OnOptionsSelectListener() {
                                @Override
                                public void onOptionsSelect(int options1, int options2, int options3, View v) {

                                    tvDepartment.setText(departmentNames.get(options1));
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
}
