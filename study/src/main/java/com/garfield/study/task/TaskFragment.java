package com.garfield.study.task;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.garfield.baselib.utils.system.L;
import com.garfield.study.app.AppBaseFragment;

public class TaskFragment extends AppBaseFragment {

    JobScheduler mJobScheduler;

    @Override
    protected int onGetFragmentLayout() {
        return 0;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        mJobScheduler = (JobScheduler) mActivity.getSystemService( Context.JOB_SCHEDULER_SERVICE );
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startJobService();
            }
        });
    }


    private void startJobService() {
        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(mActivity.getPackageName(), WakeJobService.class.getName()));
        builder.setPeriodic(3000);     //每隔三秒运行一次
        //builder.setMinimumLatency(1000);      //设置任务的延迟执行时间
        //builder.setOverrideDeadline(5000);     //设置任务最晚的延迟时间。如果到了规定的时间其他条件还未满足，任务也会被启动
        //builder.setRequiredNetworkType(int networkType)      //只有在满足指定的网络条件时才会被执行
        builder.setRequiresCharging(true);   //只有当设备在充电时这个任务才会被执行
        //builder.setPersisted(boolean isPersisted)         //当设备重启之后你的任务是否还要继续执行

        int jobId = mJobScheduler.schedule(builder.build());
        L.d(""+jobId);
    }

}
