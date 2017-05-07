package journey.forjobs.akazoo_project.activities;

import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
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
import android.widget.ProgressBar;

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

import journey.forjobs.akazoo_project.listAdapters.TracksListAdapter;

import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.model.Track;
import journey.forjobs.akazoo_project.rest.RestCallback;
import journey.forjobs.akazoo_project.rest.RestClient;

import journey.forjobs.akazoo_project.rest.pojos.GetTracksResponse;
import journey.forjobs.akazoo_project.utils.Const;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TracksActivity extends AkazooActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    @InjectView(R.id.tracks_list)
    ListView mTracksList;

    @InjectView(R.id.pb_loader)
    ProgressBar mProgressBar;

    TracksListAdapter mTracksListAdapter;
    ArrayList<Track> tracks = new ArrayList<Track>();


    private MyMessageReceiver mMessageReceiver = new MyMessageReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);

            tracks.clear();
            mTracksListAdapter.notifyDataSetChanged();

            setupTracksListAdapter();

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

        mProgressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        String id = intent.getStringExtra(Const.INTENT_SELECTED_PLAYLIST);

        getAkazooController().fetchTracks(id);

        getLoaderManager().initLoader(0,null,this);

        setupTracksListAdapter();

    }

    @Override
    protected void onResume() {
        super.onResume();

//        Intent intent = getIntent();
//        String id = intent.getStringExtra(Const.INTENT_SELECTED_PLAYLIST);
//
//        getAkazooController().fetchTracks(id);

    }

    protected void showSnackBar(String message){
        Snackbar mSnackBar = Snackbar.make(findViewById(R.id.root), message, Snackbar.LENGTH_LONG);
        mSnackBar.show();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] mProjection = {
                DBTableHelper.COLUMN_TRACKS_TRACK_ID,
                DBTableHelper.COLUMN_TRACKS_NAME,
                DBTableHelper.COLUMN_ARTIST_NAME,
                DBTableHelper.COLUMN_TRACKS_ID,
                DBTableHelper.COLUMN_TRACKS_PHOTO_URL
        };

        if (id == 0) {
            return new CursorLoader(TracksActivity.this, TracksContentProvider.CONTENT_URI, mProjection, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor mCursor) {

        ArrayList<Track> mTracks = new ArrayList<>();

        if (mCursor != null && mCursor.getCount() > 0) {
            //cursor.moveToFirst();
            while (mCursor.moveToNext()) {

                Track track = new Track();

                String tracksNameRetrievedFromDatabase = mCursor.getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_TRACKS_NAME));

                int tracksIdRetrievedFromDatabase = mCursor.getInt(mCursor.getColumnIndex(DBTableHelper.COLUMN_TRACKS_ID));

                String artistsNameRetrievedFromDatabase = mCursor.getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_ARTIST_NAME));

                String tracksPhotoUrlRetrievedFromDatabase = mCursor.getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_TRACKS_PHOTO_URL));

                track.setTrackName(tracksNameRetrievedFromDatabase);

                track.setTrackId(tracksIdRetrievedFromDatabase);

                track.setArtistName(artistsNameRetrievedFromDatabase);

                track.setImageUrl(tracksPhotoUrlRetrievedFromDatabase);

                mTracks.add(track);

            }

        }

        tracks = mTracks;
        mTracksListAdapter.notifyDataSetChanged();
        setupTracksListAdapter();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        tracks.clear();
        mTracksListAdapter.notifyDataSetChanged();
    }

    public void setupTracksListAdapter(){
        mTracksListAdapter = new TracksListAdapter(TracksActivity.this, tracks);
        mTracksList.setAdapter(mTracksListAdapter);
    }
}
