package com.googlelogger.logprovider;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * Created by Peter on 14.09.2015.
 */
public abstract class LogProvider
{
    private Context mCtx;
    private SimpleDateFormat mFormat = new SimpleDateFormat("dd-mm-yy: hh:MM:ss: ");
    private SimpleFormatter mFormatter = new SimpleFormatter();

    private static final StringBuffer mLogBuff = new StringBuffer();

    protected StringBuffer getLogBuffer()
    {
        return mLogBuff;
    }

    public abstract String getName();
    public abstract String getLog();

    protected void addLogEntry(String _key, String _value)
    {
        mLogBuff.append(mFormat.format(new Date()));
        mLogBuff.append(_key);
        mLogBuff.append(": ");
        mLogBuff.append(_value);
        mLogBuff.append("\n");
    }

    protected String clearBuffer()
    {
        String buff = mLogBuff.toString();
        mLogBuff.delete(0,mLogBuff.length());
        return buff;
    }
}
