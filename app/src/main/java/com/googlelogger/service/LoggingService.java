package com.googlelogger.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.googlelogger.logprovider.GPSLogProvider;
import com.googlelogger.logprovider.LogProvider;
import com.googlelogger.logprovider.PhoneLogProvider;
import com.googlelogger.logprovider.WifiLogProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Peter on 14.09.2015.
 */
public class LoggingService extends Service
{
    public static final String TAG = "LoggingService";
    private static final int FIRST_RUN_TIMEOUT_MILISEC = 1 * 1000;
    private static final int SERVICE_STARTER_INTERVAL_MILISEC = 1 * 1000;
    private static final int SERVICE_TASK_TIMEOUT_SEC = 5;
    private final int REQUEST_CODE = 1;

    private static final StringBuffer mLog = new StringBuffer();

    private ArrayList<LogProvider> mProviders = new ArrayList<LogProvider>();
    private AlarmManager mServiceStarterAlarmManager;

    private final ILoggingServiceAPI.Stub mBinder = new ILoggingServiceAPI.Stub() {
        @Override
        public IBinder asBinder() {
            return null;
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void logToFile(String _path) throws RemoteException {
            Log.e(TAG, "LOGGING TO FILE:"+_path);
        }
    };

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

//        startServiceStarter();
        mProviders.add(new WifiLogProvider(this));
        mProviders.add(new GPSLogProvider(this));
        mProviders.add(new PhoneLogProvider(this));
        //startServiceStarter();
        new MyTask().execute();

        return Service.START_REDELIVER_INTENT;
    }

    private void logAllProviders()
    {
        for(LogProvider lp:mProviders)
        {
            Log.d(lp.getName(), lp.getLog());
            addLogEntry(lp.getName(), lp.getLog());
        }
    }

    private void addLogEntry(String _key, String _value)
    {
        mLog.append(_key);
        mLog.append(": ");
        mLog.append(_value);
        mLog.append("\n");
    }


//    private void StopPerformingServiceTask() {
//        asyncTask.cancel(true);
//    }

//    private void GoToDesktop() {
//        Intent homeIntent= new Intent(Intent.ACTION_MAIN);
//        homeIntent.addCategory(Intent.CATEGORY_HOME);
//        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(homeIntent);
//    }

//    private void LockTheScreen() {
//        ComponentName localComponentName = new ComponentName(this, DeviceAdminDemo.class);
//
//        DevicePolicyManager localDevicePolicyManager = (DevicePolicyManager)this.getSystemService(Context.DEVICE_POLICY_SERVICE );
//        if (localDevicePolicyManager.isAdminActive(localComponentName))
//        {
//            localDevicePolicyManager.setPasswordQuality(localComponentName, DevicePolicyManager.PASSWORD_QUALITY_NUMERIC);
//        }
//
//        // locking the device
//        localDevicePolicyManager.lockNow();
//    }

    @Override
    public void onDestroy() {
        // performs when user or system kills our service
//        StopPerformingServiceTask();
//        GoToDesktop();
//        LockTheScreen();
        Log.d(TAG, mLog.toString());
    }

    private void logToFile()
    {
        try {
            FileWriter fw = new FileWriter(new File(getFilesDir().getAbsolutePath()+"/log.txt"));
            fw.write(mLog.toString());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (;;) {
                    TimeUnit.SECONDS.sleep(SERVICE_TASK_TIMEOUT_SEC);

                    // check if performing of the task is needed
                    if(isCancelled()) {
                        break;
                    }

                    //Initiating of onProgressUpdate callback that has access to UI
                   // publishProgress();
                    logAllProviders();
                    logToFile();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate(progress);
            Toast.makeText(getApplicationContext(), "Ooops!!! Try to kill me :)", Toast.LENGTH_LONG).show();
        }
    }

    // We should register our service in the AlarmManager service
    // for performing periodical starting of our service by the system
    private void startServiceStarter() {
        Intent intent;
        intent = new Intent(this, LoggingService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, this.REQUEST_CODE, intent, 0);

        if (pendingIntent == null) {
            Toast.makeText(this, "Some problems with creating of PendingIntent", Toast.LENGTH_LONG).show();
        } else {
            if (mServiceStarterAlarmManager == null) {
                mServiceStarterAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                mServiceStarterAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                        SystemClock.elapsedRealtime() + FIRST_RUN_TIMEOUT_MILISEC,
                        SERVICE_STARTER_INTERVAL_MILISEC, pendingIntent);
            }
        }
    }



}
