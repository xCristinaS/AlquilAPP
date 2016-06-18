package c.proyecto.mvp_views_interfaces;

import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.pojo.Usuario;

public interface ConversationActivityOps {
    void messageHasBeenObtained(MessagePojo m);
    void advertObtained(Anuncio a);
    void allMessagesObtained();
}
