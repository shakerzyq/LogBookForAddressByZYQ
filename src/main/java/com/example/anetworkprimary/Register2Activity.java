package com.example.anetworkprimary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register2Activity extends AppCompatActivity {
    String password;
    String password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);


        Bundle bundle = this.getIntent().getExtras();
        final String account = bundle.getString("account");
        final EditText editText = findViewById(R.id.password);
        final EditText editText1 = findViewById(R.id.password1);
        Button button = findViewById(R.id.password_b);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Register2Activity.this,"账号为"+account,Toast.LENGTH_LONG).show();
                password=editText.getText().toString();
                password2=editText1.getText().toString();
                if (!password.equals(password2)){
                    Toast.makeText(Register2Activity.this,password2+"两次密码不一样"+password,Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(Register2Activity.this,Register3Activity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("account",account);
                    bundle1.putString("password",password);
                    intent.putExtras(bundle1);
                    startActivity(intent);
                    finish();
                }

            }
        });


    }
}
