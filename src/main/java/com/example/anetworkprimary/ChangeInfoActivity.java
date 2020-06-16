package com.example.anetworkprimary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

/**
 * 修改
 */
public class ChangeInfoActivity extends AppCompatActivity {

    private MapView mapView;//声明地图组件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       SDKInitializer.initialize(getApplicationContext());//初始化地图SDK
        setContentView(R.layout.activity_change_info);
        mapView = findViewById(R.id.bmapview3);//获取地图组件

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mapView=null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
}
