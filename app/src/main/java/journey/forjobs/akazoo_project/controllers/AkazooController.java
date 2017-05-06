package journey.forjobs.akazoo_project.controllers;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import journey.forjobs.akazoo_project.R;
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

    private void sendSuccessfulBroadcastMessage(String message) {
        Intent intent = new Intent(Const.CONTROLLER_SUCCESSFULL_CALLBACK);
        intent.putExtra(Const.CONTROLLER_SUCCESSFULL_CALLBACK_MESSAGE, message);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void sendFailureBroadcastMessage(String message){
        Intent intent = new Intent(Const.CONTROLLER_FAILURE_CALLBACK);
        intent.putExtra(Const.CONTROLLER_FAILURE_CALLBACK_MESSAGE, message);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public void fetchPlaylists(){
        Call<GetPlaylistsResponse> call = RestClient.call().getPlaylist();

        call.enqueue(new ControllerRestCallback<GetPlaylistsResponse>() {

            @Override
            public void handleFailure(int statusCode, String statusMessage) {
                super.handleFailure(statusCode, statusMessage);
            }

            @Override
            public void handleSuccess(GetPlaylistsResponse response) {
                super.handleSuccess(response);

                if(response.getResult() != null && response.getResult().size() > 0){
                    getContentResolver().delete(PlaylistContentProvider.CONTENT_URI, null, null);
                    final ArrayList<Playlist> playlists = response.getResult();
                    for (Playlist playlist: playlists){
                        ContentValues values = new ContentValues();
                        values.put(DBTableHelper.COLUMN_PLAYLISTS_PLAYLIST_ID, playlist.getPlaylistId());
                        values.put(DBTableHelper.COLUMN_PLAYLISTS_NAME, playlist.getName());
                        values.put(DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT, playlist.getItemCount());
                        values.put(DBTableHelper.COLUMN_PLAYLISTS_PHOTO_URL, playlist.getPhotoUrl());
                        getContentResolver().insert(PlaylistContentProvider.CONTENT_URI, values);
                    }
                    sendSuccessfulBroadcastMessage(Const.REST_PLAYLISTS_SUCCESS);
                }else {
                    handleFailure(10, getString(R.string.playlists_error_message));
                }
            }
        });
    }

    public void fetchTracks(String id ){

        Call<GetTracksResponse> call = RestClient.call().getTracks(id);
        call.enqueue(new ControllerRestCallback<GetTracksResponse>() {

            @Override
            public void handleSuccess(GetTracksResponse response) {
                super.handleSuccess(response);
                if(response.getResult() != null && response.getResult().getItems().size() > 0){
                    getContentResolver().delete(TracksContentProvider.CONTENT_URI, null, null);
                    final ArrayList<Track> tracks = response.getResult().getItems();
                    for (Track track: tracks){
                        ContentValues values = new ContentValues();
                        values.put(DBTableHelper.COLUMN_TRACKS_TRACK_ID, track.getTrackId());
                        values.put(DBTableHelper.COLUMN_TRACKS_NAME, track.getTrackName());
                        values.put(DBTableHelper.COLUMN_ARTIST_NAME, track.getArtistName());
                        values.put(DBTableHelper.COLUMN_TRACKS_PHOTO_URL, track.getImageUrl());
                        getContentResolver().insert(TracksContentProvider.CONTENT_URI, values);
                    }

                    sendSuccessfulBroadcastMessage(Const.REST_TRACKS_SUCCESS);
                }else {
                    handleFailure(10, getString(R.string.tracks_error_message));
                }
            }

            @Override
            public void handleFailure(int statusCode, String statusMessage) {
                super.handleFailure(statusCode, statusMessage);
            }
        });

    }

    public abstract class ControllerRestCallback<T> extends RestCallback<T> {

        @Override
        public void handleExeption() {
            sendFailureBroadcastMessage(Const.REST_SERVICE_DOWN);
        }

        @Override
        public void handleFailure(int statusCode, String statusMessage) {
            sendFailureBroadcastMessage(statusMessage);
        }

        @Override
        public void handleSuccess(T response) {

        }
    }

}