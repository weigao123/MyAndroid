package com.garfield.weishu.ui.fragment;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.garfield.weishu.R;
import com.garfield.weishu.bean.ContactBean;
import com.garfield.weishu.ui.adapter.ContactAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.garfield.weishu.AppCache.USER_ACCOUNT;

/**
 * Created by gaowei3 on 2016/8/4.
 */
public class SessionFragment extends AppBaseFragment {

    @BindView(R.id.message_input_text)
    EditText mInputText;

    @BindView(R.id.message_input_send)
    TextView mSendBtn;

    @BindView(R.id.fragment_session_list)
    RecyclerView mRecyclerView;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_session;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);

        ContactAdapter contactAdapter = new ContactAdapter(new ArrayList<ContactBean>());

        mRecyclerView.setAdapter(contactAdapter);
    }

    public static SessionFragment newInstance(String account) {
        Bundle args = new Bundle();
        args.putString(USER_ACCOUNT, account);
        SessionFragment fragment = new SessionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.message_input_send)
    void sendMessage() {

    }
}
