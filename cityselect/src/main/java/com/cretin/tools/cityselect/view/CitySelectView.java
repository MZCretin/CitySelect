package com.cretin.tools.cityselect.view;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cretin.tools.cityselect.R;
import com.cretin.tools.cityselect.adapter.MainAdapter;
import com.cretin.tools.cityselect.callback.OnCitySelectListener;
import com.cretin.tools.cityselect.callback.OnItemClickListener;
import com.cretin.tools.cityselect.callback.OnLocationListener;
import com.cretin.tools.cityselect.item.CustomItemDecoration;
import com.cretin.tools.cityselect.model.CityInfoModel;
import com.cretin.tools.cityselect.model.CityModel;
import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @date: on 2019-10-30
 * @author: a112233
 * @email: mxnzp_life@163.com
 * @desc: 选择城市的View
 */
public class CitySelectView extends ConstraintLayout {
    //views
    private EditText edSearch;
    private TextView tvCancel;
    private RecyclerView mainRecyclerView;
    private FastIndexView indexSideView;
    private TextView indexView;

    //data and model
    //主要用于展示数据的list
    private List<CityInfoModel> list;
    //第一次加载之后缓存的数据
    private List<CityInfoModel> cacheList;
    //用于存储搜索结果的list
    private List<CityInfoModel> searchList;

    //页面recyclerview的适配器
    private MainAdapter mainAdapter;
    //布局管理器
    private LinearLayoutManager layoutManager;

    private Context mContext;

    //定时器
    private Timer timer;
    //定时任务
    private TimerTask timerTask;

    private OnCitySelectListener citySelectListener;

    private OnLocationListener locationListener;

    //记录是否绑定过数据
    private boolean hasBindData = false;

    public CitySelectView(Context context) {
        this(context, null, 0);
    }

    public CitySelectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CitySelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;

        View.inflate(context, R.layout.layout_city_select_view, this);

        //读取xml属性 todo

        initView();

        initAdapter();

        initListener();
    }

    private void initView() {
        edSearch = findViewById(R.id.ed_search);
        tvCancel = findViewById(R.id.tv_cancel);
        mainRecyclerView = findViewById(R.id.recyclerView);
        indexSideView = findViewById(R.id.fastIndexView);
        indexView = findViewById(R.id.tv_index);
    }

    private void initAdapter() {
        list = new ArrayList<>();
        cacheList = new ArrayList<>();
        searchList = new ArrayList<>();

        layoutManager = new LinearLayoutManager(mContext);
        mainRecyclerView.setLayoutManager(layoutManager);
        mainRecyclerView.addItemDecoration(new CustomItemDecoration(mContext, new CustomItemDecoration.TitleDecorationCallback() {
            @Override
            public String getGroupId(int position) {
                //这个是用来比较是否是同一组数据的
                return list.get(position).getSortId();
            }

            @Override
            public String getGroupName(int position) {
                CityInfoModel cityInfoModel = list.get(position);
                if (cityInfoModel.getType() == CityInfoModel.TYPE_CURRENT || cityInfoModel.getType() == CityInfoModel.TYPE_HOT) {
                    return cityInfoModel.getSortName();
                }
                //拼音都是小写的
                return cityInfoModel.getSortId().toUpperCase();
            }
        }));
        mainAdapter = new MainAdapter(mContext, list);
        //设置item的点击事件
        mainAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(CityInfoModel cityInfoModel) {
                if (citySelectListener != null) {
                    citySelectListener.onCitySelect(new CityModel(cityInfoModel.getCityName(), cityInfoModel.getExtra()));
                }
            }
        });
        //设置定位的点击时间
        mainAdapter.setLocationListener(new OnLocationListener() {
            @Override
            public void onLocation() {
                if (locationListener != null) {
                    locationListener.onLocation();
                }
            }
        });
        mainRecyclerView.setAdapter(mainAdapter);
    }

    private void initListener() {
        indexSideView.setListener(new FastIndexView.OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
                indexView.setText(letter);
                indexView.setVisibility(View.VISIBLE);

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
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                indexView.setVisibility(View.GONE);
                            }
                        });
                    }
                };
                timer = new Timer();
                timer.schedule(timerTask, 500);
            }
        });

        edSearch.addTextChangedListener(new TextWatcher() {
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

        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (citySelectListener != null) {
                    citySelectListener.onSelectCancel();
                }
            }
        });
    }

    /**
     * 给View绑定数据
     *
     * @param allCity     所有城市列表
     * @param hotCity     热门城市列表
     * @param currentCity 当前城市
     */
    public void bindData(List<CityModel> allCity, List<CityModel> hotCity, CityModel currentCity) {
        if (allCity != null) {
            for (CityModel cityModel : allCity) {
                try {
                    String pingYin = PinyinHelper.convertToPinyinString(cityModel.getCityName(), " ", PinyinFormat.WITHOUT_TONE);
                    cacheList.add(new CityInfoModel(CityInfoModel.TYPE_NORMAL, cityModel.getCityName(), pingYin.substring(0, 1), pingYin, cityModel.getExtra()));
                } catch (PinyinException e) {
                    e.printStackTrace();
                }
            }
            //排序
            Collections.sort(cacheList, new Comparator<CityInfoModel>() {
                @Override
                public int compare(CityInfoModel o1, CityInfoModel o2) {
                    return o1.getSortName().compareTo(o2.getSortName());
                }
            });

            List<CityInfoModel> hotList = new ArrayList<>();
            for (CityModel cityModel : hotCity) {
                hotList.add(new CityInfoModel(0, cityModel.getCityName(), "", "", cityModel.getExtra()));
            }

            if (hotCity != null) {
                mainAdapter.bindHotCity(hotList);
                cacheList.add(0, new CityInfoModel(CityInfoModel.TYPE_HOT, "", "#", "热门城市", "hot"));
            }

            if (currentCity != null)
                cacheList.add(0, new CityInfoModel(CityInfoModel.TYPE_CURRENT, currentCity.getCityName(), "*", "当前定位城市", currentCity.getExtra()));

            this.list.clear();
            this.list.addAll(cacheList);
            mainAdapter.notifyDataSetChanged();

            hasBindData = true;
        }
    }

    /**
     * 重新绑定当前城市
     *
     * @param currentCity
     */
    public void reBindCurrentCity(CityModel currentCity) {
        if (!hasBindData) {
            throw new RuntimeException("请先绑定数据再调用重新绑定当前城市的方法");
        }
        for (CityInfoModel cityInfoModel : cacheList) {
            if (cityInfoModel.getType() == CityInfoModel.TYPE_CURRENT) {
                //有 找到了
                cityInfoModel.setCityName(currentCity.getCityName());
                cityInfoModel.setExtra(currentCity.getExtra());
                mainAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    //滚动recyclerview
    private void moveToLetterPosition(String letter) {
        //这里主要是为了跳转到最顶端
        if ("#".equals(letter)) {
            letter = "*";
        }
        for (int i = 0; i < list.size(); i++) {
            CityInfoModel cityInfoModel = list.get(i);
            if (cityInfoModel.getSortId().toUpperCase().equals(letter)) {
                layoutManager.scrollToPositionWithOffset(i, 0);
                return;
            }
        }
    }

    //执行搜索
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
        mainAdapter.notifyDataSetChanged();
    }

    public void setOnCitySelectListener(OnCitySelectListener listener) {
        this.citySelectListener = listener;
    }

    public void setOnLocationListener(OnLocationListener locationListener) {
        this.locationListener = locationListener;
    }
}
