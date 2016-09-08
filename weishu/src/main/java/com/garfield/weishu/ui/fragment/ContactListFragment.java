package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.garfield.baselib.ui.widget.LetterIndexView;
import com.garfield.weishu.R;
import com.garfield.weishu.contact.ContactDataAdapter;
import com.garfield.weishu.contact.ContactDataProvider;
import com.garfield.weishu.contact.ItemTypes;
import com.garfield.weishu.contact.viewholder.ContactHolder;
import com.garfield.weishu.contact.viewholder.FuncHolder;
import com.garfield.weishu.contact.viewholder.LabelHolder;

import butterknife.BindView;

/**
 * Created by gaowei3 on 2016/8/1.
 */
public class ContactListFragment extends AppBaseFragment {

    @BindView(R.id.contact_list_view)
    ListView mListView;

    private ContactDataAdapter adapter;


    @BindView(R.id.contact_letter_index)
    LetterIndexView mLetterIndexView;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_contact_list;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        ContactDataProvider dataProvider = new ContactDataProvider(ItemTypes.FUNC, ItemTypes.FRIEND);

        adapter = new ContactDataAdapter(mActivity, dataProvider);
        adapter.addViewHolder(ItemTypes.LABEL, LabelHolder.class);
        adapter.addViewHolder(ItemTypes.FUNC, FuncHolder.class);
        adapter.addViewHolder(ItemTypes.FRIEND, ContactHolder.class);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.load();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.load();
    }
}
