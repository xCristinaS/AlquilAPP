package c.proyecto.mvp_presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.InicioActivity;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_presenters_interfaces.InicioPresenterOps;
import c.proyecto.mvp_models.Usuario;


public class InicioPresenter implements InicioPresenterOps, MyPresenter {

    private static WeakReference<InicioActivity> activity;
    private static InicioPresenter presentador;

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

    @Override
    public Usuario createNewUser(String email, String contra, String nombre, String apellidos) {
        return Usuario.createNewUser(email, contra, nombre, apellidos);
    }

    @Override
    public void singInRequested(String email, String contra) {
        Usuario.signIn(email, contra, this);
    }

    @Override
    public void onSingInResponsed(Usuario u) {
        if (activity.get() != null)
            activity.get().enter(u);
    }
}
