package c.proyecto.interfaces;

import java.util.ArrayList;

import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;
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
}
