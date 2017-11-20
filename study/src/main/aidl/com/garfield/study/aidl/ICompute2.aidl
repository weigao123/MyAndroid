package com.garfield.study.aidl;

// 必须显式导入
import com.garfield.study.aidl.ICallback;

interface ICompute2 {
    int add(int a, int b);
    void register(ICallback callback);
}
