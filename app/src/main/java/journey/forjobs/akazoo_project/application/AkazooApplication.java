package journey.forjobs.akazoo_project.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import journey.forjobs.akazoo_project.controllers.AkazooController;
import journey.forjobs.akazoo_project.utils.Const;


/**
 * Created by Petros Efthymiou on 22/7/2016.
 */
public class AkazooApplication extends Application {
    AkazooController.LocalBinder mLocalBinder;
    private static AkazooApplication mInstance;
    protected AkazooController mController;


    boolean mBounded;
    ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBounded = false;
            mController = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBounded = true;
            mLocalBinder = (AkazooController.LocalBinder) service;
            mController = mLocalBinder.getServerInstance();

            Intent intent = new Intent(Const.SERVICE_BIND);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Intent mIntent = new Intent(this, AkazooController.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }

    public AkazooController getmController() {
        return mController;
    }

    public static AkazooApplication getInstance(){
        return mInstance;
    }
}