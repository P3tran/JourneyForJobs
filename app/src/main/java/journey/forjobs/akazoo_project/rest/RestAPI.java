package journey.forjobs.akazoo_project.rest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.model.Track;
import journey.forjobs.akazoo_project.rest.pojos.GetPlaylistsResponse;
import journey.forjobs.akazoo_project.rest.pojos.GetTracksResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface RestAPI {

//    @GET("/TestMobileService.svc/playlists")
//    void getPlaylist(RestCallback<GetPlaylistsResponse> callback);
//
//    @GET("/TestMobileService.svc/playlist")
//    void getTracks(@Query("playlistid") String playlistid, RestCallback<GetTracksResponse> callback);


    @GET("/services/Test/TestMobileService.svc/playlists")
    Call<GetPlaylistsResponse> getPlaylist();

    @GET("/services/Test/TestMobileService.svc/playlist")
    Call<GetTracksResponse> getTracks(@Query("playlistid") String playlistid);

}