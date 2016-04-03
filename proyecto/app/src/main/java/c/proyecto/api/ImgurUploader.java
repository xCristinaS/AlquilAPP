package c.proyecto.api;

import android.os.AsyncTask;

import java.io.File;
import java.util.ArrayList;

import c.proyecto.interfaces.MyModel;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;
import c.proyecto.presenters.CrearEditarAnuncioPresenter;
import c.proyecto.presenters.EditProfilePresenter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ImgurUploader {
    //Sube las imagenes a la Api Imgur y guarda las url que den como resultado en el objeto Anuncio.
    public static void subirImagen(File file, final MyModel model, final MyPresenter presenter) {
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);

        Call<ImgurResponse> llamada = ImgurAPI.getMInstance().getService().uploadImage(body);
        llamada.enqueue(new Callback<ImgurResponse>() {
            @Override
            public void onResponse(Call<ImgurResponse> call, Response<ImgurResponse> response) {
                ImgurResponse respuesta = response.body();
                //Se añade la urls del bitmap escogido
                if (model instanceof Anuncio && presenter instanceof CrearEditarAnuncioPresenter) {
                    ((Anuncio) model).getImagenes().add(respuesta.getData().getLink());
                    ((CrearEditarAnuncioPresenter)presenter).publishNewAdvert((Anuncio) model);
                } else if (model instanceof Usuario && presenter instanceof EditProfilePresenter){
                    ((Usuario) model).setFoto(respuesta.getData().getLink());
                    ((EditProfilePresenter)presenter).updateUserProfile((Usuario) model);
                }
            }

            @Override
            public void onFailure(Call<ImgurResponse> call, Throwable t) {
                
            }
        });
    }
}

