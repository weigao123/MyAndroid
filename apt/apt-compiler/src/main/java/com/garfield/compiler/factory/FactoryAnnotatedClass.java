package com.garfield.compiler.factory;


import com.garfield.annotation.factory.Factory;
import com.garfield.compiler.utils.ProcessingException;
import com.garfield.compiler.utils.TextUtil;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

// 存放每个注解的实体类
public class FactoryAnnotatedClass {

    private TypeElement classElement;
    private String groupClassName;   //以type()为一组，是接口名
    private String id;

    public FactoryAnnotatedClass(TypeElement classElement) throws ProcessingException {
        this.classElement = classElement;
        Factory annotation = classElement.getAnnotation(Factory.class);
        id = annotation.id();

        if (TextUtil.isEmpty(id)) {
            throw new ProcessingException(classElement,
                    "id() in @%s for class %s is null or empty! that's not allowed",
                    Factory.class.getSimpleName(), classElement.getQualifiedName().toString());
        }

        try {
            Class<?> clazz = annotation.type();   // Meal.java还未编译成class文件时会报异常
            groupClassName = clazz.getCanonicalName();
            //Logger.info(groupClassName);
        } catch (MirroredTypeException mte) {
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            groupClassName = classTypeElement.getQualifiedName().toString();
            //Logger.warning(mte.toString());
            //Logger.error(mte);    //会中止程序
        }
    }

    public String getId() {
        return id;
    }

    public String getGroupClassName() {
        return groupClassName;
    }

    public TypeElement getTypeElement() {
        return classElement;
    }
}
