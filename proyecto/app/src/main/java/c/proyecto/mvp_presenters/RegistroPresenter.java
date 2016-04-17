package c.proyecto.mvp_presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.RegistroActivity;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_presenters_interfaces.RegistroPresenterOps;
import c.proyecto.pojo.Usuario;


public class RegistroPresenter implements RegistroPresenterOps, MyPresenter {
    private static WeakReference<RegistroActivity> activity;
    private static RegistroPresenter presentador;
    private UsersFirebaseManager usersManager;

    public static RegistroPresenter getPresentador(Activity activity) {
        if (presentador == null)
            presentador = new RegistroPresenter(activity);
        else
            RegistroPresenter.activity = new WeakReference<>((RegistroActivity) activity);

        return presentador;
    }

    private RegistroPresenter(Activity activity) {
        RegistroPresenter.activity = new WeakReference<>((RegistroActivity) activity);
    }

    public void setUsersManager(UsersFirebaseManager usersManager) {
        this.usersManager = usersManager;
    }

    @Override
    public void register(String user, String pass, String nombre, String apellidos) {
        usersManager.createNewUser(user, pass, nombre, apellidos);
    }

    @Override
    public void checkUser(String user) {
        usersManager.amIRegistrered(user);
    }

    @Override
    public void onCheckUserExist(boolean exist) {
        if (activity.get() != null)
            activity.get().createUser(exist);
    }

    @Override
    public void userHasBeenCreated(Usuario u) {
        if (activity.get() != null)
            activity.get().userHasBeenRegistered(u);
    }
}
