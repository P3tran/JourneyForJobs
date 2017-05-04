package journey.forjobs.akazoo_project.controllers;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;

import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.PlaylistContentProvider;
import journey.forjobs.akazoo_project.database.TracksContentProvider;
import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.model.Track;
import journey.forjobs.akazoo_project.rest.RestAPI;
import journey.forjobs.akazoo_project.rest.RestCallback;
import journey.forjobs.akazoo_project.rest.RestClient;
import journey.forjobs.akazoo_project.rest.pojos.GetPlaylistsResponse;
import journey.forjobs.akazoo_project.rest.pojos.GetTracksResponse;
import journey.forjobs.akazoo_project.utils.Const;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AkazooController extends Service {
    IBinder mBinder;

    public AkazooController() {
        mBinder = new LocalBinder();
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
        call.enqueue(new RestCallback<GetPlaylistsResponse>() {
            @Override
            public void handleExeption(Throwable exception) {
                exception.printStackTrace();
            }

            @Override
            public void handleSuccess(GetPlaylistsResponse response) {
                final ArrayList<Playlist> playlists = response.getResult();
                for (Playlist playlist: playlists){
                    ContentValues values = new ContentValues();
                    values.put(DBTableHelper.COLUMN_PLAYLISTS_PLAYLIST_ID, playlist.getPlaylistId());
                    values.put(DBTableHelper.COLUMN_PLAYLISTS_NAME, playlist.getName());
                    values.put(DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT, playlist.getItemCount());
                    getContentResolver().insert(PlaylistContentProvider.CONTENT_URI, values);
                }
                complection.onResponse();
            }

            @Override
            public void handleFailure(int statusCode, String statusMessage) {
                Log.d(String.valueOf(statusCode),statusMessage);
            }
        });
    }

    public void fetchTracks(String id , final TracksComplection complection){

        Call<GetTracksResponse> call = RestClient.call().getTracks(id);
        call.enqueue(new RestCallback<GetTracksResponse>() {
            @Override
            public void handleExeption(Throwable exception) {
                exception.printStackTrace();
            }

            @Override
            public void handleSuccess(GetTracksResponse response) {

                final Playlist playlist = response.getResult();
                final ArrayList<Track> tracks = playlist.getItems();

                complection.onResponse(tracks);

            }

            @Override
            public void handleFailure(int statusCode, String statusMessage) {
                Log.d(String.valueOf(statusCode),statusMessage);
            }
        });

    }

    public void test(){
        Log.d("APPLICATION", "TEST");
    }

    public interface TracksComplection {
        void onResponse(ArrayList<Track> complection);
    }

    public interface PlaylistsComplection {
        void onResponse();
    }

}