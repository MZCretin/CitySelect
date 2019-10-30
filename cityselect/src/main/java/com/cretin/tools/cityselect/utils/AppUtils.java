package com.cretin.tools.cityselect.utils;

import android.content.res.Resources;

/**
 * @date: on 2019-10-10
 * @author: a112233
 * @email: mxnzp_life@163.com
 * @desc: 添加描述
 */
public class AppUtils {

    private static float density;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        if (density == 0)
            density = Resources.getSystem().getDisplayMetrics().density;
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }


}
