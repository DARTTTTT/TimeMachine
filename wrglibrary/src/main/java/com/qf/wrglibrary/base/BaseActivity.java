package com.qf.wrglibrary.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.qf.wrglibrary.R;
import com.qf.wrglibrary.util.ScreenUtil;

import butterknife.ButterKnife;
import skin.support.SkinCardViewManager;
import skin.support.SkinCompatManager;
import skin.support.design.SkinMaterialManager;

/**
 * Created by Ken on 2016/9/26.9:47
 * Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity {


    private FragmentManager fragmentManager;
    //当前正在展示的Fragment
    private BaseFragment showFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentId());
        SkinMaterialManager.init(this);          // material design 控件换肤初始化[可选]
        SkinCardViewManager.init(this);          // CardView 控件换肤初始化[可选]
        SkinCompatManager.init(this).loadSkin(); // 基础控件换肤初始化并加载当前皮肤库(保存在SharedPreferences中)
        //NoHttp.initialize(this);
        //注册activity
        ButterKnife.bind(this);


        //获得FragmentManager对象
        fragmentManager = getSupportFragmentManager();

        /**
         * 沉浸式状态栏
         */
        if (isOpenStatus()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            //获得状态栏的高度
            int height = ScreenUtil.getStatusHeight(this);
            if (height != -1) {
                //设置Padding
                View view = findViewById(R.id.actionbar);
                if (view != null) {
                    view.setPadding(0, height, 0, 0);
                }
            }
        }

        init();
        bindListener();
        loadDatas();
    }


    /**
     * 初始化
     */
    protected void init() {

    }

    /**
     * 绑定监听
     */
    protected void bindListener() {

    }

    /**
     * 加载数据
     */
    protected void loadDatas() {

    }

    /**
     * 按两次退出app
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 以动画的方式启动activity
     *
     * @param intent
     * @param animinid
     * @param animoutid
     */
    public void startActivityForAnimation(Intent intent, int animinid, int animoutid) {
        startActivity(intent);
        overridePendingTransition(animinid, animoutid);
    }

    /**
     * 展示Fragment
     */
    protected void showFragment(int resid, BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //隐藏正在暂时的Fragment
        if (showFragment != null) {
            fragmentTransaction.hide(showFragment);
        }

        //展示需要显示的Fragment对象
        Fragment mFragment = fragmentManager.findFragmentByTag(fragment.getClass().getName());
        if (mFragment != null) {
            fragmentTransaction.show(mFragment);
            showFragment = (BaseFragment) mFragment;
        } else {
            fragmentTransaction.add(resid, fragment, fragment.getClass().getName());
            showFragment = fragment;
        }

        fragmentTransaction.commit();
    }

    /**
     * 获得activity显示的布局ID
     *
     * @return
     */
    protected abstract int getContentId();

    /**
     * 是否打开沉浸式状态栏
     *
     * @return
     */
    public boolean isOpenStatus() {
        return true;
    }


/*    public static void setDayTheme(AppCompatActivity activity) {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
        activity.getDelegate().setLocalNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
        // 调用 recreate() 使设置生效
        activity.recreate();
    }

    public static void setNightTheme(AppCompatActivity activity) {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);
        activity.getDelegate().setLocalNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);
        // 调用 recreate() 使设置生效
        activity.recreate();
    }

    public void setTheme(boolean isNights, AppCompatActivity activity) {
        if (isNights) {
            setNightTheme(activity);
        } else {
            setDayTheme(activity);
        }
    }

    public void setTheme(AppCompatActivity activity) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        activity.recreate();
    }*/
}
