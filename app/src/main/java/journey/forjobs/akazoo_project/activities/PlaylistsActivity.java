package journey.forjobs.akazoo_project.activities;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.controllers.AkazooController;
import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.PlaylistContentProvider;
import journey.forjobs.akazoo_project.fragments.PlaylistsFragment;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.listAdapters.PlaylistListAdapter;
import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.model.Track;
import journey.forjobs.akazoo_project.rest.RestClient;
import journey.forjobs.akazoo_project.rest.pojos.GetPlaylistsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistsActivity extends AkazooActivity {

    @InjectView(R.id.playlists_list)
    ListView mPlaylistsList;

    AkazooController mService;
    boolean status;

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
        setContentView(R.layout.activity_playlists);
        ButterKnife.inject(this);

        Intent intent = new Intent(this, AkazooController.class);
        bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);

    }

    protected void showSnackbar(String message){
        Snackbar mSnackBar = Snackbar.make(findViewById(R.id.root), message, Snackbar.LENGTH_LONG);
        mSnackBar.show();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AkazooController.LocalBinder binder = (AkazooController.LocalBinder) service;
            mService = binder.getServerInstance();
            status = true;

            mService.fetchPlaylists(new AkazooController.PlaylistsComplection() {
                @Override
                public void onResponse() {
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
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            status = false;
        }
    };

    public ArrayList<Playlist> fetchPlaylistsFromDB(){

        ArrayList<Playlist> playlists = new ArrayList<Playlist>();
            Cursor mCursor;

            String[] mProjection = {
                    DBTableHelper.COLUMN_PLAYLISTS_PLAYLIST_ID,
                    DBTableHelper.COLUMN_PLAYLISTS_NAME,
                    DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT
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

                    playlist.setName(playlistNameRetrievedFromDatabase);

                    playlist.setPlaylistId(playlistIdRetrievedFromDatabase);

                    playlist.setItemCount(tracksCountRetrievedFromDatabase);

                    playlists.add(playlist);

                }
            }

        getContentResolver().delete(PlaylistContentProvider.CONTENT_URI, null, null);

        return playlists;

    }

}
