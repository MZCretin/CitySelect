package com.cretin.tools.cityselect.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.tools.cityselect.R;
import com.cretin.tools.cityselect.callback.OnItemClickListener;
import com.cretin.tools.cityselect.callback.OnLocationListener;
import com.cretin.tools.cityselect.model.CityInfoModel;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.BaseViewHolder> {

    private List<CityInfoModel> mDatas;

    private List<CityInfoModel> hotCitys;

    private Context mContext;

    private OnItemClickListener itemClickListener;

    private OnLocationListener locationListener;

    public MainAdapter(Context context, List<CityInfoModel> data) {
        this.mDatas = data;
        this.mContext = context;
    }

    public void bindHotCity(List<CityInfoModel> hotCitys) {
        this.hotCitys = hotCitys;
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
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        CityInfoModel cityInfoModel = mDatas.get(position);
        if (cityInfoModel.getType() == CityInfoModel.TYPE_NORMAL) {
            NormalHolder realHolder = (NormalHolder) holder;
            realHolder.tvContent.setText(cityInfoModel.getCityName());
            realHolder.tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(mDatas.get(position));
                    }
                }
            });
        } else if (cityInfoModel.getType() == CityInfoModel.TYPE_CURRENT) {
            //当前城市
            final CurrentCityHolder cityHolder = (CurrentCityHolder) holder;
            cityHolder.tv_current_city.setText(cityInfoModel.getCityName());
            cityHolder.tv_retry_location.setText(mContext.getResources().getString(R.string.str_retry_location));
            cityHolder.tv_retry_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cityHolder.tv_retry_location.setText(mContext.getResources().getString(R.string.str_is_location));
                    if (locationListener != null) {
                        locationListener.onLocation();
                    }
                }
            });
        } else if (cityInfoModel.getType() == CityInfoModel.TYPE_HOT) {
            //热门城市
            if (hotCitys != null) {
                HotRecyclerViewAdapter adapter = new HotRecyclerViewAdapter(mContext, hotCitys);
                HotCityHolder hotCityHolder = (HotCityHolder) holder;
                hotCityHolder.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                adapter.setItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(CityInfoModel cityInfoModel) {
                        if (itemClickListener != null) {
                            itemClickListener.onItemClick(cityInfoModel);
                        }
                    }
                });
                hotCityHolder.recyclerView.setAdapter(adapter);
            }
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
        TextView tv_current_city;
        TextView tv_retry_location;

        public CurrentCityHolder(View itemView) {
            super(itemView);
            tv_current_city = itemView.findViewById(R.id.tv_current_city);
            tv_retry_location = itemView.findViewById(R.id.tv_retry_location);
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setLocationListener(OnLocationListener locationListener) {
        this.locationListener = locationListener;
    }
}
