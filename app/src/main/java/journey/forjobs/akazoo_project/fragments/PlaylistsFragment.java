package journey.forjobs.akazoo_project.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.activities.TracksActivity;
import journey.forjobs.akazoo_project.application.AkazooApplication;
import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.PlaylistContentProvider;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.listAdapters.PlaylistListAdapter;
import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.utils.Const;


public class PlaylistsFragment extends Fragment implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    @InjectView(R.id.playlists_list)
    ListView mPlaylistsList;

    @InjectView(R.id.pb_loader)
    ProgressBar mProgressBar;

    PlaylistListAdapter mPlaylistListAdapter;
    ArrayList<Playlist> playlists = new ArrayList<Playlist>();

    private boolean fetchStatus = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlists, container, false);
        ButterKnife.inject(this, v);

        mProgressBar.setVisibility(View.VISIBLE);
        setUpPlaylistsListAdapter();

        getLoaderManager().initLoader(1, null,this);

        setHasOptionsMenu(true);

        return v;
    }


    public void setUpPlaylistsListAdapter(){

        mPlaylistListAdapter = new PlaylistListAdapter(getActivity(), playlists);
        mPlaylistsList.setAdapter(mPlaylistListAdapter);
        mPlaylistsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), TracksActivity.class);
                intent.putExtra(Const.INTENT_SELECTED_PLAYLIST, mPlaylistListAdapter.getItem(position).getPlaylistId());
                startActivity(intent);

            }

        });
        mProgressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] mProjection = {
                DBTableHelper.COLUMN_PLAYLISTS_PLAYLIST_ID,
                DBTableHelper.COLUMN_PLAYLISTS_NAME,
                DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT,
                DBTableHelper.COLUMN_PLAYLISTS_PHOTO_URL
        };

        if (id == 1) {
            return new android.content.CursorLoader(getActivity(), PlaylistContentProvider.CONTENT_URI, mProjection, null, null, null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor mCursor) {

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
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        playlists.clear();
        mPlaylistListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_refresh:

                AkazooApplication ap = (AkazooApplication) getActivity().getApplicationContext();
                ap.getmController().fetchPlaylists();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}

