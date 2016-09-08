package com.garfield.weishu.contact.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.garfield.weishu.R;
import com.garfield.weishu.contact.ContactDataAdapter;
import com.garfield.weishu.contact.item.ContactItem;
import com.garfield.weishu.contact.item.LabelItem;

public class LabelHolder extends AbsContactViewHolder<LabelItem> {

    protected TextView name;

    @Override
    public void refresh(ContactDataAdapter adapter, int position, final LabelItem item) {

    }

    @Override
    public View inflate(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.item_contact, null);



        return view;
    }
}
