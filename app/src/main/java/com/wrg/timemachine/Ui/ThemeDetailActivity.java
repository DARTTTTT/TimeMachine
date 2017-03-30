package com.wrg.timemachine.Ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.qf.wrglibrary.base.BaseActivity;
import com.qf.wrglibrary.util.MyRetrofitUtil;
import com.qf.wrglibrary.util.Network;
import com.wrg.timemachine.Adapter.CommentPopupwindowAdapater;
import com.wrg.timemachine.Constants.Constant;
import com.wrg.timemachine.Entity.CommentEntity;
import com.wrg.timemachine.Entity.LongCommentEntity;
import com.wrg.timemachine.Entity.ZhihuListEntity;
import com.wrg.timemachine.Entity.ZhihudetailEntity;
import com.wrg.timemachine.R;
import com.wrg.timemachine.Rewrite.MyScrollView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;

import static com.wrg.timemachine.R.id.long_comments;

/**
 * Created by 翁 on 2017/3/8.
 */
@SuppressLint("SetJavaScriptEnabled")
public class ThemeDetailActivity extends BaseActivity implements MyRetrofitUtil.DownListener {

    private String id;

    private static final int REFRESH_COMPLETE = 0X110;

    private PopupWindow mPopWindow;
    private ListView listview;
    private CommentPopupwindowAdapater popadapter;

    private ImageView mImageview;


    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

   /* @Bind(R.id.detail_title)
    TextView detail_title;*/

    @Bind(long_comments)
    TextView text_commment;

    /*  @Bind(R.id.detail_image)
      ImageView detail_image;
  */
  /*  @Bind(R.id.detail_resource)
    TextView detail_resource;
*/
    @Bind(R.id.scrollView)
    MyScrollView scrollView;

    @Bind(R.id.webview)
    WebView webView;


    private String detail_url;


    private int size = 16;
    private String html;


    private List<String> idDatas = new ArrayList<>();
    private int position;
    private String detail_url_next;
    private String comment_count;
    private String comment_long;


