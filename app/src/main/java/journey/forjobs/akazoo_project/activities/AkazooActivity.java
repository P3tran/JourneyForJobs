package journey.forjobs.akazoo_project.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import journey.forjobs.akazoo_project.utils.Const;


public abstract class AkazooActivity extends AppCompatActivity {

    public class MyMessageReceiver extends BroadcastReceiver {
        protected String message;

        //method that will be called when a broadcast message is received
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }

    protected abstract MyMessageReceiver getmMessageReceiver();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Enable activity to listen to broadcast messages
        LocalBroadcastManager.getInstance(this).registerReceiver(getmMessageReceiver(),
                new IntentFilter(Const.CONTROLLER_SUCCESSFULL_CALLBACK));
        LocalBroadcastManager.getInstance(this).registerReceiver(getmMessageReceiver(),
                new IntentFilter(Const.CONTROLLER_FAILURE_CALLBACK));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Disable activity to listen broadcast messages
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getmMessageReceiver());
    }


}
