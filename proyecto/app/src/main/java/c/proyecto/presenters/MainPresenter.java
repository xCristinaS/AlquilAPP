package c.proyecto.presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import c.proyecto.activities.MainActivity;
import c.proyecto.interfaces.MainPresenterOps;
import c.proyecto.models.Anuncio;
import c.proyecto.models.Message;
import c.proyecto.models.Usuario;
import c.proyecto.pojo.MessagePojo;

/**
 * Created by Cristina on 20/03/2016.
 */
public class MainPresenter implements MainPresenterOps {

    private static WeakReference<MainActivity> activity;
    private static MainPresenter presentador;

    private MainPresenter(Activity activity){
        this.activity = new WeakReference<>((MainActivity)activity);
    }

    public static MainPresenter getPresentador(Activity a){
        if (presentador == null)
            presentador = new MainPresenter(a);
        else
            activity = new WeakReference<>((MainActivity)a);
        return presentador;
    }

    @Override
    public void removeUserAdvert(Anuncio a) {
        Anuncio.removeUserAdvert(a);
    }

    @Override
    public void removeUserSub(Anuncio a, Usuario u) {
        Anuncio.removeUserSub(a, u);
    }

    @Override
    public void initializeFirebaseListeners(Usuario usuario){
        Anuncio.initializeFirebaseListeners(this, usuario);
    }

    @Override
    public void advertHasBeenObtained(Anuncio a){
        activity.get().advertHasBeenObtained(a);
    }

    @Override
    public void adverHasBeenModified(Anuncio a){
        activity.get().adverHasBeenModified(a);
    }

    @Override
    public void subHasBeenObtained(Anuncio a){
        activity.get().subHasBeenObtained(a);
    }

    @Override
    public void subHasBeenModified(Anuncio a){
        activity.get().subHasBeenModified(a);
    }

    @Override
    public void userAdvertHasBeenObtained(Anuncio a){
        activity.get().userAdvertHasBeenObtained(a);
    }

    @Override
    public void userAdvertHasBeenModified(Anuncio a){
        activity.get().userAdvertHasBeenModified(a);
    }

    @Override
    public void removeSub(Anuncio a){
        activity.get().removeSub(a);
    }

    @Override
    public void detachListeners() {
       Anuncio.detachFirebaseListeners();
    }

    public void requestUserMessages(Usuario user){
        Message.getUserMessages(user, this);
    }

    public void userMessageHasBeenObtained(MessagePojo m){
        activity.get().userMessageHasBeenObtained(m);
    }
}
