package c.proyecto.api;


import android.graphics.Bitmap;
import android.util.Base64;

import java.io.File;
import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
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
        Call<ImgurResponse> uploadImage(@Body() RequestBody file);
    }

    private ImgurAPI(){
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(); // Para que se muestre el log de todas las peticiones.
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request();
                Request newRequest;
                newRequest = request.newBuilder().addHeader("Authorization", "Client-ID a44a206a3f1f4b3").build();
                return chain.proceed(newRequest);
            }
        });
        b.addInterceptor(logInterceptor);
        OkHttpClient client = b.build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build();





        //Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
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
