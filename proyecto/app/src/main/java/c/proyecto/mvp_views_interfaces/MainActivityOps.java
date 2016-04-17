package c.proyecto.mvp_views_interfaces;

import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Usuario;
import c.proyecto.pojo.MessagePojo;

/**
 * Created by Cristina on 20/03/2016.
 */
public interface MainActivityOps {
    void advertHasBeenObtained(Anuncio a);
    void adverHasBeenModified(Anuncio a);
    void subHasBeenObtained(Anuncio a);
    void subHasBeenModified(Anuncio a);
    void userAdvertHasBeenObtained(Anuncio a);
    void userAdvertHasBeenModified(Anuncio a);
    void removeSub(Anuncio a);
    void userMessageHasBeenObtained(MessagePojo m);
    void userAdvertHasBeenModified(Usuario user);
    void removeAdvert(Anuncio a);
    void sendAdvertHasBeenRemovedBroadcast();
}
