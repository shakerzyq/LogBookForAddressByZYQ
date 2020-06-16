package com.example.anetworkprimary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.ActicleUpdate;
import domain.Address;
import domain.Article;
import domain.User;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.HttpUtil;

/**
 * 显示内容,并且可以更新
 */
public class ShowActivity extends AppCompatActivity {

    private TextView mTextView;

    TextView textView;
    Gson gson = new Gson();
    private String type1;
    private String type;
    private String  typeId;
    private String responseData;
    private String date;
    private Address address;
    private int typeid;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        //接受数据
        Bundle bundle = this.getIntent().getExtras();
        mTextView = findViewById(R.id.text1);
        final ImageButton button4 = findViewById(R.id.imagebutton);
        final EditText editText1 = findViewById(R.id.edit_title);
        final EditText editText2 = findViewById(R.id.edit_content);
        TextView typetext = findViewById(R.id.type);

        /**
         * 接受从HistoryActivity传递过来的值
         */

        String name = bundle.getString("name");
        String content = bundle.getString("content");
        type1 = bundle.getString("type");
        String address1 = bundle.getString("address");
        String date1 = bundle.getString("date");
        final String id = bundle.getString("id");
        //sendType();
        /**
         * 将接收到的值显示到界面上
         */
        typetext.setText(type1);
        editText1.setText(name);
        editText2.setText(content);
        mTextView.setText(address1);

        /**
         * 退出操作
         */
        ImageButton exit = findViewById(R.id.exit1);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog  = new AlertDialog.Builder(ShowActivity.this).create();//其构造方法是受保护
                alertDialog.setIcon(R.mipmap.exit);
                alertDialog.setTitle("退出编辑?");
                alertDialog.setMessage("退出后不保存记录");
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "'取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();
            }

        });


/**
 * 提交操作
 *
 * 处处提交为更新提交,只更新内容和标题
 */
        button4.setOnClickListener(new View.OnClickListener() {
            ActicleUpdate updateData=new ActicleUpdate("","","");

            @Override
            public void onClick(View v) {
                HttpUtil httpUtil = new HttpUtil();
                updateData.setTitle(editText1.getText().toString());
                updateData.setContent(editText2.getText().toString());
                updateData.setId(id);
                // System.out.println("信息"+article.getTitle()+article.getAddress());
                if(!updateData.getTitle().equals("")&&!updateData.getContent().equals("")) {

                    String json = toJson(updateData);
                    RequestBody requestBody = putJsonData(json);
                    //Gson gson = new Gson();
                    System.out.println("responsebody为" + requestBody);
                    httpUtil.sendOkHttpRequest("http://10.0.2.2:8080/Logbook/UpdateServlet", requestBody,
                            new okhttp3.Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    responseData = response.body().string();
                                    //toast(responseData);

                                }
                            });

                    Toast.makeText(ShowActivity.this,"提交成功", Toast.LENGTH_LONG).show();
                    finish();

                }else{
                    Toast.makeText(ShowActivity.this,"标题和内容不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private RequestBody  putJsonData(String  gson){
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                ,gson);
        return requestBody;
    }
    private String toJson(ActicleUpdate article){
        return gson.toJson(article);
    }
}
