package com.garfield.weishu.contact.item;

import com.garfield.weishu.contact.ItemTypes;

public class LabelItem extends AbsContactItem {
    private final String text;

    public LabelItem(String text) {
        this.text = text;
    }

    @Override
    public int getItemType() {
        return ItemTypes.LABEL;
    }

    @Override
    public String belongsGroup() {
        return null;
    }

    public final String getText() {
        return text;
    }
}
