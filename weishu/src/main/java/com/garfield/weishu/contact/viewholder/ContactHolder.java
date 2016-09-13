package com.garfield.weishu.contact.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.garfield.weishu.R;
import com.garfield.weishu.contact.ContactDataAdapter;
import com.garfield.weishu.contact.item.ContactItem;

public class ContactHolder extends AbsContactViewHolder<ContactItem> {

    protected ImageView head;

    protected TextView name;

    @Override
    public void refresh(ContactDataAdapter adapter, int position, final ContactItem item) {
        head.setImageResource(R.drawable.default_avatar);
        name.setText(item.getDisplayName());
    }

    @Override
    public View inflate(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.item_contact, null);

        head = (ImageView) view.findViewById(R.id.item_contact_image);
        name = (TextView) view.findViewById(R.id.item_contact_name);

        return view;
    }
}
