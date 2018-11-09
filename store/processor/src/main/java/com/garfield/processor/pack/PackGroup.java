package com.garfield.processor.pack;

import com.garfield.processor.utils.Logger;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

/**
 * Created by gaowei on 2018/8/14
 */
public class PackGroup {

    // type
    private String groupName;

    private List<PackInfo> mPackInfos = new ArrayList<>();
    private List<String> mClass = new ArrayList<>();
    private List<String> mInterface = new ArrayList<>();

    public PackGroup(String groupName) {
        this.groupName = groupName;
    }

    public void add(PackInfo info) {
        if (info.isClass()) {
            mClass.add(info.getInterfaceName());
            Logger.info("class: "+info.getClassName() + " interface: " + info.getInterfaceName());
        } else {
            mInterface.add(info.getInterfaceName());
            Logger.info("interface: "+info.getInterfaceName());
        }
        mPackInfos.add(info);
    }

    public void generateCode(Elements elementUtils, Filer filer) throws IOException {
        //mInterface.removeIf(s -> mClass.contains(s));

        Iterator<String> iterable = mInterface.iterator();
        while (iterable.hasNext()) {
            String str = iterable.next();
            if (mClass.contains(str)) {
                iterable.remove();
            }
        }

        List<TypeName> typeNames = new ArrayList<>();
        for (String s : mInterface) {
            typeNames.add(TypeName.get(elementUtils.getTypeElement(s).asType()));
        }

//        int index = 0;
//        while (true) {
//            try {
//                Class.forName("com.didi.sdk.NimbleDataGenerator$" + index);
//            } catch (ClassNotFoundException e) {
//                break;
//            }
//        }

        TypeSpec NimDataGenerator = TypeSpec.interfaceBuilder("NimDataGenerator")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterfaces(typeNames)
                .build();

        JavaFile javaFile = JavaFile.builder("com.didi.sdk", NimDataGenerator).build();
//        javaFile.writeTo(filer);
        javaFile.writeTo(System.out);
    }
}
