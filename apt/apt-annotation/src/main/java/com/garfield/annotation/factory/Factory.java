package com.garfield.annotation.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gaowei on 2017/7/10.
 */

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Factory {

    /**
     * 工厂的名字，同一个工厂要一致
     * 生成的文件会放到这个接口所在的目录
     */
    Class type();

    /**
     * 用来表示生成哪个对象的唯一id
     */
    String id();
}
