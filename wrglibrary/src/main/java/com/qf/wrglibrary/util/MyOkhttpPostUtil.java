package com.qf.wrglibrary.util;

import android.content.Context;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 翁 on 2017/1/11.
 */

public class MyOkhttpPostUtil {


    private Context context;


    public MyOkhttpPostUtil(Context context) {
        this.context = context;
    }


    public String getPostjson(String url, Map<String, String> requestBody) {

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), JsonUtil.objectToJson(requestBody));

        final OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = null;
        //Log.d("print", "getPostjson: 0 "+JsonUtil.objectToJson(requestBody)+" ----------  "+request);
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String string = null;
        if (response != null && response.body() != null) {
            try {
                string = response.body().string();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return string;

    }


}
