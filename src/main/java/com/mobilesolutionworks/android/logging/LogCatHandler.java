package com.mobilesolutionworks.android.logging;

import android.text.TextUtils;
import android.util.Log;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by yunarta on 25/11/15.
 */
class LogCatHandler extends Handler
{
    private String mPrefix;

    private boolean mHasPrefix;

    private boolean mPrintThread;

    private boolean mShorten;

    public LogCatHandler(String prefix, boolean shorten, boolean printThread)
    {
        mPrefix = prefix;
        mHasPrefix = !TextUtils.isEmpty(mPrefix);

        mShorten = shorten;
        mPrintThread = printThread;
    }

    @Override
    public boolean isLoggable(LogRecord record)
    {
        return true; // Integer.compare(getLevel().intValue(), record.getLevel().intValue()) >= 0;
    }

    @Override
    public void flush()
    {

    }

    @Override
    public void publish(LogRecord record)
    {
        Level level = record.getLevel();
        if (level == Level.OFF)
        {
            return;
        }

//            if (Integer.compare(getLevel().intValue(), record.getLevel().intValue()) >= 0)
        {
            StringBuilder sb = new StringBuilder();
            if (mHasPrefix)
            {
                sb.append(mPrefix);
            }

            sb.append(" - ");
            if (mPrintThread)
            {
                sb.append(Thread.currentThread().toString());
            }

            String name = record.getLoggerName();
            if (mShorten)
            {
                name = name.substring(name.lastIndexOf(".") + 1);
            }

            sb.append(record.getMessage());

            String message = sb.toString();

            if (level == Level.SEVERE)
            {
                Log.e(name, message, record.getThrown());
            }
            else if (level == Level.WARNING)
            {
                Log.w(name, message, record.getThrown());
            }
            else if (level == Level.INFO)
            {
                Log.i(name, message, record.getThrown());
            }
            else if (level == Level.CONFIG)
            {
                Log.v(name, message, record.getThrown());
            }
            else
            {
                Log.d(name, message, record.getThrown());
            }
        }
    }

    @Override
    public void close()
    {

    }
}
