package c.proyecto.api;

import com.firebase.client.Firebase;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import c.proyecto.Constantes;
import c.proyecto.interfaces.MyModel;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_presenters.RegistroPresenter;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Usuario;
import c.proyecto.mvp_presenters.AdvertsDetailsPresenter;
import c.proyecto.mvp_presenters.CrearEditarAnuncioPresenter;
import c.proyecto.mvp_presenters.ProfilePresenter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ImgurUploader {


    private final LinkedList<File> mImagenes;
    private final MyModel mModel;
    private final List<MyPresenter> mPresenters;
    private boolean mainImg;

    public ImgurUploader(LinkedList<File> imagenes, MyModel model, List<MyPresenter> presenters){
        mImagenes = imagenes;
        mModel = model;
        mPresenters = presenters;
        mainImg = true;
    }

    public ImgurUploader(File img, MyModel model, List<MyPresenter> presenters){
        mImagenes = new LinkedList<>();
        mImagenes.add(img);
        mModel = model;
        mPresenters = presenters;
    }

    public void upload(){
        if (mImagenes.size() > 0)
           subirImagen(mImagenes.getFirst());
    }

    //Sube las imagenes a la Api Imgur y guarda las url que den como resultado en el objeto Anuncio.
    private void subirImagen(File file) {
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);

        Call<ImgurResponse> llamada = ImgurAPI.getMInstance().getService().uploadImage(body);
        llamada.enqueue(new Callback<ImgurResponse>() {
            @Override
            public void onResponse(Call<ImgurResponse> call, Response<ImgurResponse> response) {
                ImgurResponse respuesta = response.body();
                //Se añade la urls del bitmap escogido
                if (respuesta != null)
                    if (mModel instanceof Anuncio && mPresenters.get(0) instanceof CrearEditarAnuncioPresenter) {
                        ((Anuncio) mModel).getImagenes().put(mainImg ? Constantes.FOTO_PRINCIPAL : "foto" + respuesta.getData().getLink().hashCode(), respuesta.getData().getLink());
                        ((CrearEditarAnuncioPresenter) mPresenters.get(0)).publishNewAdvert((Anuncio) mModel);
                        if (mPresenters.size() > 1 && mPresenters.get(1) instanceof AdvertsDetailsPresenter)
                            ((AdvertsDetailsPresenter) mPresenters.get(1)).updateAdvert((Anuncio) mModel);
                    } else if (mModel instanceof Usuario && mPresenters.get(0) instanceof ProfilePresenter) {
                        ((Usuario) mModel).setFoto(respuesta.getData().getLink());
                        ((ProfilePresenter) mPresenters.get(0)).updateUserProfile((Usuario) mModel);
                    } else if (mModel instanceof Usuario && mPresenters.get(0) instanceof RegistroPresenter){
                        ((Usuario) mModel).setFoto(respuesta.getData().getLink());
                        Firebase mFirebase = new Firebase(Constantes.URL_USERS + ((Usuario) mModel).getKey() + "/");
                        mFirebase.setValue(mModel);
                    }
                mainImg = false;
                mImagenes.removeFirst();
                upload();
            }

            @Override
            public void onFailure(Call<ImgurResponse> call, Throwable t) {

            }
        });
    }
}

