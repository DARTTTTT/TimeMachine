package com.wrg.timemachine.Rewrite;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by 翁 on 2017/3/14.
 */

public class HeaderViewRecyclerAdapter extends RecyclerView.Adapter {

    private RecyclerView.Adapter mAdapter;
    ArrayList<View> mHeaderViewInfos;
    ArrayList<View> mFooterViewInfos;


    public HeaderViewRecyclerAdapter(ArrayList<View> headerViewInfos, ArrayList<View> footerViewInfos, RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        if (headerViewInfos == null) {
            mHeaderViewInfos = new ArrayList<View>();
        } else {
            mHeaderViewInfos = headerViewInfos;
        }
        if (footerViewInfos == null) {
            mFooterViewInfos = new ArrayList<View>();
        } else {
            mFooterViewInfos = footerViewInfos;
        }

    }

    //判读当前条目是什么类型的--决定渲染什么视图
    @Override
    public int getItemViewType(int position) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {    //是头部
            return RecyclerView.INVALID_TYPE;
        }
        //正常条目部分
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }
        //footer部分
        return RecyclerView.INVALID_TYPE - 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //header
        if(viewType == RecyclerView.INVALID_TYPE){//头部
            return new HeadViewHolder(mHeaderViewInfos.get(0));
        }else if(viewType == RecyclerView.INVALID_TYPE-1){//尾部
            return new HeadViewHolder(mFooterViewInfos.get(0));
        }
        return mAdapter.onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //也要划分三个区域
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {    //是头部
            return;
        }
        //adapter body
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mAdapter.onBindViewHolder(holder,adjPosition);
                return ;
            }
        }
        //footer部分

    }

    @Override
    public int getItemCount() {
        if (mAdapter != null) {
            return getFootersCount() + getHeadersCount() + mAdapter.getItemCount();
        } else {
            return getFootersCount() + getHeadersCount();
        }
    }

    public int getHeadersCount() {
        return mHeaderViewInfos.size();
    }

    public int getFootersCount() {
        return mFooterViewInfos.size();
    }

    private static class HeadViewHolder extends RecyclerView.ViewHolder{

        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }
}
