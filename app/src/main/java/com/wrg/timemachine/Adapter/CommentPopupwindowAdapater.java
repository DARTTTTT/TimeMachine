package com.wrg.timemachine.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wrg.timemachine.Entity.LongCommentEntity;
import com.wrg.timemachine.R;

import java.util.List;

/**
 * Created by 翁 on 2017/3/2.
 */

public class CommentPopupwindowAdapater extends BaseAdapter {


    private Context context;
    private List<LongCommentEntity.CommentsBean> datas;
    public CommentPopupwindowAdapater(Context context) {
        this.context=context;

    }
    public void setDatas(List<LongCommentEntity.CommentsBean> datas){
        this.datas=datas;
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView!=null){
            holder= (ViewHolder) convertView.getTag();

        }else {
            convertView= LayoutInflater.from(context).inflate(R.layout.item_popupwindow,parent,false);
            holder=new ViewHolder();
            holder.tv_kind= (TextView) convertView.findViewById(R.id.tv_item_popupwindow);
            holder.author= (TextView) convertView.findViewById(R.id.author);
            holder.avatar= (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(holder);
        }



            holder.tv_kind.setText(datas.get(position).getContent());
            holder.author.setText(datas.get(position).getAuthor());
        Glide.with(context)
                .load(datas.get(position).getAvatar())
                .centerCrop()
                .into(holder.avatar);

        return convertView;
    }
    class ViewHolder{
        TextView tv_kind,author;
        ImageView avatar;
    }
}
