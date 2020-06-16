package com.example.anetworkprimary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import domain.Address;
/**
 * 用户中心
 * 获取位置,并显示
 * 修改密码和显示地图的跳转
 */
public class UserCenterActivity extends AppCompatActivity {

    private LocationService locationService;
    private String account;
    private   double Latitude;
    private   double Longitude;
    StringBuilder sb;
    TextView textView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);

        //接收传递的数据
        Bundle bundle = this.getIntent().getExtras();
        account = bundle.getString("account");
        //开启定位服务
        locationService = new LocationService(this);
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();

        TextView textView = findViewById(R.id.account);
        textView1 = findViewById(R.id.myaddress);
        textView.setText(account);

        Button button = findViewById(R.id.address);
        Button button1 = findViewById(R.id.changinfo);



        //打开地图
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UserCenterActivity.this,MyMapActivity.class);
               Bundle bundle = new Bundle();
                bundle.putDouble("Latitude",Latitude);
                bundle.putDouble("Longitude",Longitude);

                intent1.putExtras(bundle);
                startActivity(intent1);

            }
        });

        //点击修改个人密码
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UserCenterActivity.this,ChangePasswordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("account",account);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });

    }

    BDAbstractLocationListener mListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                sb = new StringBuilder(256);
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getCountry());

                Latitude=location.getLatitude();//获取纬度
                Longitude=location.getLongitude();//获得经度
                sb.append(location.getProvince());

                sb.append(location.getCity());

                sb.append(location.getDistrict());

                textView1.setText(sb);
            }
        }
    };
}
