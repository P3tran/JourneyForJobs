package journey.forjobs.akazoo_project.activities;

import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.CursorLoader;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.application.AkazooApplication;
import journey.forjobs.akazoo_project.controllers.AkazooController;
import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.PlaylistContentProvider;
import journey.forjobs.akazoo_project.database.TracksContentProvider;
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
import android.view.ViewGroup.LayoutParams;

public class PlaylistsActivity extends AkazooActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    @InjectView(R.id.playlists_list)
    ListView mPlaylistsList;

    @InjectView(R.id.pb_loader)
    ProgressBar mProgressBar;

    PlaylistListAdapter mPlaylistListAdapter;
    ArrayList<Playlist> playlists = new ArrayList<Playlist>();

    private boolean fetchStatus = false;

    private MyMessageReceiver mMessageReceiver = new MyMessageReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);

            setUpPlaylistsListAdapter();

            if(intent.getAction() == "SERVICE_BIND" && fetchStatus == true){
                getAkazooController().fetchPlaylists();
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

        if (savedInstanceState == null) {
            Fragment newFragment = new PlaylistsFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container,newFragment).commit();
        }

        mProgressBar.setVisibility(View.VISIBLE);
        setUpPlaylistsListAdapter();

        getLoaderManager().initLoader(1, null,this);

    }

    public void setUpPlaylistsListAdapter(){

        mPlaylistListAdapter = new PlaylistListAdapter(PlaylistsActivity.this, playlists);
        mPlaylistsList.setAdapter(mPlaylistListAdapter);
        mPlaylistsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(PlaylistsActivity.this, TracksActivity.class);
                intent.putExtra(Const.INTENT_SELECTED_PLAYLIST, mPlaylistListAdapter.getItem(position).getPlaylistId());
                startActivity(intent);

            }

        });
        mProgressBar.setVisibility(View.INVISIBLE);

    }

    protected void showSnackbar(String message){
        Snackbar mSnackBar = Snackbar.make(findViewById(R.id.root), message, Snackbar.LENGTH_LONG);
        mSnackBar.show();
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

                playlists.clear();
                mPlaylistListAdapter.notifyDataSetChanged();
                getAkazooController().fetchPlaylists();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] mProjection = {
                DBTableHelper.COLUMN_PLAYLISTS_PLAYLIST_ID,
                DBTableHelper.COLUMN_PLAYLISTS_NAME,
                DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT,
                DBTableHelper.COLUMN_PLAYLISTS_PHOTO_URL
        };

        if (id == 1) {
            return new CursorLoader(PlaylistsActivity.this, PlaylistContentProvider.CONTENT_URI, mProjection, null, null, null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor mCursor) {

        if (mCursor != null && mCursor.getCount() > 0) {
            //mCursor.moveToFirst();
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

        }else {
            fetchStatus = true;
        }

        mPlaylistListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        playlists.clear();
        mPlaylistListAdapter.notifyDataSetChanged();
    }
}
