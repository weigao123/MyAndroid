package com.garfield.java;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

public class TestJava {

    public static void main(String[] args) {
        new TestJava().doTest();
    }

    private void doTest() {
        //new TestMulti().doTest();


        MethodSpec main = MethodSpec.methodBuilder("main")
                .addStatement("int total = 0")
                .beginControlFlow("for (int i = 0; i < 10; i++)")
                .addStatement("total += i")
                .addStatement("int total = 0")
                .endControlFlow()
                .build();


        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
                .build();

//        try {
//            javaFile.writeTo(System.out);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println(javaFile.toString());

    }


}
