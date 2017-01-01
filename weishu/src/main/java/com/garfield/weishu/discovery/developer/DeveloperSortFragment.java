package com.garfield.weishu.discovery.developer;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.garfield.weishu.R;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import butterknife.OnClick;

/**
 * Created by gaowei3 on 2016/12/19.
 */

public class DeveloperSortFragment extends AppBaseFragment {

    private MaterialDialog mAlgorithmDialog;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_develop_sort;
    }

    @Override
    protected String onGetToolbarTitleResource() {
        return getString(R.string.sort);
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        rootView.findViewById(R.id.menu).setVisibility(View.VISIBLE);



    }

    @OnClick(R.id.menu)
    void onClick(View view) {
        if (mAlgorithmDialog == null) {
            mAlgorithmDialog = new MaterialDialog.Builder(getContext())
                    .title(R.string.algorithm)
                    .items(R.array.sort_algorithm)
                    .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mAlgorithmDialog.dismiss();
                                }
                            }, 200);
                            return true;
                        }
                    })
                    .alwaysCallSingleChoiceCallback()
                    .autoDismiss(false)
                    .build();
        }
        mAlgorithmDialog.show();
    }
}
