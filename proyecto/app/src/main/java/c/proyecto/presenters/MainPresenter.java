package c.proyecto.presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import c.proyecto.activities.MainActivity;
import c.proyecto.interfaces.MainPresenterOps;
import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;

/**
 * Created by Cristina on 20/03/2016.
 */
public class MainPresenter implements MainPresenterOps {

    private static WeakReference<MainActivity> activity;
    private static MainPresenter presentador;

    private MainPresenter(Activity activity){
        this.activity = new WeakReference<>((MainActivity)activity);
    }

    public static MainPresenter getPresentador(Activity a){
        if (presentador == null)
            presentador = new MainPresenter(a);
        else
            activity = new WeakReference<>((MainActivity)a);
        return presentador;
    }

    @Override
    public void getAdverts() {
        Anuncio.getAdverts(this);
    }

    @Override
    public void getAllUserSubs(Usuario usuario) {

    }

    @Override
    public void getAllUserPublishAdverts(Usuario usuario) {

    }

    @Override
    public void onAdvertsRequestedResponsed(ArrayList<Anuncio> anuncios) {
        activity.get().onAdvertsRequestedResponsed(anuncios);
    }
}
