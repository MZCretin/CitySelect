package com.cretin.cityselect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.blankj.utilcode.util.ResourceUtils;
import com.cretin.tools.cityselect.CityResponse;
import com.cretin.tools.cityselect.callback.OnCitySelectListener;
import com.cretin.tools.cityselect.callback.OnLocationListener;
import com.cretin.tools.cityselect.model.CityModel;
import com.cretin.tools.cityselect.utils.JSONHelper;
import com.cretin.tools.cityselect.view.CitySelectView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CitySelectView citySelectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        citySelectView = findViewById(R.id.city_view);

        List<CityModel> allCitys = new ArrayList<>();

        final String city = ResourceUtils.readAssets2String("city.json");
        try {
            CityResponse cityResponse = JSONHelper.parseObject(city, CityResponse.class);
            List<CityResponse.DataBean> data = cityResponse.getData();
            for (CityResponse.DataBean item : data) {
                if (item.getSons() == null) {
                    allCitys.add(new CityModel(item.getName(), item.getAreaId()));
                } else {
                    for (CityResponse.DataBean.SonsBean son : item.getSons()) {
                        allCitys.add(new CityModel(son.getName(), son.getAreaId()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<CityModel> hotCitys = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            hotCitys.add(new CityModel("深圳", "10000000"));
        }

        //当前城市
        CityModel currentCity = new CityModel("深圳", "10000000");

        citySelectView.bindData(allCitys, hotCitys, currentCity);

        citySelectView.setOnCitySelectListener(new OnCitySelectListener() {
            @Override
            public void onCitySelect(CityModel cityModel) {
                Toast.makeText(MainActivity.this, "你点击了：" + cityModel.getCityName() + ":" + cityModel.getExtra().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSelectCancel() {
                Toast.makeText(MainActivity.this, "你取消了城市选择", Toast.LENGTH_SHORT).show();
            }
        });

        citySelectView.setOnLocationListener(new OnLocationListener() {
            @Override
            public void onLocation() {
                citySelectView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        citySelectView.reBindCurrentCity(new CityModel("广州", "10000001"));
                    }
                }, 2000);
            }
        });
    }
}
