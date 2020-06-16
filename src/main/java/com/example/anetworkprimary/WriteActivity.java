package com.example.anetworkprimary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
 * 添加记录,并有自动获取当前位置的功能
 */
public class WriteActivity extends AppCompatActivity {


    private TextView mTextView;
    TextView textView;
    private LocationService locationService;
    Gson gson = new Gson();

    private String type;
    private String typeId;
    private String responseData;
    private String date;
    private Address address;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private String account;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);


        mTextView = findViewById(R.id.text1);
        mTextView = findViewById(R.id.text1);
        //开启定位服务
        locationService = new LocationService(this);
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();

        Bundle bundle = this.getIntent().getExtras();
        account = bundle.getString("account");

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=parent.getItemAtPosition(position).toString();
                typeId=type;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ImageButton exit = findViewById(R.id.exit1);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog  = new AlertDialog.Builder(WriteActivity.this).create();//其构造方法是受保护
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

        final ImageButton button4 = findViewById(R.id.imagebutton);
        final EditText editText1 = findViewById(R.id.edit_title);
        final EditText editText2 = findViewById(R.id.edit_content);

        button4.setOnClickListener(new View.OnClickListener() {
            Article article=new Article("","","","","");

            @Override
            public void onClick(View v) {

                article.setAddress(mTextView.getText().toString());

                date =df.format(new Date());
                article.setTime(date);
                article.setType(typeId);
                article.setAccount(account);
                System.out.println("地址为:"+article.getAddress());
                HttpUtil httpUtil = new HttpUtil();
                article.setTitle(editText1.getText().toString());
                article.setContent(editText2.getText().toString());
                if(!article.getTitle().equals("")&&!article.getContent().equals("")) {

                    String json = toJson(article);
                    RequestBody requestBody = putJsonData(json);
                    System.out.println("responsebody为" + requestBody);
                    httpUtil.sendOkHttpRequest("http://10.0.2.2:8080/Logbook/TestServlet", requestBody,
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

                    Toast.makeText(WriteActivity.this,"提交成功", Toast.LENGTH_LONG).show();
                    finish();

                }else{
                    Toast.makeText(WriteActivity.this,"标题和内容不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }
    BDAbstractLocationListener mListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuilder sb = new StringBuilder(256);
                sb.append(location.getCountry());

                sb.append(location.getProvince());

                sb.append(location.getCity());

                sb.append(location.getDistrict());

                logMsg(sb.toString());


            }

        }
    };
    private RequestBody  putJsonData(String  gson){
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                ,gson);
        return requestBody;
    }

    private String toJson(Article article){
        return gson.toJson(article);
    }
    private void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();
        List<User> people = gson.fromJson(jsonData,new TypeToken<List<User>>(){}.getType());
    }

    /**
     * 显示请求字符串
     */
    public void logMsg(final String str) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            mTextView.setText(str);
                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
