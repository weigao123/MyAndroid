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

    private volatile boolean hasValue;

    void set() {
        L.p("set1");
        lock.lock();
        L.p("set2");
        if (hasValue) {
            L.cwait(condition);
        }
        hasValue = true;
        L.p("set value");
        L.sleep(1000);
        condition.signal();
        lock.unlock();
    }

    void get() {
        L.p("get1");
        lock.lock();
        L.p("get2");
        if (!hasValue) {
            L.cwait(condition);
        }
        hasValue = false;
        L.p("get value");
        L.sleep(1000);
        condition.signal();
        lock.unlock();
    }
}
