package com.wrg.timemachine.Ui;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qf.wrglibrary.base.BaseActivity;
import com.qf.wrglibrary.util.MyRetrofitUtil;
import com.squareup.picasso.Picasso;
import com.wrg.timemachine.Adapter.MyPagerAdapter;
import com.wrg.timemachine.Constants.Constant;
import com.wrg.timemachine.Entity.RefreshEntity;
import com.wrg.timemachine.Entity.ZhihuListEntity;
import com.wrg.timemachine.Entity.ZhihudetailEntity;
import com.wrg.timemachine.Fragment.FindFragment;
import com.wrg.timemachine.Fragment.MainFragment;
import com.wrg.timemachine.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;


public class SecondActivity extends BaseActivity implements MyRetrofitUtil.DownListener, TabLayout.OnTabSelectedListener {

    @Bind(R.id.vp)
    ViewPager mViewPager;

   /* @Bind(now_date)
    TextView date;*/

    @Bind(R.id.text_yesterday)
    TextView text_yesterday;


    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Bind(R.id.top_toobar)
    LinearLayout top_layout;

    private int[] mImageArray, mColorArray;
    private final String[] mTitles = {"碎片", "发现"};
    private String datenow;
    private Calendar calendar;
    private SimpleDateFormat formatter;


    @Override
    protected int getContentId() {
        return R.layout.activity_second_coor;
    }


    @Override
    protected void init() {
        super.init();
        EventBus.getDefault().register(this);
        formatter = new SimpleDateFormat("yyyyMMdd");
        calendar = Calendar.getInstance();
        tabLayout.setupWithViewPager(mViewPager);
        Log.d("print", "loadDatas: 当前的日期" + datenow);
        initViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(this);



    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        formatter = new SimpleDateFormat("yyyyMMdd");
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        datenow = formatter.format(calendar.getTime());
        String date_now = String.format(Constant.ZHIHULIST, datenow);
        Log.d("print", "loadDatas:初始化的 " + date_now);
        new MyRetrofitUtil(this).setDownListener(this).downJson(date_now, 0x001);

    }

    private void loadImages(ImageView imageView, String url) {
        Picasso.with(SecondActivity.this).load(url).into(imageView);
    }

    //刷新让头部的图片和当前的日期一致
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRefresh(RefreshEntity entity) {
        if (entity.getState() == 0) {
            loadDatas();
        }
    }

    private void initViewPager(ViewPager viewPager) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myPagerAdapter.addFragment(new MainFragment(), mTitles[0]);
        myPagerAdapter.addFragment(new FindFragment(), mTitles[1]);
        viewPager.setAdapter(myPagerAdapter);
    }

    /**
     * 菜单功能键
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        switch (item.getItemId()) {
            case R.id.yeserday:
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                datenow = formatter.format(calendar.getTime());
                EventBus.getDefault().post(datenow);
               // date.setText(datenow.toString());
                String date_now = String.format(Constant.ZHIHULIST, datenow);
                new MyRetrofitUtil(this).setDownListener(this).downJson(date_now, 0x001);

                break;
            case R.id.huanfu:

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean isOpenStatus() {
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public Object paresJson(String json, int requestCode) {
        if (json != null) {
            switch (requestCode) {
                case 0x001:
                    return new Gson().fromJson(json, ZhihuListEntity.class);
                case 0x002:
                    return new Gson().fromJson(json, ZhihudetailEntity.class);
            }
        }
        return null;
    }

    @Override
    public void downSucc(Object object, int requestCode) {
        if (object != null) {
            switch (requestCode) {
                case 0x001:
                    ZhihuListEntity data = (ZhihuListEntity) object;
                    String id = data.getStories().get(0).getId();
                    String detail_url = String.format(Constant.ZHIHUDETAIL, id);
                    new MyRetrofitUtil(this).setDownListener(this).downJson(detail_url, 0x002);
                    break;
                case 0x002:
                    ZhihudetailEntity zhihudetailEntity = (ZhihudetailEntity) object;
                    final String image = zhihudetailEntity.getImage();
                    /*CoordinatorTabLayout(image);*/

                    break;


            }
        }
    }

/*
    public void CoordinatorTabLayout(final String img) {
        mCoordinatorTabLayout
                .setTitle("时光机")
                .setBackEnable(false)
                .setLoadHeaderImagesListener(new LoadHeaderImagesListener() {
                    @Override
                    public void loadHeaderImages(ImageView imageView, TabLayout.Tab tab) {
                        switch (tab.getPosition()) {
                            case 0:
                                loadImages(imageView, img);
                                break;
                            case 1:
                                loadImages(imageView,"http://p2.zhimg.com/10/7b/107bb4894b46d75a892da6fa80ef504a.jpg");
                                break;
                        }
                    }
                })
                .setupWithViewPager(mViewPager);
    }*/


        @OnClick({R.id.text_yesterday})
        public void btnClick(View view){
            switch (view.getId()){
                case R.id.text_yesterday:
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    datenow = formatter.format(calendar.getTime());
                    EventBus.getDefault().post(datenow);
                    String date_now = String.format(Constant.ZHIHULIST, datenow);
                    new MyRetrofitUtil(this).setDownListener(this).downJson(date_now, 0x001);
                    break;
            }
        }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()){
            case 0:
                text_yesterday.setVisibility(View.VISIBLE);
                break;
            case 1:
                text_yesterday.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
