package com.wrg.timemachine.Entity;

/**
 * Created by 翁 on 2017/3/10.
 */

public class CommentEntity {


    /**
     * long_comments : 2
     * popularity : 41
     * short_comments : 1
     * comments : 3
     */

    private String long_comments;
    private int popularity;
    private int short_comments;
    private int comments;

    public String getLong_comments() {
        return long_comments;
    }

    public void setLong_comments(String long_comments) {
        this.long_comments = long_comments;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getShort_comments() {
        return short_comments;
    }

    public void setShort_comments(int short_comments) {
        this.short_comments = short_comments;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
}
