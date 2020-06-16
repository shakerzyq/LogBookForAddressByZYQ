package com.example.anetworkprimary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import domain.Address;

/**
 * 显示地图
 */
public class MyMapActivity extends AppCompatActivity {

    private   double Latitude;
    private   double Longitude;

    private MapView mapView;//声明地图组件
    private BaiduMap mBaiduMap;//定义百度地图对象
    private boolean isFirstLoc=true; //记录是否是第一次定位
    private MyLocationConfiguration.LocationMode locationMode;//设置当前定位模式
    //private LocationService locationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());//初始化地图SDK
        setContentView(R.layout.activity_my_map);


        Bundle bundle = this.getIntent().getExtras();
        Latitude = bundle.getDouble("Latitude");
        Longitude = bundle.getDouble("Longitude");


        mapView = findViewById(R.id.bmapview2);//获取地图组件
        mBaiduMap = mapView.getMap();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //进行权限检查

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,//指定GPS定位提供者
                1000,//间隔时间
                1,//位置间隔的距离为一米
                //设置一个监听器
                new LocationListener() {//监听GPS定位信息是否改变
                    @Override
                    public void onLocationChanged(Location location) {//GPS信息发生改变时回调
                        locationUpdates(location);
                    }

                    //GPS状态发生改变时回调
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {//定位提供者启动时回调

                    }

                    @Override
                    public void onProviderDisabled(String provider) {//定位提供者关闭时触发

                    }
                }
        );
        Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        System.out.println("asd"+location);
        locationUpdates(location);//将最新的定位信息传递给locationUpdate()方法

    }

    public void locationUpdates(Location location){//获取指定的查询信息
        if (location!=null){
            LatLng ll = new LatLng(Latitude,Longitude);//获取用户当前的经纬度
            Log.i("Location","纬度:"+location.getLatitude()+"经度"+location.getLongitude());
            if (isFirstLoc){
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);//更新地图位置
                mBaiduMap.animateMapStatus(u);//设置地图位置
                isFirstLoc=false;//取消第一次定位
            }
            //构造定位数据
            MyLocationData locationData = new MyLocationData.Builder().accuracy(location.getAccuracy())
                    .direction(100)//设置方向信息
                    .latitude(Latitude)//设置纬度
                    .longitude(Longitude)//设置经度
                    .build();//构造一个定位数据
            mBaiduMap.setMyLocationData(locationData);//设置定位数据
            //设置一个自定义图标
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background);
            locationMode=MyLocationConfiguration.LocationMode.NORMAL;//设置定位模式
            //设置构造方式
            MyLocationConfiguration configuration = new MyLocationConfiguration(locationMode,true,bitmapDescriptor);
            //显示定位图标
            mBaiduMap.setMyLocationConfiguration(configuration);
        }else{
            // textView.setText("没有获取到位置信息!");
            Log.i("Location","没有获取到GPS信息");
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);//启动定位图层
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        //停止定位图层
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
