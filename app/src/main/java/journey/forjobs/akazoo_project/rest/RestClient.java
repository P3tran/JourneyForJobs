package journey.forjobs.akazoo_project.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import journey.forjobs.akazoo_project.utils.Const;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Petros Efthymiou on 22/7/2016.
 */
public class RestClient {
    private static RestAPI REST_API;

    static {
        setupRestClient();
    }


    public static RestAPI call() {
        return REST_API;
    }

    private static void setupRestClient() {

        //setup OkHttpCLient3



        //


        //setup Retrofit


        //


        //REST_API = retorift.create(RestAPI.class);
    }


}


