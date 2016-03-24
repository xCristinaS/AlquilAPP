package c.proyecto.interfaces;

import java.util.ArrayList;
import java.util.List;

import c.proyecto.models.Anuncio;
import c.proyecto.models.Message;
import c.proyecto.models.Usuario;
import c.proyecto.pojo.MessagePojo;

/**
 * Created by Cristina on 20/03/2016.
 */
public interface MainPresenterOps {
    void removeUserAdvert(Anuncio a);
    void removeUserSub(Anuncio a, Usuario u);
    void initializeFirebaseListeners(Usuario usuario);
    void advertHasBeenObtained(Anuncio a);
    void adverHasBeenModified(Anuncio a);
    void subHasBeenObtained(Anuncio a);
    void subHasBeenModified(Anuncio a);
    void userAdvertHasBeenObtained(Anuncio a);
    void userAdvertHasBeenModified(Anuncio a);
    void removeSub(Anuncio a);
    void detachListeners();
    void requestUserMessages(Usuario user);
    void userMessageHasBeenObtained(MessagePojo m);
}
