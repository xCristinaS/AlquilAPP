package c.proyecto.mvp_presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.RegistroActivity;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_presenters_interfaces.RegistroPresenterOps;
import c.proyecto.mvp_models.Usuario;


public class RegistroPresenter implements RegistroPresenterOps, MyPresenter {
    private static WeakReference<RegistroActivity> wActivity;
    private static RegistroPresenter presentador;

    public static RegistroPresenter getPresentador(Activity activity) {
        if (presentador == null)
            presentador = new RegistroPresenter(activity);
        else
            wActivity = new WeakReference<>((RegistroActivity) activity);

        return presentador;
    }

    private RegistroPresenter(Activity activity) {
        wActivity = new WeakReference<>((RegistroActivity) activity);
    }

    @Override
    public Usuario register(String user, String pass, String nombre, String apellidos) {
        return Usuario.createNewUser(user, pass, nombre, apellidos);
    }

    @Override
    public void checkUser(String user) {
        Usuario.amIRegistrered(user, presentador);
    }

    @Override
    public void onCheckUserExist(boolean exist) {
        if (wActivity.get() != null)
            wActivity.get().createUser(exist);
    }
}
