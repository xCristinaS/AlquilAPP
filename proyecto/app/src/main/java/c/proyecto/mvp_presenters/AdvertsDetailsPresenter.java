package c.proyecto.mvp_presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.DetallesAnuncioActivity;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_models.AdvertsFirebaseManager;
import c.proyecto.mvp_models.MessagesFirebaseManager;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.pojo.Usuario;
import c.proyecto.mvp_presenters_interfaces.AdvertsDetailsPresenterOps;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.MessagePojo;

public class AdvertsDetailsPresenter implements AdvertsDetailsPresenterOps, MyPresenter {

    private static WeakReference<DetallesAnuncioActivity> activity;
    private static AdvertsDetailsPresenter presentador;
    private AdvertsFirebaseManager advertsManager;
    private MessagesFirebaseManager messagesManager;
    private UsersFirebaseManager usersManager;

    private AdvertsDetailsPresenter(Activity a) {
        activity = new WeakReference<>((DetallesAnuncioActivity) a);
    }

    public static AdvertsDetailsPresenter getPresentador(Activity a) {
        if (presentador == null)
            presentador = new AdvertsDetailsPresenter(a);
        else if (a != null)
            activity = new WeakReference<>((DetallesAnuncioActivity) a);
        return presentador;
    }

    public void setUsersManager(UsersFirebaseManager usersManager) {
        this.usersManager = usersManager;
    }

    public void setAdvertsManager(AdvertsFirebaseManager advertsManager) {
        this.advertsManager = advertsManager;
    }

    public MessagesFirebaseManager getMessagesManager() {
        return messagesManager;
    }

    public void setMessagesManager(MessagesFirebaseManager messagesManager) {
        this.messagesManager = messagesManager;
    }

    @Override
    public void advertPublisherRequested(String anunciante) {
        usersManager.getAdvertPublisher(anunciante);
    }

    @Override
    public void onAdvertPublisherRequestedResponsed(Usuario u) {
        if (activity.get() != null)
            activity.get().onAdvertPublisherRequestedResponsed(u);
    }

    @Override
    public void updateAdvert(Anuncio anuncio) {
        if (activity != null && activity.get() != null)
            activity.get().updateAdvert(anuncio);
    }

    @Override
    public void userNewSubRequested(Anuncio anuncio) {
        advertsManager.createNewUserSub(anuncio);
    }

    @Override
    public void unSubRequested(Anuncio a) {
        advertsManager.removeUserSub(a);
    }

    @Override
    public void sendNewMessage(MessagePojo m, String keyReceptor) {
        messagesManager.sendMessage(m, keyReceptor, true);
    }

    @Override
    public void getMessageIfConverExist(Anuncio anuncio) {
        messagesManager.getMessageIfConverExist(anuncio);
    }

    @Override
    public void messageIfConverExistObtained(MessagePojo m) {
        if (activity.get() != null)
            activity.get().messageIfConverExistObtained(m);
    }

    public void liberarMemoria(){
        //activity = null;
        presentador = null;
    }
}
