package com.example.anetworkprimary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

public class ChangeInfoActivity extends AppCompatActivity {

    private MapView mapView;//声明地图组件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       SDKInitializer.initialize(getApplicationContext());//初始化地图SDK
       // setContentView(R.layout.activity_change_info);
        setContentView(R.layout.activity_change_info);
        mapView = findViewById(R.id.bmapview3);//获取地图组件
        //mBaiduMap = mapView.getMap();

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

    /*@Override
    protected void onStart() {
        super.onStart();
        //mBaiduMap.setMyLocationEnabled(true);//启动定位图层
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mBaiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView=null;
        //停止定位图层
    }*/
}
