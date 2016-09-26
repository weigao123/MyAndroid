package com.garfield.weishu.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.garfield.weishu.R;
import com.garfield.weishu.bean.ContactBean;
import com.garfield.weishu.session.InputPanel;
import com.garfield.weishu.news.NewsAdapter;

import java.util.ArrayList;

import butterknife.BindView;

import static com.garfield.weishu.AppCache.USER_ACCOUNT;

/**
 * Created by gaowei3 on 2016/8/4.
 */
public class SessionFragment extends AppBaseFragment {



    @BindView(R.id.fragment_session_list)
    RecyclerView mRecyclerView;

    private String mAccount;
    private InputPanel mInputPanel;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_session;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        mAccount = getArguments().getString(USER_ACCOUNT);

        NewsAdapter contactAdapter = new NewsAdapter(new ArrayList<ContactBean>());

        mRecyclerView.setAdapter(contactAdapter);

        mInputPanel = new InputPanel(rootView, mAccount);
    }

    public static SessionFragment newInstance(String account) {
        Bundle args = new Bundle();
        args.putString(USER_ACCOUNT, account);
        SessionFragment fragment = new SessionFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
