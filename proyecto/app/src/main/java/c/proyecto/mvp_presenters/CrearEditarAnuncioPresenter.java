package c.proyecto.mvp_presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.CrearAnuncio2Activity;
import c.proyecto.mvp_models.AdvertsFirebaseManager;
import c.proyecto.mvp_presenters_interfaces.CrearEditarAnuncioPresenterOps;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.pojo.Anuncio;

public class CrearEditarAnuncioPresenter implements CrearEditarAnuncioPresenterOps, MyPresenter {

    private static CrearEditarAnuncioPresenter presentador;

    private AdvertsFirebaseManager advertsManager;

    private CrearEditarAnuncioPresenter() {
    }

    public void setAdvertsManager(AdvertsFirebaseManager advertsManager) {
        this.advertsManager = advertsManager;
    }

    public static CrearEditarAnuncioPresenter getPresentador() {
        if (presentador == null)
            presentador = new CrearEditarAnuncioPresenter();
        return presentador;
    }

    @Override
    public void publishNewAdvert(Anuncio anuncio) {
        advertsManager.publishNewAdvert(anuncio);
    }

    @Override
    public void liberarMemoria() {
        presentador = null;
    }
}
