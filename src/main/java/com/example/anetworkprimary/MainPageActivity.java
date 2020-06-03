package com.example.anetworkprimary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        ImageView imageView1 = findViewById(R.id.image1);
        ImageView imageView2 = findViewById(R.id.image2);
        ImageView imageView3 = findViewById(R.id.image3);

        //添加单击事件监听器
        imageView1.setOnClickListener(l);
        imageView2.setOnClickListener(l);
        imageView3.setOnClickListener(l);

    }
    //由于4个都是一样的所以创建单击事件监听器对象
    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //获取fragmentmanager管理器
            FragmentManager fm = getSupportFragmentManager();
            //获取FragmentTransaction对象
            FragmentTransaction ft = fm.beginTransaction();
            //初始化一个Fragment对象
            Fragment f =null;
            //编写一个switch语句,通过swith语句判断单击了哪个按钮,并根据结果创建相应的对象
            switch(v.getId()){
                case R.id.image1:
                    f=new AddFragment();
                    break;
                case R.id.image2:
                    f=new LogFragment();
                    break;
                case R.id.image3:
                    //f=new Find_Fragment();
                    break;
                default:
                    break;
            }
            //替换每次所显示的Fragment
            ft.replace(R.id.fragment,f);
            ft.commit();
        }
    };
}
