package com.wrg.timemachine.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qf.wrglibrary.base.BaseFragment;
import com.qf.wrglibrary.util.MyRetrofitUtil;
import com.qf.wrglibrary.util.Network;
import com.qf.wrglibrary.widget.rewriterview.RewriteGridView;
import com.wrg.timemachine.Adapter.FindGridAdapter;
import com.wrg.timemachine.Adapter.FindRecyclerAdapter;
import com.wrg.timemachine.Adapter.HotListAdapter;
import com.wrg.timemachine.Adapter.HotRecyclerAdapter;
import com.wrg.timemachine.Constants.Constant;
import com.wrg.timemachine.Entity.HotEntity;
import com.wrg.timemachine.Entity.ThemeEntity;
import com.wrg.timemachine.R;
import com.wrg.timemachine.Ui.DayDailyActivity;
import com.wrg.timemachine.Ui.ThemeDetailActivity;

import java.util.List;

import butterknife.Bind;

/**
 * Created by 翁 on 2017/3/13.
 */

public class FindFragment extends BaseFragment implements MyRetrofitUtil.DownListener, SwipeRefreshLayout.OnRefreshListener, FindRecyclerAdapter.ItemClickListener, HotRecyclerAdapter.ItemClickListener, AdapterView.OnItemClickListener {
    /*@Bind(R.id.recyclerview)
    RewriteGridView recyclerView;*/
    private RewriteGridView recyclerView;


    @Bind(R.id.recyclerview_hot)
    ListView recyclerView_hot;

    private HotListAdapter recyclerAdapter;

    private static final int REFRESH_COMPLETE = 0X110;

    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private FindGridAdapter findRecyclerAdapter;




    @Override
    protected int getContentId() {
        return R.layout.fragment_find;
    }


    @Override
    protected void init(View view) {
        super.init(view);

        View headview= LayoutInflater.from(getContext()).inflate(R.layout.recycler_head_item,null);
        recyclerView= (RewriteGridView) headview.findViewById(R.id.recyclerview);

        findRecyclerAdapter=new FindGridAdapter(getContext());
        //recyclerView.setLayoutManager(new FullyGridLayoutManager(recyclerView.getContext(),3));
        recyclerView.setAdapter(findRecyclerAdapter);
        recyclerView.setOnItemClickListener(this);

        recyclerAdapter=new HotListAdapter(getContext());
        //recyclerView_hot.setLayoutManager(new FullyLinearLayoutManager(recyclerView_hot.getContext()));
        //recyclerAdapter.setmListener(this);
        recyclerView_hot.addHeaderView(headview);
        recyclerView_hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object adapter = adapterView.getAdapter();
                HeaderViewListAdapter hotListAdapter= (HeaderViewListAdapter) adapter;
                Object item = hotListAdapter.getItem(i);
                HotEntity.TopStoriesBean data= (HotEntity.TopStoriesBean) item;

                String id = data.getId();

                Intent intent = new Intent(new Intent(getContext(), ThemeDetailActivity.class));
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                intent.putExtra("bundle", bundle);
                startActivity(intent);

            }
        });

        recyclerView_hot.setAdapter(recyclerAdapter);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(this);
        //findRecyclerAdapter.setmListener(this);



    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    loadDatas();
                    //EventBus.getDefault().post(new RefreshEntity("refresh",0));
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
    protected void loadDatas() {
        super.loadDatas();
        new MyRetrofitUtil(getContext()).setDownListener(this).downJson(Constant.ZHIHUTHEME,0x001);

        new MyRetrofitUtil(getContext()).setDownListener(this).downJson(Constant.ZHIHUTOP,0x002);
    }

    @Override
    public Object paresJson(String json, int requestCode) {
        if (json!=null){
            switch (requestCode){
                case 0x001:
                    Log.d("print", "paresJson: 发现页面的"+json);
                    return new Gson().fromJson(json, ThemeEntity.class);
                case 0x002:
                    return new Gson().fromJson(json, HotEntity.class);
            }
        }

        return null;
    }

    @Override
    public void downSucc(Object object, int requestCode) {
        if (object!=null){
            switch (requestCode){
                case 0x001:
                    ThemeEntity themeEntity= (ThemeEntity) object;
                    List<ThemeEntity.OthersBean> others = themeEntity.getOthers();
                    Log.d("print", "downSucc: "+others.toString());
                    findRecyclerAdapter.setDatas(others);
                    break;
                case 0x002:
                    HotEntity hotEntity= (HotEntity) object;
                    List<HotEntity.TopStoriesBean> top_stories = hotEntity.getTop_stories();
                    recyclerAdapter.setDatas(top_stories);
            }
        }
    }

    @Override
    public void onItemClick(View view, int position, List<ThemeEntity.OthersBean> datas) {
        String id = datas.get(position).getId();
        Log.d("print", "onItemClick:点击的 "+id);
        Intent intent=new Intent(getContext(), DayDailyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("cover",datas.get(position).getThumbnail());
        bundle.putString("name",datas.get(position).getName());
        //bundle.putInt("position",position);
        //bundle.putSerializable("datas", (Serializable) datas);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }


    @Override
    public void hotOnItemClick(View view, int position, List<HotEntity.TopStoriesBean> datas) {
        String id = datas.get(position).getId();

        Intent intent = new Intent(new Intent(getContext(), ThemeDetailActivity.class));
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        /*bundle.putInt("position",position);
        bundle.putSerializable("datas", (Serializable) datas);*/
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Object adapter = adapterView.getAdapter();
        FindGridAdapter hotListAdapter= (FindGridAdapter) adapter;
        Object item = hotListAdapter.getItem(i);
        ThemeEntity.OthersBean data= (ThemeEntity.OthersBean) item;
        String id = data.getId();
        Log.d("print", "onItemClick:点击的 "+id);
        Intent intent=new Intent(getContext(), DayDailyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("cover",data.getThumbnail());
        bundle.putString("name",data.getName());
        //bundle.putInt("position",position);
        //bundle.putSerializable("datas", (Serializable) datas);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }
}
