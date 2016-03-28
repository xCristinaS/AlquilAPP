package c.proyecto.presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.EditProfileActivity;
import c.proyecto.interfaces.EditProfilePresenterOps;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.models.Usuario;

public class EditProfilePresenter implements EditProfilePresenterOps, MyPresenter {

    private static WeakReference<EditProfileActivity> activity;
    private static EditProfilePresenter presentador;

    private EditProfilePresenter(Activity activity){
        this.activity = new WeakReference<>((EditProfileActivity)activity);
    }

    public static EditProfilePresenter getPresentador(Activity a){
        if (presentador == null)
            presentador = new EditProfilePresenter(a);
        else
            activity = new WeakReference<>((EditProfileActivity)a);
        return presentador;
    }

    @Override
    public void updateUserProfile(Usuario u) {
        Usuario.updateUserProfile(u);
    }
}
