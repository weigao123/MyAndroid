package com.garfield.weishu.contact.item;


import com.garfield.weishu.R;
import com.garfield.weishu.contact.ItemTypes;

public class FuncItem extends AbsContactItem {

    public static final int TYPE_NEW_FRIEND = 1;
    public static final int TYPE_ADVANCED_GROUP = 2;
    public static final int TYPE_DISCUSSION_GROUP = 3;
    public static final int TYPE_BLACK_LIST = 4;


    private int imageId;
    private int nameId;
    private final int funcType;

    public FuncItem(int funcType) {
        this.funcType = funcType;
        switch (funcType) {
            case TYPE_NEW_FRIEND:
                imageId = R.drawable.ic_add_friend;
                nameId = R.string.new_friend;
                break;
            case TYPE_ADVANCED_GROUP:
                imageId = R.drawable.ic_advanced_group;
                nameId = R.string.advanced_group;
                break;
            case TYPE_DISCUSSION_GROUP:
                imageId = R.drawable.ic_discussion_group;
                nameId = R.string.discussion_group;
                break;
            case TYPE_BLACK_LIST:
                imageId = R.drawable.ic_black_list;
                nameId = R.string.black_list;
                break;
        }
    }

    public int getImageId() {
        return imageId;
    }

    public int getNameId() {
        return nameId;
    }

    public int getFuncType() {
        return funcType;
    }

    @Override
	public int getItemType() {
		return ItemTypes.FUNC;
	}

    @Override
    public String belongsGroup() {
        return null;
    }

}
