package com.garfield.weishu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.garfield.baselib.fragmentation.SupportFragment;
import com.garfield.weishu.R;
import com.garfield.weishu.ui.fragment.MainFragment;

/**
 * Created by gaowei3 on 2016/7/31.
 */
public class MainActivity extends AppBaseActivity {

    private boolean isBackPressedToBack = false;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }


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
