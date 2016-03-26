package c.proyecto.api;


import android.graphics.Bitmap;
import android.util.Base64;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public class ImgurAPI {

    private final String BASE_URL = "https://api.imgur.com/";
    private final String API_KEY = "ProyectoFinCurso";
    private static ImgurAPI mInstance;
    private final RetrofitInterface service;

    public interface RetrofitInterface{

        @POST("3/upload")
        Call<ImgurResponse> uploadImage(@Header("Authorization") String auth,
                                        @Body() RequestBody file);
    }

    private ImgurAPI(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(RetrofitInterface.class);
    }

    public static synchronized ImgurAPI getMInstance(){
        if(mInstance == null)
            mInstance = new ImgurAPI();
        return mInstance;
    }

    public RetrofitInterface getService(){
        return service;
    }
}
