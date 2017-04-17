package journey.forjobs.akazoo_project.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.PlaylistContentProvider;
import journey.forjobs.akazoo_project.fragments.PlaylistsFragment;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.model.Playlist;

public class PlaylistsActivity extends AkazooActivity {

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

        //dummy code to test the database
        {
            //insert playlist 1 into databse
            ContentValues values = new ContentValues();
            values.put(DBTableHelper.COLUMN_PLAYLISTS_PLAYLIST_ID, "id1");
            values.put(DBTableHelper.COLUMN_PLAYLISTS_NAME, "playlist1");
            values.put(DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT, 22);
            getContentResolver().insert(
                    PlaylistContentProvider.CONTENT_URI, values);

            //insert playlist 2 into database
            ContentValues values2 = new ContentValues();
            values2.put(DBTableHelper.COLUMN_PLAYLISTS_PLAYLIST_ID, "id2");
            values2.put(DBTableHelper.COLUMN_PLAYLISTS_NAME, "playlist2");
            values2.put(DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT, 66);
            getContentResolver().insert(
                    PlaylistContentProvider.CONTENT_URI, values2);

            //retrieve info from the database
            {
            Cursor mCursor;

            //used to know which columns name we need to retrieve
            String[] mProjection = {
                    DBTableHelper.COLUMN_PLAYLISTS_PLAYLIST_ID,
                    DBTableHelper.COLUMN_PLAYLISTS_NAME,
                    DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT
            };

            //queries the database
            mCursor = getContentResolver().query(
                    PlaylistContentProvider.CONTENT_URI,  // The content URI of the words table - You need to use TracksContentProvider.CONTENT_URI to test yours
                    mProjection,                       // The columns to return for each row
                    null,
                    null,
                    null);

            if (mCursor != null) {
                while (mCursor.moveToNext()) {
                    //create empty object
                    Playlist playlist = new Playlist();

                    // Gets the playlist name from the dabase
                    String playlistNameRetrievedFromDatabase = mCursor.getString(mCursor.getColumnIndex(DBTableHelper.COLUMN_PLAYLISTS_NAME));

                    // Gets the track count from the column database
                    int tracksCountRetrievedFromDatabase = mCursor.getInt(mCursor.getColumnIndex(DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT));

                    //put the value retrieved from databse into our object
                    playlist.setName(playlistNameRetrievedFromDatabase);

                    //put the value retrieved from databse into our object
                    playlist.setItemCount(tracksCountRetrievedFromDatabase);


                    //display the information we retrieved in the logs
                    Log.d("MA TAG", "the playlist name is " + playlist.getName());
                    Log.d("MA TAG", "the playlist track count is  " + playlist.getItemCount());
                }
            }}

            //delete all values from database
            getContentResolver().delete(PlaylistContentProvider.CONTENT_URI, null, null);
        }
    }

}
