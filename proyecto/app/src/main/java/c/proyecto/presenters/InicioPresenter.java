package c.proyecto.presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.InicioActivity;
import c.proyecto.interfaces.InicioPresenterOps;
import c.proyecto.models.Usuario;


/**
 * Created by Cristina on 19/03/2016.
 */
public class InicioPresenter implements InicioPresenterOps {

    private static WeakReference<InicioActivity> activity;
    private static InicioPresenter presentador;

    private InicioPresenter(Activity activity){
        this.activity = new WeakReference<>((InicioActivity)activity);
    }

    public static InicioPresenter getPresentador(Activity a){
        if (presentador == null)
            presentador = new InicioPresenter(a);
        else
            activity = new WeakReference<>((InicioActivity)a);
        return presentador;
    }

    @Override
    public boolean createNewUser(String email, String contra) {
        return Usuario.createNewUser(email, contra);
    }

    @Override
    public void singInRequested(String email, String contra) {
        Usuario.signIn(email, contra, this);
    }

    @Override
    public void onSingInSuccess() {
        activity.get().enter();
    }
}
