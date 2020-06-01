package com.zhengzhiqiang.healthmanagement.CommonActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhengzhiqiang.healthmanagement.Entity.Patient;
import com.zhengzhiqiang.healthmanagement.Entity.Province;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.GetJsonDataUtil;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;
import com.zhengzhiqiang.healthmanagement.Utils.PhotoUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EditPersonalCenterActivity extends AppCompatActivity {

    private TextView tvBack;
    private TextView tvSave;
    private ImageView ivHeadImage;
    private EditText etNickName;
    private RadioGroup rgGender;
    private TextView tvBorn;
    private EditText etEmail;
    private TextView tvArea;
    private EditText etIdNumber;

    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private AlertDialog picDialog;

    /**
     * 相册选取图片
     */
    private final int CODE_PICK_PHOTO = 4;
    /**
     * 拍照
     */
    private final int CODE_TAKE_PHOTO = 5;
    //省
    private List<Province> options1Items = new ArrayList<Province>();
    //市
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    //区
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Uri headImageUri;
    private String gender = "男";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_personal_center);
        if (ActivityCompat.checkSelfPermission(EditPersonalCenterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditPersonalCenterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        initView();
    }

    //获取到相册里的照片后显示出来
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_TAKE_PHOTO){
            ivHeadImage.setImageURI(headImageUri);
        }else if (data != null && requestCode == CODE_PICK_PHOTO) {
                headImageUri = data.getData();
                ivHeadImage.setImageURI(headImageUri);
        }
    }

    private void initView() {
        tvBack = findViewById(R.id.edit_personal_tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvSave = findViewById(R.id.edit_personal_tv_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Patient patient = new Patient();
                patient.setPhone(Long.valueOf(MyApplication.phone));
                if (patient == null){
                    Log.d("EditPersonal","patient为空");
                }

                if (headImageUri != null) {
                    String imagePath = PhotoUtils.getRealFilePath(EditPersonalCenterActivity.this,headImageUri);
                    Log.d("Edit",imagePath);
                  String headImageString = PhotoUtils.bitmapToString(imagePath);
                    patient.setPhoto(headImageString);
                }

                String nickName = etNickName.getText().toString().trim();
                Log.d("EditPersonal","nickName:"+nickName);
                if (!nickName.equals("")) {
                    patient.setNickName(nickName);
                }

                patient.setSex(gender);

                String birthday = tvBorn.getText().toString().trim();
                if (!birthday.equals("")) {
                    patient.setBorn(birthday);
                }

                String email = etEmail.getText().toString().trim();
                if (!email.equals("")) {
                    patient.setEmail(email);
                }

                String area = tvArea.getText().toString().trim();
                if (!area.equals("")) {
                    patient.setArea(area);
                }

                String idNumber = etIdNumber.getText().toString().trim();
                if (!idNumber.equals("")) {
                    patient.setId(idNumber);
                }

                HttpUtils.updatePatientInformation(patient, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(EditPersonalCenterActivity.this, "网络出错！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.d("EditPersonal","result:"+result);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200 && result.equals("1")) {
                                    Toast.makeText(EditPersonalCenterActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                    setResult(1);
                                    finish();
                                } else {
                                    Toast.makeText(EditPersonalCenterActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });

        ivHeadImage = findViewById(R.id.edit_personal_et_head_image);
        ivHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        etNickName = findViewById(R.id.edit_personal_et_nick_name);

        rgGender = findViewById(R.id.edit_personal_rg_gender);
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                gender = rb.getText().toString().trim();
            }
        });

        //设置生日
        tvBorn = findViewById(R.id.edit_personal_tv_born);
        tvBorn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate();
            }
        });

        etEmail = findViewById(R.id.edit_personal_et_email);

        tvArea = findViewById(R.id.edit_personal_tv_area);
        tvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //解析json格式的地区数据
                parseData();

                //显示选择器
                showAreaPickerView();
            }
        });

        etIdNumber = findViewById(R.id.edit_personal_et_id_number);
    }

    private void showAreaPickerView() {

        OptionsPickerView areaOptionsPicker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).name + "-" +
                        options2Items.get(options1).get(options2) + "-" +
                        options3Items.get(options1).get(options2).get(options3);

                Toast.makeText(EditPersonalCenterActivity.this, tx, Toast.LENGTH_SHORT).show();
                tvArea.setText(tx);
            }
        })
                .setTitleText("地区选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        areaOptionsPicker.setPicker(options1Items, options2Items, options3Items);//三级选择器
        areaOptionsPicker.show();
    }

    /**
     * 解析数据并组装成自己想要的list
     */
    private void parseData() {
        String jsonStr = new GetJsonDataUtil().getJson(EditPersonalCenterActivity.this, "province.json");//获取assets目录下的json文件数据
//     数据解析
        Gson gson = new Gson();
        Type type = new TypeToken<List<Province>>() {
        }.getType();
        List<Province> provinceList = gson.fromJson(jsonStr, type);
//     把解析后的数据组装成想要的list
        options1Items = provinceList;
//     遍历省
        for (int i = 0; i < provinceList.size(); i++) {
            //存放城市
            ArrayList<String> cityList = new ArrayList<>();
            //存放区
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();
            //遍历市
            for (int c = 0; c < provinceList.get(i).city.size(); c++) {
                //拿到城市名称
                String cityName = provinceList.get(i).city.get(c).name;
                cityList.add(cityName);

                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表
                if (provinceList.get(i).city.get(c).area == null || provinceList.get(i).city.get(c).area.size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(provinceList.get(i).city.get(c).area);
                }
                province_AreaList.add(city_AreaList);
            }
            /**
             * 添加城市数据
             */
            options2Items.add(cityList);
            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODE_TAKE_PHOTO:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCamera();
                } else {
                    Toast.makeText(EditPersonalCenterActivity.this, "相机权限禁用了。请务必开启相机权", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showCamera() {
        //有权限，直接拍照
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues values = new ContentValues();
            headImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, headImageUri);
            startActivityForResult(intent, CODE_TAKE_PHOTO);
        }
    }

    private void chooseDate(){
        //通过自定义AlertDialog控件实现
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPersonalCenterActivity.this);
        View view = LayoutInflater.from(EditPersonalCenterActivity.this).inflate(R.layout.choose_date_dialog, null);
        final DatePicker datePicker = view.findViewById(R.id.date_picker);
        //设置日期简略显示，否则详细显示 ，星期/周
        //datePicker.setCalendarViewShown(false);
        //初始化当前日期
        Calendar calendar = Calendar.getInstance();
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
        //设置date布局
        builder.setView(view);
        builder.setTitle("选择日期");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //日期格式
                StringBuilder date = new StringBuilder();
                date.append(String.format("%d-%02d-%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                tvBorn.setText(date);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawableResource(android.R.color.white);
    }

    private void choosePhoto(){
        View picView = LayoutInflater.from(EditPersonalCenterActivity.this).inflate(R.layout.pic_alert_dialog, null);
        btn_take_photo = picView.findViewById(R.id.btn_take_photo);
        btn_pick_photo = picView.findViewById(R.id.btn_pick_photo);
        btn_cancel = picView.findViewById(R.id.btn_cancel);
        //添加按钮监听
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picDialog.cancel();
            }
        });

        //从手机相册里选
        btn_pick_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picDialog.cancel();
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, CODE_PICK_PHOTO);
            }
        });

        //拍照
        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picDialog.cancel();
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(EditPersonalCenterActivity.this, Manifest.permission.CAMERA);
                    if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(EditPersonalCenterActivity.this,new String[]{Manifest.permission.CAMERA},CODE_TAKE_PHOTO);
                        return;
                    }else{
                        //有权限，直接拍照
                        showCamera();
                    }
                } else {
                    showCamera();
                }

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(EditPersonalCenterActivity.this)
                .setView(picView);
        picDialog = builder.create();
        picDialog.show();
        Window window = picDialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }
}
