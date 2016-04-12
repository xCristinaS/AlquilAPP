package c.proyecto.mvp_models;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_presenters.InicioPresenter;
import c.proyecto.mvp_presenters.RegistroPresenter;

public class UsersFirebaseManager {

    private static final String URL_USERS = "https://proyectofinaldam.firebaseio.com/usuarios/";
    private static final String URL_MAIN_FIREBASE = "https://proyectofinaldam.firebaseio.com";

    private MyPresenter presenter;

    public UsersFirebaseManager(MyPresenter presenter) {
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
                ((RegistroPresenter) presenter).userHasBeenCreated(u);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }

    public void signIn(final String email, final String contra) {
        Firebase ref = new Firebase(URL_MAIN_FIREBASE);
        ref.authWithPassword(email, contra, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(final AuthData authData) {
                Firebase firebase = new Firebase(URL_USERS);
                firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean stop = false;
                        Iterator i = dataSnapshot.getChildren().iterator();
                        //Recorre todos los usuarios comprobando que el pasado por parámetro existe
                        while (i.hasNext() && !stop) {
                            Usuario u = ((DataSnapshot) i.next()).getValue(Usuario.class);
                            if (u.getKey().equals(authData.getUid())) {
                                stop = true;
                                if (u.getFoto() == null)
                                    u.setFoto((String) authData.getProviderData().get("profileImageURL"));
                                ((InicioPresenter) presenter).onSingInResponsed(u);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                if (firebaseError.getCode() == FirebaseError.INVALID_PASSWORD)
                    ((InicioPresenter) presenter).onSingInResponsed(null);
            }
        });
    }
}
