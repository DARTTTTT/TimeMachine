package com.wrg.timemachine.Entity;

/**
 * Created by 翁 on 2017/3/10.
 */

public class RefreshEntity {

    private String refresh;
    private int state;

    public RefreshEntity(String refresh, int state) {
        this.refresh = refresh;
        this.state = state;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
