package com.cretin.cityselect;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.BaseViewHolder> {

    private List<CityInfoModel> mDatas;

    private Context mContext;

    public MainRecyclerViewAdapter(Context context, List<CityInfoModel> data) {
        this.mDatas = data;
        this.mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建不同的 ViewHolder
        View view = null;

        //根据viewtype来创建条目
        if (viewType == CityInfoModel.TYPE_NORMAL) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_normal, parent, false);
            return new NormalHolder(view);
        } else if (viewType == CityInfoModel.TYPE_CURRENT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_current_city, parent, false);
            return new CurrentCityHolder(view);
        } else if (viewType == CityInfoModel.TYPE_HOT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_hot_view, parent, false);
            return new HotCityHolder(view);
        }

        return new NormalHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        CityInfoModel cityInfoModel = mDatas.get(position);
        if (cityInfoModel.getType() == CityInfoModel.TYPE_NORMAL) {
            NormalHolder realHolder = (NormalHolder) holder;
            realHolder.tvContent.setText(cityInfoModel.getCityName());
        } else if (cityInfoModel.getType() == CityInfoModel.TYPE_CURRENT) {
            //当前城市
        } else if (cityInfoModel.getType() == CityInfoModel.TYPE_HOT) {
            //热门城市
            List<CityInfoModel> list = new ArrayList<>();
            list.add(new CityInfoModel("深圳"));
            list.add(new CityInfoModel("深圳"));
            list.add(new CityInfoModel("深圳"));
            list.add(new CityInfoModel("深圳"));
            list.add(new CityInfoModel("深圳"));
            list.add(new CityInfoModel("深圳"));
            list.add(new CityInfoModel("深圳"));
            list.add(new CityInfoModel("深圳"));
            HotRecyclerViewAdapter adapter = new HotRecyclerViewAdapter(mContext,list);
            HotCityHolder hotCityHolder = (HotCityHolder) holder;
            hotCityHolder.recyclerView.setLayoutManager(new GridLayoutManager(mContext,3));
            hotCityHolder.recyclerView.setAdapter(adapter);
        }
    }

    static class BaseViewHolder extends RecyclerView.ViewHolder {
        BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    //根据条件返回条目的类型
    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getType();
    }

    private class NormalHolder extends BaseViewHolder {
        TextView tvContent;

        public NormalHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_city);
        }
    }

    private class HotCityHolder extends BaseViewHolder {
        RecyclerView recyclerView;

        public HotCityHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }

    private class CurrentCityHolder extends BaseViewHolder {

        public CurrentCityHolder(View itemView) {
            super(itemView);

        }
    }
}
