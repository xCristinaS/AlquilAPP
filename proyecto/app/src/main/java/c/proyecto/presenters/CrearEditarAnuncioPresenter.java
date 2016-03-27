package c.proyecto.presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.CrearAnuncio2Activity;
import c.proyecto.interfaces.CrearEditarAnuncioPresenterOps;
import c.proyecto.models.Anuncio;

public class CrearEditarAnuncioPresenter implements CrearEditarAnuncioPresenterOps{

    private static WeakReference<CrearAnuncio2Activity> activity;
    private static CrearEditarAnuncioPresenter presentador;

    private CrearEditarAnuncioPresenter(Activity activity) {
        this.activity = new WeakReference<>((CrearAnuncio2Activity) activity);
    }

    public static CrearEditarAnuncioPresenter getPresentador(Activity a) {
        if (presentador == null)
            presentador = new CrearEditarAnuncioPresenter(a);
        else
            activity = new WeakReference<>((CrearAnuncio2Activity) a);
        return presentador;
    }

    @Override
    public void publishNewAdvert(Anuncio anuncio) {
        Anuncio.publishNewAdvert(anuncio);
    }
}
