package com.garfield.baselib.utils.string;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by gwball on 2016/8/3.
 */
public class ListUtils {

    public static List<String> getRandomSublist(String[] array, int amount) {
        ArrayList<String> list = new ArrayList<>(amount);
        Random random = new Random();
        while (list.size() < amount) {
            list.add(array[random.nextInt(array.length)]);
        }
        return list;
    }
}
