package com.garfield.java.javacreate;

import com.garfield.java.util.L;

import java.net.URL;

/**
 * Created by gaowei on 2017/7/25.
 */

public class JAssist {

    public void test() throws Exception {

        MyClassLoader my1 = new MyClassLoader(new URL[]{ new URL("file:/C:/Projects/MyAndroid/java/build/classes/main/") });
        MyClassLoader my2 = new MyClassLoader(new URL[]{ new URL("file:/C:/Projects/MyAndroid/java/build/classes/main/") });
        Class<?> clz = my1.load("com.garfield.java.javacreate.A", true);
        //clz.newInstance();
        //my1.load("com.garfield.java.javacreate.A");

        L.d("\n");
        //clz.newInstance();
        L.d("[A] " + clz.getClassLoader());



        //Class<?> clz = Class.forName("com.garfield.java.javacreate.A", true, my1);
        //L.d(clz.getClassLoader());





//
//        A a = new A();  // 加载类A
//        B b = new B();  // 加载类B
//        a.setB(b);  // A引用了B，把b对象拷贝到A.b
//        System.out.printf("A classLoader is %s\n", a.getClass().getClassLoader());
//        System.out.printf("B classLoader is %s\n", b.getClass().getClassLoader());
//        System.out.printf("A.b classLoader is %s\n", a.getB().getClass().getClassLoader());
//
//        try {
//            URL[] urls = new URL[]{ new URL("file:/C:/Projects/MyAndroid/java/build/classes/main/") };
//            HotSwapClassLoader c1 = new HotSwapClassLoader(urls, a.getClass().getClassLoader());
//            Class clazz = c1.load("com.garfield.java.javacreate.A");  // 用hot swap重新加载类A
//            Object aInstance = clazz.newInstance();  // 创建A类对象
//            //A newA = (A)aInstance;
//            L.d(clazz.getClassLoader());
//            L.d(a.getClass().getClassLoader());
//            L.d(B.class.getClassLoader());
//            Method method1 = clazz.getMethod("setB", B.class);  // 获取setB(B b)方法
//            //Method method2 = clazz.getMethod("setA", A.class);  // 获取setB(B b)方法
//            method1.invoke(aInstance, b);    // 调用setB(b)方法，重新把b对象拷贝到A.b
//            Method method3 = clazz.getMethod("getB");  // 获取getB()方法
//            Object bInstance = method3.invoke(aInstance);  // 调用getB()方法
//            System.out.printf("Reloaded A.b classLoader is %s\n", bInstance.getClass().getClassLoader());
//        } catch (MalformedURLException | ClassNotFoundException |
//                InstantiationException | IllegalAccessException |
//                NoSuchMethodException | SecurityException |
//                IllegalArgumentException | InvocationTargetException e) {
//            e.printStackTrace();
//        }



//        new A();
//
//        ClassPool cp = ClassPool.getDefault();
//        CtClass cc = cp.get("com.garfield.java.javacreate.A");
//        CtMethod m = cc.getDeclaredMethod("say");
//        m.insertBefore("{ System.out.println(\"Hello.say():\"); }");
//        //cc.setName("com.garfield.java.javacreate.B");
//        Class c = cc.toClass();
//        //A h = (A)c.newInstance();
//        //h.say();
//
//
//
//        cc.writeFile("C://test");















//        ClassPool cp=ClassPool.getDefault();
//        // 创建一个类
//        CtClass ctClass=cp.makeClass("com.garfield.JavassistClass");
//
//        StringBuffer body=null;
//        // 创建一个属性
//        // 参数  1：属性类型  2：属性名称  3：所属类CtClass
//        CtField ctField=new CtField(cp.get("java.lang.String"), "name", ctClass);
//        ctField.setModifiers(Modifier.PRIVATE);
//
//        //设置一个属性的get set方法
//        ctClass.addMethod(CtNewMethod.setter("setName", ctField));
//        ctClass.addMethod(CtNewMethod.getter("getName", ctField));
//        ctClass.addField(ctField, CtField.Initializer.constant("default"));
//
//        // 创建一个构造器
//        // 参数  1：参数类型   2：所属类CtClass
//        CtConstructor ctConstructor=new CtConstructor(new CtClass[]{}, ctClass);
//        body=new StringBuffer();
//        body.append("{\n name=\"me\";\n}");
//        ctConstructor.setBody(body.toString());
//        ctClass.addConstructor(ctConstructor);
//
//        //参数：  1：返回类型  2：方法名称  3：传入参数类型  4：所属类CtClass
//        CtMethod ctMethod=new CtMethod(CtClass.voidType,"execute",new CtClass[]{},ctClass);
//        ctMethod.setModifiers(Modifier.PUBLIC);
//        body=new StringBuffer();
//        body.append("{\n System.out.println(name);");
//        body.append("\n System.out.println(\"execute ok\");");
//        body.append("\n return ;");
//        body.append("\n}");
//        ctMethod.setBody(body.toString());
//        ctClass.addMethod(ctMethod);
//        Class<?> c=ctClass.toClass();
//        Object o=c.newInstance();
//        Method method=o.getClass().getMethod("execute", new Class[]{});
//        //调用字节码生成类的execute方法
//        method.invoke(o, new Object[]{});
    }
}
