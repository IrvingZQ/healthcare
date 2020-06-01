package com.zhengzhiqiang.healthmanagement.PatientActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhengzhiqiang.healthmanagement.Entity.Department;
import com.zhengzhiqiang.healthmanagement.Entity.Doctor;
import com.zhengzhiqiang.healthmanagement.Entity.Hospital;
import com.zhengzhiqiang.healthmanagement.Entity.RegisterForm;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

;

public class MakeAnAppointmentActivity extends AppCompatActivity {

    private TextView tvBack;
    private TextView tvGoRecords;
    private TextView tvFee;
    private TextView tvChooseHospital;
    private TextView tvChooseDepartment;
    private TextView tvChooseDoctor;
    private TextView tvChooseDateTime;
    private TextView tvPatientName;
    private TextView tvPatientId;
    private TextView tvAddPatient;
    private Button btnConfirm;

    private Doctor selectedDoctor;
    private Timestamp selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_make_an_appointment);
        initView();
    }

    private void initView() {
        tvBack = findViewById(R.id.appointment_tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvGoRecords = findViewById(R.id.appointment_tv_go_records);
        tvGoRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MakeAnAppointmentActivity.this, RecordsOfAppointmentActivity.class));
            }
        });

        tvFee = findViewById(R.id.appointment_tv_value_fee);

        tvChooseHospital = findViewById(R.id.appointment_tv_choose_hospital);
        tvChooseHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseHospital();
            }
        });

        tvChooseDepartment = findViewById(R.id.appointment_tv_choose_department);
        tvChooseDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvChooseHospital.getText().toString().trim().equals("")) {
                    Toast.makeText(MakeAnAppointmentActivity.this, "请先选择医院", Toast.LENGTH_SHORT).show();
                    return;
                }

                chooseDepartment();
            }
        });

        tvChooseDoctor = findViewById(R.id.appointment_tv_choose_doctor);
        tvChooseDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvChooseDepartment.getText().toString().trim().equals("")) {
                    Toast.makeText(MakeAnAppointmentActivity.this, "请先选择科室", Toast.LENGTH_SHORT).show();
                    return;
                }

                chooseDoctor();

            }
        });

        tvChooseDateTime = findViewById(R.id.appointment_tv_choose_date);
        tvChooseDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker((TextView) v);
            }
        });

        tvPatientName = findViewById(R.id.appointment_tv_patient_default);
        tvPatientName.setText(MyApplication.patient.getName());

        tvPatientId = findViewById(R.id.appointment_tv_patient_default_id);
        tvPatientId.setText(MyApplication.patient.getId());
