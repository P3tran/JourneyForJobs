package journey.forjobs.akazoo_project.rest;


import journey.forjobs.akazoo_project.rest.pojos.GetPlaylistsResponse;
import journey.forjobs.akazoo_project.rest.pojos.GetTracksResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestAPI {

    @GET("TestMobileService.svc/playlists")
    Call<GetPlaylistsResponse> getPlaylists();

    @GET("TestMobileService.svc/playlist")
    Call<GetTracksResponse> getTracks(@Query("playlistid") String playlistid);

}