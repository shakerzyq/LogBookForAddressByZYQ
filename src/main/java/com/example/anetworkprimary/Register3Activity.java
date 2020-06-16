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
 * 注册界面3
 */
public class Register3Activity extends AppCompatActivity {

    private RequestBody requestBody;
    private String url="http://10.0.2.2:8080/Logbook/RsgisterController";
    boolean result1=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        Bundle bundle = this.getIntent().getExtras();
        final String account = bundle.getString("account");
        final String password = bundle.getString("password");

        final EditText editText  = findViewById(R.id.username);
        Button button = findViewById(R.id.bt_username);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editText.getText().toString();
                Personal personal = new Personal(account,password,username);
                Gson gson = new Gson();
                String gsons = gson.toJson(personal);
                requestBody = putJsonData(gsons);
                getResult();
                if (result1){
                    Toast.makeText(Register3Activity.this,"注册成功",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Register3Activity.this,LoginActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("account",account);
                    bundle1.putString("password",password);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(Register3Activity.this,"抱歉!系统异常,注册失败!",Toast.LENGTH_LONG).show();
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
    //封装成json
    private RequestBody putJsonData(String  gson){
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                ,gson);
        return requestBody;
    }
}
