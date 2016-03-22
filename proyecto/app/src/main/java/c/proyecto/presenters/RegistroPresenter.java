package c.proyecto.presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.RegistroActivity;
import c.proyecto.interfaces.RegistroPresenterOps;
import c.proyecto.models.Usuario;


public class RegistroPresenter implements RegistroPresenterOps{
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
        wActivity = new WeakReference<>((RegistroActivity)activity);
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
        wActivity.get().createUser(exist);
    }
}
