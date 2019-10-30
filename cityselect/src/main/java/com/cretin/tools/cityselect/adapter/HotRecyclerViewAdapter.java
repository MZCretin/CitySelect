package com.cretin.tools.cityselect.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cretin.tools.cityselect.R;
import com.cretin.tools.cityselect.callback.OnItemClickListener;
import com.cretin.tools.cityselect.model.CityInfoModel;

import java.util.List;

public class HotRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CityInfoModel> mDatas;

    private Context mContext;

    private OnItemClickListener itemClickListener;

    public HotRecyclerViewAdapter(Context context, List<CityInfoModel> data) {
        this.mDatas = data;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalHolder(LayoutInflater.from(mContext).inflate(R.layout.item_layout_hot_city, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CityInfoModel cityInfoModel = mDatas.get(position);
        NormalHolder realHolder = (NormalHolder) holder;
        realHolder.tvCity.setText(cityInfoModel.getCityName());
        realHolder.tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(mDatas.get(position));
                }
            }
        });
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

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
