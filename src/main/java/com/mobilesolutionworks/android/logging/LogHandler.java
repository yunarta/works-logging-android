package com.mobilesolutionworks.android.logging;

import android.text.TextUtils;

import java.util.logging.Handler;

/**
 * Created by yunarta on 17/12/15.
 */
public abstract class LogHandler extends Handler
{
    protected String mPrefix;

    protected boolean mShorten;

    protected boolean mThread;

    protected boolean mHasPrefix;

    public void setup(String prefix, boolean shorten, boolean thread)
    {
        mPrefix = prefix;
        mHasPrefix = !TextUtils.isEmpty(mPrefix);

        mShorten = shorten;
        mThread = thread;
    }
}
