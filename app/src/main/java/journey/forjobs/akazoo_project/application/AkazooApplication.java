package journey.forjobs.akazoo_project.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import journey.forjobs.akazoo_project.activities.PlaylistsActivity;
import journey.forjobs.akazoo_project.activities.TracksActivity;
import journey.forjobs.akazoo_project.controllers.AkazooController;
import journey.forjobs.akazoo_project.listAdapters.PlaylistListAdapter;

public class AkazooApplication extends Application {

    private static AkazooApplication mInstance;
    protected AkazooController mController;
    boolean status;

    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent = new Intent(getApplicationContext(), AkazooController.class);
        getApplicationContext().bindService(intent,serviceConnection, getApplicationContext().BIND_AUTO_CREATE);

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AkazooController.LocalBinder binder = (AkazooController.LocalBinder) service;
            mController = binder.getServerInstance();
            status = true;

            Log.d("APPLICATIOM", "SERVICE BIND");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            status = false;
        }
    };

    public static AkazooApplication getmInstance() {
        return mInstance;
    }

    public AkazooController getmController() {
        return mController;
    }
}
