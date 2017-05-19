package journey.forjobs.akazoo_project.activities;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.application.AkazooApplication;
import journey.forjobs.akazoo_project.controllers.AkazooController;
import journey.forjobs.akazoo_project.utils.Const;


public abstract class AkazooActivity extends AppCompatActivity{

    ProgressDialog progressDialog;

    public class MyMessageReceiver extends BroadcastReceiver {
        protected String message;

        //method that will be called when a broadcast message is received
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()){
                case Const.CONTROLLER_FAILURE_CALLBACK:
                    Log.d("REST ERROR", intent.getStringExtra(Const.CONTROLLER_FAILURE_CALLBACK_MESSAGE));
                    dismissProgressDialog();
                    showPopUp(intent.getStringExtra(Const.CONTROLLER_FAILURE_CALLBACK_MESSAGE));
                    break;
                case Const.CONTROLLER_SUCCESSFULL_CALLBACK:
                    Log.d("REST SUCCESS", intent.getStringExtra(Const.CONTROLLER_SUCCESSFULL_CALLBACK_MESSAGE));

                    if (intent.getStringExtra(Const.CONTROLLER_SUCCESSFULL_CALLBACK_MESSAGE) == Const.SHOW_PROGRESS){
                        showProgressDialog();
                    }else {
                        dismissProgressDialog();
                    }
                    break;
            }

        }
    }

    protected abstract MyMessageReceiver getmMessageReceiver();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(getmMessageReceiver(),
                new IntentFilter(Const.CONTROLLER_SUCCESSFULL_CALLBACK));

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Enable activity to listen to broadcast messages

        LocalBroadcastManager.getInstance(this).registerReceiver(getmMessageReceiver(),
                new IntentFilter(Const.CONTROLLER_FAILURE_CALLBACK));

    }

    @Override
    protected void onPause() {
        super.onPause();
        //Disable activity to listen broadcast messages
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getmMessageReceiver());
    }

    protected AkazooController getAkazooController(){

        return AkazooApplication.getmInstance().getmController();
    }

    protected void showPopUp(String message){

        new AlertDialog.Builder(AkazooActivity.this).setTitle(getString(R.string.popup_error_title)).setMessage(message).setNeutralButton(getString(R.string.popup_error_dismiss_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    public void showProgressDialog(){
        progressDialog = new ProgressDialog(AkazooActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }

    public void dismissProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }

    }

}
