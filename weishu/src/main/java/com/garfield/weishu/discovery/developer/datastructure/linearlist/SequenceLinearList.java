package com.garfield.weishu.discovery.developer.datastructure.linearlist;

/**
 * Created by gaowei3 on 2016/12/2.
 */

/**
 * 顺序线性列表
 *
 * 连续排列
 * 由预先初始化好的数组构成
 *
 */
public class SequenceLinearList<T> implements ILinearList<T> {
    private static final int MAX_SIZE = 100;

    /**
     * 不能用T，因为T不能被new
     */
    private Object[] mDataList = new Object[MAX_SIZE];
    private int mLength;

    @Override
    public boolean insert(int index, T elem) {
        if (index >= 0 && index <= mLength && mLength + 1 <= MAX_SIZE) {
            for (int i = mLength - 1; i >= index; i--) {
                mDataList[i + 1] = mDataList[i];
            }
            mDataList[index] = elem;
            ++ mLength;
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int index) {
        if (index >= 0 && index <= mLength - 1) {
            for (int i = index; i <= mLength - 1; i++) {
                if (i == mLength - 1) {
                    mDataList[i] = null;
                    break;
                }
                mDataList[i] = mDataList[i + 1];
            }
            -- mLength;
            return true;
        }
        return false;
    }

    @Override
    public T get(int index) {
        if (index >= 0 && index <= mLength - 1) {
            return (T) mDataList[index];
        }
        return null;
    }

    @Override
    public int locate(T elem) {
        for (int i = 0; i <= mLength - 1; i ++) {
            if (mDataList[i] == elem) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void clear() {
        for (int i = 0; i <= mLength - 1; i++) {
            mDataList[i] = null;
        }
        mLength = 0;
    }

    @Override
    public int length() {
        return mLength;
    }

//    public String toString() {
//
//        String str;
//        for (int i = 0; i <= mLength - 1; i ++) {
//            str
//        }
//    }
}
