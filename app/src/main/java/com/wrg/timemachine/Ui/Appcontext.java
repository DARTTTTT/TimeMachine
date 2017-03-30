package com.wrg.timemachine.Ui;

import android.app.Application;

import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * Created by 翁 on 2017/3/8.
 */

public class Appcontext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinCompatManager.init(this)                          // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())  // material design 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())      // CardView 控件换肤初始化[可选]
                .loadSkin();
    }
}