    @Override
    protected int getContentId() {
        return R.layout.activity_themedetail;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void init() {
        super.init();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        id = bundle.getString("id");
        position = bundle.getInt("position") + 1;
        popadapter = new CommentPopupwindowAdapater(this);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                boolean networkAvailable = Network.isNetworkAvailable(getApplicationContext());

                if (networkAvailable == false) {
                    Toast.makeText(getApplicationContext(), "当前没有网络，请连接网络。", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);

                }
                loadDatas();
            }
        });
        swipeRefreshLayout.setEnabled(false);
        List<ZhihuListEntity.StoriesBean> datas = (List<ZhihuListEntity.StoriesBean>) bundle.getSerializable("datas");

       /* for (int i = 0; i < datas.size(); i++) {
            idDatas.add(datas.get(i).getId());
        }
        Log.d("print", "init:得到当天所有的数据ID " + idDatas.toString());*/
        webView.getSettings().getJavaScriptEnabled();
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setSupportZoom(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);


        //rg.setOnCheckedChangeListener(this);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorBlack));
        //swipeRefreshLayout.setOnRefreshListener(this);

        webView.addJavascriptInterface(new JavascriptInterface(this), "imagelistner");
        webView.setWebViewClient(new MyWebViewClient());
    }


    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        public void openImage(String img) {
            Intent intent = new Intent();
            intent.putExtra("image", img);
            intent.setClass(context, ShowWebImageActivity.class);
            context.startActivity(intent);
            //overridePendingTransition(R.anim.zoomout, R.anim.zoomin);

        }
    }

    // 监听
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            view.getSettings().setJavaScriptEnabled(true);

            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);

        }
    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }


    @Override
    protected void loadDatas() {
        super.loadDatas();
        detail_url = String.format(Constant.ZHIHUDETAIL, id);
        new MyRetrofitUtil(this).setDownListener(this).downJson(detail_url, 0x001);

        comment_count = String.format(Constant.ZHIHUCOMMENT, id);
        new MyRetrofitUtil(this).setDownListener(this).downJson(comment_count, 0x002);

        comment_long = String.format(Constant.ZHIHUCOMMENT_LONG, id);
        new MyRetrofitUtil(this).setDownListener(this).downJson(comment_long, 0x003);

    }

    @Override
    public boolean isOpenStatus() {
        return true;
    }

    @OnClick({R.id.back, long_comments})
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        /*    case R.id.next:
                if (position < idDatas.size()) {
                    String id = idDatas.get(position);
                    detail_url_next = String.format(Constant.ZHIHUDETAIL, id);
                    comment_count = String.format(Constant.ZHIHUCOMMENT, id);
                    comment_long = String.format(Constant.ZHIHUCOMMENT_LONG, id);

                    position++;
                    getNext();
                } else {
                    Toast.makeText(this, "到底啦！", Toast.LENGTH_SHORT).show();
                    return;
                }

                break;*/
            case long_comments:
                showPopupWindow(view);
                break;


        }

    }


    /**
     * 展示popupwindow
     *
     * @param view
     */
    public void showPopupWindow(View view) {
        View popview = LayoutInflater.from(this).inflate(R.layout.commentpopupwindow_layout, null);
        mPopWindow = new PopupWindow(popview);
        int screenWith = this.getWindowManager().getDefaultDisplay().getWidth();
        listview = (ListView) popview.findViewById(R.id.pop_lv);

        listview.setAdapter(popadapter);
        mPopWindow.setWidth((screenWith * 9) / 11);
        mPopWindow.setHeight((screenWith * 9) / 7);

        backgroundAlpha(0.5f);
        mPopWindow.setFocusable(true);
        //mPopWindow.setAnimationStyle(R.style.mypopwindow_anim_style);


        mPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        //点击空白的地方消失
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.showAsDropDown(text_commment);
        mPopWindow.setOnDismissListener(new poponDismissListener());

    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    public class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    public void getNext() {
        new MyRetrofitUtil(this).setDownListener(this).downJson(detail_url_next, 0x001);
        new MyRetrofitUtil(this).setDownListener(this).downJson(comment_count, 0x002);
        new MyRetrofitUtil(this).setDownListener(this).downJson(comment_long, 0x003);

    }


    @Override
    public Object paresJson(String json, int requestCode) {
        if (json != null) {
            switch (requestCode) {
                case 0x001:
                    return new Gson().fromJson(json, ZhihudetailEntity.class);
                case 0x002:
                    return new Gson().fromJson(json, CommentEntity.class);
                case 0x003:
                    return new Gson().fromJson(json, LongCommentEntity.class);

            }
        }
        return null;

    }

    @Override
    public void downSucc(Object object, int requestCode) {
        if (object != null) {
            switch (requestCode) {
                case 0x001:
                    ZhihudetailEntity zhihudetailEntity = (ZhihudetailEntity) object;


                    String title = zhihudetailEntity.getTitle();
                    // detail_title.setText(title);
                    html = zhihudetailEntity.getBody();
                    if (html != null) {
                        String html1 = getHtml(html, size);
                        webView.loadDataWithBaseURL(detail_url, html1, "text/html", "UTF-8", null);
                        EventBus.getDefault().post(zhihudetailEntity);

                    }

                    String image = zhihudetailEntity.getImage();
                   /* Glide.with(getApplicationContext())
                            .load(image)
                            .centerCrop()
                            .into(detail_image);


                    detail_image.setAlpha(0.8f);*/

                    // detail_resource.setText(zhihudetailEntity.getImage_source());
                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 500);
                    break;
                case 0x002:
                    CommentEntity commentEntity = (CommentEntity) object;
                    String long_comment = commentEntity.getLong_comments();
                    text_commment.setText("长评论" + "(" + long_comment + ")");
                    break;
                case 0x003:
                    LongCommentEntity longCommentEntity = (LongCommentEntity) object;
                    List<LongCommentEntity.CommentsBean> comments = longCommentEntity.getComments();
                    popadapter.setDatas(comments);
                    Log.d("print", "downSucc: " + comments.toString());


                    break;
            }
        }
    }


    /**
     * webview中图片文字自适应处理
     *
     * @param html
     * @return
     */
    public String getHtml(String html, int size) {
        String imgStyle = "<style> img{ max-width:100%; height:auto;} </style>";
        String textStyle = "<html> \n" +
                "<head> \n" +
                "<style type=\"text/css\"> \n" +
                "body {text-align:justify; font-size: " + (size) + "px; line-height: " + (size + 12) + "px}\n" +
                "</style> \n" +
                "</head> \n" +
                "<body>" + "</body> \n </html>";
        String reg = "style=\"([^\"]+)\"";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(html);
        String s = matcher.replaceAll("");
        String s1 = imgStyle + s + textStyle;
        return s1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
