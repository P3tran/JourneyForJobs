package journey.forjobs.akazoo_project.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

        Intent intent = getIntent();
        String id  = intent.getExtras().getString("id");

        Call<GetTracksResponse> call = RestClient.call().getTracks(id);
        call.enqueue(new Callback<GetTracksResponse>() {
            @Override
            public void onResponse(Call<GetTracksResponse> call, Response<GetTracksResponse> response) {
                if(response.isSuccessful()){
                    final Playlist playlist = response.body().getResult();
                    final ArrayList<Track> tracks = playlist.getItems();

                    final TracksListAdapter mTracksListAdapter = new TracksListAdapter(TracksActivity.this, tracks);
                    mTracksList.setAdapter(mTracksListAdapter);
                }
            }

            @Override
            public void onFailure(Call<GetTracksResponse> call, Throwable t) {
                Log.d("Error", t.getLocalizedMessage());
            }
        });

    }

    protected void showSnackBar(String message){
        Snackbar mSnackBar = Snackbar.make(findViewById(R.id.root), message, Snackbar.LENGTH_LONG);
        mSnackBar.show();
    }

}
