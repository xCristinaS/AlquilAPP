package c.proyecto.mvp_presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.InicioActivity;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_presenters_interfaces.InicioPresenterOps;
import c.proyecto.pojo.Usuario;


public class InicioPresenter implements InicioPresenterOps, MyPresenter {

    private static WeakReference<InicioActivity> activity;
    private static InicioPresenter presentador;
    private UsersFirebaseManager usersManager;

    private InicioPresenter(Activity activity) {
        this.activity = new WeakReference<>((InicioActivity) activity);
    }

    public static InicioPresenter getPresentador(Activity a) {
        if (presentador == null)
            presentador = new InicioPresenter(a);
        else
            activity = new WeakReference<>((InicioActivity) a);
        return presentador;
    }

    public void setUsersManager(UsersFirebaseManager usersManager) {
        this.usersManager = usersManager;
    }

    @Override
    public void singInRequested(String email, String contra) {
        usersManager.signIn(email, contra);
    }

    @Override
    public void onSingInResponsed(Usuario u) {
        if (activity.get() != null)
            activity.get().enter(u);
    }
}
