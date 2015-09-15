package com.googlelogger.logprovider;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by Peter on 14.09.2015.
 */
public class GPSLogProvider extends LogProvider
{
    public static final String TAG = "GPSLogProvider";

    private Context mCtx = null;
    private LocationManager mLocMan = null;

    public GPSLogProvider(Context _ctx)
    {
        mCtx = _ctx;
        mLocMan = (LocationManager)mCtx.getSystemService(Context.LOCATION_SERVICE);

    }




    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public String getLog()
    {
        for(String locProv:mLocMan.getAllProviders())
        {

            Location loc = mLocMan.getLastKnownLocation(locProv);
            if(null!=loc)
            {
                addLogEntry(locProv, loc.toString());
            }
            else
            {
                addLogEntry(locProv, "not available");
            }
        }
        return clearBuffer();
    }
}
