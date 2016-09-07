package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garfield.weishu.R;
import com.garfield.weishu.adapter.ContactAdapter;
import com.garfield.weishu.bean.ContactBean;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by gaowei3 on 2016/8/3.
 */

public class NewsTabFragment extends AppBaseFragment {

    private Random mRandom = new Random();
    private RecyclerView mRecyclerView;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_news_tab;
    }

    @Override
    protected void onInitView(View view) {
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(new ContactAdapter(getContactList(30)));
    }

    private ArrayList<ContactBean> getContactList(int sum) {
        ArrayList<ContactBean> contactList = new ArrayList<>();
        for (int i = 0; i < sum; i++) {
            ContactBean contact = new ContactBean();
            contact.setHeadImage(getRandomImageResource());
            contact.setName(getRandomName());
            contactList.add(contact);
        }
        return contactList;
    }

    private int getRandomImageResource() {
        switch (mRandom.nextInt(5)) {
            default:
            case 0:
                return R.drawable.cheese_1;
            case 1:
                return R.drawable.cheese_2;
            case 2:
                return R.drawable.cheese_3;
            case 3:
                return R.drawable.cheese_4;
            case 4:
                return R.drawable.cheese_5;
        }
    }
    private String getRandomName() {
        switch (mRandom.nextInt(5)) {
            default:
            case 0:
                return "apple1";
            case 1:
                return "apple2";
            case 2:
                return "apple3";
            case 3:
                return "apple4";
            case 4:
                return "apple5";
        }
    }
}
