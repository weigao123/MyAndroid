package com.garfield.study.ui.viewdraghelper.util;

public interface Callback {
	void onBefore();

	boolean onRun();

	void onAfter(boolean b);
}
