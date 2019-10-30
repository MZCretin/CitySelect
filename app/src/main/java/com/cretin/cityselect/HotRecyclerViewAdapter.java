package com.cretin.cityselect;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HotRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CityInfoModel> mDatas;

    private Context mContext;

    public HotRecyclerViewAdapter(Context context, List<CityInfoModel> data) {
        this.mDatas = data;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalHolder(LayoutInflater.from(mContext).inflate(R.layout.item_layout_hot_city, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CityInfoModel cityInfoModel = mDatas.get(position);
        NormalHolder realHolder = (NormalHolder) holder;
        realHolder.tvCity.setText(cityInfoModel.getCityName());
    }


    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    private class NormalHolder extends RecyclerView.ViewHolder {
        TextView tvCity;

        public NormalHolder(View itemView) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.tv_city);
        }
    }

}
