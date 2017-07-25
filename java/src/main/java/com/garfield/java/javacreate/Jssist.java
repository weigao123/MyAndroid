package com.garfield.java.javacreate;

import java.lang.reflect.Method;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;

/**
 * Created by gaowei on 2017/7/25.
 */

public class Jssist {

    public static void test() throws Exception {
        ClassPool cp=ClassPool.getDefault();
        // 创建一个类
        CtClass ctClass=cp.makeClass("com.garfield.JavassistClass");

        StringBuffer body=null;
        // 创建一个属性
        // 参数  1：属性类型  2：属性名称  3：所属类CtClass
        CtField ctField=new CtField(cp.get("java.lang.String"), "name", ctClass);
        ctField.setModifiers(Modifier.PRIVATE);

        //设置一个属性的get set方法
        ctClass.addMethod(CtNewMethod.setter("setName", ctField));
        ctClass.addMethod(CtNewMethod.getter("getName", ctField));
        ctClass.addField(ctField, CtField.Initializer.constant("default"));

        // 创建一个构造器
        // 参数  1：参数类型   2：所属类CtClass
        CtConstructor ctConstructor=new CtConstructor(new CtClass[]{}, ctClass);
        body=new StringBuffer();
        body.append("{\n name=\"me\";\n}");
        ctConstructor.setBody(body.toString());
        ctClass.addConstructor(ctConstructor);

        //参数：  1：返回类型  2：方法名称  3：传入参数类型  4：所属类CtClass
        CtMethod ctMethod=new CtMethod(CtClass.voidType,"execute",new CtClass[]{},ctClass);
        ctMethod.setModifiers(Modifier.PUBLIC);
        body=new StringBuffer();
        body.append("{\n System.out.println(name);");
        body.append("\n System.out.println(\"execute ok\");");
        body.append("\n return ;");
        body.append("\n}");
        ctMethod.setBody(body.toString());
        ctClass.addMethod(ctMethod);
        Class<?> c=ctClass.toClass();
        Object o=c.newInstance();
        Method method=o.getClass().getMethod("execute", new Class[]{});
        //调用字节码生成类的execute方法
        method.invoke(o, new Object[]{});
    }
}
