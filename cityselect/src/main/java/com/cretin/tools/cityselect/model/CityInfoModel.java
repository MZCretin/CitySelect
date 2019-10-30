package com.cretin.tools.cityselect.model;

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
    private String cityName;//用于显示的城市的名字
    private String sortId;//用于排序的id 在这里是城市拼音的首字母
    private String sortName;//用于排序的全拼音 这个是用于后面的排序以及搜索
    private Object extra;//附加参数 比如 一般来说 城市都有对应的一个id 可以存储在这里

    public CityInfoModel() {

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

    public Object getExtra() {
        return extra;
    }
}
