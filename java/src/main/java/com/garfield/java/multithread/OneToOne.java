package com.garfield.java.multithread;

import com.garfield.java.util.L;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by gaowei on 2017/6/30.
 */

class OneToOne {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private boolean hasValue;

    void set() {
        L.d("set1");
        lock.lock();
        L.d("set2: "+hasValue);
        if (hasValue) {
            L.cwait(condition);
        }
        hasValue = true;
        L.d("set value");
        L.sleep(1000);
        condition.signal();
        lock.unlock();
    }

    void get() {
        L.d("get1");
        lock.lock();
        L.d("get: "+hasValue);
        if (!hasValue) {
            L.cwait(condition);
        }
        hasValue = false;
        L.d("get value");
        L.sleep(1000);
        condition.signal();
        lock.unlock();
    }
}
