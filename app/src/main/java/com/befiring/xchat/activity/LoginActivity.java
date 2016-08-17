package com.befiring.xchat.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.befiring.xchat.R;
import com.befiring.xchat.Utils.HttpUtil;
import com.befiring.xchat.Utils.SharedUtil;
import com.befiring.xchat.bean.MyUser;

import java.util.UUID;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    String baseUrl="http://192.168.2.100:8080/XChat/servlet/GetTokenServlet";
    String token="uzdk4owYYM/eOxqwNlWI/m2mrBelrv4kocirc8Pu9MFFarBuWEUvODMuJoIMcCscy0NlWllYGrupuJB8RfHpWA==";
//    String token="UTSPriiSFKpPyOPYGM6gSxIx1U0lHbDwE9xsj0yiVg5X0ruX0UIYvDcQvebQ075fTqTJzks83xAaJ0KNHm0YFA==";

    private EditText userNmaeEt;
    private EditText passwordEt;
    private Button loginBtn;
    private Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
//        connectRongCloud();
    }

    private void initView(){
        userNmaeEt= (EditText) findViewById(R.id.et_user_name);
        passwordEt= (EditText) findViewById(R.id.et_password);
        loginBtn= (Button) findViewById(R.id.btn_login);
        registerBtn= (Button) findViewById(R.id.btn_register);

        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }


    private void connectRongCloud(String token){
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                //Connect Token 失效的状态处理，需要重新获取 Token
            }
            @Override
            public void onSuccess(String userId) {
                Log.e("MainActivity", "——onSuccess—-" + userId);
                /**
                 * 启动单聊
                 * context - 应用上下文。
                 * targetUserId - 要与之聊天的用户 Id。
                 * title - 聊天的标题，如果传入空值，则默认显示与之聊天的用户名称。
                 */
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(LoginActivity.this, "0002", "befiring");
                }

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("MainActivity", "——onError—-" + errorCode);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    private void login() {
        MyUser user=new MyUser();
        final String userName=userNmaeEt.getText().toString();
        user.setUsername(userName);
        user.setPassword(passwordEt.getText().toString());
        user.login(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser bmobUser, BmobException e) {
                if(e==null){
                    final String userId=bmobUser.getUserId();
                    if(getTokenFromShared().equals("")||getTokenFromShared()==null){
                        new LoginAsyncTask(baseUrl,userId,userName,"").execute();
                        return;
                    }
                    connectRongCloud(getTokenFromShared());

                }else {
                    Toast.makeText(LoginActivity.this,"登录失败，请重试",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void register() {
        MyUser user=new MyUser();
        user.setUsername(userNmaeEt.getText().toString());
        user.setPassword(passwordEt.getText().toString());
        user.setUserId(UUID.randomUUID().toString());
        user.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser bmobUser, BmobException e) {
                if(e==null){
                    Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    login();
                }else {
                    Toast.makeText(LoginActivity.this,"注册失败，请重试",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getTokenFromShared(){
        return SharedUtil.getInstance().getToken();
    }


    private class LoginAsyncTask extends AsyncTask<String ,Integer ,String >{
        String baseUrl;
        String userId;
        String userName;
        String potraitUrl;

        public LoginAsyncTask(String baseUrl, String userId, String userName, String potraitUrl) {
            this.baseUrl = baseUrl;
            this.userId = userId;
            this.userName = userName;
            this.potraitUrl = potraitUrl;
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpUtil.sendGet(baseUrl,"userId="+userId+"&userName="+userName+"&potraitUrl=");
        }

        @Override
        protected void onPostExecute(String token) {
            super.onPostExecute(token);
            connectRongCloud(token);
            SharedUtil.getInstance().setToken(token);
        }
    }

}
