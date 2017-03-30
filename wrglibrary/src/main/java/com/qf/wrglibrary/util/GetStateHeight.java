package com.qf.wrglibrary.util;

import android.content.Context;

/**
 * Created by 翁 on 2017/1/10.
 */

public class GetStateHeight {
    public Context context;

    public int getStateHeight(Context context) {
        this.context = context;

        int statusBarHeight1 = -1;
//获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight1;
    }
}
