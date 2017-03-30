package com.wrg.timemachine.Entity;

import java.util.List;

/**
 * Created by 翁 on 2017/3/10.
 */

public class LongCommentEntity {


    private List<CommentsBean> comments;

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        @Override
        public String toString() {
            return "CommentsBean{" +
                    "author='" + author + '\'' +
                    ", content='" + content + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", time=" + time +
                    ", id=" + id +
                    ", likes=" + likes +
                    '}';
        }

        /**
         * author : 凌舞柒妖
         * content : 朋友去拔牙，结果医生拔了四个小时还是把她的牙拔坏了，在拔了两个小时的时候和她说：你看，你当时也签过字的吧，手术有风险的，巴拉巴拉的一堆（大致意思就是各种推卸责任）。其实是当时拔牙前医生没好好看拍的片子就拔了，弄到现在一个多月了牙肉也都愈合不了，她去其它医院看其他医生和她说这情况还是蛮严重的。 我家里人动手术，明明是右腿，推进手术室里在给她的左腿消毒啥的，也是无语。 真是有时候不是我们患者想闹，有些医生连基本的道德都没有。
         * avatar : http://pic3.zhimg.com/ae8ec419e_im.jpg
         * time : 1489131836
         * id : 28357157
         * likes : 0
         */


        private String author;
        private String content;
        private String avatar;
        private int time;
        private int id;
        private int likes;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }
    }
}
