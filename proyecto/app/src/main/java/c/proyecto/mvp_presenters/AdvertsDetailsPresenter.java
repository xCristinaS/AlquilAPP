package c.proyecto.mvp_presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.DetallesAnuncioActivity;
import c.proyecto.mvp_presenters_interfaces.AdvertsDetailsPresenterOps;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.pojo.Anuncio;
import c.proyecto.mvp_models.Usuario;

/**
 * Created by Cristina on 23/03/2016.
 */
public class AdvertsDetailsPresenter implements AdvertsDetailsPresenterOps, MyPresenter {

    private static WeakReference<DetallesAnuncioActivity> activity;
    private static AdvertsDetailsPresenter presentador;

    private AdvertsDetailsPresenter(Activity activity) {
        this.activity = new WeakReference<>((DetallesAnuncioActivity) activity);
    }

    public static AdvertsDetailsPresenter getPresentador(Activity a) {
        if (presentador == null)
            presentador = new AdvertsDetailsPresenter(a);
        else
            activity = new WeakReference<>((DetallesAnuncioActivity) a);
        return presentador;
    }

    @Override
    public void advertPublisherRequested(String anunciante) {
        Usuario.getAdvertPublisher(anunciante, this);
    }

    @Override
    public void onAdvertPublisherRequestedResponsed(Usuario u) {
        if (activity.get() != null)
            activity.get().onAdvertPublisherRequestedResponsed(u);
    }

    @Override
    public void updateAdvert(Anuncio anuncio) {
        if (activity.get() != null)
            activity.get().updateAdvert(anuncio);
    }
}
