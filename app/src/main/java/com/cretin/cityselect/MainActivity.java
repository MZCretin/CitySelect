package com.cretin.cityselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.cretin.tools.cityselect.model.CityModel;

public class MainActivity extends AppCompatActivity {
    public static final int requestCode = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取城市
        findViewById(R.id.tv_select_city_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelfSelectCityActivity.class);
                intent.putExtra("type", 0);
                startActivityForResult(intent, requestCode);
            }
        });

        findViewById(R.id.tv_select_city_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelfSelectCityActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, requestCode);
            }
        });

        findViewById(R.id.tv_select_city_03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelfSelectCityActivity.class);
                intent.putExtra("type", 2);
                startActivityForResult(intent, requestCode);
            }
        });

        findViewById(R.id.tv_select_city_04).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelfSelectCityActivity.class);
                intent.putExtra("type", 3);
                startActivityForResult(intent, requestCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.requestCode) {
            if (resultCode == RESULT_OK) {
                CityModel model = (CityModel) data.getSerializableExtra("model");
                ((TextView) findViewById(R.id.city_result)).setText(
                        "您已选择城市：" + model.getCityName() + " " + model.getExtra().toString());
            }
        }
    }
}
