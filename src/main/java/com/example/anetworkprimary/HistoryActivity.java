package com.example.anetworkprimary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import domain.Address;
import domain.Article;
import domain.ArticleForFind;
import domain.SelectType;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import util.HttpUtil;

public class HistoryActivity extends AppCompatActivity {
    private List<ArticleForFind> article ;
    private List<Address> addresses;
    private String responseData;
    private String url="http://10.0.2.2:8080/Logbook/SelectAllServlet";
    private String account;
    private String type;
    ListView listView;
    SimpleAdapter adapter;
    int length=0;
   // private String[] stype={"","","","","","","","","","","","",""};
    //private String[] stype2=new String[length];
   private List<String> alist =new ArrayList<String>();
   private HashSet<String> set = new HashSet<String>();
   //List<String> alist ;
    //List<String> strList;
    //List<String> arrayList;
    private int MENU_DELETE;
    private int MENU_SIGN;
    private String name;
    //用于查询的变量
    private String cardNumber;
    private String Logtype;
    //用于查询的类(封装成json数据)
    private SelectType selectType =new SelectType();
    //用于查询的数组
    private String[] typeList = {"所有","生活","学习","工作","其它"};
    //new一个JSON对象
    Gson gson = new Gson();
    //responsebody对象
    private RequestBody requestBody;
    //用于记录spinner的位置

    private Spinner spinner1;
    private Spinner spinner;

    private int location=0;
    private int typeSite=0;

    List<Map<String,Object>> listitem = new ArrayList<Map<String,Object>>();
    private Map<String,Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Bundle bundle = this.getIntent().getExtras();
        account = bundle.getString("account");
        //account="t4";
        spinner1 = findViewById(R.id.spinnerFind);
        spinner = findViewById(R.id.spinnerSe);

