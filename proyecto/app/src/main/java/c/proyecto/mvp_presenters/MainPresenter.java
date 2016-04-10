package c.proyecto.mvp_presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.MainActivity;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_models.AdvertsFirebaseManager;
import c.proyecto.mvp_models.MessagesFirebaseManager;
import c.proyecto.mvp_presenters_interfaces.MainPresenterOps;
import c.proyecto.pojo.Anuncio;
import c.proyecto.mvp_models.Usuario;
import c.proyecto.pojo.MessagePojo;

public class MainPresenter implements MainPresenterOps, MyPresenter {

    private static WeakReference<MainActivity> activity;
    private static MainPresenter presentador;
    private AdvertsFirebaseManager advertsManager;
    private MessagesFirebaseManager messagesManager;

    private MainPresenter(Activity a) {
        activity = new WeakReference<>((MainActivity) a);
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
        advertsManager.removeUserAdvert(a);
    }

    @Override
    public void removeUserSub(Anuncio a) {
        advertsManager.removeUserSub(a);
    }

    @Override
    public void initializeFirebaseListeners(Usuario usuario) {
        advertsManager.initializeFirebaseListeners();
        Usuario.initializeOnUserChangedListener(this, usuario);
        messagesManager.initializeMessagesListeners();
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
    public void removeAdvert(Anuncio a){
        if (activity.get() != null)
            activity.get().removeAdvert(a);
    }

    @Override
    public void detachListeners() {
        advertsManager.detachFirebaseListeners();
        Usuario.detachFirebaseListeners();
        messagesManager.detachMessagesListeners();
    }

    @Override
    public void requestUserMessages(Usuario user) {
        messagesManager.getUserMessages();
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

    public void setAdvertsManager(AdvertsFirebaseManager advertsManager) {
        this.advertsManager = advertsManager;
    }

    public void setMessagesManager(MessagesFirebaseManager messagesManager) {
        this.messagesManager = messagesManager;
    }
}
