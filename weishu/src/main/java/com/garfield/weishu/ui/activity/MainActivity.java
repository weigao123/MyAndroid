package com.garfield.weishu.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.garfield.baselib.fragmentation.SupportFragment;
import com.garfield.weishu.R;
import com.garfield.weishu.base.AppBaseActivity;
import com.garfield.weishu.ui.fragment.MainFragment;

/**
 * Created by gaowei3 on 2016/7/31.
 */
public class MainActivity extends AppBaseActivity {

    private boolean isBackPressedToBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 旋转时会非空
        if (savedInstanceState == null) {
            loadRootFragment(R.id.main_activity_fragment_container, (SupportFragment) Fragment.instantiate(this, MainFragment.class.getName()));
        }
        //   startActivity(new Intent(this, SwipeBackTestActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (isBackPressedToBack) {
            moveTaskToBack(false);
        } else {
            super.onBackPressed();
        }
    }
}
