package journey.forjobs.akazoo_project.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.application.AkazooApplication;
import journey.forjobs.akazoo_project.controllers.AkazooController;
import journey.forjobs.akazoo_project.utils.Const;


public abstract class AkazooActivity extends AppCompatActivity {

    public class MyMessageReceiver extends BroadcastReceiver {
        protected String message;

        //method that will be called when a broadcast message is received
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Const.CONTROLLER_SUCCESSFULL_CALLBACK)) {
                message = intent.getStringExtra(Const.CONTROLLER_SUCCESSFULL_CALLBACK_MESSAGE);
            } else if (intent.getAction().equals(Const.CONTROLLER_FAILURE_CALLBACK)) {
                message="";
                showErrorPopup(intent.getStringExtra(Const.CONTROLLER_FAILURE_CALLBACK_MESSAGE));
            }
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

    protected AkazooController getAkazooController() {
        AkazooController mController = AkazooApplication.getInstance().getmController();
        return mController;
    }

    protected void showErrorPopup(String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(message);
        alertDialog.setTitle(getString(R.string.popup_error_title));
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.giannis), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    protected void showCustomDialog(String title, String message, String okButtonLabel, String cancelButtonLabel, DialogInterface.OnClickListener positiveAction, DialogInterface.OnClickListener negativeAction) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, okButtonLabel, positiveAction);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, cancelButtonLabel, negativeAction);
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
