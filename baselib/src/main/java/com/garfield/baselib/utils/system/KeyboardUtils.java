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

    private static int MIN_KEYBOARD_HEIGHT = ScreenUtils.dp2px(220);
    private static int LAST_SAVE_KEYBOARD_HEIGHT = MIN_KEYBOARD_HEIGHT;
    private static String KEY_KEYBOARD_HEIGHT = "keyboard_height";

    private static boolean saveKeyboardHeight(int keyboardHeight) {
        if (LAST_SAVE_KEYBOARD_HEIGHT == keyboardHeight) {
            return false;
        }
        LAST_SAVE_KEYBOARD_HEIGHT = keyboardHeight;
        return SharedPreferencesUtil.saveInt(KEY_KEYBOARD_HEIGHT, keyboardHeight);
    }

    public static int getKeyboardHeight() {
        return LAST_SAVE_KEYBOARD_HEIGHT;
    }

    public static void toggleKeyboard() {
        InputMethodManager imm = (InputMethodManager) Cache.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 通过检查当前的contentView是否小于去掉状态栏的屏幕的高来判断，随时可用
     */
    public static boolean isKeyboardShowing(View view) {
        int contentHeight = ScreenUtils.getDisplayFrameHeight(view);
        return ScreenUtils.screenHeight - ScreenUtils.statusBarHeight > contentHeight;
    }

    /**
     * 一个Activity一个
     */
    public static class KeyboardSizeMeasure {

        private View mContentView;
        private int mNormalContentHeight;
        private static int mKeyboardHeight;

        public KeyboardSizeMeasure(Activity activity) {
            mContentView = activity.findViewById(android.R.id.content);
            mContentView.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }

        private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int contentHeight = ScreenUtils.getDisplayFrameHeight(mContentView);
                if (contentHeight < ScreenUtils.screenHeight - ScreenUtils.statusBarHeight) {
                    // 键盘显示
                    int inputHeight = mNormalContentHeight - contentHeight;
                    if (mKeyboardHeight != inputHeight) {
                        mKeyboardHeight = inputHeight;
                        saveKeyboardHeight(mKeyboardHeight);
                    }
                } else {
                    // 键盘隐藏
                    mNormalContentHeight = contentHeight;
                }

            }
        };

        public void destroy() {
            // noinspection deprecation
            mContentView.getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
        }
    }

}
