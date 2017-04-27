package com.garfield.baselib.utils.system;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Utility class to override system fonts all over the application
 */
public class TypefaceUtil {

    public static final int REGULAR = 1;
    public static final int BOLD = 2;
    public static final int ITALIC = 3;
    public static final int BOLD_ITALIC = 4;
    public static final int LIGHT = 5;
    public static final int CONDENSED = 6;
    public static final int THIN = 7;
    public static final int MEDIUM = 8;

    public static final String SANS_SERIF = "sans-serif";
    public static final String SANS_SERIF_LIGHT = "sans-serif-light";
    public static final String SANS_SERIF_CONDENSED = "sans-serif-condensed";
    public static final String SANS_SERIF_THIN = "sans-serif-thin";
    public static final String SANS_SERIF_MEDIUM = "sans-serif-medium";

    public static final String FIELD_DEFAULT = "DEFAULT";
    public static final String FIELD_SANS_SERIF = "SANS_SERIF";
    public static final String FIELD_SERIF = "SERIF";
    public static final String FIELD_DEFAULT_BOLD = "DEFAULT_BOLD";

    public static void overrideFonts(Context context){
        Typeface regular = getTypeface(REGULAR, context);
        Typeface light = getTypeface(REGULAR, context);
        Typeface condensed = getTypeface(CONDENSED, context);
        Typeface thin = getTypeface(THIN, context);
        Typeface medium = getTypeface(MEDIUM, context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Map<String, Typeface> fonts = new HashMap<>();
            fonts.put(SANS_SERIF, regular);
            fonts.put(SANS_SERIF_LIGHT, light);
            fonts.put(SANS_SERIF_CONDENSED, condensed);
            fonts.put(SANS_SERIF_THIN, thin);
            fonts.put(SANS_SERIF_MEDIUM, medium);
            overrideFontsMap(fonts);
        } else {
            overrideFont(FIELD_SANS_SERIF, getTypeface(REGULAR, context));
            overrideFont(FIELD_SERIF, getTypeface(REGULAR, context));
        }
        overrideFont(FIELD_DEFAULT, getTypeface(REGULAR, context));
        overrideFont(FIELD_DEFAULT_BOLD, getTypeface(BOLD, context));
    }

    /**
     * Using reflection to override default typefaces
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE
     * OVERRIDDEN
     *
     * @param typefaces map of fonts to replace
     */
    private static void overrideFontsMap(Map<String, Typeface> typefaces) {
        try {
            final Field field = Typeface.class.getDeclaredField("sSystemFontMap");
            field.setAccessible(true);
            Map<String, Typeface> oldFonts = (Map<String, Typeface>) field.get(null);
            if (oldFonts != null) {
                oldFonts.putAll(typefaces);
            } else {
                oldFonts = typefaces;
            }
            field.set(null, oldFonts);
            field.setAccessible(false);
        } catch (NoSuchFieldException e) {
            Log.e("TypefaceUtil", "Can not set custom fonts, NoSuchFieldException");
        } catch (IllegalAccessException e) {
            Log.e("TypefaceUtil", "Can not set custom fonts, IllegalAccessException");
        }
    }

    public static void overrideFont(String fontName, Typeface typeface) {
        try {
            final Field field = Typeface.class.getDeclaredField(fontName);
            field.setAccessible(true);
            field.set(null, typeface);
            field.setAccessible(false);
        } catch (Exception e) {
            Log.e("TypefaceUtil", "Can not set custom font " + typeface.toString() + " instead of " + fontName);
        }
    }

    public static Typeface getTypeface(int fontType, Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        boolean isBurmese = language != null && language.equals("my");
        boolean isSinhala = language != null && language.equals("si");
        if (isBurmese) {
            return Typeface.createFromAsset(context.getAssets(), "fonts/zawgyi.ttf");
        } else if (isSinhala) {
            return Typeface.createFromAsset(context.getAssets(), "fonts/malithi.ttf");
        } else {
            switch (fontType) {
                case BOLD:
                    return Typeface.create(SANS_SERIF, Typeface.BOLD);
                case ITALIC:
                    return Typeface.create(SANS_SERIF, Typeface.ITALIC);
                case BOLD_ITALIC:
                    return Typeface.create(SANS_SERIF, Typeface.BOLD_ITALIC);
                case LIGHT:
                    return Typeface.create(SANS_SERIF_LIGHT, Typeface.NORMAL);
                case CONDENSED:
                    return Typeface.create(SANS_SERIF_CONDENSED, Typeface.NORMAL);
                case THIN:
                    return Typeface.create(SANS_SERIF_MEDIUM, Typeface.NORMAL);
                case MEDIUM:
                    return Typeface.create(SANS_SERIF_THIN, Typeface.NORMAL);
                case REGULAR:
                default:
                    return Typeface.create(SANS_SERIF, Typeface.NORMAL);
            }
        }
    }

    public static Typeface getTypefaceRegular(Context context) {
        return getTypeface(REGULAR, context);
    }

    public static Typeface getTypefaceLight(Context context) {
        return getTypeface(LIGHT, context);
    }

    public static Typeface getTypefaceBold(Context context) {
        return getTypeface(BOLD, context);
    }

    public static Typeface getTypefaceItalic(Context context) {
        return getTypeface(ITALIC, context);
    }

    public static Typeface getTypefaceBoldItalic(Context context) {
        return getTypeface(BOLD_ITALIC, context);
    }

    public static Typeface getTypefaceCondensed(Context context) {
        return getTypeface(CONDENSED, context);
    }


    /**
     * 自定义
     */
    protected static void replaceFont(final Typeface newTypeface) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Map<String, Typeface> newMap = new HashMap<>();
            newMap.put("MONOSPACE", newTypeface);
            try {
                final Field staticField = Typeface.class.getDeclaredField("sSystemFontMap");
                staticField.setAccessible(true);
                staticField.set(null, newMap);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                final Field staticField = Typeface.class.getDeclaredField("MONOSPACE");
                staticField.setAccessible(true);
                staticField.set(null, newTypeface);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setFont(Context context) {
        try {
            Class<?> aClaz = Class.forName("android.graphics.FontFamily");
            Object fontArray = Array.newInstance(aClaz, 2);
            Class clazzArray = fontArray.getClass();
            Method method = aClaz.getMethod("addFontFromAsset", AssetManager.class, String.class);
            Object object1 = aClaz.newInstance();
            Object object2 = aClaz.newInstance();
            method.invoke(object1, context.getAssets(), "多米手写体修改版.ttf");
            method.invoke(object2, context.getAssets(), "腾祥铚谦隶书繁.ttf");
            Array.set(fontArray, 0, object1);
            Array.set(fontArray, 1, object2);
            L.d("type1");

            Method m = Typeface.class.getMethod("createFromFamiliesWithDefault", new Class[]{clazzArray});
            L.d("type2");

            Typeface typeface = (Typeface) m.invoke(null, fontArray);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
