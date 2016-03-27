package c.proyecto.presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.EditProfileActivity;

public class EditProfilePresenter {
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
}
