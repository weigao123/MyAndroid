package com.garfield.baselib.utils.system;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.garfield.baselib.Cache;

/**
 * Created by gaowei3 on 2016/9/21.
 */

public class KeyboardUtils {

    public static final int MIN_KEYBOARD_HEIGHT = ScreenUtils.dp2px(220);
    public static final int MIN_KEYBOARD_HEIGHT_LANDSCAPE = ScreenUtils.dp2px(150);
    
    private static int LAST_SAVE_KEYBOARD_HEIGHT = MIN_KEYBOARD_HEIGHT;
    private static final String KEY_KEYBOARD_HEIGHT = "keyboard_height";

    public static void showKeyboard(View focusView) {
        if (focusView == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) focusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(focusView, 0);
    }

    public static void hideKeyboard(View focusView) {
        if (focusView == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) focusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
    }

    public static int getKeyboardHeight() {
        if (LAST_SAVE_KEYBOARD_HEIGHT == MIN_KEYBOARD_HEIGHT) {
            LAST_SAVE_KEYBOARD_HEIGHT = SharedPreferencesUtil.getInt(KEY_KEYBOARD_HEIGHT, MIN_KEYBOARD_HEIGHT);
        }
        return LAST_SAVE_KEYBOARD_HEIGHT;
    }

    public static void toggleKeyboard() {
        InputMethodManager imm = (InputMethodManager) Cache.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 通过检查当前的contentView是否小于去掉状态栏的屏幕的高来判断，随时可用，仅适用于竖屏
     */
    public static boolean isKeyboardShowing(View view) {
        int contentHeight = ScreenUtils.getDisplayFrameHeight(view);
        return ScreenUtils.screenHeight - ScreenUtils.statusBarHeight > contentHeight;
    }

    private static boolean saveKeyboardHeight(int keyboardHeight) {
        if (LAST_SAVE_KEYBOARD_HEIGHT == keyboardHeight) {
            return false;
        }
        LAST_SAVE_KEYBOARD_HEIGHT = keyboardHeight;
        return SharedPreferencesUtil.saveInt(KEY_KEYBOARD_HEIGHT, keyboardHeight);
    }

    /**
     * 一个Activity一个
     */
    public static class KeyboardSizeMeasure {

        private View mContentView;
        private static int mKeyboardHeight;

        public KeyboardSizeMeasure(Activity activity) {
            mContentView = activity.findViewById(android.R.id.content);
            mContentView.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }

        private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (!ScreenUtils.isPortrait()) {
                    return;
                }
                int contentHeight = ScreenUtils.getDisplayFrameHeight(mContentView);
                // 键盘出现
                if (contentHeight < ScreenUtils.contentHeight) {
                    int inputHeight = ScreenUtils.contentHeight - contentHeight;
                    if (mKeyboardHeight != inputHeight) {
                        mKeyboardHeight = inputHeight;
                        saveKeyboardHeight(mKeyboardHeight);
                    }
                }
            }
        };

        public void finish() {
            // noinspection deprecation
            mContentView.getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
        }
    }

}
