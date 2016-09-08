package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.garfield.baselib.ui.widget.LetterIndexView;
import com.garfield.weishu.R;
import com.garfield.weishu.contact.ContactDataAdapter;
import com.garfield.weishu.contact.ContactDataProvider;
import com.garfield.weishu.contact.ItemTypes;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/8/1.
 */
public class ContactListFragment extends AppBaseFragment {

    @BindView(R.id.contact_list_view)
    ListView mListView;

    private ContactDataAdapter adapter;


    @BindView(R.id.contact_letter_index)
    private LetterIndexView mLetterIndexView;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_contact_list;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        ContactDataProvider dataProvider = new ContactDataProvider(ItemTypes.FRIEND);

        adapter = new ContactDataAdapter(mActivity, dataProvider);

        mListView.setAdapter(adapter);

    }



}
