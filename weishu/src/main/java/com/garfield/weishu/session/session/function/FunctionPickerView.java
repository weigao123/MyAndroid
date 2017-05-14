package com.garfield.weishu.session.session.function;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.garfield.baselib.utils.system.KeyboardUtil;
import com.garfield.baselib.utils.system.ScreenUtil;

/**
 * Created by gaowei on 2017/5/2.
 */

public class FunctionPickerView extends FrameLayout {

    public FunctionPickerView(@NonNull Context context) {
        super(context);
    }

    public FunctionPickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FunctionPickerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            refreshHeight();
        }
    }

    private void refreshHeight() {
        int height = ScreenUtil.isPortrait() ? KeyboardUtil.getKeyboardHeight() : KeyboardUtil.MIN_KEYBOARD_HEIGHT_LANDSCAPE;
        if (getLayoutParams() != null && getLayoutParams().height != height) {
            getLayoutParams().height = height;
        }
    }
}
