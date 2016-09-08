package com.garfield.weishu.contact.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.garfield.weishu.R;
import com.garfield.weishu.contact.ContactDataAdapter;
import com.garfield.weishu.contact.item.FuncItem;

public class FuncHolder extends AbsContactViewHolder<FuncItem> {

    protected ImageView head;

    protected TextView name;

    @Override
    public void refresh(ContactDataAdapter adapter, int position, final FuncItem item) {

    }

    @Override
    public View inflate(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.item_contact, null);

        head = (ImageView) view.findViewById(R.id.item_contact_image);
        name = (TextView) view.findViewById(R.id.item_contact_name);

        return view;
    }
}
