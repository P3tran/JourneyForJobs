package journey.forjobs.akazoo_project.rest;

import android.util.Log;

import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.application.AkazooApplication;
import journey.forjobs.akazoo_project.rest.pojos.GenericResponse;
import journey.forjobs.akazoo_project.utils.Const;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RestCallback<T> implements Callback<T> {

    public RestCallback() {

    }

    /**
     * Handling of exceptions. This forwards the actual exception to
     * handleException method.
     */
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        t.printStackTrace();
        handleException();
    }

    /**
     * Handling of successful response (might contain business errors though).
     * Checks the generic attributes of the response.
     */
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.i(Const.LOG_REST_TAG, "Generic success");
        T responseContent = (T) response.body();
        GenericResponse castedResponse = (GenericResponse) responseContent;
        if(response == null ||  castedResponse.isError())
            handleFailure(castedResponse.getErrorId(), AkazooApplication.getInstance().getResources().getString(R.string.generic_rest_error_message));
        else
            handleSuccess(responseContent);
    }

    /**
     * Implementors will handle the successful response using this method.
     *
     * @param response
     */
    public abstract void handleSuccess(T response);

    /**
     * Implementors will handle business errors using this method.
     *
     * @param statusCode
     * @param statusMessage
     */
    public abstract void handleFailure(int statusCode, String statusMessage);

    /**
     * Implementors will handle exceptions using this method.
     *
     *
     */
    public abstract void handleException();

}
