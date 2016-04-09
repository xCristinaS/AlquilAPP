package c.proyecto.mvp_presenters_interfaces;

import c.proyecto.pojo.Anuncio;
import c.proyecto.mvp_models.Usuario;
import c.proyecto.pojo.MessagePojo;

/**
 * Created by Cristina on 20/03/2016.
 */
public interface MainPresenterOps {
    void removeUserAdvert(Anuncio a);
    void removeUserSub(Anuncio a);
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
    public void userHasBeenModified(Usuario user);
}
