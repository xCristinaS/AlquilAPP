package c.proyecto.mvp_presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.ConversationActivity;
import c.proyecto.mvp_models.AdvertsFirebaseManager;
import c.proyecto.mvp_models.MessagesFirebaseManager;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_presenters_interfaces.ConversationPresenterOps;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.pojo.Usuario;

public class ConversationPresenter implements ConversationPresenterOps, MyPresenter {

    private static WeakReference<ConversationActivity> activity;
    private static ConversationPresenter presentador;
    private MessagesFirebaseManager messagesManager;
    private AdvertsFirebaseManager advertsManager;

    private ConversationPresenter(Activity a) {
        activity = new WeakReference<>((ConversationActivity) a);
    }

    public static ConversationPresenter getPresentador(Activity a) {
        if (presentador == null)
            presentador = new ConversationPresenter(a);
        else if (a != null)
            activity = new WeakReference<>((ConversationActivity) a);
        return presentador;
    }

    public void setMessagesManager(MessagesFirebaseManager messagesManager) {
        this.messagesManager = messagesManager;
    }

    public void setAdvertsManager(AdvertsFirebaseManager advertsManager) {
        this.advertsManager = advertsManager;
    }

    @Override
    public void userConversationRequested(MessagePojo mensaje) {
        messagesManager.getUserConversation(mensaje);
    }

    @Override
    public void messageHasBeenObtained(MessagePojo m) {
        if (activity.get() != null)
            activity.get().messageHasBeenObtained(m);
    }

    @Override
    public void sendMessage(MessagePojo mensaje, String keyReceptor, boolean isFirstMessageSended) {
        messagesManager.sendMessage(mensaje, keyReceptor, isFirstMessageSended);
    }

    @Override
    public void detachFirebaseListeners() {
        messagesManager.detachConversationListeners();
    }

    @Override
    public void removeMessage(MessagePojo m) {
        messagesManager.removeMessage(m);
    }

    @Override
    public void getAdvertFromTitle(String tituloAnuncio) {
        advertsManager.getAdvertFromTitle(tituloAnuncio);
    }

    @Override
    public void advertObtained(Anuncio a){
        if (activity.get() != null)
            activity.get().advertObtained(a);
    }

    @Override
    public void allMessagesObtained() {
        if (activity.get() != null)
            activity.get().allMessagesObtained();
    }
}
