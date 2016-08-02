package com.garfield.baselib.fragmentation.anim;


import android.os.Parcel;
import android.os.Parcelable;

import com.garfield.baselib.R;


public class DefaultNoAnimator extends FragmentAnimator implements Parcelable {
    public DefaultNoAnimator() {
        enter = R.anim.no_anim;
        exit = R.anim.no_anim;
        popEnter = R.anim.no_anim;
        popExit = R.anim.no_anim;
    }

    protected DefaultNoAnimator(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DefaultNoAnimator> CREATOR = new Creator<DefaultNoAnimator>() {
        @Override
        public DefaultNoAnimator createFromParcel(Parcel in) {
            return new DefaultNoAnimator(in);
        }

        @Override
        public DefaultNoAnimator[] newArray(int size) {
            return new DefaultNoAnimator[size];
        }
    };
}
