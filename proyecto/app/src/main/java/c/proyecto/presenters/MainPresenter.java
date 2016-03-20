package c.proyecto.presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import c.proyecto.activities.MainActivity;
import c.proyecto.interfaces.MainPresenterOps;
import c.proyecto.models.Anuncio;

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
    public ArrayList<Anuncio> getAdverts() {
        return null;
    }

    @Override
    public ArrayList<Anuncio> getAllUserSubs(String userEmail) {
        return null;
    }

    @Override
    public ArrayList<Anuncio> getAllUserPublishAdverts(String userEmail) {
        return null;
    }
}
