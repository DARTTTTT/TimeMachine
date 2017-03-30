package com.qf.wrglibrary.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;

/**
 * Created by 翁 on 2017/1/20.
 */

public class MySnackbarUtil {
    private Context context;
    private String name;
    private View view;

    public MySnackbarUtil(Context context, View view, String name) {
        this.context = context;
        this.name = name;
        this.view = view;
        TSnackbar snackbar = TSnackbar
                .make(view, name, TSnackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();

        snackbarView.setBackgroundColor(Color.parseColor("#e93030"));
        TextView textView= (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.parseColor("#ffffff"));
        snackbar.show();
    }


}
