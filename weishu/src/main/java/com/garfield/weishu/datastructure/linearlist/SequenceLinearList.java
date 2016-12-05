package com.garfield.weishu.datastructure.linearlist;

/**
 * Created by gaowei3 on 2016/12/2.
 */

public class SequenceLinearList<T> implements ILinearList<T> {
    private static final int MAX_SIZE = 100;

    private Object[] mDataList = new Object[MAX_SIZE];
    private int mLength;

    @Override
    public void clear() {
        for (int i = 0; i <= mLength - 1; i++) {
            mDataList[i] = null;
        }
        mLength = 0;
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
    public boolean insert(int index, Object elem) {
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
