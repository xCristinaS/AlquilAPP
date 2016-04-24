package c.proyecto.mvp_views_interfaces;

import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.pojo.Usuario;

/**
 * Created by Cristina on 24/03/2016.
 */
public interface ConversationActivityOps {
    void messageHasBeenObtained(MessagePojo m);
    void advertObtained(Anuncio a);
}
