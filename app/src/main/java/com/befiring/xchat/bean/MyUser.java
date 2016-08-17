package com.befiring.xchat.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/8/10.
 */
public class MyUser extends BmobUser{
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
