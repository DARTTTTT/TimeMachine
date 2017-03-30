package com.wrg.timemachine.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wrg.timemachine.Entity.ThemeDetailEntity;
import com.wrg.timemachine.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 翁 on 2017/3/1.
 */

public class ThemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ThemeDetailEntity.StoriesBean> datas;

    private ItemClickListener mListener;


    public void setmListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }

    public ThemeAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();

    }

    public void setDatas(List<ThemeDetailEntity.StoriesBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.item_main_noimg, parent, false);
                holder = new OneViewHolder(view);
                break;
            case 1:
            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.item_main_noimg, parent, false);
                holder = new TwoViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                OneViewHolder oneholder = (OneViewHolder) holder;
                oneholder.tv.setText(datas.get(position).getTitle());
                break;
            case 1:
            case 2:
                TwoViewHolder twoholder = (TwoViewHolder) holder;
                twoholder.tv.setText(datas.get(position).getTitle());
               /* String s = datas.get(position).getImages().get(0);
                if (!TextUtils.isEmpty(s)&&s!=null){
                    Glide.with(context)
                            .load(s)
                            .into(twoholder.img);

                }*/

                break;
        }


    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class OneViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv;

        public OneViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_num);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition(), datas);
            }
        }
    }

    class TwoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv;
        ImageView img;

        public TwoViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_num);
           // img = (ImageView) itemView.findViewById(R.id.zhihu_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getPosition(), datas);
            }
        }
    }


    public interface ItemClickListener {
        void onItemClick(View view, int position, List<ThemeDetailEntity.StoriesBean> datas);

    }


    @Override
    public int getItemViewType(int position) {
        int type = datas.get(position).getType();
        Log.d("print", "getItemViewType:类型 " + type);
        switch (type) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
        }
        return 3;
    }


}
