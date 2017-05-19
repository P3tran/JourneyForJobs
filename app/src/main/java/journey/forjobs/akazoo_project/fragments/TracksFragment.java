package journey.forjobs.akazoo_project.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.TracksContentProvider;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.listAdapters.TracksListAdapter;
import journey.forjobs.akazoo_project.model.Track;
import journey.forjobs.akazoo_project.utils.Const;


public class TracksFragment extends Fragment implements android.app.LoaderManager.LoaderCallbacks<Cursor>{

    @InjectView(R.id.tracks_list)
    ListView mTracksList;

    @InjectView(R.id.tv_playlist)
    TextView mTextView;

    TracksListAdapter mTracksListAdapter;
    ArrayList<Track> tracks = new ArrayList<Track>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tracks, container, false);
        ButterKnife.inject(this, v);


        Intent intent = getActivity().getIntent();
        mTextView.setText(intent.getStringExtra("name"));

        setupTracksListAdapter();

        getLoaderManager().initLoader(0,null,this);

        return v;

    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {


        String[] mProjection = {
                DBTableHelper.COLUMN_TRACKS_TRACK_ID,
                DBTableHelper.COLUMN_TRACKS_NAME,
                DBTableHelper.COLUMN_ARTIST_NAME,
                DBTableHelper.COLUMN_TRACKS_ID,
                DBTableHelper.COLUMN_TRACKS_PHOTO_URL
        };

        if (id == 0) {
            return new android.content.CursorLoader(getActivity(), TracksContentProvider.CONTENT_URI, mProjection, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor mCursor) {

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
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        tracks.clear();
        mTracksListAdapter.notifyDataSetChanged();
    }

    public void setupTracksListAdapter(){
        mTracksListAdapter = new TracksListAdapter(getActivity(), tracks);
        mTracksList.setAdapter(mTracksListAdapter);
    }
}

