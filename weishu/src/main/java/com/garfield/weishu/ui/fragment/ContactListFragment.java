package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.garfield.baselib.widget.LetterIndexView;
import com.garfield.weishu.R;
import com.garfield.weishu.base.AppBaseFragment;

/**
 * Created by gaowei3 on 2016/8/1.
 */
public class ContactListFragment extends AppBaseFragment {

    private ListView mListView;
    private LetterIndexView mLetterIndexView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.contact_list_view);
        mLetterIndexView = (LetterIndexView) view.findViewById(R.id.contact_letter_index);
    }



}
