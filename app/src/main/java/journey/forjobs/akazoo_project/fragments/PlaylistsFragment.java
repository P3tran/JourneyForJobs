package journey.forjobs.akazoo_project.fragments;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.activities.AkazooActivity;
import journey.forjobs.akazoo_project.activities.PlaylistsActivity;
import journey.forjobs.akazoo_project.activities.TracksActivity;
import journey.forjobs.akazoo_project.application.AkazooApplication;
import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.PlaylistContentProvider;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.listAdapters.PlaylistListAdapter;
import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.utils.Const;


public class PlaylistsFragment extends Fragment implements android.app.LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.playlists_list)
    ListView mPlaylistsList;

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeContainer;

    PlaylistListAdapter mPlaylistListAdapter;
    ArrayList<Playlist> playlists = new ArrayList<Playlist>();

    TextView mTextView;
    String transitionName;

    private boolean fetchStatus = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlists, container, false);
        ButterKnife.inject(this, v);

        setUpPlaylistsListAdapter();

        getLoaderManager().initLoader(1, null,this);

        setHasOptionsMenu(true);

        mSwipeContainer.setOnRefreshListener(this);

        return v;
    }


    public void setUpPlaylistsListAdapter(){

        mPlaylistListAdapter = new PlaylistListAdapter(getActivity(), playlists);
        mPlaylistsList.setAdapter(mPlaylistListAdapter);
        mPlaylistsList.setOnItemClickListener(this);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        AkazooActivity activity = (AkazooActivity) getActivity();
        AkazooApplication application = (AkazooApplication) activity.getApplication();
        application.getmController().fetchTracks(mPlaylistListAdapter.getItem(position).getPlaylistId());

        Intent intent = new Intent(getActivity(), TracksActivity.class);

        transitionName = mPlaylistListAdapter.getItem(position).getName();
        intent.putExtra("name", transitionName);

        mTextView = (TextView) view.findViewById(R.id.playlist_name);

        Pair<View, String> pr = Pair.create((View) mTextView, getString(R.string.transition_playlist_name));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pr);

        getActivity().startActivity(intent, options.toBundle());

    }

    @Override
    public void onRefresh() {
        AkazooApplication ap = (AkazooApplication) getActivity().getApplicationContext();
        ap.getmController().fetchPlaylists(false);
    }
}

