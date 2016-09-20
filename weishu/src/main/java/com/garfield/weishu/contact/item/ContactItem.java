package com.garfield.weishu.contact.item;


public class ContactItem extends AbsContactItem {

	private String account;
	private String displayName;

	// 不只有friend属于ContactItem
	private final int itemType;

	public ContactItem(String account, String displayName, int type) {
		this.account = account;
		this.displayName = displayName;
		this.itemType = type;
	}

	@Override
	public int getItemType() {
		return itemType;
	}

	@Override
	public String belongsGroup() {
		return null;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
