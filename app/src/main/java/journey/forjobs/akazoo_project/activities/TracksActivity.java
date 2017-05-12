package journey.forjobs.akazoo_project.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.application.AkazooApplication;
import journey.forjobs.akazoo_project.controllers.AkazooController;
import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.PlaylistContentProvider;
import journey.forjobs.akazoo_project.database.TracksContentProvider;
import journey.forjobs.akazoo_project.fragments.TracksFragment;
import journey.forjobs.akazoo_project.listadapters.TracksListAdapter;
import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.model.Track;
import journey.forjobs.akazoo_project.utils.Const;

public class TracksActivity extends AkazooActivity {

    private MyMessageReceiver mMessageReceiver = new MyMessageReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            if (intent.getAction().equals(Const.SERVICE_BIND)) {
                getAkazooController().getTracks("768f797f-ce35-4515-b6d8-56dcf91b8253");
            } else if (message.equals(Const.REST_TRACKS_SUCCESS)) {
                mTracksFragment.updateTracksList();
            }
        }
    };

    TracksFragment mTracksFragment;


    @Override
    protected MyMessageReceiver getmMessageReceiver() {
        return mMessageReceiver;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);
        ButterKnife.inject(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(getmMessageReceiver(),
                new IntentFilter(Const.SERVICE_BIND));

        if (savedInstanceState == null) {
            mTracksFragment = new TracksFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.root, mTracksFragment).commit();
        }
    }


    protected void showSnackbar(String message) {
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.root),
                message, Snackbar.LENGTH_LONG);
        mySnackbar.show();
    }

}
