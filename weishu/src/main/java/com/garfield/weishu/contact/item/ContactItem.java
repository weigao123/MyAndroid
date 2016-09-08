package com.garfield.weishu.contact.item;


public class ContactItem extends AbsContactItem {

	private int contactId;
	private String displayName;
	private final int itemType;

	public ContactItem(int contactId, String displayName, int type) {
		this.contactId = contactId;
		this.displayName = displayName;
		this.itemType = type;
	}

	@Override
	public int getItemType() {
		return itemType;
	}

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
