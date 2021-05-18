package com.yuukidach.ucount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnLogin;
    private EditText mEtUser, mEtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        setListener();
    }

    /**
     * 按钮点击监听
     */
    private void setListener() {
        mBtnLogin.setOnClickListener(this);
    }

    /**
     * 绑定布局
     */
    private void initView() {
        mBtnLogin = findViewById(R.id.btn_login);
        mEtUser = findViewById(R.id.et_username);
        mEtPwd = findViewById(R.id.et_pwd);
    }

    /**
     * 点击之后的事件
     * @param v 点击的 View
     */
    @Override
    public void onClick(View v) {
        // 点击了登录按钮
        if (v.getId() == R.id.btn_login) {
            String user = mEtUser.getText().toString();
            String pwd = mEtPwd.getText().toString();

            // 判断账号密码不为空
            if (TextUtils.isEmpty(user)) {
                Toast.makeText(this, "账号不能为空", Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
                return;
            }

            // 开始登录
            startLogin(user, pwd);
        }
    }

    /**
     * 开始登录的功能
     * @param user 账号
     * @param pwd 密码
     */
    private void startLogin(String user, String pwd) {

        String url = "http://bs.jiyehoo.com:81/lmx";

        Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("###lmx", "请求失败");
                runOnUiThread(() ->
                        Toast.makeText(LoginActivity.this, "网络请求失败", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                {
//                    "user": lmx,
//                    "pwd": 123456
//                }
                String res = Objects.requireNonNull(response.body()).string();

                LoginBean loginBean = new Gson().fromJson(res, LoginBean.class);
                String cloudUser = loginBean.getUser();
                String cloudPwd = loginBean.getPwd();

                if (cloudUser.equals(user) && cloudPwd.equals(pwd)) {
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show());

                }
            }
        };

        HttpUtil.sendOkHttpRequest(url, callback);


    }


}