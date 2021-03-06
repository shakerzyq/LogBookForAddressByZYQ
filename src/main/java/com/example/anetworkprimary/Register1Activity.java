package com.example.anetworkprimary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import domain.Personal;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.HttpUtil;

/**
 * 注册界面一
 */
public class Register1Activity extends AppCompatActivity {
    private String account=null;
    private String responseData="";
    public  static int i=0;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        final EditText editText = findViewById(R.id.account);
        Button button = findViewById(R.id.account_b);

        textView = findViewById(R.id.toast);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account=editText.getText().toString();

                HttpUtil httpUtil = new HttpUtil();
                Personal personal = new Personal(account,"","");
                if (account.length()==11) {


                    if (!personal.getUserAccount().equals("")) {
                        Gson gson = new Gson();
                        String json = gson.toJson(personal);
                        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                                , json);
                        System.out.println("responsebody为" + requestBody);
                        httpUtil.sendOkHttpRequest("http://10.0.2.2:8080/Logbook/RsgisterController", requestBody,
                                new okhttp3.Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        responseData = response.body().string();
                                        if (!responseData.equals("")) {
                                            Intent intent = new Intent(Register1Activity.this, Register2Activity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("account", account);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                    }
                    if (responseData.equals("")) {
                        textView.setText("该账号已存在");
                    }
                }else{
                    textView.setText("请输入电话号码");
                }
            }
        });
    }
}
