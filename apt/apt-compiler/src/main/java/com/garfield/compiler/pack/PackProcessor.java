package com.garfield.compiler.pack;

import com.garfield.annotation.pack.PackType;
import com.garfield.compiler.utils.Logger;
import com.garfield.compiler.utils.TextUtil;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
public class PackProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;

    private Map<String, PackGroup> packClasses = new LinkedHashMap<>();
    private Map<String, String> allInterface = new LinkedHashMap<>();   //key=interface,value=groupName
    private List<PackInfo> allPack = new ArrayList<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        Logger.setMessager(processingEnv.getMessager());
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(PackType.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(PackType.class)) {
                if (!(annotatedElement instanceof TypeElement)) {
                    Logger.error("Only classes can be annotated with " + PackType.class.getSimpleName());
                    return true;
                }
                TypeElement typeElement = (TypeElement) annotatedElement;
                PackInfo annotatedClass = new PackInfo(typeElement);
                allPack.add(annotatedClass);

                if (annotatedElement.getKind() == ElementKind.INTERFACE) {
                    allInterface.put(annotatedClass.getInterfaceName(), annotatedClass.getGroupName());
                }
            }

            if (!roundEnv.getElementsAnnotatedWith(PackType.class).isEmpty()) {
                for (PackInfo info : allPack) {
                    if (TextUtil.isEmpty(info.getGroupName())) {
                        info.setGroupName(allInterface.get(info.getInterfaceName()));
                    }

                    PackGroup factoryClass = packClasses.get(info.getGroupName());
                    if (factoryClass == null) {
                        String groupClassName = info.getGroupName();
                        factoryClass = new PackGroup(groupClassName);
                        packClasses.put(groupClassName, factoryClass);
                    }
                    factoryClass.add(info);
                }

                for (PackGroup factoryClass : packClasses.values()) {
                    factoryClass.generateCode(elementUtils, filer);
                }
            }

            // 必须要清除，因为会第二次调进来process，不需要再次生成代码
            packClasses.clear();

        } catch (IOException e) {
            Logger.error(e);
        }
        return true;
    }
}
