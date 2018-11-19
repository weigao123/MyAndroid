package com.garfield.plugin.util

import com.android.builder.model.Version
import org.gradle.api.Project

class XmlUtil {

    static void addMetaData(Project project, String key, String value) {

        project.android.applicationVariants.all { variant ->
            variant.outputs.each { output ->
                output.processManifest.doLast {

                    File manifestFile
                    if (Version.ANDROID_GRADLE_PLUGIN_VERSION >= '3.0.0') {
                        manifestFile = project.file("$manifestOutputDirectory/AndroidManifest.xml")
                    } else {
                        manifestFile = manifestOutputFile
                    }

                    def root = new XmlSlurper().parseText(manifestFile.getText())
                    def meta = new XmlSlurper().parseText(String.format(
                            "<meta-data xmlns:android=\"http://schemas.android.com/apk/res/android\" " +
                                    "android:name=\"%s\" " +
                                    "android:value=\"%s\"/>", key, value))

                    root.'application'.appendNode(meta)
                    manifestFile.write(groovy.xml.XmlUtil.serialize(root))
                }
            }
        }
    }

    

}