package com.garfield.weishu.discovery.developer;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.garfield.baselib.utils.string.RandomUtils;
import com.garfield.baselib.utils.system.L;
import com.garfield.baselib.utils.system.TaskUtils;
import com.garfield.weishu.R;
import com.garfield.weishu.datastructure.sort.BubbleSort;
import com.garfield.weishu.datastructure.sort.HeapSort;
import com.garfield.weishu.datastructure.sort.ISort;
import com.garfield.weishu.datastructure.sort.QuickSort;
import com.garfield.weishu.datastructure.sort.ShellSort;
import com.garfield.weishu.datastructure.sort.SimpleSelectionSort;
import com.garfield.weishu.datastructure.sort.StraightInsertionSort;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaowei3 on 2016/12/19.
 */

public class DeveloperSortFragment extends AppBaseFragment {

    private MaterialDialog mAlgoDialog;

    @BindView(R.id.fragment_developer_sort_quantity)
    RadioGroup mDataQuantity;

    @BindView(R.id.fragment_developer_sort_origin)
    TextView mOriginArrayView;

    @BindView(R.id.fragment_developer_sort_result)
    TextView mResultArrayView;

    @BindView(R.id.fragment_developer_sort_title)
    TextView mTitleView;

    @BindView(R.id.fragment_developer_sort_time)
    TextView mTimeView;

    @BindView(R.id.fragment_developer_sort_start)
    TextView mSortButton;

    private int[] mOriginArray;
    private ISort mSortAlgo;

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

        mDataQuantity.check(R.id.fragment_developer_sort_quantity_1);
        mDataQuantity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                refreshData();
            }
        });
        switchAlgo(getString(R.string.shell_sort));
        //refreshData();
    }

    @OnClick({R.id.menu, R.id.fragment_developer_sort_refresh, R.id.fragment_developer_sort_start})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                if (mAlgoDialog == null) {
                    mAlgoDialog = new MaterialDialog.Builder(getContext())
                            .title(R.string.algorithm)
                            .items(R.array.sort_algorithm)
                            .itemsColorRes(R.color.black)
                            .itemsCallbackSingleChoice(3, new MaterialDialog.ListCallbackSingleChoice() {
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
            case R.id.fragment_developer_sort_refresh:
                refreshData();
                break;
            case R.id.fragment_developer_sort_start:
                sortData();
                break;
        }

    }

    private void refreshData() {
        int num = 1000;
        int quantityId = mDataQuantity.getCheckedRadioButtonId();
        if (quantityId == R.id.fragment_developer_sort_quantity_1) {
            num = 1000;
        } else if (quantityId == R.id.fragment_developer_sort_quantity_2) {
            num = 10000;
        } else if (quantityId == R.id.fragment_developer_sort_quantity_3) {
            num = 100000;
        } else if (quantityId == R.id.fragment_developer_sort_quantity_4) {
            num = 1000000;
        }
        final int finalNum = num;
        new TaskUtils.Invoker(new TaskUtils.Callback() {
            private Dialog dialog;
            private String originString;
            @Override
            public void onBefore() {
                dialog = new MaterialDialog.Builder(getContext())
                        .content(R.string.initializing)
                        .contentColorRes(R.color.black)
                        .progress(true, 0)
                        .cancelable(false)
                        .show();
            }

            @Override
            public boolean doInBackground() {
                mOriginArray = RandomUtils.getRandomArray(finalNum);
                if (mOriginArray.length <= 1000) {
                    originString = Arrays.toString(mOriginArray);
                } else {
                    originString = "";
                }
                return true;
            }

            @Override
            public void onAfter(boolean b) {
                mTimeView.setText("");
                if (TextUtils.isEmpty(originString)) {
                    mOriginArrayView.setTextColor(getResources().getColor(R.color.black_gray));
                    mOriginArrayView.setText(R.string.data_is_too_large_to_show);
                } else {
                    mOriginArrayView.setTextColor(getResources().getColor(R.color.black));
                    mOriginArrayView.setText(originString);
                }
                mResultArrayView.setText("");
                dialog.dismiss();
            }
        }).start();
    }

    private void sortData() {
        if (mSortAlgo == null) {
            L.show("该算法待开发");
            return;
        }
        new TaskUtils.Invoker(new TaskUtils.Callback() {
            private long time;
            private Dialog dialog;
            private String resultString;
            @Override
            public void onBefore() {
                dialog = new MaterialDialog.Builder(getContext())
                        .content(R.string.sorting)
                        .contentColorRes(R.color.black)
                        .progress(true, 0)
                        .cancelable(false)
                        .show();
            }

            @Override
            public boolean doInBackground() {
                int[] originArrayCopy = mOriginArray.clone();
                time = mSortAlgo.sort(originArrayCopy);
                if (mOriginArray.length <= 1000) {
                    resultString = Arrays.toString(originArrayCopy);
                } else {
                    resultString = "";
                }
                return true;
            }

            @Override
            public void onAfter(boolean b) {
                mTimeView.setText(getString(R.string.time_consuming, time));
                if (TextUtils.isEmpty(resultString)) {
                    mResultArrayView.setTextColor(getResources().getColor(R.color.black_gray));
                    mResultArrayView.setText(R.string.data_is_too_large_to_show);
                } else {
                    mResultArrayView.setTextColor(getResources().getColor(R.color.black));
                    mResultArrayView.setText(resultString);
                }
                dialog.dismiss();
            }
        }).start();
    }

    private void switchAlgo(CharSequence algo) {
        if (getString(R.string.bubble_sort).equals(algo)) {
            mSortAlgo = new BubbleSort();
            mTitleView.setText(R.string.bubble_sort);
        } else if (getString(R.string.simple_selection_sort).equals(algo)) {
            mSortAlgo = new SimpleSelectionSort();
            mTitleView.setText(R.string.simple_selection_sort);
        } else if (getString(R.string.straight_insertion_sort).equals(algo)) {
            mSortAlgo = new StraightInsertionSort();
            mTitleView.setText(R.string.straight_insertion_sort);
        } else if (getString(R.string.shell_sort).equals(algo)) {
            mSortAlgo = new ShellSort();
            mTitleView.setText(R.string.shell_sort);
        } else if (getString(R.string.fast_sort).equals(algo)) {
            mSortAlgo = new QuickSort();
            mTitleView.setText(R.string.fast_sort);
        } else if (getString(R.string.heap_sort).equals(algo)) {
            mSortAlgo = new HeapSort();
            mTitleView.setText(R.string.heap_sort);
        } else if (getString(R.string.merging_sort).equals(algo)) {
            mSortAlgo = null;
            mTitleView.setText(R.string.merging_sort);
        }
        //mResultArrayView.setText("");
        //mTimeView.setText("");
    }
}
