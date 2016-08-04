package com.garfield.newsreader.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.garfield.baselib.base.BaseActivity;
import com.garfield.baselib.base.BaseFragment;
import com.garfield.baselib.fragmentation.SupportActivity;
import com.garfield.newsreader.R;
import com.garfield.newsreader.ui.fragment.ChatFragment;
import com.garfield.newsreader.ui.fragment.NewsFragment;
import com.garfield.newsreader.ui.fragment.PersonalFragment;
import com.garfield.newsreader.ui.fragment.TestFragment;
import com.garfield.newsreader.ui.fragment.VideoFragment;

public class MainActivity extends SupportActivity implements View.OnClickListener {

    private Class[] mFragmentClasses = new Class[]{
            NewsFragment.class,
            VideoFragment.class,
            ChatFragment.class,
            PersonalFragment.class};

    private View[] mTabs = new View[mFragmentClasses.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    protected void initView() {
        mTabs[0] = findViewById(R.id.newsreader_main_news);
        mTabs[1] = findViewById(R.id.newsreader_main_video);
        mTabs[2] = findViewById(R.id.newsreader_main_chat);
        mTabs[3] = findViewById(R.id.newsreader_main_personal);
        //switchFragment(mFragmentClasses[0]);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newsreader_main_news:
                startFragment(TestFragment.class);
                break;
            case R.id.newsreader_main_video:
                switchFragment(mFragmentClasses[1]);
                break;
            case R.id.newsreader_main_chat:
                switchFragment(mFragmentClasses[2]);
                break;
            case R.id.newsreader_main_personal:
                switchFragment(mFragmentClasses[3]);
                break;
        }
    }

    private void switchFragment(Class targetClass) {
        BaseFragment fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mFragmentClasses.length; i++) {
            Class thisClass = mFragmentClasses[i];
            fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(thisClass.getSimpleName());
            if (thisClass == targetClass) {
                if (fragment == null) {
                    fragment = (BaseFragment) Fragment.instantiate(this, thisClass.getName(), null);
                    transaction.add(R.id.newsreader_main_fragment_container, fragment, thisClass.getSimpleName());
                }
                transaction.show(fragment);
                mTabs[i].setSelected(true);
            } else {
                if (fragment != null) {
                    transaction.hide(fragment);
                }
                mTabs[i].setSelected(false);
            }
        }
        transaction.commit();
    }
}