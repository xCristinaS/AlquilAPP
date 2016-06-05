package c.proyecto.mvp_models;

import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import c.proyecto.Constantes;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_presenters.AdvertsDetailsPresenter;
import c.proyecto.mvp_presenters.InicioPresenter;
import c.proyecto.mvp_presenters.MainPresenter;
import c.proyecto.mvp_presenters.RegistroPresenter;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Usuario;

public class UsersFirebaseManager {

    private MyPresenter presenter;
    private static Firebase mFirebase;
    private static ValueEventListener listener;

    public UsersFirebaseManager(MyPresenter presenter) {
        this.presenter = presenter;
    }

    public void createNewUser(final String email, final String contra, final String nombre, final String apellidos) {
        Firebase ref = new Firebase(Constantes.URL_MAIN_FIREBASE);
        ref.createUser(email, contra, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                String key = (String) result.get("uid");
                Firebase mFirebase = new Firebase(Constantes.URL_USERS + key + "/");
                Usuario u = new Usuario(email, nombre, apellidos, key);
                mFirebase.setValue(u);
                signIn(email, contra);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                checkError(firebaseError);
            }
        });
    }

    public void signIn(final String email, final String contra) {
        Firebase ref = new Firebase(Constantes.URL_MAIN_FIREBASE);
        ref.authWithPassword(email, contra, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(final AuthData authData) {
                Firebase firebase = new Firebase(Constantes.URL_USERS);
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
                                    if (authData.getProviderData().get("profileImageURL") != null) {
                                        u.setFoto((String) authData.getProviderData().get("profileImageURL"));
                                        updateUserProfile(u);
                                    }
                                }
                                if (presenter instanceof InicioPresenter)
                                    ((InicioPresenter) presenter).onSignInResponsed(u);
                                else
                                    ((RegistroPresenter) presenter).userHasBeenCreated(u);
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
                checkError(firebaseError);
            }
        });
    }

    private void checkError(FirebaseError error) {
        String resp;
        switch (error.getCode()) {
            case FirebaseError.INVALID_PASSWORD:
                resp = "Contraseña incorrecta";
                break;
            case FirebaseError.EMAIL_TAKEN:
                resp = "Email ya en uso";
                break;
            case FirebaseError.INVALID_EMAIL:
                resp = "El email epecificado no es válido";
                break;
            case FirebaseError.NETWORK_ERROR:
                resp = "Error de conexión";
                break;
            case FirebaseError.UNKNOWN_ERROR:
                resp = "Error inesperado";
                break;
            case FirebaseError.USER_DOES_NOT_EXIST:
                resp = "La cuenta de usuario no existe";
                break;
            default:
                resp = "";
                break;
        }
        if (presenter instanceof InicioPresenter)
            ((InicioPresenter) presenter).onSignInResponsed(resp);
        else
            ((RegistroPresenter) presenter).userHasBeenCreated(resp);
    }

    public void signInWithTwitter(final String email, final String contra) {
        Firebase ref = new Firebase(Constantes.URL_MAIN_FIREBASE);
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
            mFirebase = new Firebase(Constantes.URL_USERS + usuario.getKey());
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
        Firebase firebase = new Firebase(Constantes.URL_USERS);

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
        Firebase mFirebase = new Firebase(Constantes.URL_USERS).child(anunciante);
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
        Firebase mFirebase = new Firebase(Constantes.URL_USERS + u.getKey() + "/");
        mFirebase.setValue(u);
    }


    public void getSolicitantes(final View itemView, final Anuncio anuncio) {
        Firebase f = new Firebase(Constantes.URL_USERS);
        final HashMap<String, Boolean> solicitantes = anuncio.getSolicitantes();
        final ArrayList<Usuario> listaSolicitantes = new ArrayList<>();
        final Iterator it = solicitantes.keySet().iterator();
        String solicitanteKey;
        for (int i = 0; i < solicitantes.size(); i++) {
            solicitanteKey = (String) it.next();
            final int iAux = i;
            f.child(solicitanteKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    listaSolicitantes.add(dataSnapshot.getValue(Usuario.class));
                    if (iAux == solicitantes.size() - 1)
                        ((MainPresenter) presenter).solicitantesObtained(itemView, listaSolicitantes, anuncio);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }

    public void detachFirebaseListeners() {
        mFirebase.removeEventListener(listener);
        listener = null;
        mFirebase = null;
    }
}
