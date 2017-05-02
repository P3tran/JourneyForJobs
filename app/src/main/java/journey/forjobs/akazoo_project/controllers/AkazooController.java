package journey.forjobs.akazoo_project.controllers;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;

import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.model.Track;
import journey.forjobs.akazoo_project.rest.RestAPI;
import journey.forjobs.akazoo_project.rest.RestClient;
import journey.forjobs.akazoo_project.rest.pojos.GetPlaylistsResponse;
import journey.forjobs.akazoo_project.rest.pojos.GetTracksResponse;
import journey.forjobs.akazoo_project.utils.Const;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AkazooController extends Service {
    IBinder mBinder = new LocalBinder();

    public AkazooController() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class LocalBinder extends Binder {
        public AkazooController getServerInstance() {
            return AkazooController.this;
        }
    }

    private void sendBroadcastMessage(String message) {
        Intent intent = new Intent(Const.CONTROLLER_SUCCESSFULL_CALLBACK);
        intent.putExtra(Const.CONTROLLER_SUCCESSFULL_CALLBACK_MESSAGE, message);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public void fetchPlaylists(final PlaylistsComplection complection){

        Call<GetPlaylistsResponse> call = RestClient.call().getPlaylist();
        call.enqueue(new Callback<GetPlaylistsResponse>() {
            @Override
            public void onResponse(Call<GetPlaylistsResponse> call, final Response<GetPlaylistsResponse> response) {
                if(response.isSuccessful()){
                    final ArrayList<Playlist> playlists = response.body().getResult();
                    complection.onResponse(playlists);
                }
            }

            @Override
            public void onFailure(Call<GetPlaylistsResponse> call, Throwable t) {
                Log.d("Error", t.getLocalizedMessage());
            }
        });
    }

    public void fetchTracks(String id , final TracksComplection complection){

        Call<GetTracksResponse> call = RestClient.call().getTracks(id);
        call.enqueue(new Callback<GetTracksResponse>() {
            @Override
            public void onResponse(Call<GetTracksResponse> call, Response<GetTracksResponse> response) {
                if(response.isSuccessful()){
                    final Playlist playlist = response.body().getResult();
                    final ArrayList<Track> tracks = playlist.getItems();
                    complection.onResponse(tracks);
                }
            }

            @Override
            public void onFailure(Call<GetTracksResponse> call, Throwable t) {
                Log.d("Error", t.getLocalizedMessage());
            }
        });

    }


    public interface TracksComplection {
        void onResponse(ArrayList<Track> tracks);
    }

    public interface PlaylistsComplection {
        void onResponse(ArrayList<Playlist> playlists);
    }

}