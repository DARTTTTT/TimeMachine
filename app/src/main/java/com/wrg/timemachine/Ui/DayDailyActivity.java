package com.wrg.timemachine.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qf.wrglibrary.base.BaseActivity;
import com.qf.wrglibrary.util.MyRetrofitUtil;
import com.qf.wrglibrary.util.Network;
import com.qf.wrglibrary.widget.rewriterview.FullyLinearLayoutManager;
import com.wrg.timemachine.Adapter.ThemeAdapter;
import com.wrg.timemachine.Constants.Constant;
import com.wrg.timemachine.Entity.RefreshEntity;
import com.wrg.timemachine.Entity.ThemeDetailEntity;
import com.wrg.timemachine.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 翁 on 2017/3/13.
 */

public class DayDailyActivity extends BaseActivity implements MyRetrofitUtil.DownListener, ThemeAdapter.ItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final int REFRESH_COMPLETE = 0X110;

    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private ThemeAdapter themeAdapter;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;

    @Bind(R.id.daliy_name)
    TextView daily_name;

    @Bind(R.id.daily_content)
    TextView daily_content;

    /*@Bind(R.id.daliy_cover)
    ImageView daily_cover;*/



    @Bind(R.id.top_cover)
    ImageView top_cover;
    private String id;

    @Override
    protected int getContentId() {
        return R.layout.activity_daydaily;
    }


    @Override
    protected void init() {
        super.init();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        id = bundle.getString("id");
        String cover = bundle.getString("cover");
        String name = bundle.getString("name");
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                boolean networkAvailable = Network.isNetworkAvailable(getApplicationContext());

                if (networkAvailable == false) {
                    Toast.makeText(getApplicationContext(), "当前没有网络，请连接网络。", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);

                }
                loadDatas();
            }
        });
        daily_name.setText(name);
       /* Glide.with(this).load(cover)
                .centerCrop()
                .into(daily_cover);*/
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        themeAdapter=new ThemeAdapter(this);
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(this));
        recyclerView.setAdapter(themeAdapter);
        themeAdapter.setmListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorBlack));
        swipeRefreshLayout.setOnRefreshListener(this);
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    loadDatas();
                    EventBus.getDefault().post(new RefreshEntity("refresh",0));
                    swipeRefreshLayout.setRefreshing(false);//停止转圈圈
                    boolean networkAvailable = Network.isNetworkAvailable(getApplicationContext());

                    if (networkAvailable == false) {
                        Toast.makeText(getApplicationContext(), "当前没有网络，请连接网络。", Toast.LENGTH_LONG).show();

                    }
                    break;


            }
        }


    };


    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);

    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        String Day_url = String.format(Constant.THEMEDETAIL, id);
        new MyRetrofitUtil(this).setDownListener(this).downJson(Day_url, 0x001);

    }

    @OnClick({R.id.back})
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;


        }
    }

    @Override
    public boolean isOpenStatus() {
        return true;
    }

    @Override
    public Object paresJson(String json, int requestCode) {
        if (json != null) {


            switch (requestCode) {
                case 0x001:
                    return new Gson().fromJson(json, ThemeDetailEntity.class);
            }
        }
        return null;
    }

    @Override
    public void downSucc(Object object, int requestCode) {
        if (object != null) {
            switch (requestCode) {
                case 0x001:
                ThemeDetailEntity themeDetailEntity= (ThemeDetailEntity) object;
                    List<ThemeDetailEntity.StoriesBean> stories = themeDetailEntity.getStories();
                    String background = themeDetailEntity.getBackground();
                    Glide.with(getApplicationContext()).load(background)
                            .centerCrop()
                            .into(top_cover);
                    themeAdapter.setDatas(stories);
                    daily_content.setText(themeDetailEntity.getDescription());
                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 500);
                    break;
            }
        }
    }

    @Override
    public void onItemClick(View view, int position, List<ThemeDetailEntity.StoriesBean> datas) {
        String id = datas.get(position).getId();

        Intent intent = new Intent(new Intent(this, ThemeDetailActivity.class));
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
      /*  bundle.putInt("position",position);
        bundle.putSerializable("datas", (Serializable) datas);*/
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }


    /*@Override
    public void setTheme(boolean isNights, AppCompatActivity activity) {
        super.setTheme(isNights, activity);
        setTheme(DayDailyActivity.this);
    }*/
}
