package com.garfield.processor.factory;

import com.garfield.annotation.factory.Factory;
import com.garfield.processor.utils.Logger;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
public class FactoryProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;

    private Map<String, FactoryGroupedClasses> factoryClasses = new LinkedHashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        Logger.setMessager(processingEnv.getMessager());

        // 在这里打印gradle文件传进来的参数
        Map<String, String> map = processingEnv.getOptions();
        for (String key : map.keySet()) {
            Logger.info("key：" + map.get(key));
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Factory.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Logger.info("process start");
        try {
            // 遍历所有被注解了@Factory的元素，第二遍时如果没有新生成的带注解的，就是空，跳出for
            for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Factory.class)) {

                // 检查注解的内容是否是类，接口也不行
                // 配合TypeMirror使用ElementKind或者TypeKind
                // if (!(annotatedElement instanceof TypeElement))错误，因为接口（interface）也是TypeElement
                if (annotatedElement.getKind() != ElementKind.CLASS) {
                    Logger.error("Only classes can be annotated with " + Factory.class.getSimpleName());
                    return true;
                }

                TypeElement typeElement = (TypeElement) annotatedElement;
                FactoryAnnotatedClass annotatedClass = new FactoryAnnotatedClass(typeElement);

                checkValidClass(annotatedClass);

                FactoryGroupedClasses factoryClass = factoryClasses.get(annotatedClass.getGroupClassName());
                if (factoryClass == null) {
                    String groupClassName = annotatedClass.getGroupClassName();
                    factoryClass = new FactoryGroupedClasses(groupClassName);
                    factoryClasses.put(groupClassName, factoryClass);
                }
                factoryClass.add(annotatedClass);
            }

            for (FactoryGroupedClasses factoryClass : factoryClasses.values()) {
                factoryClass.generateCode(elementUtils, filer);
            }

            // 必须要清除，因为会第二次调进来process，不需要再次生成代码
            factoryClasses.clear();

        } catch (ProcessingException | IOException e) {
            Logger.error(e);
        }

        Logger.info("process end");
        return true;
    }

    private void checkValidClass(FactoryAnnotatedClass annotatedClass) throws ProcessingException {


        TypeElement classElement = annotatedClass.getTypeElement();
        Logger.info("checkValidClass: " + classElement.getSimpleName());

        if (!classElement.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ProcessingException(classElement, "The class %s is not public.",
                    classElement.getQualifiedName().toString());
        }

        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new ProcessingException(classElement,
                    "The class %s is abstract. You can't annotate abstract classes with @%",
                    classElement.getQualifiedName().toString(), Factory.class.getSimpleName());
        }

        // 检查继承关系: @Factory修饰的类必须是@Factory.type()指定的类型子类
        TypeElement superClassElement = elementUtils.getTypeElement(annotatedClass.getGroupClassName());
        if (superClassElement.getKind() == ElementKind.INTERFACE) {
            // type是个接口，检查是否实现了这个接口
            if (!classElement.getInterfaces().contains(superClassElement.asType())) {
                throw new ProcessingException(classElement,
                        "The class %s annotated with @%s must implement the interface %s",
                        classElement.getQualifiedName().toString(), Factory.class.getSimpleName(),
                        annotatedClass.getGroupClassName());
            }
        } else {
            TypeElement currentClass = classElement;
            // type是类，遍历检查是否继承了该类
            while (true) {
                TypeMirror superClassType = currentClass.getSuperclass();

                if (superClassType.getKind() == TypeKind.NONE) {
                    // Basis class (java.lang.Object) reached, so exit
                    throw new ProcessingException(classElement,
                            "The class %s annotated with @%s must inherit from %s",
                            classElement.getQualifiedName().toString(), Factory.class.getSimpleName(),
                            annotatedClass.getGroupClassName());
                }

                if (superClassType.toString().equals(annotatedClass.getGroupClassName())) {
                    // 成功
                    break;
                }

                currentClass = (TypeElement) typeUtils.asElement(superClassType);
                //或
                //currentClass = (TypeElement) ((DeclaredType) currentClass.getSuperclass()).asElement();
            }
        }

        // 检查是否提供了默认公开构造函数
        for (Element enclosed : classElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructorElement = (ExecutableElement) enclosed;
                if (constructorElement.getParameters().size() == 0 && constructorElement.getModifiers()
                        .contains(Modifier.PUBLIC)) {
                    // Found an empty constructor
                    return;
                }
            }
        }

        // No empty constructor found
        throw new ProcessingException(classElement,
                "The class %s must provide an public empty default constructor",
                classElement.getQualifiedName().toString());
    }
}
