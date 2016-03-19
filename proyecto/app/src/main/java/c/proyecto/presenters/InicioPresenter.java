package c.proyecto.presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.interfaces.InicioPresenterOps;
import c.proyecto.models.Usuario;


/**
 * Created by Cristina on 19/03/2016.
 */
public class InicioPresenter implements InicioPresenterOps {

    private WeakReference<Activity> activity;
    private static InicioPresenter presentador;

    private InicioPresenter(Activity activity){
        this.activity = new WeakReference<>(activity);
    }

    public static InicioPresenter getPresentador(Activity activity){
        if (presentador == null)
            presentador = new InicioPresenter(activity);
        return presentador;
    }

    @Override
    public boolean createNewUser() {
        return Usuario.createNewUser();
    }

    @Override
    public boolean singInRequested() {
        return false;
    }
}
