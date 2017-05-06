package journey.forjobs.akazoo_project.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.listadapters.TracksListAdapter;
import journey.forjobs.akazoo_project.model.Track;

public class TracksActivity extends AkazooActivity {

    private MyMessageReceiver mMessageReceiver = new MyMessageReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
        }
    };

    @InjectView(R.id.tracks_list)
    ListView mTracksList;

    @Override
    protected MyMessageReceiver getmMessageReceiver() {
        return mMessageReceiver;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_tracks);
        ButterKnife.inject(this);
        ArrayList<Track> allTracks = new ArrayList<Track>();

        //TODO use controller to get tracks for track id "768f797f-ce35-4515-b6d8-56dcf91b8253"

        //TODO query database to get tracks and show them in the list (look Playlist Activity how to query database but query tracks array)

/*        for (int i =0; i < 20 ; i++) {
            Track track = new Track();
            track.setArtistName("artist" + i);
            track.setTrackName("track" + i);
            track.setTrackId(i);
            allTracks.add(track);
        }*/
        final TracksListAdapter mTracksListAdapter = new TracksListAdapter(this, allTracks);
        mTracksList.setAdapter(mTracksListAdapter);

        mTracksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showSnackbar("You have clicked on: " + mTracksListAdapter.getTracks().
                        get(position).getTrackName());
            }

        });


    }


    protected void showSnackbar(String message) {
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.root),
                message, Snackbar.LENGTH_LONG);
        mySnackbar.show();
    }
}
