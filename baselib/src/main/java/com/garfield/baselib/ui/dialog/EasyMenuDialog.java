package com.garfield.baselib.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garfield.baselib.R;
import com.garfield.baselib.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaowei3 on 2016/8/28.
 */
public class EasyMenuDialog extends DialogFragment {

    public static final int ITEM_HEIGHT = 48;
    public static final int ITEM_MARGIN = 90;
    public static final int TEXT_LEFT_MARGIN = 30;
    public static final int TEXT_SIZE = 16;


    private List<String> mList = new ArrayList<>();

    private OnItemClickListener mItemClickListener;

    public EasyMenuDialog() {
        super();
        Bundle bundle = getArguments();
        mList.add("第一条");
        mList.add("第二条");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.DialogBackgroundDim);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_round_corner_white));
        int width = SizeUtils.getScreenWidth(getActivity()) - SizeUtils.dp2px(getActivity(), ITEM_MARGIN);
        // 无效
        //linearLayout.setLayoutParams(new ViewGroup.LayoutParams(width, -2));

        View viewLine = null;
        for (final String string : mList) {
            TextView textView = new TextView(getActivity());
            textView.setText(string);
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_press_gray));
            textView.setTextSize(TEXT_SIZE);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setPadding(SizeUtils.dp2px(getActivity(), TEXT_LEFT_MARGIN), 0, 0, 0);
            textView.setLayoutParams(new LinearLayout.LayoutParams(width, SizeUtils.dp2px(getActivity(), ITEM_HEIGHT)));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(string);
                    }
                    dismiss();
                }
            });
            linearLayout.addView(textView);

            viewLine = new View(getActivity());
            viewLine.setBackgroundColor(getResources().getColor(R.color.gray));
            viewLine.setLayoutParams(new ViewGroup.LayoutParams(width, 1));
            linearLayout.addView(viewLine);
        }
        if (viewLine != null) {
            linearLayout.removeView(viewLine);
        }

        return linearLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().setCanceledOnTouchOutside(true);
    }

    public void setItemClickListener() {

    }

    interface OnItemClickListener {
        void onItemClick(String string);
    }
}
