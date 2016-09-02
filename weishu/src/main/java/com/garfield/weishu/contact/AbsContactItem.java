package com.garfield.weishu.contact;

/**
 * 通讯录数据项抽象类
 */
public abstract class AbsContactItem {
    /**
     * 所属的类型
     *
     * @see com.garfield.weishu.contact.ItemTypes
     */
    public abstract int getItemType();

    /**
     * 所属的分组
     */
    public abstract String belongsGroup();

    protected final int compareType(AbsContactItem item) {
        return compareType(getItemType(), item.getItemType());
    }

    public static int compareType(int lhs, int rhs) {
        return lhs - rhs;
    }
}
