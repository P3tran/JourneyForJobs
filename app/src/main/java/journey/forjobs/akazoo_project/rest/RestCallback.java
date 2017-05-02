package journey.forjobs.akazoo_project.rest;

import android.util.Log;

import java.util.List;

import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.utils.Const;
import retrofit2.Callback;

/**
 * Created by petros on 11/6/2015.
 */
public abstract class RestCallback<T> implements Callback<T> {

    RestCallback() {

    }
}
