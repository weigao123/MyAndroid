package com.garfield.weishu.contact.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.garfield.weishu.contact.item.AbsContactItem;
import com.garfield.weishu.contact.ContactDataAdapter;


public abstract class AbsContactViewHolder<T extends AbsContactItem> {
    protected View mRootView;

    protected Context context;

    public AbsContactViewHolder() {

    }

    public abstract void refresh(ContactDataAdapter adapter, int position, T item);

    public abstract View inflate(LayoutInflater inflater);

    public final View getView() {
        return mRootView;
    }

    public void createView(Context context) {
        this.context = context;
        this.mRootView = inflate(LayoutInflater.from(context));
    }
}