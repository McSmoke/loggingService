package com.googlelogger;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;

import com.googlelogger.service.ILoggingServiceAPI;
import com.googlelogger.service.LoggingService;

public class MainActivity extends Activity
{

    public static final String TAG = "MainActivity";

    private Button mSaveFileButt = null;


    private ILoggingServiceAPI mLogServAPI = null;

    private Intent mServiceIntent = null;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            mLogServAPI = ILoggingServiceAPI.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSaveFileButt = (Button)findViewById(R.id.btnMainActSaveFile);
        mSaveFileButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mLogServAPI.logToFile("**hello**");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        mServiceIntent = new Intent(this, LoggingService.class);
        startService(mServiceIntent);
        bindService(mServiceIntent, mConnection, Service.BIND_NOT_FOREGROUND);
        hideAppIcon();
//        finish();
    }

    @Override
    protected void onDestroy()
    {
        unbindService(mConnection);
        super.onDestroy();
    }

    private void hideAppIcon() {
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, MainActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }


}
