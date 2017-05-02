package journey.forjobs.akazoo_project.rest.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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

