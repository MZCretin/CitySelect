package com.cretin.cityselect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ResourceUtils;
import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private MainRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private EditText searchView;
    private TextView tvRight;
    private FastIndexView fastIndexView;
    private List<CityInfoModel> list;
    private List<CityInfoModel> cacheList;
    private List<CityInfoModel> searchList;
    private TextView tvIndex;
    private Timer timer;
    private TimerTask timerTask;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fastIndexView = findViewById(R.id.fastIndexView);
        tvIndex = findViewById(R.id.tv_index);
        searchView = findViewById(R.id.ed_search);
        tvRight = findViewById(R.id.tv_right);

        list = new ArrayList<>();
        cacheList = new ArrayList<>();
        searchList = new ArrayList<>();

        final String city = ResourceUtils.readAssets2String("city.json");
        try {
            CityResponse cityResponse = JSONHelper.parseObject(city, CityResponse.class);
            List<CityResponse.DataBean> data = cityResponse.getData();
            for (CityResponse.DataBean item : data) {
                if (item.getSons() == null) {
                    String pingYin = PinyinHelper.convertToPinyinString(item.getName(), " ", PinyinFormat.WITHOUT_TONE);
                    cacheList.add(new CityInfoModel(CityInfoModel.TYPE_NORMAL, item.getName(), pingYin.substring(0, 1), pingYin, item.getAreaId()));
                } else {
                    for (CityResponse.DataBean.SonsBean son : item.getSons()) {
                        String pingYin = PinyinHelper.convertToPinyinString(son.getName(), " ", PinyinFormat.WITHOUT_TONE);
                        cacheList.add(new CityInfoModel(CityInfoModel.TYPE_NORMAL, son.getName(), pingYin.substring(0, 1), pingYin, item.getAreaId()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(cacheList, new Comparator<CityInfoModel>() {
            @Override
            public int compare(CityInfoModel o1, CityInfoModel o2) {
                return o1.getSortName().compareTo(o2.getSortName());
            }
        });

        cacheList.add(0, new CityInfoModel(CityInfoModel.TYPE_HOT, "", "#", "热门城市", "hot"));
        cacheList.add(0, new CityInfoModel(CityInfoModel.TYPE_CURRENT, "", "*", "当前定位城市", "current"));
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new CustomItemDecoration(this, new CustomItemDecoration.TitleDecorationCallback() {
            @Override
            public String getGroupId(int position) {
                return list.get(position).getSortId();
            }

            @Override
            public String getGroupName(int position) {
                CityInfoModel cityInfoModel = list.get(position);
                if (cityInfoModel.getType() == CityInfoModel.TYPE_CURRENT || cityInfoModel.getType() == CityInfoModel.TYPE_HOT) {
                    return cityInfoModel.getSortName();
                }
                return cityInfoModel.getSortId().toUpperCase();
            }
        }));

        list.clear();
        list.addAll(cacheList);
        recyclerViewAdapter = new MainRecyclerViewAdapter(this, list);
        recyclerView.setAdapter(recyclerViewAdapter);

        fastIndexView.setListener(new FastIndexView.OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
                tvIndex.setText(letter);
                tvIndex.setVisibility(View.VISIBLE);

                moveToLetterPosition(letter);

                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }

                if (timerTask != null) {
                    timerTask.cancel();
                    timerTask = null;
                }
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvIndex.setVisibility(View.GONE);
                            }
                        });
                    }
                };
                timer = new Timer();
                timer.schedule(timerTask, 500);
            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void moveToLetterPosition(String letter) {
        if ("#".equals(letter)) {
            letter = "*";
        }
        for (int i = 0; i < list.size(); i++) {
            CityInfoModel cityInfoModel = list.get(i);
            if (cityInfoModel.getSortId().toUpperCase().equals(letter)) {
                linearLayoutManager.scrollToPositionWithOffset(i, 0);
                return;
            }
        }
    }

    private void search(String key) {
        searchList.clear();
        boolean isChiness = ChineseHelper.containsChinese(key);
        if (isChiness) {
            for (CityInfoModel cityInfoModel : cacheList) {
                boolean has = true;
                HH:
                for (char c : key.toCharArray()) {
                    if (!cityInfoModel.getCityName().contains(c + "")) {
                        has = false;
                        break HH;
                    }
                }
                if (has) {
                    searchList.add(cityInfoModel);
                }
            }
        } else {
            for (CityInfoModel cityInfoModel : cacheList) {
                boolean has = true;
                HH:
                for (char c : key.toCharArray()) {
                    if (!cityInfoModel.getSortName().contains(c + "")) {
                        has = false;
                        break HH;
                    }
                }
                if (has) {
                    searchList.add(cityInfoModel);
                }
            }
        }
        list.clear();
        list.addAll(searchList);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}
