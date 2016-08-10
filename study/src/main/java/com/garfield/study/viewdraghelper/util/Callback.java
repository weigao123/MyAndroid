package com.garfield.study.viewdraghelper.util;

public interface Callback {
	void onBefore();

	boolean onRun();

	void onAfter(boolean b);
}
