package com.garfield.study.backup;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class SimplePopupBaseWithData {

    private static final String TAG = "SimplePopupBaseWithData";

    protected LinkedHashMap<IPopupData, Object> mData;

    private int[] mCurAllIndex;

    protected int mColumnCount;

    public void setData(@NonNull LinkedHashMap<IPopupData, Object> data) {
        if (!checkAndGetDepth(data)) {
            throw new IllegalArgumentException();
        }
        mData = data;
        mCurAllIndex = new int[mColumnCount];
    }

    /**
     * 检查key类型是ISimplePopupData，value是LinkedHashMap<IPopupData, ...>
     */
    private boolean checkAndGetDepth(Object data) {
        return checkAndGetDepth(data, 0);
    }

    private boolean checkAndGetDepth(Object data, int depth) {
        if (data == null) {
            //Log.d(TAG, "depth: "+depth);
            if (mColumnCount == 0) {
                mColumnCount = depth;
            } else {
                return mColumnCount == depth;
            }
            return true;
        }
        if (!(data instanceof LinkedHashMap)) {
            return false;
        }

        Set entry = ((LinkedHashMap)data).entrySet();
        Iterator iterator = entry.iterator();
        boolean result = true;   //默认当前竖列的第一个元素合法
        ++ depth;
        while (iterator.hasNext()) {
            Object cur = iterator.next();
            if (cur instanceof Map.Entry) {   //检查当前帧
                Object key = ((Map.Entry)cur).getKey();
                Object value = ((Map.Entry)cur).getValue();
                if (key != null && key instanceof IPopupData) {   //检查当前帧
                    //Log.d(TAG, ((IPopupData) key).getSimpleData());
                    result = checkAndGetDepth(value, depth);   //检查下一帧
                } else {
                    result = false;
                }
            } else {
                result = false;
            }
            if (!result) {
                break;
            }
        }
        return result;
    }

    /**
     * 获取当前选择下所有列的所有数据
     * @return
     */
    protected List<Entry> getCurrentData() {
        return getIndexData(mCurAllIndex);
    }

    /**
     * 获取某一确定位置的所有列数据，不够的部分自动补0
     * @param allIndex
     * @return
     */
    @SuppressWarnings("unchecked")
    protected List<Entry> getIndexData(int... allIndex) {
        if (allIndex.length > mColumnCount)
            throw new IllegalArgumentException();

        List<Entry> curData = new ArrayList<>();
        LinkedHashMap<IPopupData, Object> tmp = mData;
        for (int i = 0; i < mColumnCount; i++) {
            int index = 0;
            if (i < allIndex.length) {
                index = allIndex[i];
            }
            Entry entry = new Entry();
            entry.columnData = new ArrayList<>(tmp.keySet());
            entry.index = index;
            curData.add(entry);
            tmp = (LinkedHashMap<IPopupData, Object>) tmp.get(entry.columnData.get(index));
        }
        return curData;
    }

    /**
     * 修改某一个列以后调用，得到新的所有列数据
     * @param column
     * @param index
     * @return
     */
    protected List<Entry> modifyColumn(int column, int index) {
        if (column > mColumnCount)
            throw new IllegalArgumentException();

        List<Entry> lastData = getCurrentData();
        mCurAllIndex[column] = index;
        List<Entry> curData = getCurrentData();
        boolean resetRest = false;
        for (int i = column + 1; i < mColumnCount; i++) {
            if (!resetRest) {
                Entry lastEntry = lastData.get(i);
                Entry nowEntry = curData.get(i);
                int t_index = nowEntry.columnData.indexOf(lastEntry.columnData.get(mCurAllIndex[i]));
                if (t_index >= 0) {
                    mCurAllIndex[i] = t_index;
                } else {
                    resetRest = true;
                    mCurAllIndex[i] = 0;
                }
            } else {
                mCurAllIndex[i] = 0;
            }
        }
        return getIndexData(mCurAllIndex);
    }

    /**
     * 返回当前选择下的数据
     * @return
     */
    protected List<IPopupData> getSelectedData() {
        List<Entry> data = getCurrentData();
        List<IPopupData> result = new ArrayList<>();
        for (int i = 0; i < mCurAllIndex.length; i++) {
            result.add(data.get(i).columnData.get(mCurAllIndex[i]));
        }
        return result;
    }



    /**
     * 某一列的数据和当前该列被选择的索引行
     */
    protected static class Entry {
        public List<IPopupData> columnData;
        public int index;
        public List<String> getStringData() {
            List<String> result = new ArrayList<>();
            for (IPopupData data : columnData) {
                result.add(data.getSimpleData());
            }
            return result;
        }
    }


    public interface IPopupData {
        String getSimpleData();
    }







    public static LinkedHashMap<IPopupData, Object> getPopupData2(boolean isRepeat) {
        LinkedHashMap<IPopupData, Object> day = new LinkedHashMap<>();
        day.put(new PopupStringData("今天"), generateHour("今天_", isRepeat));
        day.put(new PopupStringData("明天"), generateHour("明天_", isRepeat));
        day.put(new PopupStringData("后天"), generateHour("后天_", isRepeat));
        return day;
    }

    private static LinkedHashMap<IPopupData, Object> generateHour(String str, boolean isRepeat) {
        LinkedHashMap<IPopupData, Object> hour = new LinkedHashMap<>();
        if (isRepeat) {
            str = "";
        }
        hour.put(new PopupStringData(str+"1点"), generateMinute(str+"1点_", isRepeat));
        hour.put(new PopupStringData(str+"2点"), generateMinute(str+"2点_", isRepeat));
        hour.put(new PopupStringData(str+"3点"), generateMinute(str+"3点_", isRepeat));
        hour.put(new PopupStringData(str+"4点"), generateMinute(str+"4点_", isRepeat));
        hour.put(new PopupStringData(str+"5点"), generateMinute(str+"5点_", isRepeat));
        hour.put(new PopupStringData(str+"6点"), generateMinute(str+"6点_", isRepeat));
        return hour;
    }

    private static LinkedHashMap<IPopupData, Object> generateMinute(String str, boolean isRepeat) {
        if (isRepeat) {
            str = "";
        }
        LinkedHashMap<IPopupData, Object> minute = new LinkedHashMap<>();
        minute.put(new PopupStringData(str+"1分"), null);
        minute.put(new PopupStringData(str+"2分"), null);
        minute.put(new PopupStringData(str+"3分"), null);
        minute.put(new PopupStringData(str+"4分"), null);
        minute.put(new PopupStringData(str+"5分"), null);
        minute.put(new PopupStringData(str+"6分"), null);
        return minute;
    }


    public static class PopupStringData implements IPopupData {

        private String mData;

        public PopupStringData(String data) {
            mData = data;
        }

        @Override
        public String getSimpleData() {
            return mData;
        }

        @Override
        public String toString() {
            return mData;
        }
    }
}
