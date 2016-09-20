package com.garfield.weishu.contact.item;


import android.text.TextUtils;

import com.garfield.weishu.contact.model.ContactGroupStrategy;
import com.garfield.weishu.contact.query.TextComparator;

public class ContactItem extends AbsContactItem implements Comparable<ContactItem> {

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

	public String getAccount() {
		return account;
	}

	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String belongsGroup() {
		if (account == null) {
			return ContactGroupStrategy.GROUP_NULL;
		}

		String group = TextComparator.getLeadingUp(displayName);
		return !TextUtils.isEmpty(group) ? group : ContactGroupStrategy.GROUP_SHARP;
	}

	@Override
	public int compareTo(ContactItem item) {
		int compare = compareType(item);
		if (compare != 0) {
			return compare;
		} else {
			return TextComparator.compareIgnoreCase(displayName, item.getDisplayName());
		}
	}
}
