package com.garfield.baselib.utils;

import android.os.Handler;
import android.os.Looper;

public class InvokerUtils extends Thread {

	private Callback callback;

	public InvokerUtils(Callback callback) {
		this.callback = callback;
	}

	@Override
	public synchronized void start() {
		callback.onBefore();
		super.start();
	}

	@Override
	public void run() {
		final boolean b = callback.onRun();
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				callback.onAfter(b);
			}
		});
	}

	public interface Callback {
		void onBefore();
		boolean onRun();
		void onAfter(boolean b);
	}

}
