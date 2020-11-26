package com.garfield.plugin.util;

public class Logger {
    //字背景颜色范围:40----49
    //40:黑
    //41:深红
    //42:绿
    //43:黄色
    //44:蓝色
    //45:紫色
    //46:深绿
    //47:白色
    //
    //字颜色:30-----------39
    //30:黑
    //31:红
    //32:绿
    //33:黄
    //34:蓝色
    //35:紫色
    //36:深绿
    //37:白色
    //
    //===============================================
    //    ANSI控制码的说明
    //\33[0m 关闭所有属性
    //\33[1m 加粗
    //\33[4m 下划线
    //\33[5m 闪烁
    //\33[7m 反显
    //\33[8m 消隐
    //\33[30m -- \33[37m 设置前景色
    //\33[40m -- \33[47m 设置背景色
    //\33[nA 光标上移n行
    //\33[nB 光标下移n行
    //\33[nC 光标右移n行
    //\33[nD 光标左移n行
    //\33[y;xH设置光标位置
    //\33[2J 清屏
    //\33[K 清除从光标到行尾的内容
    //\33[s 保存光标位置
    //\33[u 恢复光标位置
    //\33[?25l 隐藏光标
    //\33[?25h 显示光标

    public static void d(String msg) {
        System.out.println("\033[36m" + msg + "\033[0m");
    }

    public static void d(Object msg) {
        System.out.println("\033[36m" + msg.toString() + "\033[0m");
    }

    public static void i(String msg) {
        System.out.println("\033[33m" + msg + "\033[0m");
    }

    public static void w(String msg) {
        System.out.println("\033[33;1m" + msg + "\033[0m");
    }

    public static void e(String msg) {
        System.out.println("\033[31;1m" + msg + "\033[0m");
    }
}