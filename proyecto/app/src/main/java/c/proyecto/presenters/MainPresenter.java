package c.proyecto.presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.MainActivity;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.interfaces.MainPresenterOps;
import c.proyecto.models.Anuncio;
import c.proyecto.models.Message;
import c.proyecto.models.Usuario;
import c.proyecto.pojo.MessagePojo;

/**
 * Created by Cristina on 20/03/2016.
 */
public class MainPresenter implements MainPresenterOps, MyPresenter {

    private static WeakReference<MainActivity> activity;
    private static MainPresenter presentador;

    private MainPresenter(Activity activity) {
        this.activity = new WeakReference<>((MainActivity) activity);
    }

    public static MainPresenter getPresentador(Activity a) {
        if (presentador == null)
            presentador = new MainPresenter(a);
        else
            activity = new WeakReference<>((MainActivity) a);
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
    public void initializeFirebaseListeners(Usuario usuario) {
        Anuncio.initializeFirebaseListeners(this, usuario);
        Usuario.initializeOnUserChangedListener(this, usuario);
    }

    @Override
    public void advertHasBeenObtained(Anuncio a) {
        if (activity.get() != null)
            activity.get().advertHasBeenObtained(a);
    }

    @Override
    public void adverHasBeenModified(Anuncio a) {
        if (activity.get() != null)
            activity.get().adverHasBeenModified(a);
    }

    @Override
    public void subHasBeenObtained(Anuncio a) {
        if (activity.get() != null)
            activity.get().subHasBeenObtained(a);
    }

    @Override
    public void subHasBeenModified(Anuncio a) {
        if (activity.get() != null)
            activity.get().subHasBeenModified(a);
    }

    @Override
    public void userAdvertHasBeenObtained(Anuncio a) {
        if (activity.get() != null)
            activity.get().userAdvertHasBeenObtained(a);
    }

    @Override
    public void userAdvertHasBeenModified(Anuncio a) {
        if (activity.get() != null)
            activity.get().userAdvertHasBeenModified(a);
    }

    @Override
    public void removeSub(Anuncio a) {
        if (activity.get() != null)
            activity.get().removeSub(a);
    }

    @Override
    public void detachListeners() {
        Anuncio.detachFirebaseListeners();
        Usuario.detachFirebaseListeners();
        Message.detachMessagesListeners();
    }

    @Override
    public void requestUserMessages(Usuario user) {
        Message.getUserMessages(user, this);
    }

    @Override
    public void userMessageHasBeenObtained(MessagePojo m) {
        if (activity.get() != null)
            activity.get().userMessageHasBeenObtained(m);

    }

    @Override
    public void userHasBeenModified(Usuario user) {
        if (activity.get() != null)
            activity.get().userAdvertHasBeenModified(user);
    }
}
