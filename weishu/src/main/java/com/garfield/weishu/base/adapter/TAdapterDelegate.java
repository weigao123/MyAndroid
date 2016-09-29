package com.garfield.weishu.base.adapter;

public interface TAdapterDelegate {

	int getViewTypeCount();

	// 根据position确定item，再根据item确定ViewHolderType
	Class<? extends TViewHolder> getViewHolderClassAtPosition(int position);

	boolean enabled(int position);
}