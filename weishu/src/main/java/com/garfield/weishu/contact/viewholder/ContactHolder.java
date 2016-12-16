package com.garfield.weishu.contact.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.garfield.weishu.R;
import com.garfield.weishu.contact.ContactDataAdapter;
import com.garfield.weishu.contact.item.ContactItem;
import com.garfield.weishu.ui.view.HeadImageView;

public class ContactHolder extends AbsContactViewHolder<ContactItem> {

    private HeadImageView headImage;

    protected TextView name;

    @Override
    public void refresh(ContactDataAdapter adapter, int position, final ContactItem item) {
        headImage.loadBuddyAvatar(item.getAccount());
        name.setText(item.getDisplayName());
    }

    @Override
    public View inflate(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.item_contact, null);
        headImage = (HeadImageView) view.findViewById(R.id.item_contact_image);
        name = (TextView) view.findViewById(R.id.item_contact_name);
        view.setBackgroundResource(R.drawable.bg_press_white_to_gray);
        return view;
    }
}
