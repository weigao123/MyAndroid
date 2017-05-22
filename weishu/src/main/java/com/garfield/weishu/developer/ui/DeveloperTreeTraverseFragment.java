package com.garfield.weishu.developer.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.garfield.weishu.R;
import com.garfield.weishu.developer.datastructure.tree.BaseTraverse;
import com.garfield.weishu.developer.datastructure.tree.PreOrderTraverse;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaowei on 2017/5/22.
 */

public class DeveloperTreeTraverseFragment extends AppBaseFragment {

    @BindView(R.id.tree_traverse_result)
    TextView mTvResult;

    @BindView(R.id.tree_traverse_title)
    TextView mTvTitle;

    private BaseTraverse mTraverse = new PreOrderTraverse();

    private MaterialDialog mAlgoDialog;

    @Override
    protected String onGetToolbarTitle() {
        return "二叉树";
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_developer_tree_traverse;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        rootView.findViewById(R.id.menu).setVisibility(View.VISIBLE);

    }

    @OnClick({R.id.menu, R.id.tree_traverse_start})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                if (mAlgoDialog == null) {
                    mAlgoDialog = new MaterialDialog.Builder(getContext())
                            .backgroundColorRes(R.color.bg_itemFragment)
                            .title(R.string.traverse)
                            .items(R.array.traverse_algorithm)
                            .itemsColorRes(R.color.mainTextColor)
                            .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View itemView, final int which, final CharSequence text) {
                                    switchAlgo(text);
                                    getHandler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAlgoDialog.dismiss();
                                        }
                                    }, 200);
                                    return true;
                                }
                            })
                            .alwaysCallSingleChoiceCallback()
                            .autoDismiss(false)
                            .build();
                }
                mAlgoDialog.show();
                break;
            case R.id.tree_traverse_start:
                doTraverse();
                break;
        }
    }

    private void doTraverse() {
        mTraverse.traverse();
        Character[] c = mTraverse.getResult().toArray(new Character[]{});
        String result = Arrays.toString(c);
        mTvResult.setText(result);
    }

    private void switchAlgo(CharSequence algo) {
        mTvResult.setText(null);
        if (getString(R.string.pre_traverse).equals(algo)) {
            mTraverse = new PreOrderTraverse();
            mTvTitle.setText(R.string.pre_traverse);
        } else if (getString(R.string.in_traverse).equals(algo)) {
            mTraverse = new PreOrderTraverse();
            mTvTitle.setText(R.string.in_traverse);
        } else if (getString(R.string.post_traverse).equals(algo)) {
            mTraverse = new PreOrderTraverse();
            mTvTitle.setText(R.string.post_traverse);
        }

    }

}
