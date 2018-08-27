package com.garfield.baselib.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import com.garfield.baselib.R;


/**
 * 带有图标和删除符号的可编辑输入框，用户可以自定义传入的显示图标
 * 
 */
public class ClearableEditText extends EditText implements OnTouchListener, TextWatcher {

	// 删除符号
	private Drawable deleteImage = getResources().getDrawable(R.drawable.icon_edit_delete);

	private Drawable headIcon;

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearableEditText(Context context) {
        super(context);
        init();
    }

	private void init() {
		setOnTouchListener(this);
		addTextChangedListener(this);
		deleteImage.setBounds(0, 0, (int)getTextSize(), (int)getTextSize());
		manageClearButton();
	}

	/**
	 * 传入左边要显示的图标资源id
	 * 
	 * @param id
	 */
	public void setIconResource(int id) {
		headIcon = getResources().getDrawable(id);
		headIcon.setBounds(0, 0, headIcon.getIntrinsicWidth(), headIcon.getIntrinsicHeight());
		manageClearButton();
	}

    /**
     * 传入右边要删除的图标资源id
     * @param id
     */
    public void setDeleteImage(int id) {
        deleteImage = getResources().getDrawable(id);
        deleteImage.setBounds(0, 0, deleteImage.getIntrinsicWidth(), deleteImage.getIntrinsicHeight());
        manageClearButton();
    }

	void manageClearButton() {
		if (getText().toString().equals(""))
			removeClearButton();
		else
			addClearButton();
	}

	void removeClearButton() {
		setCompoundDrawables(headIcon, getCompoundDrawables()[1], null, getCompoundDrawables()[3]);
	}

	void addClearButton() {
		setCompoundDrawables(headIcon, getCompoundDrawables()[1], deleteImage, getCompoundDrawables()[3]);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (getCompoundDrawables()[2] == null)
			return false;
		if (event.getAction() != MotionEvent.ACTION_UP)
			return false;
		if (event.getX() > getWidth() - getPaddingRight() - deleteImage.getBounds().width()) {
			setText("");
			removeClearButton();
		}
		return false;
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		manageClearButton();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}
