package com.example.anetworkprimary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import domain.Personal;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.HttpUtil;

/**
 * 修改密码
 */
public class ChangePasswordActivity extends AppCompatActivity {

    private String account;
    private String password;
    private String password2;
    private RequestBody requestBody;
    private String url="http://10.0.2.2:8080/Logbook/ChangePwdServlet";
    boolean result1=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //接收传递过来的账号
        Bundle bundle = this.getIntent().getExtras();
        account = bundle.getString("account");

        final EditText editText = findViewById(R.id.cpassword);
        final EditText editText1 = findViewById(R.id.cpassword1);
        Button button = findViewById(R.id.password_c);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password=editText.getText().toString();
                password2=editText1.getText().toString();
                if(!password2.equals("")) {
                    if (!password.equals(password2)) {
                        Toast.makeText(ChangePasswordActivity.this, "两次密码不一样", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                        Personal personal = new Personal(account, password2, "");
                        Gson gson = new Gson();
                        String gsons = gson.toJson(personal);
                        requestBody = putJsonData(gsons);
                        getResult();
                        Intent intent = new Intent(ChangePasswordActivity.this, UserCenterActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("account", account);
                        bundle1.putString("password", password);
                        intent.putExtras(bundle1);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Toast.makeText(ChangePasswordActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void getResult() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendOkHttpRequest1(url,requestBody,new Callback(){
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //得到服务器返回的具体内容
                        String responseData=response.body().string();
                        if(responseData!=null){
                            result1=true;
                        }else
                            result1=false;
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        //在这里进行异常情况处理
                    }
                });
            }
        }).start();
    }
    //解析json
    private RequestBody putJsonData(String  gson){
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                ,gson);
        return requestBody;
    }
}
