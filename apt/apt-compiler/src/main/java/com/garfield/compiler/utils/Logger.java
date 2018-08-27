package com.garfield.compiler.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;


public class Logger {

    private static Messager msg;

    public static void setMessager(Messager messager) {
        msg = messager;
    }

    public static void info(CharSequence info) {
        msg.printMessage(Diagnostic.Kind.NOTE, Consts.DEBUG_TAG + info);
        //System.out.println(Consts.DEBUG_TAG + "info: " + info);
    }

    public static void error(CharSequence error) {
        msg.printMessage(Diagnostic.Kind.ERROR, Consts.DEBUG_TAG + "An exception is encountered, [" + error + "]");
        //System.out.println(Consts.DEBUG_TAG + "error: " + error);
    }

    public static void error(Throwable error) {
        msg.printMessage(Diagnostic.Kind.ERROR, Consts.DEBUG_TAG + "An exception is encountered, [" + error.getMessage() + "]" + "\n" + formatStackTrace(error.getStackTrace()));
    }

    public static void warning(CharSequence warning) {
        msg.printMessage(Diagnostic.Kind.WARNING, Consts.DEBUG_TAG + warning);
    }

    private static String formatStackTrace(StackTraceElement[] stackTrace) {
//        StringWriter stackTrace = new StringWriter();
//        e.printStackTrace(new PrintWriter(stackTrace));

        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            sb.append("    at ").append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
