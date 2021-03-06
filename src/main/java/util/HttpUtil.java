package util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.Address;
import domain.SelectType;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpUtil {
    public static final MediaType FROM_CONTENT_TYPE
            =MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) throws IOException {
        new Thread(new Runnable() {

            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader((new InputStreamReader(in)));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onfinish(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }
        }).start();
    }
    //用于删除
    public static void sendOkHttpRequest(String address,String type,String DName,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("type",type)
                .add("dname",DName)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
        // Response response =client.newCall(request).execute();

    }
    //用于精准查询

    public static void sendOkHttpRequest1(String address, RequestBody requestBody, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
       /* RequestBody requestBody = new FormBody.Builder()
                .add("type1",type1)
                .add("type2",type2)
                .build();*/
      /* Map<String,String> formParams = new HashMap<String,String>();

       formParams.put("type1",type1);
       formParams.put("type2",type2);
       StringBuffer sb = new StringBuffer();
       for (String key:formParams.keySet()){
           sb.append(key+":"+formParams.get(key)+"&");
        }
       RequestBody requestBody = RequestBody.create(FROM_CONTENT_TYPE,sb.toString());*/
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
        // Response response =client.newCall(request).execute();

    }
    //用于查询所有
    public static void sendOkHttpRequest(String address,String type,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("account",type)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
       client.newCall(request).enqueue(callback);
       // Response response =client.newCall(request).execute();

    }
    public  void sendOkHttpRequest(String address, RequestBody requestBody,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
        // Response response =client.newCall(request).execute();

    }
    public void  sendRequest2(final String url, final String type){
        List<Address> addresses = new ArrayList<Address>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("type",type)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    Gson gson = new Gson();
                    List<Address> article1 = gson.fromJson(responseData,new TypeToken<List<Address>>(){}
                    .getType());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public List<Address>  sendRequest3( String url,  String type) throws IOException {
        List<Address> addresses = new ArrayList<Address>();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("type",type)
                            .build();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    Gson gson = new Gson();
                addresses = gson.fromJson(responseData,new TypeToken<List<Address>>(){}
                            .getType());




        return addresses;
    }
    public static void sendOkHttpRequest2(final String address,final okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);

    }
}
