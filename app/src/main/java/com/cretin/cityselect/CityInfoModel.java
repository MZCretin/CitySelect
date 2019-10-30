package com.cretin.cityselect;

/**
 * @date: on 2019-10-29
 * @author: a112233
 * @email: mxnzp_life@163.com
 * @desc: 添加描述
 */
public class CityInfoModel {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_CURRENT = 1;//当前城市
    public static final int TYPE_HOT = 2;//热门城市

    private int type;
    private String cityName;
    private String sortId;
    private String sortName;
    private Object extra;

    public CityInfoModel(String cityName) {
        this.cityName = cityName;
    }

    public CityInfoModel() {
    }

    public CityInfoModel(int type, String cityName, String sortId, String sortName) {
        this.type = type;
        this.cityName = cityName;
        this.sortId = sortId;
        this.sortName = sortName;
    }

    public CityInfoModel(int type, String cityName, String sortId, String sortName, Object extra) {
        this.type = type;
        this.cityName = cityName;
        this.sortId = sortId;
        this.sortName = sortName;
        this.extra = extra;
    }

    public CityInfoModel(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }
}
