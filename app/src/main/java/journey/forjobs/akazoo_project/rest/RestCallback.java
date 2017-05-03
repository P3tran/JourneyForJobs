package journey.forjobs.akazoo_project.rest;

import android.util.Log;

import java.util.List;

import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.utils.Const;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by petros on 11/6/2015.
 */
public abstract class RestCallback<T> implements Callback<T> {

    RestCallback() {

    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        handleExeption(t);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.i(Const.LOG_REST_TAG, "Generic success");
        T responseContent = (T) response.body();
        handleSuccess(responseContent);
    }

    public abstract void handleExeption(Throwable exception);

    public abstract void handleSuccess(T response);

    public abstract void handleFailure(int statusCode,String statusMessage);
}
