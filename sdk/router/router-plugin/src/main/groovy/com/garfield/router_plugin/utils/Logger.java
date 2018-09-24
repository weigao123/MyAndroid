package com.garfield.router_plugin.utils;

public class Logger {

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
