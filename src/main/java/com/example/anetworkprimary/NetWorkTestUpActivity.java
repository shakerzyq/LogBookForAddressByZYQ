package com.example.anetworkprimary;

import android.app.Person;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import domain.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetWorkTestUpActivity extends AppCompatActivity implements View.OnClickListener{
    TextView textView;
    private static String userURL = "https://10.0.2.2:8080" + "/get";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_work_test_up);
        Button button = findViewById(R.id.send_request1);
        textView = findViewById(R.id.response_text1);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.send_request1){
            sendRequestWithOkHttp();
        }
    }
    private void sendRequestWithOkHttp(){
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://10.0.2.2:8080/Logbook/TestServlet")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    //showResponse(responseData);
                    //parseJSONWithJSONObject(responseData);
                    //Gson gson = new Gson();
                    //List<User> people = gson.fromJson(responseData,new TypeToken<List<User>>(){}.getType());
                    parseJSONWithGSON(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    private void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();
        List<User> people = gson.fromJson(jsonData,new TypeToken<List<User>>(){}.getType());
        for(User user:people){
            Log.d("dsa","id is" +user.getId());
            Log.d("asd","id is" +user.getUsername());
            Log.d("ds","id is" +user.getPassword());
            Log.d("dsa","id is" +user.getName());
        }
    }

    private void parseJSONWithJSONObject(String jsonData){
        try{
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i =0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String username = jsonObject.getString("username");
                String  name= jsonObject.getString("name");
                Log.d("dsa","id is" +id);
                Log.d("asd","id is" +username);
                Log.d("ds","id is" +name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //在这里进行UI操作,将结果显示到界面上
                textView.setText(response);
                System.out.println("hellowordl"+response);
            }
        });
    }
   /* Response response ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_work_test_up);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .build();
        try {
            response = client.newCall(request).execute();
            String responseData = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
}
