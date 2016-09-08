package com.garfield.weishu.contact.item;


import com.garfield.weishu.contact.ItemTypes;

public class FuncItem extends AbsContactItem {

	private int imageId;
    private String name;

	public FuncItem(int imageId, String name) {
        this.imageId = imageId;
        this.name = name;
	}

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    @Override
	public int getItemType() {
		return ItemTypes.FUNC;
	}

}
