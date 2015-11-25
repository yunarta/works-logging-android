package com.mobilesolutionworks.android.logging;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by yunarta on 13/11/15.
 */
public class LogUtil
{
    public static Logger getLogger(Class cl)
    {
        return Logger.getLogger(cl.getName());
    }

    public static Logger getLogger(String name)
    {
        return Logger.getLogger(name);
    }

    public static void configure(Context context, int id)
    {
        doConfigure(context, id);
    }

    public static void configure(Context context)
    {
        doConfigure(context, context.getResources().getIdentifier("logutil", "xml", context.getPackageName()));
    }

    private static void doConfigure(Context context, int identifier)
    {
        Resources resources = context.getResources();

        if (identifier != 0)
        {
            XmlResourceParser xml = resources.getXml(identifier);
            boolean parsing = true;
            try
            {
                while (parsing)
                {
                    int token = xml.next();
                    switch (token)
                    {
                        case XmlResourceParser.END_DOCUMENT:
                        {
                            parsing = false;
                            break;
                        }

                        case XmlPullParser.START_TAG:
                        {
                            String name = xml.getName();
                            if ("package".equals(name))
                            {
                                try
                                {
                                    String packageName = xml.getAttributeValue(null, "name");

                                    boolean shorten = xml.getAttributeBooleanValue(null, "shortName", false);
                                    boolean printThread = xml.getAttributeBooleanValue(null, "printThread", false);
                                    String prefix = xml.getAttributeValue(null, "prefix");

                                    Logger logger;
                                    Handler[] handlers;

                                    logger = Logger.getLogger(packageName);
                                    handlers = logger.getHandlers();
                                    for (Handler h : handlers)
                                    {
                                        if (h instanceof LoggingHandler)
                                        {
                                            logger.removeHandler(h);
                                        }
                                    }

                                    Level level = Level.parse(xml.getAttributeValue(null, "level").toUpperCase());

                                    LoggingHandler handler = new LoggingHandler(prefix, shorten, printThread);
                                    handler.setLevel(level);

                                    logger.addHandler(handler);
                                    logger.setLevel(level);


                                }
                                catch (Exception e)
                                {
                                    // e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
//                e.printStackTrace();
            }
        }
    }

    private static class LoggingHandler extends Handler
    {
        private String mPrefix;

        private boolean mHasPrefix;

        private boolean mPrintThread;

        private boolean mShorten;

        public LoggingHandler(String prefix, boolean shorten, boolean printThread)
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

                if (mPrintThread)
                {
                    sb.append(Thread.currentThread().toString());
                }

                String name = record.getLoggerName();
                if (mShorten)
                {
                    name = name.substring(name.lastIndexOf(".") + 1);
                }

                sb.append(" - ");
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
}
