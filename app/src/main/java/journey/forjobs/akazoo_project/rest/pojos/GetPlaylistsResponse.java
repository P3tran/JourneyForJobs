package journey.forjobs.akazoo_project.rest.pojos;

import java.util.ArrayList;

import journey.forjobs.akazoo_project.model.Playlist;

/**
 * Created by Petros Efthymiou on 22/7/2016.
 */
public class GetPlaylistsResponse extends GenericResponse{

    private ArrayList<Playlist> Result;

    public ArrayList<Playlist> getResult() {
        return Result;
    }

    public void setResult(ArrayList<Playlist> result) {
        Result = result;
    }
}
