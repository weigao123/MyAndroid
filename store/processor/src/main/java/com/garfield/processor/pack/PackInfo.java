package com.garfield.processor.pack;

import com.garfield.annotation.pack.PackType;
import com.garfield.processor.utils.Logger;

import java.util.List;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by gaowei on 2018/8/14
 */
public class PackInfo {

    private TypeElement classElement;
    private String className;  //debug
    private String groupName;
    private String interfaceName;
    private boolean isClass;

    public PackInfo(TypeElement classElement) {
        this.classElement = classElement;
        PackType annotation = classElement.getAnnotation(PackType.class);

        if (classElement.getKind() == ElementKind.CLASS) {
            List<? extends TypeMirror> interfaceMirrors = classElement.getInterfaces();
            if (!interfaceMirrors.isEmpty()) {
                interfaceName = interfaceMirrors.get(0).toString();
                isClass = true;
            } else {
                Logger.error("注解不合规则");
            }
            className = classElement.getQualifiedName().toString();
        } else if (classElement.getKind() == ElementKind.INTERFACE) {
            className = interfaceName = classElement.getQualifiedName().toString();
            groupName = annotation.type();
        } else {
            Logger.error("注解不合规则");
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String group) {
        groupName = group;
    }

    public TypeElement getTypeElement() {
        return classElement;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public boolean isClass() {
        return isClass;
    }

    public String getClassName() {
        return className;
    }
}