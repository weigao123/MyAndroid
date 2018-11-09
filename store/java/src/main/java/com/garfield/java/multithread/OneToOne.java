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
        L.dl("set1");
        lock.lock();
        L.dl("set2: "+hasValue);
        if (hasValue) {
            L.cwait(condition);
        }
        hasValue = true;
        L.dl("set value");
        L.sleep(1000);
        condition.signal();
        lock.unlock();
    }

    void get() {
        L.dl("get1");
        lock.lock();
        L.dl("get: "+hasValue);
        if (!hasValue) {
            L.cwait(condition);
        }
        hasValue = false;
        L.dl("get value");
        L.sleep(1000);
        condition.signal();
        lock.unlock();
    }
}
