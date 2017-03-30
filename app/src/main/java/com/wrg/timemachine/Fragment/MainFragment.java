package com.wrg.timemachine.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qf.wrglibrary.base.BaseFragment;
import com.qf.wrglibrary.util.MyRetrofitUtil;
import com.qf.wrglibrary.util.Network;
import com.wrg.timemachine.Adapter.RecyclerAdapter;
import com.wrg.timemachine.Constants.Constant;
import com.wrg.timemachine.Entity.RefreshEntity;
import com.wrg.timemachine.Entity.ZhihuListEntity;
import com.wrg.timemachine.R;
import com.wrg.timemachine.Ui.NewsDetailActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;

/**
 * Created by 翁 on 2017/3/1.
 */

public class MainFragment extends BaseFragment implements MyRetrofitUtil.DownListener, SwipeRefreshLayout.OnRefreshListener, RecyclerAdapter.ItemClickListener {
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;

    private static final int REFRESH_COMPLETE = 0X110;

    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;

   /* @Bind(R.id.now_date)
    TextView now_date;*/

    @Override
    protected int getContentId() {
        return R.layout.fragment_main;


    }


    @Override
    protected void loadDatas() {
        super.loadDatas();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        String datenow = formatter.format(calendar.getTime());
        Log.d("print", "loadDatas: 当前的日期" + datenow);

        String zhihuurl = String.format(Constant.ZHIHULIST, datenow);
        new MyRetrofitUtil(getContext()).setDownListener(this).downJson(zhihuurl, 0x001);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getYeserday(String datenow) {
        Log.d("print", "getYeserday: "+datenow);
        String zhihuurl = String.format(Constant.ZHIHULIST, datenow);
        new MyRetrofitUtil(getContext()).setDownListener(this).downJson(zhihuurl, 0x001);
    }

    @Override
    protected void init(View view) {
        super.init(view);
        EventBus.getDefault().register(this);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                boolean networkAvailable = Network.isNetworkAvailable(getContext());

                if (networkAvailable == false) {
                    Toast.makeText(getContext(), "当前没有网络，请连接网络。", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);

                }
                loadDatas();
            }
        });

        loadDatas();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter(mRecyclerView.getContext()));
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter.setmListener(this);
    }


    @Override
    public Object paresJson(String json, int requestCode) {
        if (json != null) {
            return new Gson().fromJson(json.toString(), ZhihuListEntity.class);
        }
        return null;
    }

    @Override
    public void downSucc(Object object, int requestCode) {
        if (object != null) {
            ZhihuListEntity zhihuListEntity = (ZhihuListEntity) object;
            Log.d("print", "downSucc: " + zhihuListEntity.toString());
            List<ZhihuListEntity.StoriesBean> stories = zhihuListEntity.getStories();
          /*  String date = zhihuListEntity.getDate();
            String s = stories.get(0).getImages().get(0);
            Log.d("print", "downSucc: 图片"+s);
            String dates=date.substring(0,4)+"年"+date.substring(4,6)+"月"+date.substring(6,8)+"日";
            now_date.setText("--"+dates+"--");*/
            mAdapter.setDatas(stories);
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 500);
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    loadDatas();
                    EventBus.getDefault().post(new RefreshEntity("refresh",0));
                    swipeRefreshLayout.setRefreshing(false);//停止转圈圈
                    boolean networkAvailable = Network.isNetworkAvailable(getContext());

                    if (networkAvailable == false) {
                        Toast.makeText(getContext(), "当前没有网络，请连接网络。", Toast.LENGTH_LONG).show();

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
    public void onItemClick(View view, int position, List<ZhihuListEntity.StoriesBean> datas) {
        String id = datas.get(position).getId();

        Intent intent = new Intent(new Intent(getContext(), NewsDetailActivity.class));
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putInt("position",position);
        bundle.putSerializable("datas", (Serializable) datas);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
        //getActivity().overridePendingTransition(R.anim.zoomout, R.anim.zoomin);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
