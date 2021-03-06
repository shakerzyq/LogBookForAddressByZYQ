package com.example.anetworkprimary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import domain.ArticleForFind;
import domain.Personal;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.HttpUtil;

/**
 * 登录
 * 按钮: 1: 注册  2:登录
 * 功能:点击登录后通过后台查询对账号和密码进行验证,如果密码或账号不符合将会弹出Toast提示
 */
public class LoginActivity extends AppCompatActivity {
    String account;
    public static Personal personal = new Personal();
    EditText editText;
    EditText editText1;
    Button button;
    private String url;
    public String responseDatauser="";
    RequestBody requestBody ;
    Personal personal1 = new Personal("","","");
    public static int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText = findViewById(R.id.yonghu);
        editText1 = findViewById(R.id.password);
        TextView textView = findViewById(R.id.zhuche);
        button = findViewById(R.id.denglu);

        editText.addTextChangedListener(textWatcher);
        editText1.addTextChangedListener(textWatcher);
        editText.setText(personal.getUserAccount());
        editText1.setText(personal.getPassword());
        if(i>0){
            Toast.makeText(LoginActivity.this,"用户名或密码不正确",Toast.LENGTH_SHORT).show();
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,Register1Activity.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil httpUtil = new HttpUtil();
                personal.setUserAccount(editText.getText().toString());
                personal.setPassword(editText1.getText().toString());
                account=editText.getText().toString();
                if(!personal.getUserAccount().equals("")&&!personal.getPassword().equals("")) {
                    Gson gson = new Gson();
                    String json = gson.toJson(personal);
                    RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                            ,json);
                    httpUtil.sendOkHttpRequest("http://10.0.2.2:8080/Logbook/LoginServlet", requestBody,
                            new okhttp3.Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Toast.makeText(getApplicationContext(),"用户名或密码不正确",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    responseDatauser = response.body().string();
                                            if (!responseDatauser.equals("")){
                                                Intent intent1 = new Intent(LoginActivity.this,HistoryActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("account",account);
                                                intent1.putExtras(bundle);
                                                startActivity(intent1);
                                                finish();
                                            }else{
                                                Intent intent1 = new Intent(LoginActivity.this,LoginActivity.class);
                                                i++;
                                                startActivity(intent1);
                                                finish();
                                            }
                                }
                            });

                }
            }
        });
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //如果编辑框长度大于零就是可用状态
            if(editText.length()>0&&editText1.length()>0){
                button.setEnabled(true);

            }else{
                button.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
