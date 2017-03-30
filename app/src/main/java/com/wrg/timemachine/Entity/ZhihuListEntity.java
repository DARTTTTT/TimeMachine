package com.wrg.timemachine.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 翁 on 2017/3/1.
 */

public class ZhihuListEntity implements Serializable {


    @Override
    public String toString() {
        return "ZhihuListEntity{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                '}';
    }

    /**
     * date : 20170301
     * stories : [{"images":["http://pic1.zhimg.com/db754f80b0a8917bc6097a1677351574.jpg"],"type":0,"id":9258308,"ga_prefix":"030116","title":"闻起来香香的那些护肤品可以放心买吗？"},{"images":["http://pic2.zhimg.com/a094bd6243a0a465c8e4d63787f77095.jpg"],"type":0,"id":9258115,"ga_prefix":"030115","title":"垃圾食品是不能多吃，但也别什么锅都推到它们身上"},{"images":["http://pic3.zhimg.com/c083172f186ec765d6150fcbce4bcfa6.jpg"],"type":0,"id":9257684,"ga_prefix":"030114","title":"龚琳娜明明可以「好好唱歌」，为什么不「好好唱」？"},{"images":["http://pic4.zhimg.com/be3be9e5f7a25ddeadd39f7cdb21f43f.jpg"],"type":0,"id":9256772,"ga_prefix":"030113","title":"家里总得备点药，但是\u2026\u2026该备哪些？需要的时候怎么用？"},{"images":["http://pic1.zhimg.com/45433d134abee9cd8d80126f86ec7f10.jpg"],"type":0,"id":9255783,"ga_prefix":"030112","title":"大误 · 盾牌扣地上是个严肃的问题"},{"images":["http://pic4.zhimg.com/b9c8b8f2d3185f0116431231ca70921b.jpg"],"type":0,"id":9253911,"ga_prefix":"030111","title":"现在觉得没有必要花几千块买洗碗机，以后你就懂了"},{"title":"看着是随手乱拍的照片，难道因为他是艺术家就得说好吗？","ga_prefix":"030110","images":["http://pic3.zhimg.com/b9da55bb77f33edd37e62d095002eb12.jpg"],"multipic":true,"type":0,"id":9256880},{"images":["http://pic1.zhimg.com/567c23149480132d273c157ae8dfc270.jpg"],"type":0,"id":9251897,"ga_prefix":"030109","title":"哪些人群和行业不怕经济危机？"},{"images":["http://pic1.zhimg.com/cca2c34f64f98cfbe78722de15a19704.jpg"],"type":0,"id":9255363,"ga_prefix":"030108","title":"如果互联网是一条商业街，街上是不是已经挂满广告牌？"},{"title":"一块普通的老豆腐，如何变成毛豆腐和豆腐乳？","ga_prefix":"030107","images":["http://pic3.zhimg.com/2805e9b82b97f718c673dbbb87d62a82.jpg"],"multipic":true,"type":0,"id":9256575},{"images":["http://pic4.zhimg.com/9646469199cdea69cfd212d195e8b313.jpg"],"type":0,"id":9256023,"ga_prefix":"030107","title":"你不一定会买的安踏现在市值超了 600 亿，全球第五"},{"images":["http://pic4.zhimg.com/5f49ddb4f3e82749562483e346223b77.jpg"],"type":0,"id":9167471,"ga_prefix":"030107","title":"你怎么看待找好下家再分手这种行为？"},{"images":["http://pic4.zhimg.com/e2cf6564f5ebd5b71801b0cd0d2b49bb.jpg"],"type":0,"id":9247633,"ga_prefix":"030106","title":"瞎扯 · 如何正确地吐槽"}]
     */

    private String date;
    private List<StoriesBean> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public static class StoriesBean  implements Serializable{
        @Override
        public String toString() {
            return "StoriesBean{" +
                    "type=" + type +
                    ", id=" + id +
                    ", ga_prefix='" + ga_prefix + '\'' +
                    ", title='" + title + '\'' +
                    ", multipic=" + multipic +
                    ", images=" + images +
                    '}';
        }

        /**
         * images : ["http://pic1.zhimg.com/db754f80b0a8917bc6097a1677351574.jpg"]
         * type : 0
         * id : 9258308
         * ga_prefix : 030116
         * title : 闻起来香香的那些护肤品可以放心买吗？
         * multipic : true
         */

        private int type;
        private String id;
        private String ga_prefix;
        private String title;
        private boolean multipic;
        private List<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isMultipic() {
            return multipic;
        }

        public void setMultipic(boolean multipic) {
            this.multipic = multipic;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
