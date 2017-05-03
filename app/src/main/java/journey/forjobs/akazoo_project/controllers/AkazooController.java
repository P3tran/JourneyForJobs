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

//        Call<GetTracksResponse> call = RestClient.call().getTracks(id);
//        call.enqueue(new Callback<GetTracksResponse>() {
//            @Override
//            public void onResponse(Call<GetTracksResponse> call, Response<GetTracksResponse> response) {
//                if(response.isSuccessful()){
//                    final Playlist playlist = response.body().getResult();
//                    final ArrayList<Track> tracks = playlist.getItems();
//
//                    for (Track track: tracks){
//                        ContentValues values = new ContentValues();
//                        values.put(DBTableHelper.COLUMN_TRACKS_TRACK_ID, track.getTrackId());
//                        values.put(DBTableHelper.COLUMN_TRACKS_NAME, track.getTrackName());
//                        values.put(DBTableHelper.COLUMN_ARTIST_NAME, track.getArtistName());
//                        getContentResolver().insert(
//                                TracksContentProvider.CONTENT_URI, values);
//                    }
//
//                    complection.onResponse();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetTracksResponse> call, Throwable t) {
//                Log.d("Error", t.getLocalizedMessage());
//            }
//        });

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

                    for (Track track: tracks){
                        ContentValues values = new ContentValues();
                        values.put(DBTableHelper.COLUMN_TRACKS_TRACK_ID, track.getTrackId());
                        values.put(DBTableHelper.COLUMN_TRACKS_NAME, track.getTrackName());
                        values.put(DBTableHelper.COLUMN_ARTIST_NAME, track.getArtistName());
                        getContentResolver().insert(
                                TracksContentProvider.CONTENT_URI, values);
                    }

                    complection.onResponse();

            }

            @Override
            public void handleFailure(int statusCode, String statusMessage) {
                Log.d(String.valueOf(statusCode),statusMessage);
            }
        });

    }


    public interface TracksComplection {
        void onResponse();
    }

    public interface PlaylistsComplection {
        void onResponse();
    }

}