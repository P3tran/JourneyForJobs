package journey.forjobs.akazoo_project.activities;

import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.application.AkazooApplication;
import journey.forjobs.akazoo_project.controllers.AkazooController;
import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.PlaylistContentProvider;
import journey.forjobs.akazoo_project.fragments.PlaylistsFragment;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.listAdapters.PlaylistListAdapter;
import journey.forjobs.akazoo_project.listAdapters.TracksListAdapter;
import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.model.Track;
import journey.forjobs.akazoo_project.rest.RestClient;
import journey.forjobs.akazoo_project.rest.pojos.GetPlaylistsResponse;
import journey.forjobs.akazoo_project.utils.Const;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistsActivity extends AkazooActivity {

    @InjectView(R.id.playlists_list)
    ListView mPlaylistsList;

    @InjectView(R.id.pb_loader)
    ProgressBar mProgressBar;

    private MyMessageReceiver mMessageReceiver = new MyMessageReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);

            String msg = intent.getStringExtra(Const.CONTROLLER_SUCCESSFULL_CALLBACK_MESSAGE);
            Log.d("RECEIVER TEST", msg);
            if (msg == Const.REST_PLAYLISTS_SUCCESS){
                setUpPlaylistsListAdapter();
            }

        }
    };

    @Override
    protected MyMessageReceiver getmMessageReceiver() {
        return mMessageReceiver;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);
        ButterKnife.inject(this);

//        if (savedInstanceState == null) {
//            Fragment newFragment = new PlaylistsFragment();
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.add(, newFragment).commit();
//        }

        mProgressBar.setVisibility(View.VISIBLE);

        setUpPlaylistsListAdapter();

    }

    public void setUpPlaylistsListAdapter(){

        final PlaylistListAdapter mPlaylistListAdapter = new PlaylistListAdapter(PlaylistsActivity.this, fetchPlaylistsFromDB());
        mPlaylistsList.setAdapter(mPlaylistListAdapter);
        mPlaylistsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PlaylistsActivity.this, TracksActivity.class);
                intent.putExtra("id", mPlaylistListAdapter.getItem(position).getPlaylistId());
                startActivity(intent);
            }

        });
        mProgressBar.setVisibility(View.INVISIBLE);

    }

    protected void showSnackbar(String message){
        Snackbar mSnackBar = Snackbar.make(findViewById(R.id.root), message, Snackbar.LENGTH_LONG);
        mSnackBar.show();
    }


    public ArrayList<Playlist> fetchPlaylistsFromDB(){

        ArrayList<Playlist> playlists = new ArrayList<Playlist>();
            Cursor mCursor;

            String[] mProjection = {
                    DBTableHelper.COLUMN_PLAYLISTS_PLAYLIST_ID,
                    DBTableHelper.COLUMN_PLAYLISTS_NAME,
                    DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT,
                    DBTableHelper.COLUMN_PLAYLISTS_PHOTO_URL
            };

            mCursor = getContentResolver().query(
                    PlaylistContentProvider.CONTENT_URI,
                    mProjection,
                    null,
                    null,
                    null);

            if (mCursor != null) {
                while (mCursor.moveToNext()) {

                    Playlist playlist = new Playlist();

                    String playlistNameRetrievedFromDatabase = mCursor.getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_PLAYLISTS_NAME));

                    int tracksCountRetrievedFromDatabase = mCursor.getInt(mCursor.getColumnIndex(DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT));

                    String playlistIdRetrievedFromDatabase = mCursor.getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_PLAYLISTS_PLAYLIST_ID));

                    String playlistPhotoUrlRetrievedFromDatabase = mCursor.getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_PLAYLISTS_PHOTO_URL));

                    playlist.setName(playlistNameRetrievedFromDatabase);

                    playlist.setPlaylistId(playlistIdRetrievedFromDatabase);

                    playlist.setItemCount(tracksCountRetrievedFromDatabase);

                    playlist.setPhotoUrl(playlistPhotoUrlRetrievedFromDatabase);

                    playlists.add(playlist);

                }
            }

        if (mCursor.getCount() > 0){
            return playlists;
        }else {
            return null;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_refresh:
                mPlaylistsList.setAdapter(null);
                mProgressBar.setVisibility(View.VISIBLE);

                getAkazooController().fetchPlaylists();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
