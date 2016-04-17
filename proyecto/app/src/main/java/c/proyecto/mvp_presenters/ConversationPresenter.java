package c.proyecto.mvp_presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.ConversationActivity;
import c.proyecto.mvp_models.MessagesFirebaseManager;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_presenters_interfaces.ConversationPresenterOps;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.pojo.Usuario;

public class ConversationPresenter implements ConversationPresenterOps, MyPresenter {

    private static WeakReference<ConversationActivity> activity;
    private static ConversationPresenter presentador;
    private MessagesFirebaseManager messagesManager;
    private UsersFirebaseManager usersManager;

    private ConversationPresenter(Activity activity) {
        this.activity = new WeakReference<>((ConversationActivity) activity);
    }

    public static ConversationPresenter getPresentador(Activity a) {
        if (presentador == null)
            presentador = new ConversationPresenter(a);
        else
            activity = new WeakReference<>((ConversationActivity) a);
        return presentador;
    }

    @Override
    public void userConversationRequested(MessagePojo mensaje) {
        messagesManager.getUserConversation(mensaje);
    }

    public void setUsersManager(UsersFirebaseManager usersManager) {
        this.usersManager = usersManager;
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

    public void setMessagesManager(MessagesFirebaseManager messagesManager) {
        this.messagesManager = messagesManager;
    }

    @Override
    public void getReceptor(String keyReceptor) {
        usersManager.getAdvertPublisher(keyReceptor);
    }

    @Override
    public void receptorObtained(Usuario usuario) {
        if (activity.get() != null)
            activity.get().receptorObtained(usuario);
    }
}
