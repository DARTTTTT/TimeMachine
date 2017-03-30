package com.wrg.timemachine.Constants;

/**
 * Created by 翁 on 2017/3/1.
 */

public interface Constant {
    //主题日报
    String ZHIHUTHEME="http://news-at.zhihu.com/api/7/themes";
    //主题日报详情
    String THEMEDETAIL="http://news-at.zhihu.com/api/4/theme/%s";
    //知乎新闻列表
    String ZHIHULIST="http://news-at.zhihu.com/api/4/news/before/%s";
    //知乎详情
    String ZHIHUDETAIL="http://news-at.zhihu.com/api/4/news/%s";
    String CACHE="cache";
    //热门
    String ZHIHUTOP="http://news-at.zhihu.com/api/4/news/latest";
    //查看长评论
    String ZHIHUCOMMENT_LONG="http://news-at.zhihu.com/api/4/story/%s/long-comments";
    //评论或者点赞数
    String ZHIHUCOMMENT="http://news-at.zhihu.com/api/4/story-extra/%s";

    //查看短评
    String ZHIHUCOMMENT_SHORT="http://news-at.zhihu.com/api/4/story/%s/short_comments";
}
