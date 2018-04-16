package com.garfield.study.task;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.widget.Toast;

import com.garfield.baselib.utils.system.L;

public class WakeJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        L.d("onStartJob");
        Toast.makeText(this, "onStartJob", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        L.d("onStopJob");
        return false;
    }
}
