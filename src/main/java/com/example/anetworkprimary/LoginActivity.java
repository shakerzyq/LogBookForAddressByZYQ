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

public class LoginActivity extends AppCompatActivity {
    String account;
    String password;
    public static Personal personal = new Personal();
    EditText editText;
    EditText editText1;
    Button button;
    private String url;
    public String responseDatauser=null;
    RequestBody requestBody ;
    Personal personal1 = new Personal("","","");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Bundle bundle = this.getIntent().getExtras();
        //account = bundle.getString("account");
        //password = bundle.getString("password");
        //Register3Activity.personal.getPassword();

        editText = findViewById(R.id.yonghu);
        editText1 = findViewById(R.id.password);
        TextView textView = findViewById(R.id.zhuche);
        button = findViewById(R.id.denglu);

        editText.addTextChangedListener(textWatcher);
        editText1.addTextChangedListener(textWatcher);
        editText.setText(personal.getUserAccount());
        editText1.setText(personal.getPassword());

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
                // System.out.println("信息"+article.getTitle()+article.getAddress());
                if(!personal.getUserAccount().equals("")&&!personal.getPassword().equals("")) {
                    Gson gson = new Gson();
                    String json = gson.toJson(personal);
                    RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                            ,json);
                    System.out.println("responsebody为" + requestBody);
                    httpUtil.sendOkHttpRequest("http://10.0.2.2:8080/Logbook/LoginServlet", requestBody,
                            new okhttp3.Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    responseDatauser = response.body().string();
                                            if (!responseDatauser.equals("{}")){
                                               //personal=parseJSONWithGSON(responseData);
                                                Intent intent1 = new Intent(LoginActivity.this,HistoryActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("account",account);
                                                intent1.putExtras(bundle);
                                                startActivity(intent1);
                                                finish();
                                                // Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                            }
                                            }
                            });
                    if(responseDatauser==null){

                            Toast.makeText(getApplicationContext(),"用户名或密码不正确",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void sendRequestWithOkHttpForSelectByAddressAndType() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //在子线程中执行Http请求，并将最终的请求结果回调到okhttp3.Callback中
                HttpUtil.sendOkHttpRequest1(url,requestBody,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //得到服务器返回的具体内容
                        responseDatauser=response.body().string();
                        personal1 = parseJSONWithGSON(responseDatauser);
                        //显示UI界面，调用的showResponse方法

                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        //在这里进行异常情况处理
                    }
                });
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String a=personal1.getUserName();
                if (!personal1.getUserName().equals("")){
                    Intent intent1 = new Intent(LoginActivity.this,HistoryActivity.class);
                    startActivity(intent1);
                    //Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(LoginActivity.this,"用户名密码不正确",Toast.LENGTH_SHORT).show();
                }
            }
        }).start();


    }

    private Personal parseJSONWithGSON(String jsonData) {
        //使用轻量级的Gson解析得到的json
        Gson gson = new Gson();
        Personal appList = gson.fromJson(jsonData, new TypeToken<List<ArticleForFind>>() {}.getType());

        return appList;
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
