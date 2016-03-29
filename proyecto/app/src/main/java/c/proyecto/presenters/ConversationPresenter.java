package c.proyecto.presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.ConversationActivity;
import c.proyecto.interfaces.ConversationPresenterOps;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.models.Message;
import c.proyecto.models.Usuario;
import c.proyecto.pojo.MessagePojo;

/**
 * Created by Cristina on 24/03/2016.
 */
public class ConversationPresenter implements ConversationPresenterOps, MyPresenter {

    private static WeakReference<ConversationActivity> activity;
    private static ConversationPresenter presentador;

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
    public void userConversationRequested(Usuario u, MessagePojo mensaje) {
        Message.getUserConversation(u, mensaje, this);
    }

    @Override
    public void messageHasBeenObtained(MessagePojo m) {
        if (activity.get() != null)
            activity.get().messageHasBeenObtained(m);
    }

    @Override
    public void sendMessage(MessagePojo mensaje, String keyReceptor) {
        Message.sendMessage(mensaje, keyReceptor);
    }

    @Override
    public void detachFirebaseListeners() {
        Message.detachConversationListeners();
    }

    @Override
    public void removeMessage(MessagePojo m) {
        Message.removeMessage(m);
    }
}
