package com.googlelogger.logprovider;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.PowerManager;
import android.telephony.TelephonyManager;

/**
 * Created by Peter on 14.09.2015.
 */
public class PhoneLogProvider extends LogProvider
{

    private static final String TAG = "PhoneLogProvider";
    private Context mCtx = null;
    private TelephonyManager mTelMan = null;
    private StringBuffer mLog = null;


    public PhoneLogProvider(Context _ctx)
    {
        mCtx = _ctx;
        mTelMan = (TelephonyManager)mCtx.getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    @TargetApi(17)
    public String getLog()
    {
        mLog = new StringBuffer();

//        if(null!=mTelMan.getAllCellInfo())
//        {
//            for(CellInfo ci:mTelMan.getAllCellInfo())
//            {
//                mLog.append(ci.toString());
//            }
//        }

        addLogEntry("DeviceID", mTelMan.getDeviceId());
        addLogEntry("LineNumber", mTelMan.getLine1Number());

        return clearBuffer();
    }


}