        listView = findViewById(R.id.listview1);
        ImageButton imageButton = findViewById(R.id.finding);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listitem = new ArrayList<Map<String,Object>>();
                selectType.setAddress(cardNumber);
                selectType.setType(Logtype);
                selectType.setAccount(account);
                System.out.println("地址和类型"+cardNumber+Logtype);
                //将对象封装成json数据
                //map =new HashMap<String, Object>();
               // adapter.notifyDataSetChanged();
                //sendRequestWithOkHttp(true);
                String gsons = gson.toJson(selectType);
                requestBody = putJsonData(gsons);
                url="http://10.0.2.2:8080/Logbook/SelectUpServlet";
                Toast.makeText(HistoryActivity.this,"选择的类型为:"+cardNumber+Logtype,Toast.LENGTH_SHORT).show();
                sendRequestWithOkHttpForSelectByAddressAndType();
                //spinner.setSelection(location);


            }
        });

        //触发添加事件
        ImageView imgbutton = findViewById(R.id.add);
        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(HistoryActivity.this,WriteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("account",account);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });

        //触发个人中心事件
        ImageView imageView = findViewById(R.id.mine);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(HistoryActivity.this,UserCenterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("account",account);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });
        sendRequestWithOkHttp();
    }

    //将对象转为json的方法
    private RequestBody  putJsonData(String  gson){
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                ,gson);
        return requestBody;
    }


    //用于精准查询

    private void sendRequestWithOkHttpForSelectByAddressAndType() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //在子线程中执行Http请求，并将最终的请求结果回调到okhttp3.Callback中
                HttpUtil.sendOkHttpRequest1(url,requestBody,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //得到服务器返回的具体内容
                        String responseData=response.body().string();
                        List<ArticleForFind> address2 = parseJSONWithGSON(responseData);
                        //显示UI界面，调用的showResponse方法
                        showResponse(address2);
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        //在这里进行异常情况处理
                    }
                });
            }
        }).start();
    }
    //用于全查询

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //在子线程中执行Http请求，并将最终的请求结果回调到okhttp3.Callback中
                //adapter.notifyDataSetChanged();
                HttpUtil.sendOkHttpRequest(url,account,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //得到服务器返回的具体内容
                        String responseData=response.body().string();
                        List<ArticleForFind> address2 = parseJSONWithGSON(responseData);
                        //显示UI界面，调用的showResponse方法
                        showResponse(address2);
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        //在这里进行异常情况处理
                    }
                });
            }
        }).start();
    }
    //将json转为对象
    private List<ArticleForFind> parseJSONWithGSON(String jsonData) {
        //使用轻量级的Gson解析得到的json
        Gson gson = new Gson();
        List<ArticleForFind> appList = gson.fromJson(jsonData, new TypeToken<List<ArticleForFind>>() {}.getType());
        for (ArticleForFind app : appList) {
            //控制台输出结果，便于查看
            Log.d("MainActivity", "title" + app.getContent());
            Log.d("MainActivity", "content" + app.getTitle());
        }
        return appList;
    }
    private void showResponse(final List<ArticleForFind> response) {
        //在子线程中更新UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // Looper.prepare();
                // 在这里进行UI操作，将结果显示到界面上
                //responseText.setText(response);
                  //  Address address = new Address();
              //  List<Map<String,Object>> listitem = new ArrayList<Map<String,Object>>();
                for (int i=0;i<response.size();i++){

                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("name",response.get(i).getTitle());
                    map.put("think",response.get(i).getContent());
                    map.put("type",response.get(i).getType());
                    map.put("address",response.get(i).getAddress());
                    map.put("date",response.get(i).getTime());
                    map.put("id",response.get(i).getId());
                    listitem.add(map);
                    set.add(response.get(i).getAddress());
                }
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(HistoryActivity.this,
                        android.R.layout.simple_spinner_item);
                final List<String> lista = new ArrayList<String>();
                lista.add("所有");
                for(String n:set){
                    lista.add(n);
                }
                for (int i =0;i<lista.size();i++){
                    adapter1.add(lista.get(i));
                }

                spinner.setAdapter(adapter1);
                spinner.setSelection(location);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        cardNumber =lista.get(position);

                        location=position;
                        Toast.makeText(HistoryActivity.this,cardNumber+"  "+position,Toast.LENGTH_LONG).show();
                        //sendRequestWithOkHttp();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(HistoryActivity.this,
                        android.R.layout.simple_spinner_item);
                for (String n:typeList){
                    adapter2.add(n);
                }

                spinner1.setAdapter(adapter2);
                spinner1.setSelection(typeSite);
                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Logtype =typeList[position];

                        typeSite=position;
                        //spinner1.setSelection(position);
                        Toast.makeText(HistoryActivity.this,Logtype+"  "+position,Toast.LENGTH_LONG).show();
                        //sendRequestWithOkHttp();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                //创建适配器
                    adapter = new SimpleAdapter(HistoryActivity.this, listitem, R.layout.listviewmodel
                            , new String[]{"name", "think", "type", "address", "date"}, new int[]{R.id.title, R.id.think, R.id.type
                            , R.id.address, R.id.date});

                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            map = (Map<String, Object>) parent.getItemAtPosition(position);
                            //Toast.makeText(HistoryActivity.this,map.get("name").toString(),Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(HistoryActivity.this, ShowActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("name", map.get("name").toString());
                            bundle.putString("content", map.get("think").toString());
                            bundle.putString("type", map.get("type").toString());
                            bundle.putString("address", map.get("address").toString());
                            bundle.putString("date", map.get("date").toString());
                            bundle.putString("id", map.get("id").toString());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    registerForContextMenu(listView);
               // Looper.loop();
                }


               // openContextMenu(listView);
               // listView.setAdapter(adapter1);
              /*  listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        registerForContextMenu(listView);
                        openContextMenu(listView);
                        return false;
                    }
                });*/


        });
    }
    private void show(){

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu,v, menuInfo);
        menu.add(0,1,0,"删除");
        menu.add(0,2,0,"重点标记");
    }

    @SuppressLint("ShowToast")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = (int)listView.getAdapter().getItemId(info.position);
        name = listitem.get(pos).get("id").toString();

        switch(item.getItemId()){
            case 1:
                if(listitem.remove(pos)!=null){
                   // name = listitem.get(pos).get("name").toString();
                    System.out.println("success"+pos+"   name: "+name);
                }else{
                    System.out.println("failed");
                }
                type="delete";
                url="http://10.0.2.2:8080/Logbook/DeleteServlet";
                        AlertDialog alertDialog  = new AlertDialog.Builder(HistoryActivity.this).create();//其构造方法是受保护
                        alertDialog.setIcon(R.mipmap.exit);
                        alertDialog.setTitle("确认删除");
                        alertDialog.setMessage("删除后无法恢复数据");
                        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "'取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Looper.prepare();
                                        //Looper.perpare();//增加部分
                                        try {
                                            OkHttpClient client = new OkHttpClient();
                                            RequestBody requestBody = new FormBody.Builder()
                                                    .add("type",type)
                                                    .add("dbname",name)
                                                    .build();

                                            Request request = new Request.Builder()
                                                    .url(url)
                                                    .post(requestBody)
                                                    .build();
                                            client.newCall(request).execute();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(getBaseContext(),"删除成功",Toast.LENGTH_SHORT).show();
                                        Looper.loop();//增加部分
                                    }


                                }).start();
                            }

                        });
                        alertDialog.show();
                        adapter.notifyDataSetChanged();
                break;
            case 2:
                Toast.makeText(this,"点击了gengxin",Toast.LENGTH_LONG);
                break;
            default:
                Toast.makeText(getBaseContext(),"wufanyin ",Toast.LENGTH_LONG);
                return super.onContextItemSelected(item);
        }

        return true;
    }
    private void sendRequestWithOkHttpD() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //在子线程中执行Http请求，并将最终的请求结果回调到okhttp3.Callback中
                HttpUtil.sendOkHttpRequest(url,type,name,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        //在这里进行异常情况处理
                    }
                });
            }
        }).start();
    }


}
