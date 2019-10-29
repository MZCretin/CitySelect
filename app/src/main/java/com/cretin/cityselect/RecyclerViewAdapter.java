package com.cretin.cityselect;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.BaseViewHolder> {

    public static final int TYPE_NORMAL = 0;

    private List<CityInfoModel> mDatas;

    private Context mContext;

    public RecyclerViewAdapter(Context context,List<CityInfoModel> data) {
        this.mDatas = data;
        this.mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建不同的 ViewHolder
        View view = null;

        //根据viewtype来创建条目
        if (viewType == TYPE_NORMAL) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_normal, parent, false);
            return new NormalHolder(view);
        }

        return new NormalHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        CityInfoModel cityInfoModel = mDatas.get(position);
        if (cityInfoModel.getType() == TYPE_NORMAL) {
            NormalHolder realHolder = (NormalHolder) holder;
            realHolder.tvContent.setText(cityInfoModel.getCityName());
        }
    }

    static class BaseViewHolder extends RecyclerView.ViewHolder{
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
}