//        if (!MyApplication.patient.getId().equals("")){
//            tvPatientId.setText(MyApplication.patient.getId());
//        }

        tvAddPatient = findViewById(R.id.appointment_tv_patient_add);
        tvAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MakeAnAppointmentActivity.this, "目前只能本人预约哦", Toast.LENGTH_SHORT).show();
            }
        });

        btnConfirm = findViewById(R.id.appointment_btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.patient.getId().equals("")) {
                    Toast.makeText(MakeAnAppointmentActivity.this, "请先验证身份", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedDoctor == null || selectedDateTime == null) {
                    Toast.makeText(MakeAnAppointmentActivity.this, "请先选完预约信息哦！", Toast.LENGTH_SHORT).show();
                    return;
                }
                RegisterForm registerForm = new RegisterForm();
                registerForm.setDate(selectedDateTime);
                registerForm.setDoctorPhone(selectedDoctor.getPhone());
                registerForm.setPatientPhone(MyApplication.patient.getPhone());
                registerForm.setFee(selectedDoctor.getRegisterFee());
                HttpUtils.postRegisterForm(registerForm, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(MakeAnAppointmentActivity.this, "网络出错！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200 && result.equals("1")) {
                                    Toast.makeText(MakeAnAppointmentActivity.this, "预约成功！", Toast.LENGTH_SHORT).show();
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(new Intent(MakeAnAppointmentActivity.this, RecordsOfAppointmentActivity.class));
                                } else {
                                    Toast.makeText(MakeAnAppointmentActivity.this, "当天预约已满！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });


            }
        });

    }

    private void showDateTimePicker(TextView textView) {
        Calendar calendar = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) + 1);
        endDate.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) + 1);
        endDate.add(Calendar.DAY_OF_MONTH, 14);
        TimePickerView timePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                selectedDateTime = new Timestamp(date.getTime());
                TextView textView1 = (TextView) v;
                String startTime = getTime(date, new SimpleDateFormat("yyyy-mm-dd HH:mm"));
                String endTime = getTime(new Date(date.getTime() + 60 * 30 * 1000), new SimpleDateFormat("HH:mm"));
                textView1.setText(startTime + "~" + endTime);
            }
        })
                .setType(new boolean[]{true, true, true, true, true, false})
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentTextSize(15)
                .setDate(startDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .setTitleText("选择开始就诊时间")
                .setTitleSize(15)
                .build();
        timePickerView.show(textView);

    }

    private void chooseHospital(){
        HttpUtils.getHospitals(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MakeAnAppointmentActivity.this, "网络出错！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String hospitalJson = response.body().string();
                Log.d("Appointment", "hospitalJson:" + hospitalJson);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200 && !hospitalJson.equals("[]")) {
                            Gson gson = new Gson();
                            List<Hospital> hospitals = gson.fromJson(hospitalJson, new TypeToken<List<Hospital>>() {
                            }.getType());
                            List<String> hospitalNames = new ArrayList<>(hospitals.size());
                            for (int i = 0; i < hospitals.size(); i++) {
                                hospitalNames.add(hospitals.get(i).getName());
                            }
                            OptionsPickerView optionsPicker = new OptionsPickerBuilder(MakeAnAppointmentActivity.this, new OnOptionsSelectListener() {
                                @Override
                                public void onOptionsSelect(int options1, int options2, int options3, View v) {

                                    tvChooseHospital.setText(hospitalNames.get(options1));
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
                            Toast.makeText(MakeAnAppointmentActivity.this, "请求出错！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void chooseDepartment(){
        HttpUtils.getDepartments(tvChooseHospital.getText().toString().trim(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MakeAnAppointmentActivity.this, "网络出错！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String departmentJson = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200 ) {
                            if (departmentJson.equals("[]")){
                                Toast.makeText(MakeAnAppointmentActivity.this,"部门信息还没完善哦！",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Gson gson = new Gson();
                            List<Department> departments = gson.fromJson(departmentJson, new TypeToken<List<Department>>() {
                            }.getType());
                            List<String> departmentNames = new ArrayList<>(departments.size());
                            for (Department department : departments) {
                                departmentNames.add(department.getName());
                            }
                            OptionsPickerView optionsPicker = new OptionsPickerBuilder(MakeAnAppointmentActivity.this, new OnOptionsSelectListener() {
                                @Override
                                public void onOptionsSelect(int options1, int options2, int options3, View v) {

                                    tvChooseDepartment.setText(departmentNames.get(options1));
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

    private void chooseDoctor(){
        HttpUtils.getDoctors(tvChooseHospital.getText().toString().trim(), tvChooseDepartment.getText().toString().trim(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MakeAnAppointmentActivity.this, "网络出错！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String doctorsJson = response.body().string();
                Log.d("Main",doctorsJson);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200) {
                            if (doctorsJson.equals("[]")){
                                Toast.makeText(MakeAnAppointmentActivity.this,"还没有医生入驻哦！",Toast.LENGTH_SHORT).show();
                                return;
                            }


                            Gson gson = new Gson();
                            List<Doctor> doctors = gson.fromJson(doctorsJson, new TypeToken<List<Doctor>>() {
                            }.getType());
                            List<String> doctorsNames = new ArrayList<>(doctors.size());
                            for (Doctor doctor : doctors) {
                                doctorsNames.add(doctor.getName());
                            }
                            OptionsPickerView optionsPicker = new OptionsPickerBuilder(MakeAnAppointmentActivity.this, new OnOptionsSelectListener() {
                                @Override
                                public void onOptionsSelect(int options1, int options2, int options3, View v) {

                                    tvChooseDoctor.setText(doctorsNames.get(options1));
                                    //选中的医生的phone
                                    selectedDoctor = doctors.get(options1);
                                    tvFee.setText(selectedDoctor.getRegisterFee().toString());
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

    private String getTime(Date date, SimpleDateFormat format) {//可根据需要自行截取数据显示
        return format.format(date);
    }
}
