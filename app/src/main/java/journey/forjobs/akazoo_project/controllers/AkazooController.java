package journey.forjobs.akazoo_project.controllers;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.PlaylistContentProvider;
import journey.forjobs.akazoo_project.database.TracksContentProvider;
import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.model.Track;
import journey.forjobs.akazoo_project.rest.RestCallback;
import journey.forjobs.akazoo_project.rest.RestClient;
import journey.forjobs.akazoo_project.rest.pojos.GetPlaylistsResponse;
import journey.forjobs.akazoo_project.rest.pojos.GetTracksResponse;
import journey.forjobs.akazoo_project.utils.Const;
import retrofit2.Call;

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

    private void sendSucessfullBroadcastMessage(String message) {
        Intent intent = new Intent(Const.CONTROLLER_SUCCESSFULL_CALLBACK);
        intent.putExtra(Const.CONTROLLER_SUCCESSFULL_CALLBACK_MESSAGE, message);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void sendFailureBroadcastMessage(String message) {
        Intent intent = new Intent(Const.CONTROLLER_FAILURE_CALLBACK);
        intent.putExtra(Const.CONTROLLER_FAILURE_CALLBACK_MESSAGE, message);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public void getPlaylists() {
        Call<GetPlaylistsResponse> call = RestClient.call().getPlaylists();
        call.enqueue(new ControllerRestCallback<GetPlaylistsResponse>() {
            @Override
            public void handleSuccess(GetPlaylistsResponse response) {
                super.handleSuccess(response);
                if (response.getResult() != null && response.getResult().size() > 0) {
                    getContentResolver().delete(PlaylistContentProvider.CONTENT_URI, null, null);
                    ContentValues values = new ContentValues();
                    for (Playlist pl : response.getResult()) {
                        values.put(DBTableHelper.COLUMN_PLAYLISTS_NAME, pl.getName());
                        values.put(DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT, pl.getItemCount());
                        values.put(DBTableHelper.COLUMN_PLAYLISTS_PLAYLIST_ID, pl.getPlaylistId());
                        getContentResolver().insert(
                                PlaylistContentProvider.CONTENT_URI, values);
                    }

                    sendSucessfullBroadcastMessage(Const.REST_PLAYLISTS_SUCCESS);
                } else
                    handleFailure(10, getString(R.string.playlists_error_message));
            }

            @Override
            public void handleFailure(int statusCode, String statusMessage) {
                super.handleFailure(statusCode, statusMessage);
                sendSucessfullBroadcastMessage(Const.REST_PLAYLISTS_FAIL);
            }
        });
    }

    public void getTracks(String playlistId) {
        Call<GetTracksResponse> call = RestClient.call().getTracks(playlistId);
        call.enqueue(new ControllerRestCallback<GetTracksResponse>() {
            @Override
            public void handleSuccess(GetTracksResponse response) {
                super.handleSuccess(response);
                if (response.getResult() != null && response.getResult().getItems() != null && response.getResult().getItems().size() > 0) {
                    getContentResolver().delete(TracksContentProvider.CONTENT_URI, null, null);
                    ContentValues values = new ContentValues();
                    for (Track tr : response.getResult().getItems()) {
                        values.put(DBTableHelper.COLUMN_TRACK_NAME, tr.getTrackName());
                        values.put(DBTableHelper.COLUMN_TRACK_ARTIST, tr.getArtistName());
                        values.put(DBTableHelper.COLUMN_TRACK_IMAGE_URL, tr.getImageUrl());
                        getContentResolver().insert(
                                TracksContentProvider.CONTENT_URI, values);
                    }

                    sendSucessfullBroadcastMessage(Const.REST_TRACKS_SUCCESS);
                } else {
                    handleFailure(11, getString(R.string.tracks_refresh_failed));
                }
            }

            @Override
            public void handleFailure(int statusCode, String statusMessage) {
                super.handleFailure(statusCode, statusMessage);
            }
        });

    }

    //

    public abstract class ControllerRestCallback<T> extends RestCallback<T> {

        @Override
        public void handleSuccess(T response) {
        }

        @Override
        public void handleFailure(int statusCode, String statusMessage) {
            sendFailureBroadcastMessage(statusMessage);
        }

        @Override
        public void handleException() {
            sendFailureBroadcastMessage(Const.REST_SERVICE_DOWN);
        }
    }

}