package c.proyecto.api;

import java.io.File;
import java.util.List;

import c.proyecto.Constantes;
import c.proyecto.interfaces.MyModel;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Usuario;
import c.proyecto.mvp_presenters.AdvertsDetailsPresenter;
import c.proyecto.mvp_presenters.CrearEditarAnuncioPresenter;
import c.proyecto.mvp_presenters.EditProfilePresenter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ImgurUploader {
    //Sube las imagenes a la Api Imgur y guarda las url que den como resultado en el objeto Anuncio.
    public static void subirImagen(File file, final MyModel model, final List<MyPresenter> presenter, final boolean mainImage) {
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);

        Call<ImgurResponse> llamada = ImgurAPI.getMInstance().getService().uploadImage(body);
        llamada.enqueue(new Callback<ImgurResponse>() {
            @Override
            public void onResponse(Call<ImgurResponse> call, Response<ImgurResponse> response) {
                ImgurResponse respuesta = response.body();
                //Se añade la urls del bitmap escogido
                if (model instanceof Anuncio && presenter.get(0) instanceof CrearEditarAnuncioPresenter) {
                    ((Anuncio) model).getImagenes().put(mainImage? Constantes.FOTO_PRINCIPAL:"foto"+respuesta.getData().getLink().hashCode(), respuesta.getData().getLink());
                    ((CrearEditarAnuncioPresenter)presenter.get(0)).publishNewAdvert((Anuncio) model);
                    if (presenter.size() > 1 && presenter.get(1) instanceof AdvertsDetailsPresenter)
                        ((AdvertsDetailsPresenter) presenter.get(1)).updateAdvert((Anuncio) model);
                } else if (model instanceof Usuario && presenter.get(0) instanceof EditProfilePresenter){
                    ((Usuario) model).setFoto(respuesta.getData().getLink());
                    ((EditProfilePresenter)presenter.get(0)).updateUserProfile((Usuario) model);
                }
            }

            @Override
            public void onFailure(Call<ImgurResponse> call, Throwable t) {

            }
        });
    }
}

