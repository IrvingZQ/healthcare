package com.zhengzhiqiang.healthmanagement.CommonActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.landptf.bean.BannerBean;
import com.landptf.view.BannerM;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zhengzhiqiang.healthmanagement.Adapter.CommunityRecyclerAdapter;
import com.zhengzhiqiang.healthmanagement.Adapter.DoctorAppointmentRecyclerAdapter;
import com.zhengzhiqiang.healthmanagement.Adapter.KnowledgeRecyclerAdapter;
import com.zhengzhiqiang.healthmanagement.CustomView.RoundImageView;
import com.zhengzhiqiang.healthmanagement.DoctorActivity.CareerInformationActivity;
import com.zhengzhiqiang.healthmanagement.Entity.Doctor;
import com.zhengzhiqiang.healthmanagement.Entity.IllnessHistory;
import com.zhengzhiqiang.healthmanagement.Entity.RegisterForm;
import com.zhengzhiqiang.healthmanagement.Entity.News;
import com.zhengzhiqiang.healthmanagement.Entity.Patient;
import com.zhengzhiqiang.healthmanagement.Entity.Sticker;
import com.zhengzhiqiang.healthmanagement.PatientActivity.AssessmentRecordActivity;
import com.zhengzhiqiang.healthmanagement.PatientActivity.HealthAssessmentActivity;
import com.zhengzhiqiang.healthmanagement.PatientActivity.HealthRecordsActivity;
import com.zhengzhiqiang.healthmanagement.PatientActivity.MakeAnAppointmentActivity;
import com.zhengzhiqiang.healthmanagement.PatientActivity.OnlineInquiryFirstActivity;
import com.zhengzhiqiang.healthmanagement.PatientActivity.ShowSingleNewsActivity;
import com.zhengzhiqiang.healthmanagement.R;
import com.zhengzhiqiang.healthmanagement.Utils.DateUtil;
import com.zhengzhiqiang.healthmanagement.Utils.HttpUtils;
import com.zhengzhiqiang.healthmanagement.Utils.MyApplication;
import com.zhengzhiqiang.healthmanagement.Utils.PhotoUtils;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_home, btn_schedule, btn_calendar, btn_setting;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    List<View> mViews = new ArrayList<>();
    private TextView selectedLine;

    private View pageMessage, pageCommunity, pagePersonalCenter;

    //首页
    private View pageHome;
    private BannerM bannerM;
    List<News> newsList;
    List<News> knowledgeList;
    private List<BannerBean> bannerList;
    private ImageView ivMakeAppointment;
    private ImageView ivOnlineInquiry;
    private ImageView ivHealthAssessment;
    private RecyclerView knowledgeRecyclerView;
    private KnowledgeRecyclerAdapter knowledgeRecyclerAdapter;


    //社区页
    private RecyclerView communityRecyclerView;
    private CommunityRecyclerAdapter communityRecyclerAdapter;
    private TextView tvPostSticker;
    private SmartRefreshLayout communitySmartRefreshLayout;
    private List<Sticker> stickerList;

    //个人中心页
    private TextView tvEditPersonal;//编辑按钮
    private ImageView ivBackground;
    private RoundImageView rivHeadImage;//头像
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvGender;
    private TextView tvAge;
    private TextView tvArea;
    private TextView tvSignature;
    private TextView tvNickName;
    private TextView tvEmail;
    private TextView tvBorn;
    private TextView tvIdNumber;
    private TextView tvPersonalHealthRecords;
    private TextView tvAssessmentResult;
    private TextView tvChangePassword;

    //医生端首页
    private View pageDoctorHome;
    private RecyclerView doctorAppointRecyclerView;
    private DoctorAppointmentRecyclerAdapter doctorAppointmentRecyclerAdapter;
    private SmartRefreshLayout registerRecordRefreshLayout;

    //医生端个人中心页
    private TextView tvHospital;
    private TextView tvDepartment;
    private TextView tvPosition;
    private TextView tvCareer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initViewpager();
    }

    /**
     * 初始化Viewpager
     */
    private void initViewpager() {

        btn_home = findViewById(R.id.bt_home);
        btn_home.setOnClickListener(this);
        if(MyApplication.IDENTITY_TYPE){
            pageDoctorHome = LayoutInflater.from(this).inflate(R.layout.doctor_home, null);
            mViews.add(pageDoctorHome);
            initDoctorHome();
        }else{
            pageHome = LayoutInflater.from(this).inflate(R.layout.patient_home, null);
            mViews.add(pageHome);
            initPatientHome();
        }

        btn_schedule = findViewById(R.id.bt_message);
        btn_schedule.setOnClickListener(this);
        pageMessage = LayoutInflater.from(this).inflate(R.layout.patient_message, null);
        mViews.add(pageMessage);

        btn_calendar = findViewById(R.id.bt_community);
        btn_calendar.setOnClickListener(this);
        pageCommunity = LayoutInflater.from(this).inflate(R.layout.community, null);
        mViews.add(pageCommunity);
        initCommunity();


        btn_setting = findViewById(R.id.bt_personal_center);
        btn_setting.setOnClickListener(this);
        if (MyApplication.IDENTITY_TYPE){
            pagePersonalCenter = LayoutInflater.from(this).inflate(R.layout.doctor_personal_center, null);
            mViews.add(pagePersonalCenter);
            initDoctorPersonalCenter();
        }else{
            pagePersonalCenter = LayoutInflater.from(this).inflate(R.layout.patient_personal_center, null);
            mViews.add(pagePersonalCenter);
            initPersonalCenter();
        }

        selectedLine = findViewById(R.id.selected_line);

        mViewPager = findViewById(R.id.vp);


        /**
         * 为ViewPager创建并设置适配器
         */
        mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(mViews.get(position));
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(mViews.get(position));
                return mViews.get(position);
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        /**
         * 为ViewPager添加监听事件
         */
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                float leftMargin = v * getLineDistance() + i * getLineDistance() + btn_home.getWidth() / 2 - selectedLine.getWidth() / 2;//计算pager向右滑动距离
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) selectedLine.getLayoutParams();
                params.setMargins((int) leftMargin, 0, 0, 0);
                selectedLine.setLayoutParams(params);//设置导航栏的选择线条的位置刷新
            }

            @Override
            public void onPageSelected(int i) {

                resetBtn();

                switch (i) {
                    case 0:
                        btn_home.setTextAppearance(MainActivity.this, R.style.Btn_pressed);
                        break;
                    case 1:
                        btn_schedule.setTextAppearance(MainActivity.this, R.style.Btn_pressed);

                        break;
                    case 2:
                        btn_calendar.setTextAppearance(MainActivity.this, R.style.Btn_pressed);
                        break;
                    case 3:
                        btn_setting.setTextAppearance(MainActivity.this, R.style.Btn_pressed);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        
    }

    /**
     * 根据屏幕宽度计算导航栏按钮上方可移动横线的间隔
     */
    private int getLineDistance() {
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point.x / 4;
    }

    /**
     * 设置底部导航栏的点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_home:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.bt_message:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.bt_community:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.bt_personal_center:
                mViewPager.setCurrentItem(3);
                break;
        }
    }

    /**
     * 重置底部导航栏按钮状态
     */
    @SuppressLint("ResourceType")
    private void resetBtn() {
        btn_home.setTextAppearance(MainActivity.this, R.style.Btn_common);
        btn_schedule.setTextAppearance(MainActivity.this, R.style.Btn_common);
        btn_calendar.setTextAppearance(MainActivity.this, R.style.Btn_common);
        btn_setting.setTextAppearance(MainActivity.this, R.style.Btn_common);
    }

    /**
     * 患者首页方法区
     */

    private void initPatientHome() {
        initBannerM();
        initServiceArea();
        initRecyclerView();
    }

    /**
     * 显示轮播图
     */
    private void showBannerM() {

        bannerM = (BannerM) pageHome.findViewById(R.id.bm_banner);
        if (bannerM != null) {
            bannerM.setBannerBeanList(bannerList)
                    .setDefaultImageResId(R.drawable.news_picture)
                    .setIndexPosition(BannerM.INDEX_POSITION_BOTTOM)
                    .setIndexColor(getResources().getColor(R.color.colorPrimary))
                    .setIntervalTime(5)
                    .setOnItemClickListener(position -> {
                        Log.e("landptf", "position = " + position);
                        if (newsList !=null){
                            Intent intent = new Intent(MainActivity.this, ShowSingleNewsActivity.class);
                            intent.putExtra("news",(Serializable) newsList.get(position));
                            startActivity(intent);
                        }

                    })
                    .show();
        }
    }

    /**
     * 初始化轮播图数据
     */
    private void initBannerM() {
        HttpUtils.getNews(MyApplication.NEWS_TYPE_EVENT,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                bannerList = new ArrayList<>(4);
                for (int i=0;i<4;i++){
                    BannerBean banner = new BannerBean("获取数据失败", "http://asdasdas", "");
                    bannerList.add(banner);
                }
                showBannerM();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                String newsJson = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200 && !newsJson.equals("")){
                            Gson gson = new Gson();
                            newsList = gson.fromJson(newsJson,new TypeToken<List<News>>(){}.getType());
                            bannerList = new ArrayList<>(newsList.size());
                            for (int i=0;i<newsList.size();i++){
                                BannerBean banner = new BannerBean(newsList.get(i).getTitle(), MyApplication.Images_Url+newsList.get(i).getPicture(), "");
                                bannerList.add(banner);
                            }
                        }else{
                            bannerList = new ArrayList<>(4);
                            for (int i=0;i<4;i++){
                                BannerBean banner = new BannerBean("获取数据失败", "http://asdasdas", "");
                                bannerList.add(banner);
                            }
                        }
                        showBannerM();
                    }
                });

            }
        });
    }

    private void initServiceArea(){
        ivMakeAppointment = pageHome.findViewById(R.id.iv_register_center);
        ivMakeAppointment.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MakeAnAppointmentActivity.class)));
        ivOnlineInquiry = pageHome.findViewById(R.id.iv_online_inquiry);
        ivOnlineInquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OnlineInquiryFirstActivity.class));
            }
        });
        ivHealthAssessment = pageHome.findViewById(R.id.iv_health_assessment);
        ivHealthAssessment.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HealthAssessmentActivity.class)));

    }


    private void initRecyclerView() {
        HttpUtils.getNews(MyApplication.NEWS_TYPE_KNOWLEDGE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"网络出错！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String newsJson = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200 && !newsJson.equals("")){
                            Gson gson = new Gson();
                            knowledgeList = gson.fromJson(newsJson,new TypeToken<List<News>>(){}.getType());
                            knowledgeRecyclerView = pageHome.findViewById(R.id.recycler_health_knowledge);
                            knowledgeRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            knowledgeRecyclerAdapter = new KnowledgeRecyclerAdapter(knowledgeList,MainActivity.this);
                            knowledgeRecyclerAdapter.setOnItemClickListener(new KnowledgeRecyclerAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Intent intent = new Intent(MainActivity.this,ShowSingleNewsActivity.class);
                                    intent.putExtra("news",(Serializable) knowledgeList.get(position));
                                    startActivity(intent);
                                }
                            });
                            knowledgeRecyclerView.setAdapter(knowledgeRecyclerAdapter);
                            knowledgeRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                                @Override
                                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                                    super.getItemOffsets(outRect, view, parent, state);
                                    outRect.set(2, 10, 2, 10);

                                }
                            });
                        }else{

                        }
                    }
                });
            }
        });

    }


    /**
     * 社区页方法区
     */

    private void initCommunity() {

        initCommunityRecyclerView();

        communitySmartRefreshLayout =pageCommunity.findViewById(R.id.community_smart_refresh_layout);
        communitySmartRefreshLayout.setRefreshHeader(new BezierRadarHeader(this).setPrimaryColor(Color.GRAY));
        communitySmartRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setAnimatingColor(Color.WHITE));
        communitySmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshCommunityRecyclerView();
                refreshlayout.finishRefresh(1000,true);
            }
        });
        communitySmartRefreshLayout.setOnLoadmoreListener(refreshlayout -> refreshlayout.finishLoadmore(2000,false));

        refreshCommunityRecyclerView();

        tvPostSticker =pageCommunity.findViewById(R.id.tv_post_sticker);
        tvPostSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PostStickerActivity.class));
            }
        });

    }

    /**
     * 初始化咨询社区页
     */
    private void initCommunityRecyclerView(){
        communityRecyclerView = pageCommunity.findViewById(R.id.recycler_stickers);
        communityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        communityRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(20,0,20,20);
            }

            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);
                int childCount = parent.getChildCount();
                DisplayMetrics displayMetrics=getResources().getDisplayMetrics();
                Paint paint = new Paint();
                paint.setColor(Color.GRAY);
                for (int i=0;i<childCount;i++){
                    View view = parent.getChildAt(i);
                    int top = view.getBottom()-10;
                    int right =displayMetrics.widthPixels;
                    int bottom=view.getBottom()+10;
                    c.drawRect(0,top,right,bottom,paint);
                }
            }
        });
        communityRecyclerView.setItemAnimator(new RecyclerView.ItemAnimator() {
            @Override
            public boolean animateDisappearance(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @Nullable ItemHolderInfo postLayoutInfo) {
                return false;
            }

            @Override
            public boolean animateAppearance(@NonNull RecyclerView.ViewHolder viewHolder, @Nullable ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
                return false;
            }

            @Override
            public boolean animatePersistence(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
                return false;
            }

            @Override
            public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull RecyclerView.ViewHolder newHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
                return false;
            }

            @Override
            public void runPendingAnimations() {

            }

            @Override
            public void endAnimation(@NonNull RecyclerView.ViewHolder item) {

            }

            @Override
            public void endAnimations() {

            }

            @Override
            public boolean isRunning() {
                return false;
            }
        });
    }


    private void refreshCommunityRecyclerView(){
        HttpUtils.getStickers(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MainActivity","getStickers请求网络出错了");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                String stickerJson =response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200 && !stickerJson.equals("")){
                            Gson gson = new GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd HH:mm")
                                    .create();
                            stickerList = gson.fromJson(stickerJson,new TypeToken<List<Sticker>>(){}.getType());
                            communityRecyclerAdapter = new CommunityRecyclerAdapter(stickerList,MainActivity.this);
                            communityRecyclerAdapter.setOnItemClickListener(position -> {
                                Intent intent = new Intent(MainActivity.this,ShowStickerDetailActivity.class);
                                intent.putExtra("sticker",stickerList.get(position));
                                startActivity(intent);
                            });
                            communityRecyclerView.setAdapter(communityRecyclerAdapter);
                        }
                    }
                });
            }
        });
    }

    /**
     * 个人中心方法区
     */

    private void initPersonalCenter(){

        /**
         * 编辑按钮点击事件
         */
        tvEditPersonal = pagePersonalCenter.findViewById(R.id.personal_tv_edit);
        tvEditPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, EditPersonalCenterActivity.class),1);
            }
        });

        rivHeadImage = pagePersonalCenter.findViewById(R.id.patient_riv_head_image);
        tvName = pagePersonalCenter.findViewById(R.id.patient_tv_name);
        tvPhone = pagePersonalCenter.findViewById(R.id.patient_tv_phone);
        tvGender = pagePersonalCenter.findViewById(R.id.patient_tv_gender);
        tvAge = pagePersonalCenter.findViewById(R.id.patient_tv_age);
        tvArea = pagePersonalCenter.findViewById(R.id.patient_tv_area);
        tvSignature = pagePersonalCenter.findViewById(R.id.patient_tv_signature);
        tvNickName = pagePersonalCenter.findViewById(R.id.patient_tv_nick_name);
        tvEmail = pagePersonalCenter.findViewById(R.id.patient_tv_email);
        tvBorn = pagePersonalCenter.findViewById(R.id.patient_tv_born);
        tvIdNumber = pagePersonalCenter.findViewById(R.id.patient_tv_id_number);
        refreshPersonalInformation();
        tvPersonalHealthRecords = pagePersonalCenter.findViewById(R.id.patient_tv_health_records);
        tvPersonalHealthRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HealthRecordsActivity.class));
            }
        });

        tvAssessmentResult = pagePersonalCenter.findViewById(R.id.patient_tv_assessment_results);
        tvAssessmentResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AssessmentRecordActivity.class));
            }
        });

        tvChangePassword = pagePersonalCenter.findViewById(R.id.patient_tv_change_password);
        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
            }
        });

    }

    /**
     * 刷新患者个人中心页
     */
    private void refreshPersonalInformation(){

        HttpUtils.getUserInformation(MyApplication.IDENTITY_TYPE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"刷新失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("getPatient","result:"+result);
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        if (response.code() == 200 && !result.equals("")){
                            Gson gson = new Gson();
                            Patient patient = gson.fromJson(result ,Patient.class);
                            MyApplication.patient = patient;
                            tvName.setText(patient.getName());
                            tvPhone.setText(String.valueOf(patient.getPhone()));
                            tvGender.setText(patient.getSex());
                            if (patient.getBorn()!=null){

                                try {
                                    Date date = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(patient.getBorn()).getTime());
                                    tvAge.setText(String.valueOf(DateUtil.getAgeByBirth(date)) +"岁");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            tvArea.setText(patient.getArea());
                            tvNickName.setText("昵称："+patient.getNickName());
                            tvEmail.setText("邮箱："+patient.getEmail());
                            tvBorn.setText("生日："+patient.getBorn());
                            tvIdNumber.setText("身份证号："+patient.getId());
                            if (patient.getPhoto() != null){
                                Picasso.with(MainActivity.this)
                                        .load(MyApplication.Images_Url+patient.getPhoto())
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .networkPolicy(NetworkPolicy.NO_CACHE)
                                        .placeholder(R.drawable.head_image)
                                        .into(rivHeadImage);
                                rivHeadImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PhotoUtils.showLargePhoto(MainActivity.this,patient.getPhoto());
                                    }
                                });
                            }
                            MyApplication.patient = patient;
                        }else{
                            Toast.makeText(MainActivity.this,"获取数据失败！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    /**
     * 医生首页方法区
     */

    private void initDoctorHome(){
        refreshRegisterRecord();
        registerRecordRefreshLayout = pageDoctorHome.findViewById(R.id.register_record_refresh_layout);
        registerRecordRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshRegisterRecord();
                refreshlayout.finishRefresh();
            }
        });
    }

    private void refreshRegisterRecord(){
        HttpUtils.getRegisterFormsByDoctor(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Main","请求getRegisterFormsByDoctor失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String registerFormJson = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200 && !registerFormJson.equals("")){
                            List<RegisterForm> registerFormList = HttpUtils.gson.fromJson(registerFormJson,new TypeToken<List<RegisterForm>>(){}.getType());
                            doctorAppointRecyclerView = pageDoctorHome.findViewById(R.id.recycler_doctor_appointment);
                            doctorAppointRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            doctorAppointmentRecyclerAdapter = new DoctorAppointmentRecyclerAdapter(registerFormList);
                            doctorAppointmentRecyclerAdapter.setOnItemClickListener(new DoctorAppointmentRecyclerAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_appointment_record,null,false);
                                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                            .setView(view)
                                            .create();
                                    EditText etIllnessDescription = view.findViewById(R.id.dialog_et_illness_description);
                                    EditText etIllnessTreatment = view.findViewById(R.id.dialog_et_illness_treatment);
                                    Button btnCancel = view.findViewById(R.id.dialog_btn_register_record_cancel);
                                    btnCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.cancel();
                                        }
                                    });
                                    Button btnPost = view.findViewById(R.id.dialog_btn_register_record_post);
                                    btnPost.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            RegisterForm registerForm = registerFormList.get(position);
                                            String time = "时间："+DateUtil.FormatDate(new Date(DateUtil.getCurrentDate().getTime()),"yyyy-MM-dd HH:mm");
                                            IllnessHistory illnessHistory = new IllnessHistory();
                                            illnessHistory.setPhrId(registerForm.getPhrId());
                                            illnessHistory.setDoctorName(MyApplication.doctor.getName());

                                            if (etIllnessDescription.getText().toString().trim().equals(""))
                                                return;
                                            illnessHistory.setCondition(etIllnessDescription.getText().toString().trim());

                                            if (etIllnessTreatment.getText().toString().trim().equals(""))
                                                return;
                                            illnessHistory.setTreatment(etIllnessTreatment.getText().toString().trim());

                                            illnessHistory.setDate(registerForm.getDate());
                                            HttpUtils.insertIllnessHistory(illnessHistory,registerForm.getPatientPhone(),registerForm.getDoctorPhone(), new Callback() {
                                                @Override
                                                public void onFailure(Call call, IOException e) {
                                                    Log.e("MainActivity","请求updateHealthRecord失败");
                                                    dialog.cancel();
                                                }

                                                @Override
                                                public void onResponse(Call call, Response response) throws IOException {
                                                    String result = response.body().string();
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (response.code() == 200 && result.equals("1")){
                                                                Toast.makeText(MainActivity.this,"录入成功",Toast.LENGTH_SHORT).show();
                                                                refreshRegisterRecord();
                                                                dialog.cancel();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                    dialog.show();
                                }
                            });
                            doctorAppointRecyclerView.setAdapter(doctorAppointmentRecyclerAdapter);
                        }
                    }
                });
            }
        });
    }

    /**
     * 医生端个人中心方法区
     */

    private void initDoctorPersonalCenter(){

        /**
         * 编辑按钮点击事件
         */
        tvEditPersonal = pagePersonalCenter.findViewById(R.id.doctor_personal_tv_edit);
        tvEditPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, EditPersonalCenterActivity.class),1);
            }
        });

        rivHeadImage = pagePersonalCenter.findViewById(R.id.doctor_riv_head_image);
        tvName = pagePersonalCenter.findViewById(R.id.doctor_tv_name);
        tvNickName = pagePersonalCenter.findViewById(R.id.doctor_tv_nick_name);
        tvPhone = pagePersonalCenter.findViewById(R.id.doctor_tv_phone);
        tvGender = pagePersonalCenter.findViewById(R.id.doctor_tv_gender);
        tvAge = pagePersonalCenter.findViewById(R.id.doctor_tv_age);
        tvArea = pagePersonalCenter.findViewById(R.id.doctor_tv_area);
        tvEmail = pagePersonalCenter.findViewById(R.id.doctor_tv_email);
        tvBorn = pagePersonalCenter.findViewById(R.id.doctor_tv_born);
        tvIdNumber = pagePersonalCenter.findViewById(R.id.doctor_tv_id_number);
        refreshDoctorPersonalInformation();

        tvCareer = pagePersonalCenter.findViewById(R.id.doctor_tv_career);
        tvCareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CareerInformationActivity.class));
            }
        });

        tvChangePassword = pagePersonalCenter.findViewById(R.id.doctor_tv_change_password);
        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
            }
        });

    }

    /**
     * 刷新医生个人信息
     */
    private void refreshDoctorPersonalInformation(){

        HttpUtils.getUserInformation(MyApplication.IDENTITY_TYPE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Main","请求获取医生信息失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String doctorString = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200 && !doctorString.equals("")){
                            Gson gson = new Gson();
                            Doctor doctor = gson.fromJson(doctorString ,Doctor.class);
                            MyApplication.doctor = doctor;
                            //Log.e("Main","doctor:"+doctorString);
                            tvName.setText(doctor.getName());
                            tvPhone.setText(String.valueOf(doctor.getPhone()));
                           if (doctor.getSex() !=null &&!doctor.getSex().equals("")){
                               Log.e("Main","getSex:"+doctor.getSex());
                               tvGender.setText(doctor.getSex());
                           }
                            if (doctor.getBorn()!=null){

                                try {
                                    Date date = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(doctor.getBorn()).getTime());
                                    tvAge.setText(String.valueOf(DateUtil.getAgeByBirth(date)) +"岁");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (doctor.getArea() !=null){
                                tvArea.setText(doctor.getArea());
                            }

                            if (doctor.getNickName() !=null){
                                tvNickName.setText(doctor.getNickName());
                            }if (doctor.getEmail() !=null){
                                tvEmail.setText(doctor.getEmail());
                            }
                            tvBorn.setText(doctor.getBorn());
                            tvIdNumber.setText(doctor.getId());
                            if (doctor.getPhoto() != null){
                                Picasso.with(MainActivity.this)
                                        .load(MyApplication.Images_Url+doctor.getPhoto())
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .networkPolicy(NetworkPolicy.NO_CACHE)
                                        .into(rivHeadImage);
                                rivHeadImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PhotoUtils.showLargePhoto(MainActivity.this,doctor.getPhoto());
                                    }
                                });
                            }
                            MyApplication.doctor = doctor;
                        }else{
                            Toast.makeText(MainActivity.this,"获取数据失败！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    /**
     * 刷新个人中心页
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1){

            if (MyApplication.IDENTITY_TYPE ==MyApplication.IDENTITY_TYPE_PATIENT)
            refreshPersonalInformation();
            else
                refreshDoctorPersonalInformation();
        }
    }
}
