package com.cretin.cityselect;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.blankj.utilcode.util.ResourceUtils;
import com.cretin.cityselect.utils.JSONHelper;
import com.cretin.tools.cityselect.CityResponse;
import com.cretin.tools.cityselect.callback.OnCitySelectListener;
import com.cretin.tools.cityselect.callback.OnLocationListener;
import com.cretin.tools.cityselect.model.CityModel;
import com.cretin.tools.cityselect.view.CitySelectView;

import java.util.ArrayList;
import java.util.List;

public class SelectCityActivity extends AppCompatActivity {

    private CitySelectView citySelectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隐藏actionBar 好看点
        getSupportActionBar().hide();

        //状态栏透明 好看点 你自己看着办哈
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //状态看文字黑色好看点 你随意哈
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_select_city);

        citySelectView = findViewById(R.id.city_view);

        //拉取大数据还是要在子线程做的 我这是图简单 你别这样玩啊
        new Thread() {
            @Override
            public void run() {
                final List<CityModel> allCitys = new ArrayList<>();

                //这里是模仿网络请求获取城市列表 这里就放了一个json
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

                //设置热门城市列表 这都是瞎写的 哈哈哈
                final List<CityModel> hotCitys = new ArrayList<>();
                hotCitys.add(new CityModel("深圳", "10000000"));
                hotCitys.add(new CityModel("广州", "10000001"));
                hotCitys.add(new CityModel("北京", "10000002"));
                hotCitys.add(new CityModel("天津", "10000003"));
                hotCitys.add(new CityModel("武汉", "10000004"));

                //设置当前城市数据
                final CityModel currentCity = new CityModel("深圳", "10000000");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //绑定数据到视图 需要 所有城市列表 热门城市列表 和 当前城市列表
                        citySelectView.bindData(allCitys, hotCitys, currentCity);
                    }
                });
            }
        }.start();


        //设置城市选择之后的事件监听
        citySelectView.setOnCitySelectListener(new OnCitySelectListener() {
            @Override
            public void onCitySelect(CityModel cityModel) {
                Toast.makeText(SelectCityActivity.this, "你点击了：" + cityModel.getCityName() + ":" + cityModel.getExtra().toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("model", cityModel);
                setResult(RESULT_OK, intent);

                finish();
            }

            @Override
            public void onSelectCancel() {
                Toast.makeText(SelectCityActivity.this, "你取消了城市选择", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        //设置点击重新定位之后的事件监听
        citySelectView.setOnLocationListener(new OnLocationListener() {
            @Override
            public void onLocation() {
                //这里模拟定位 两秒后给个随便的定位数据
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
