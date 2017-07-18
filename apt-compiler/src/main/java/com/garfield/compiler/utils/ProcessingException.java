package com.garfield.compiler.utils;

import javax.lang.model.element.Element;

/**
 * Created by gaowei on 2017/7/10.
 */
public class ProcessingException extends Exception {

    Element element;

    public ProcessingException(Element element, String msg, Object... args) {
        super(String.format(msg, args));
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}
