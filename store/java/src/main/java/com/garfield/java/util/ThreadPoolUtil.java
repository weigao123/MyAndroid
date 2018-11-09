package com.garfield.java.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gaowei on 2017/6/30.
 */

public class ThreadPoolUtil {

    public static ExecutorService mExecutors = Executors.newFixedThreadPool(10);

}
