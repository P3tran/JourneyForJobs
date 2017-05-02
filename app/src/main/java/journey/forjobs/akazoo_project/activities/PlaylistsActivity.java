package journey.forjobs.akazoo_project.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
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

        Call<GetPlaylistsResponse> call = RestClient.call().getPlaylist();
        call.enqueue(new Callback<GetPlaylistsResponse>() {
            @Override
            public void onResponse(Call<GetPlaylistsResponse> call, final Response<GetPlaylistsResponse> response) {
                if(response.isSuccessful()){
                    final ArrayList<Playlist> playlists = response.body().getResult();

                    final PlaylistListAdapter mPlaylistListAdapter = new PlaylistListAdapter(PlaylistsActivity.this, playlists);
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
            }

            @Override
            public void onFailure(Call<GetPlaylistsResponse> call, Throwable t) {
                Log.d("Error", t.getLocalizedMessage());
            }
        });

    }

    protected void showSnackbar(String message){
        Snackbar mSnackBar = Snackbar.make(findViewById(R.id.root), message, Snackbar.LENGTH_LONG);
        mSnackBar.show();
    }

}
