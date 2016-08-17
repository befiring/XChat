package com.befiring.xchat.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.befiring.xchat.App.XChatApplication;


/**
 * Created by Administrator on 2016/8/11.
 */
public class SharedUtil {

    static SharedPreferences preferences = XChatApplication.getInstance().getSharedPreferences("XChat", Context.MODE_PRIVATE);
    static SharedPreferences.Editor editor=preferences.edit();
    static SharedUtil sharedUtil;

    public static SharedUtil getInstance() {
        if (sharedUtil == null) {
            sharedUtil = new SharedUtil();
        }
        return sharedUtil;
    }

    public void setToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    public String getToken() {
        return preferences.getString("token", "");
    }
}
