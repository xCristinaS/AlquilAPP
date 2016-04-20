package c.proyecto.mvp_models;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_presenters.AdvertsDetailsPresenter;
import c.proyecto.mvp_presenters.ConversationPresenter;
import c.proyecto.mvp_presenters.InicioPresenter;
import c.proyecto.mvp_presenters.MainPresenter;
import c.proyecto.mvp_presenters.RegistroPresenter;
import c.proyecto.pojo.Usuario;

public class UsersFirebaseManager {

    private static final String URL_USERS = "https://proyectofinaldam.firebaseio.com/usuarios/";
    private static final String URL_MAIN_FIREBASE = "https://proyectofinaldam.firebaseio.com";

    private MyPresenter presenter;
    private static Firebase mFirebase;
    private static ValueEventListener listener;

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
                Usuario u = new Usuario(email, nombre, apellidos, key);
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
                                if (u.getFoto() == null) {
                                    u.setFoto((String) authData.getProviderData().get("profileImageURL"));
                                    updateUserProfile(u);
                                }
                                ((InicioPresenter) presenter).onSignInResponsed(u);
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
                    ((InicioPresenter) presenter).onSignInResponsed(null);
            }
        });
    }

    public void signInWithTwitter(final String email, final String contra) {
        Firebase ref = new Firebase(URL_MAIN_FIREBASE);
        Map<String, String> options = new HashMap<String, String>();
        options.put("oauth_token", "3131343071-q9oM9NEb6NX1HZs7FEuxda6cmvgBMSvtxdZCFKh");
        options.put("oauth_token_secret", "mO4pPS4S9LoW0fcwAp1Jw19sPq2L1SMdhINgbShpInYnY");
        options.put("user_id", email);
        ref.authWithOAuthToken("twitter", options, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("SESION INICIADA");
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                System.out.println(firebaseError.getDetails());
            }
        });
    }

    public void signInWithFacebook(String email, String contra) {
        
    }

    public void initializeOnUserChangedListener(Usuario usuario) {
        if (listener != null)
            mFirebase.removeEventListener(listener);
        if (mFirebase == null)
            mFirebase = new Firebase(URL_USERS + usuario.getKey());
        if (listener == null)
            listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ((MainPresenter) presenter).userHasBeenModified(dataSnapshot.getValue(Usuario.class));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            };
        mFirebase.addValueEventListener(listener);
    }

    //Comprueba si existe algún usuario con este usuario.
    public void amIRegistrered(final String user) {
        Firebase firebase = new Firebase(URL_USERS);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean existe = false;
                Iterator i = dataSnapshot.getChildren().iterator();
                //Recorre todos los usuarios comprobando si existe un usuario con ese Email
                while (i.hasNext() && !existe) {
                    Usuario u = ((DataSnapshot) i.next()).getValue(Usuario.class);
                    if (u.getEmail().equals(user))
                        existe = true;
                }
                ((RegistroPresenter) presenter).onCheckUserExist(existe);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void getAdvertPublisher(String anunciante) {
        Firebase mFirebase = new Firebase(URL_USERS).child(anunciante);
        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (presenter instanceof AdvertsDetailsPresenter)
                    ((AdvertsDetailsPresenter) presenter).onAdvertPublisherRequestedResponsed(dataSnapshot.getValue(Usuario.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void updateUserProfile(Usuario u) {
        Firebase mFirebase = new Firebase(URL_USERS + u.getKey() + "/");
        mFirebase.setValue(u);
    }

    public void detachFirebaseListeners() {
        mFirebase.removeEventListener(listener);
        listener = null;
        mFirebase = null;
    }
}
