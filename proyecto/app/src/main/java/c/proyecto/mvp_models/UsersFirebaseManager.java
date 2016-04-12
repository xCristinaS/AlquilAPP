package c.proyecto.mvp_models;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_presenters.RegistroPresenter;

public class UsersFirebaseManager {

    private static final String URL_USERS = "https://proyectofinaldam.firebaseio.com/usuarios/";
    private static final String URL_MAIN_FIREBASE = "https://proyectofinaldam.firebaseio.com";

    private MyPresenter presenter;

    public UsersFirebaseManager(MyPresenter presenter){
        this.presenter = presenter;
    }

    public void createNewUser(final String email, final String contra, final String nombre, final String apellidos) {
        Firebase ref = new Firebase(URL_MAIN_FIREBASE);
        ref.createUser(email, contra, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                String key = (String) result.get("uid");
                Firebase mFirebase = new Firebase(URL_USERS + key + "/");
                Usuario u = new Usuario(email, contra, nombre, apellidos, key);
                mFirebase.setValue(u);
                ((RegistroPresenter)presenter).userHasBeenCreated(u);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }
}
