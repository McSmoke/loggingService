package com.googlelogger.logprovider;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by Peter on 14.09.2015.
 */
public class WifiLogProvider extends LogProvider {

    private static final String TAG = "WifiLogProvider";
    private Context mCtx = null;
    private WifiManager mWifiMan = null;

    public WifiLogProvider(Context _ctx)
    {
        mCtx = _ctx;
        mWifiMan = (WifiManager) mCtx.getSystemService(Context.WIFI_SERVICE);


    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public String getLog() {
        WifiInfo info = mWifiMan.getConnectionInfo();
        addLogEntry("ConnectionInfo", info.toString());
        return clearBuffer();
    }
}
