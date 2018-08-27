package com.garfield.baselib.utils.drawable;

import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import com.garfield.baselib.Cache;

/**
 * Created by gaowei3 on 2016/11/18.
 */

public class ColorUtils {

    public static int getColor(@ColorRes int res) {
        return ContextCompat.getColor(Cache.getContext(), res);
    }

    /**
     * 颜色必须是ARGB
     * 等同于 new ArgbEvaluator().evaluate(fraction, startInt, endInt);
     */
    public static Integer evaluate(float fraction, Integer startInt, Integer endInt) {
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;
        return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
                | (int) ((startR + (int) (fraction * (endR - startR))) << 16)
                | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
                | (int) ((startB + (int) (fraction * (endB - startB))));
    }
}
