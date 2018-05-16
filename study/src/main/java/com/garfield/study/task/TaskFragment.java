package com.garfield.study.task;

import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

import com.garfield.baselib.utils.system.L;
import com.garfield.study.app.AppBaseFragment;

public class TaskFragment extends AppBaseFragment {

    @Override
    protected int onGetFragmentLayout() {
        return 0;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WakeJobService.startJobService(mActivity);
                startAlarmManager();
            }
        });
    }

    private void startAlarmManager() {
        AlarmManager am = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent("com.garfield.study.broadcast");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mActivity, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, pendingIntent);
    }


}
