package journey.forjobs.akazoo_project.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
                updateTracksList();
            }
        }
    };

    @InjectView(R.id.tracks_list)
    ListView mTracksList;

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
    }


    protected void showSnackbar(String message) {
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.root),
                message, Snackbar.LENGTH_LONG);
        mySnackbar.show();
    }

    private void updateTracksList() {
        ArrayList<Track> allTracks = new ArrayList<Track>();
        Cursor mCursor;

        //used to know which columns name we need to retrieve
        String[] mProjection = {
                DBTableHelper.COLUMN_TRACK_NAME,
                DBTableHelper.COLUMN_TRACK_ARTIST,
                DBTableHelper.COLUMN_TRACK_IMAGE_URL
        };

        //queries the database
        mCursor = getContentResolver().query(
                TracksContentProvider.CONTENT_URI,  // The content URI of the words table - You need to use TracksContentProvider.CONTENT_URI to test yours
                mProjection,                       // The columns to return for each row
                null,
                null,
                null);

        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                //create empty object
                Track track = new Track();

                String trackName = mCursor.getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_TRACK_NAME));
                String trackArtist = mCursor.getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_TRACK_ARTIST));
                String trackImageUrl = mCursor.getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_TRACK_IMAGE_URL));

                track.setTrackName(trackName);
                track.setArtistName(trackArtist);
                track.setImageUrl(trackImageUrl);

                Log.d("MA TAG", "the track name is " + track.getTrackName());
                Log.d("MA TAG", "the track artist name is  " + track.getArtistName());

                allTracks.add(track);
            }

            final TracksListAdapter mTracksListAdapter = new TracksListAdapter(this, allTracks);
            mTracksList.setAdapter(mTracksListAdapter);

            mTracksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showSnackbar("You have clicked on: " + mTracksListAdapter.getTracks().
                            get(position).getTrackName());
                }

            });

        }



    }
}
