package com.garfield.weishu.contact.item;


import com.garfield.weishu.contact.ItemTypes;

public class FuncItem extends AbsContactItem {

	private int resource;
    private String content;

	public FuncItem(int resource, String content) {
        this.resource = resource;
        this.content = content;
	}

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
	public int getItemType() {
		return ItemTypes.FUNC;
	}

}
