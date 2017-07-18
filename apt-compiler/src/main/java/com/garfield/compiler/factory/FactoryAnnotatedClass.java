package com.garfield.compiler.factory;


import com.garfield.annotation.factory.Factory;
import com.garfield.compiler.utils.Logger;
import com.garfield.compiler.utils.ProcessingException;

import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

/**
 * Created by gaowei on 2017/7/10.
 */

// 存放每个注解的实体类
public class FactoryAnnotatedClass {

    private TypeElement annotatedClassElement;
    private String qualifiedGroupClassName;
    private String simpleFactoryGroupName;
    private String id;

    public FactoryAnnotatedClass(TypeElement classElement) throws ProcessingException {
        this.annotatedClassElement = classElement;
        Factory annotation = classElement.getAnnotation(Factory.class);
        id = annotation.id();

        if (StringUtils.isEmpty(id)) {
            throw new ProcessingException(classElement,
                    "id() in @%s for class %s is null or empty! that's not allowed",
                    Factory.class.getSimpleName(), classElement.getQualifiedName().toString());
        }

        // 未编译成class文件会报异常
        try {
            Class<?> clazz = annotation.type();
            qualifiedGroupClassName = clazz.getCanonicalName();
            simpleFactoryGroupName = clazz.getSimpleName();
            Logger.warning(clazz.getName());
        } catch (MirroredTypeException mte) {
            //Logger.error(mte);
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            qualifiedGroupClassName = classTypeElement.getQualifiedName().toString();
            simpleFactoryGroupName = classTypeElement.getSimpleName().toString();
            Logger.warning(mte.toString());
        }
    }

    /**
     * 获取在{@link Factory#id()}中指定的id
     */
    public String getId() {
        return id;
    }

    /**
     * 获取在{@link Factory#type()}指定的类型合法全名
     */
    public String getQualifiedFactoryGroupName() {
        return qualifiedGroupClassName;
    }

    /**
     * 获取在 {@link Factory#type()} 中指定的类型的简单名字
     */
    public String getSimpleFactoryGroupName() {
        return simpleFactoryGroupName;
    }

    /**
     * 获取被@Factory注解的原始元素
     */
    public TypeElement getTypeElement() {
        return annotatedClassElement;
    }
}
