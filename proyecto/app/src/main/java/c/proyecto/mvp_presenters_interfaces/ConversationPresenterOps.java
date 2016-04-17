package c.proyecto.mvp_presenters_interfaces;

import c.proyecto.pojo.MessagePojo;

/**
 * Created by Cristina on 24/03/2016.
 */
public interface ConversationPresenterOps {
    void userConversationRequested(MessagePojo mensaje);
    void messageHasBeenObtained(MessagePojo m);
    void sendMessage(MessagePojo mensaje, String keyReceptor, boolean isFirstMessageSended);
    void detachFirebaseListeners();
    void removeMessage(MessagePojo m);
}
