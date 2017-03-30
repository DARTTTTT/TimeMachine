package com.wrg.timemachine.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wrg.timemachine.Entity.ThemeEntity;
import com.wrg.timemachine.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 翁 on 2017/3/14.
 */

public class FindGridAdapter extends BaseAdapter {
    private Context context;
    private List<ThemeEntity.OthersBean> datas;

    public FindGridAdapter(Context context) {
        this.context = context;
        datas = new ArrayList<>();

    }

    public void setDatas(List<ThemeEntity.OthersBean> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if (view!=null){
            viewHolder= (ViewHolder) view.getTag();
        }else {
            viewHolder=new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.item_find_grid,null);
             viewHolder.title= (TextView) view.findViewById(R.id.tv_num);
            viewHolder.img= (ImageView) view.findViewById(R.id.zhihu_img);
            view.setTag(viewHolder);
        }

         viewHolder.title.setText(datas.get(i).getName());
        Glide.with(context)
                .load(datas.get(i).getThumbnail())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.img);


        return view;
    }


    class ViewHolder{
        TextView title;
        ImageView img;
    }
}
