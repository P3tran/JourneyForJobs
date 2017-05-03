package journey.forjobs.akazoo_project.activities;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Handler;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.controllers.AkazooController;
import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.PlaylistContentProvider;
import journey.forjobs.akazoo_project.database.TracksContentProvider;
import journey.forjobs.akazoo_project.listAdapters.PlaylistListAdapter;
import journey.forjobs.akazoo_project.listAdapters.TracksListAdapter;
import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.model.Track;
import journey.forjobs.akazoo_project.rest.RestCallback;
import journey.forjobs.akazoo_project.rest.RestClient;
import journey.forjobs.akazoo_project.rest.pojos.GetPlaylistsResponse;
import journey.forjobs.akazoo_project.rest.pojos.GetTracksResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TracksActivity extends AkazooActivity {

    @InjectView(R.id.tracks_list)
    ListView mTracksList;

    AkazooController mService;
    boolean status;

    String id;

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

        Intent i = new Intent(this, AkazooController.class);
        bindService(i,serviceConnection,Context.BIND_AUTO_CREATE);

    }

    protected void showSnackBar(String message){
        Snackbar mSnackBar = Snackbar.make(findViewById(R.id.root), message, Snackbar.LENGTH_LONG);
        mSnackBar.show();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AkazooController.LocalBinder binder = (AkazooController.LocalBinder) service;
            mService = binder.getServerInstance();
            status = true;

            Intent intent = getIntent();
            id  = intent.getExtras().getString("id");

            mService.fetchTracks(id, new AkazooController.TracksComplection() {
                @Override
                public void onResponse() {
                    final TracksListAdapter mTracksListAdapter = new TracksListAdapter(TracksActivity.this, fetchTracksFromDB());
                    mTracksList.setAdapter(mTracksListAdapter);
                }
            });


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            status = false;
        }
    };

    public ArrayList<Track> fetchTracksFromDB(){

        ArrayList<Track> tracks = new ArrayList<Track>();
        Cursor mCursor;

        String[] mProjection = {
                DBTableHelper.COLUMN_TRACKS_TRACK_ID,
                DBTableHelper.COLUMN_TRACKS_NAME,
                DBTableHelper.COLUMN_ARTIST_NAME
        };

        mCursor = getContentResolver().query(
                TracksContentProvider.CONTENT_URI,
                mProjection,
                null,
                null,
                null);

        if (mCursor != null) {
            while (mCursor.moveToNext()) {

                Track track = new Track();

                String trackNameRetrievedFromDatabase = mCursor.getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_TRACKS_NAME));

                String tracksArtistsNameRetrievedFromDatabase = mCursor.getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_ARTIST_NAME));

                Long tracksIdRetrievedFromDatabase = mCursor.getLong(mCursor.getColumnIndex(DBTableHelper.COLUMN_TRACKS_TRACK_ID));

                track.setTrackName(trackNameRetrievedFromDatabase);

                track.setArtistName(tracksArtistsNameRetrievedFromDatabase);

                track.setTrackId(tracksIdRetrievedFromDatabase);

                tracks.add(track);

            }
        }

        getContentResolver().delete(TracksContentProvider.CONTENT_URI, null, null);

        return tracks;

    }

}
