package c.proyecto.mvp_presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.InicioActivity;
import c.proyecto.interfaces.MyInicio;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_presenters_interfaces.InicioPresenterOps;
import c.proyecto.pojo.Usuario;


public class InicioPresenter implements InicioPresenterOps, MyPresenter {

    private static WeakReference<MyInicio> activity;
    private static InicioPresenter presentador;
    private UsersFirebaseManager usersManager;

    private InicioPresenter(Activity a) {
        activity = new WeakReference<>((MyInicio) a);
    }

    public static InicioPresenter getPresentador(Activity a) {
        if (presentador == null)
            presentador = new InicioPresenter(a);
        else if (a != null)
            activity = new WeakReference<>((MyInicio) a);
        return presentador;
    }

    public void setUsersManager(UsersFirebaseManager usersManager) {
        this.usersManager = usersManager;
    }

    @Override
    public void signInRequested(String email, String contra) {
        usersManager.signIn(email, contra);
    }

    @Override
    public void onSignInResponsed(Object o) {
        if (activity.get() != null)
            activity.get().enter(o);
    }

    @Override
    public void liberarMemoria() {
        presentador = null;
    }
}
