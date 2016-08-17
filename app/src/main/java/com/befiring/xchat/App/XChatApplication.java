package com.befiring.xchat.App;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;
import io.rong.imkit.RongIM;


/**
 * Created by Administrator on 2016/7/25.
 */
public class XChatApplication extends Application {

    private static XChatApplication mApp;
    @Override
    public void onCreate() {
        super.onCreate();

        mApp=this;
        Bmob.initialize(this,"fd1f4dd505b1b89c6475d47ad5cffe0d");
        RongIM.init(this);
    }

    public  static Context getInstance(){
        return mApp;
    }
}
