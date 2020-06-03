package com.example.anetworkprimary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import domain.Address;
import domain.Article;
import domain.User;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import util.HttpUtil;

public class MainActivity extends AppCompatActivity{
    private TextView mTextView;
    TextView textView;
    private LocationService locationService;
    Gson gson = new Gson();
    Article article;
    private String type;
    private String  typeId;
    private String responseData;
    private String date;

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = findViewById(R.id.bt1);
        mTextView = findViewById(R.id.text1);
        locationService = new LocationService(this);
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        article = new Article();
       // locationService.start();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WriteActivity.class);
                startActivity(intent);
            }
        });
        Button button2 = findViewById(R.id.bt2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NetWorkTestActivity.class);
                startActivity(intent);
            }
        });
        Button button3 = findViewById(R.id.bt3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NetWorkTestUpActivity.class);
                startActivity(intent);
            }
        });
        Button button5 = findViewById(R.id.bt5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LocationActivity.class);
                startActivity(intent);
            }
        });
        Button button7 = findViewById(R.id.page);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MainPageActivity.class);
                startActivity(intent);
            }
        });
        Button button6 = findViewById(R.id.bt6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
                startActivity(intent);
            }
        });

        Button button_7 = findViewById(R.id.bt7);
        button_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });
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

        final Button button4 = findViewById(R.id.bt4);
        final EditText editText1 = findViewById(R.id.edit_title);
        final EditText editText2 = findViewById(R.id.edit_content);

        button4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                article.setAddress(mTextView.getText().toString());

                date =df.format(new Date());
                article.setTime(date);
                article.setType(typeId);
                System.out.println("地址为:"+article.getAddress());
                button4.setText(article.getAddress());
                //对应的显示操作

                HttpUtil httpUtil = new HttpUtil();
                //System.out.println("标题为"+editText1.getText());
                article.setTitle(editText1.getText().toString());
                //article.setTitle("Hello,World");
               // System.out.println(article.getTitle());
                article.setContent(editText2.getText().toString());
                //article.setContent("Hello,World");
                System.out.println("信息"+article.getTitle()+article.getAddress());
                String json = toJson(article);
                RequestBody requestBody = putJsonData(json);
                Gson gson = new Gson();
                System.out.println("responsebody为"+requestBody);
                httpUtil.sendOkHttpRequest("http://10.0.2.2:8080/Logbook/TestServlet",requestBody,
                        new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                responseData = response.body().string();
                            }
                        });

                Toast.makeText(MainActivity.this,responseData,Toast.LENGTH_SHORT).show();

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
        public Address address = new Address();

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                // button.setText("停止定位");

                StringBuilder sb = new StringBuilder(256);

                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                //article.setTime(location.getTime());
                sb.append(location.getCountry());
/*
                sb.append(location.getProvince());
                System.out.println(location.getCountry());*/

                sb.append(location.getCity());
                System.out.println("城市"+location.getCity());

              /*  sb.append(location.getDistrict());
                System.out.println(location.getDistrict());*/
              /** 累加地址 **/
              /*  if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }*/
               // if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果

//                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                    // 运营商信息
//                    if (location.hasAltitude()) {// *****如果有海拔高度*****
//                        sb.append("\nheight : ");
//                        sb.append(location.getAltitude());// 单位：米
//                    }
//                    sb.append("\noperationers : ");// 运营商信息
//                    sb.append(location.getOperators());
//                    sb.append("\ndescribe : ");
//                    sb.append("网络定位成功");
//                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                    sb.append("\ndescribe : ");
//                    sb.append("离线定位成功，离线定位结果也是有效的");
                //} else

                /**
                 * 显示异常
                 */
                    /*if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }*/



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
