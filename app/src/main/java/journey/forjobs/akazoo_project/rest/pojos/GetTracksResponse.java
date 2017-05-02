package journey.forjobs.akazoo_project.rest.pojos;

import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.model.Track;

/**
 * Created by Petros Efthymiou on 22/7/2016.
 */
public class GetTracksResponse extends GenericResponse {
    private Playlist Result;

    public Playlist getResult() {
        return Result;
    }

    public void setResult(Playlist result) {
        this.Result = result;
    }
}

