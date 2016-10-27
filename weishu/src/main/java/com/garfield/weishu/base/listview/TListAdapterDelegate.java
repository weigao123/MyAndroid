package com.garfield.weishu.base.listview;

public interface TListAdapterDelegate {

	int getViewTypeCount();

	// 根据position确定item，再根据item确定ViewHolderType
	Class<? extends TListViewHolder> getViewHolderClassAtPosition(int position);

	boolean enabled(int position);
}