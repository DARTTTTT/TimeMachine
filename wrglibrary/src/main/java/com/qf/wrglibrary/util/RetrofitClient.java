package com.qf.wrglibrary.util;

import android.content.Context;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/27 0027.
 * Author by RuiGan
 */

public class RetrofitClient {

    private static Context context;
    private PersistentCookieStore cookieStore;

    private static RetrofitClient INSTANCE = null;
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;
    private CookieHandler cookieHandler;

    public CookieHandler getCookieHandler() {
        return cookieHandler;
    }

    public void setCookieHandler(CookieHandler cookieHandler) {
        this.cookieHandler = cookieHandler;
    }

    private MyApiService apiService;


    public static RetrofitClient getInstance(Context context) {

        if (INSTANCE != null) {
            return INSTANCE;
        } else if (RetrofitClient.context == null) {
            RetrofitClient.context = context;
        }
        INSTANCE = new RetrofitClient(context);

        return INSTANCE;
    }


    /**
     * 构造方法
     */
    private RetrofitClient(Context context) {

        /*okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();*/
       /* CookieHandler cookieHandler = new CookieManager(new PersistentCookieStore(context),
                CookiePolicy.ACCEPT_ALL);*/
        //Log.d("print", "RetrofitClient: " + cookieHandler);

        cookieStore = new PersistentCookieStore(context);

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(7676, TimeUnit.MILLISECONDS)
                .connectTimeout(7676, TimeUnit.MILLISECONDS)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        if (cookies != null && cookies.size() > 0) {
                            for (Cookie item : cookies) {
                                cookieStore.add(url, item);
                            }
                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url);
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();


        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(MyApiService.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    /**
     * 创建Retrofit对象
     */
    public RetrofitClient create() {

        apiService = retrofit.create(MyApiService.class);
        return this;
    }

    /**
     * 使用自定义的接口
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> tClass) {
        T apiService = retrofit.create(tClass);
        return apiService;
    }

    /**
     * 下载JSON
     */
    public void getDatas(String jsonUrl, Action1<String> action1) {
        Observable<String> json = apiService.getJSON(jsonUrl);
        json.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }


}
