package com.cretin.cityselect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ResourceUtils;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<CityInfoModel> list;
    private FastIndexView fastIndexView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fastIndexView = findViewById(R.id.fastIndexView);

        list = new ArrayList<>();

        String city = ResourceUtils.readAssets2String("city.json");
        try {
            CityResponse cityResponse = JSONHelper.parseObject(city, CityResponse.class);
            List<CityResponse.DataBean> data = cityResponse.getData();
            for (CityResponse.DataBean item : data) {
                if (item.getSons() == null) {
                    String firstStr = "";
                    if (!TextUtils.isEmpty(item.getName())) {
                        firstStr = item.getName().substring(0, 1);
                    }
                    String shortPinyin = PinyinHelper.getShortPinyin(firstStr);
                    String pingYin = PinyinHelper.convertToPinyinString(item.getName(), " ", PinyinFormat.WITHOUT_TONE);
                    list.add(new CityInfoModel(RecyclerViewAdapter.TYPE_NORMAL, item.getName(), shortPinyin, pingYin, item.getAreaId()));
                } else {
                    for (CityResponse.DataBean.SonsBean son : item.getSons()) {
                        String firstStr = "";
                        if (!TextUtils.isEmpty(son.getName())) {
                            firstStr = son.getName().substring(0, 1);
                        }
                        String shortPinyin = PinyinHelper.getShortPinyin(firstStr);
                        String pingYin = PinyinHelper.convertToPinyinString(son.getName(), " ", PinyinFormat.WITHOUT_TONE);
                        list.add(new CityInfoModel(RecyclerViewAdapter.TYPE_NORMAL, son.getName(), shortPinyin, pingYin, item.getAreaId()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(list, new Comparator<CityInfoModel>() {
            @Override
            public int compare(CityInfoModel o1, CityInfoModel o2) {
                return o1.getSortName().compareTo(o2.getSortName());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new CustomItemDecoration(this, new CustomItemDecoration.TitleDecorationCallback() {
            @Override
            public String getGroupId(int position) {
                return list.get(position).getSortId();
            }

            @Override
            public String getGroupName(int position) {
                return list.get(position).getSortId().toUpperCase();
            }
        }));
        recyclerViewAdapter = new RecyclerViewAdapter(this, list);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
