package journey.forjobs.akazoo_project.rest;

import android.util.Log;

import java.util.List;

import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.application.AkazooApplication;
import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.rest.pojos.GenericResponse;
import journey.forjobs.akazoo_project.utils.Const;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by petros on 11/6/2015.
 */
public abstract class RestCallback<T> implements Callback<T> {

    public RestCallback() {

    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        handleExeption();
        t.printStackTrace();
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.i(Const.LOG_REST_TAG, "Generic success");
        T responseContent = (T) response.body();
        GenericResponse castingResponse = (GenericResponse) responseContent;
        if (castingResponse.isError() || castingResponse == null){
            handleFailure(castingResponse.getErrorId(), AkazooApplication.getmInstance().getResources().getString(R.string.generic_rest_error_message));
        }else {
            handleSuccess(responseContent);
        }
    }

    public abstract void handleExeption();

    public abstract void handleSuccess(T response);

    public abstract void handleFailure(int statusCode,String statusMessage);
}
