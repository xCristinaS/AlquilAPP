package c.proyecto.interfaces;

import c.proyecto.models.Usuario;
import c.proyecto.pojo.MessagePojo;

/**
 * Created by Cristina on 24/03/2016.
 */
public interface ConversationPresenterOps {
    void userConversationRequested(Usuario u, MessagePojo mensaje);
    void messageHasBeenObtained(MessagePojo m);
    void sendMessage(MessagePojo mensaje, String keyReceptor, boolean isFirstMessageSended);
    void detachFirebaseListeners();
    void removeMessage(MessagePojo m);
}
