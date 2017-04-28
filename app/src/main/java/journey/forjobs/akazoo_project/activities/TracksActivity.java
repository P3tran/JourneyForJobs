package journey.forjobs.akazoo_project.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.TracksContentProvider;
import journey.forjobs.akazoo_project.listAdapters.TracksListAdapter;
import journey.forjobs.akazoo_project.model.Track;

public class TracksActivity extends AkazooActivity {

    @InjectView(R.id.tracks_list)
    ListView mTracksList;

    private MyMessageReceiver mMessageReceiver = new MyMessageReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
        }
    };

    @Override
    protected MyMessageReceiver getmMessageReceiver() {
        return mMessageReceiver;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);
        ButterKnife.inject(this);

        ArrayList<Track> allTracks = new ArrayList();

        for (int i=0; i<20; i++){
            Track track = new Track();
            track.setTrackId(i);
            track.setTrackName("track" + i);
            track.setArtistName("artist" + i);

            allTracks.add(track);
        }

        final TracksListAdapter mTracksListAdapter = new TracksListAdapter(this, allTracks);
        mTracksList.setAdapter(mTracksListAdapter);
        mTracksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showSnackBar("Your have clicked " + mTracksListAdapter.getTracks().get(position).getTrackName());
            }
        });

        {
            //insert track 1 into databse
            ContentValues values = new ContentValues();
            values.put(DBTableHelper.COLUMN_TRACKS_TRACK_ID, "id3");
            values.put(DBTableHelper.COLUMN_TRACKS_NAME, "track1");
            values.put(DBTableHelper.COLUMN_ARTIST_NAME, "Blabla");
            getContentResolver().insert(
                    TracksContentProvider.CONTENT_URI, values);

            //insert track 2 into database
            ContentValues values2 = new ContentValues();
            values2.put(DBTableHelper.COLUMN_TRACKS_TRACK_ID, "id4");
            values2.put(DBTableHelper.COLUMN_TRACKS_NAME, "track2");
            values2.put(DBTableHelper.COLUMN_ARTIST_NAME, "BlueBlue");
            getContentResolver().insert(
                    TracksContentProvider.CONTENT_URI, values2);

            //retrieve info from the database
            {
                Cursor mCursor;

                //used to know which columns name we need to retrieve
                String[] mProjection = {
                        DBTableHelper.COLUMN_TRACKS_TRACK_ID,
                        DBTableHelper.COLUMN_TRACKS_NAME,
                        DBTableHelper.COLUMN_ARTIST_NAME
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

                        // Gets the track name from the dabase
                        String trackNameRetrievedFromDatabase = mCursor.getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_TRACKS_NAME));

                        // Gets the track count from the column database
                        String tracksCountRetrievedFromDatabase = mCursor.getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_ARTIST_NAME));

                        //put the value retrieved from databse into our object
                        track.setTrackName(trackNameRetrievedFromDatabase);

                        //put the value retrieved from databse into our object
                        track.setArtistName(tracksCountRetrievedFromDatabase);


                        //display the information we retrieved in the logs
                        Log.d("MA TAG", "the track name is " + track.getTrackName());
                        Log.d("MA TAG", "the track artist is  " + track.getArtistName());
                    }
                }}

            //delete all values from database
            getContentResolver().delete(TracksContentProvider.CONTENT_URI, null, null);
        }
    }

    protected void showSnackBar(String message){
        Snackbar mSnackBar = Snackbar.make(findViewById(R.id.root), message, Snackbar.LENGTH_LONG);
        mSnackBar.show();
    }

}
