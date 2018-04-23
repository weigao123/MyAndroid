package com.garfield.study.task;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.widget.Toast;

import com.garfield.baselib.utils.system.L;

public class WakeJobService extends JobService {

    public static void startJobService(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE );
        JobInfo.Builder builder = new JobInfo.Builder(10, new ComponentName(context.getPackageName(), WakeJobService.class.getName()));
        //builder.setPeriodic(3000);     //每隔三秒运行一次
        //builder.setMinimumLatency(1000);      //设置任务的延迟执行时间
        builder.setOverrideDeadline(5000);     //设置任务最晚的延迟时间
        //builder.setRequiredNetworkType(int networkType)
        //builder.setRequiresCharging(true);
        //builder.setPersisted(boolean isPersisted)

        int jobId = jobScheduler.schedule(builder.build());
        L.d(""+jobId);
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        L.d("onStartJob");
        Toast.makeText(this, "onStartJob", Toast.LENGTH_SHORT).show();
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                jobFinished(params, false);

//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                L.d("cancelAll");
//                mJobScheduler.cancelAll();
            }
        }.start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        L.d("onStopJob");
        return false;
    }
}
