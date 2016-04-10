package c.proyecto.mvp_models;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_presenters.MainPresenter;
import c.proyecto.pojo.Anuncio;

public class AdvertsFirebaseManager {

    private static final String URL_ANUNCIOS = "https://proyectofinaldam.firebaseio.com/anuncios/";
    private static final String URL_SOLICITUDES = "https://proyectofinaldam.firebaseio.com/solicitudes/";

    private static boolean userSubRemoved = false;
    private static Firebase mFirebase;
    private static ChildEventListener listener;

    private Usuario currentUser;
    private MyPresenter presenter;

    public AdvertsFirebaseManager(MyPresenter presenter, Usuario currentUser){
        this.presenter = presenter;
        this.currentUser = currentUser;
    }

    public void publishNewAdvert(Anuncio a) { // crea un nuevo anuncio en la rama /anuncios/keyAnuncio/valorAnuncio --> keyAnuncio = numeroIdentificadoUsuarioQuePublica_numeroIdentificadorAnuncio
        Firebase mFirebase = new Firebase(URL_ANUNCIOS + a.getKey() + "/");
        mFirebase.setValue(a);
    }

    public void initializeFirebaseListeners() {
        Firebase firebaseSubs = new Firebase(URL_SOLICITUDES).child(currentUser.getKey());
        final Firebase firebaseAdvertSub = new Firebase(URL_ANUNCIOS);
        firebaseSubs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String subKey = (String)data.getValue(HashMap.class).keySet().iterator().next();
                    firebaseAdvertSub.child(subKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ((MainPresenter)presenter).subHasBeenObtained(dataSnapshot.getValue(Anuncio.class));
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        if (mFirebase == null)
            mFirebase = new Firebase(URL_ANUNCIOS);

        if (listener != null)
            mFirebase.removeEventListener(listener);

        if (listener == null) {
            listener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Anuncio a = dataSnapshot.getValue(Anuncio.class);
                    if (dataSnapshot.getKey().contains(currentUser.getKey()))
                        ((MainPresenter)presenter).userAdvertHasBeenObtained(a);
                    else if (!a.getSolicitantes().containsKey(currentUser.getKey()))
                        ((MainPresenter)presenter).advertHasBeenObtained(a);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Anuncio a = dataSnapshot.getValue(Anuncio.class);
                    if (dataSnapshot.getKey().contains(currentUser.getKey()))
                        ((MainPresenter)presenter).userAdvertHasBeenModified(a);
                    else if (!userSubRemoved && a.getSolicitantes().containsKey(currentUser.getKey())) {
                        ((MainPresenter) presenter).subHasBeenModified(a);
                        ((MainPresenter) presenter).removeAdvert(a);
                    } else if (userSubRemoved) {
                        ((MainPresenter) presenter).advertHasBeenObtained(a);
                        ((MainPresenter) presenter).removeSub(a);
                    } else
                        ((MainPresenter)presenter).adverHasBeenModified(a);

                    userSubRemoved = false;
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Anuncio a = dataSnapshot.getValue(Anuncio.class);
                    if (a.getSolicitantes().containsKey(currentUser.getKey()))
                        ((MainPresenter)presenter).removeSub(a);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            };
        }
        mFirebase.addChildEventListener(listener);
    }

    public void removeUserAdvert(Anuncio a) {
        Firebase mFirebase = new Firebase(URL_ANUNCIOS + a.getKey() + "/");
        mFirebase.setValue(null);
    }

    public void removeUserSub(final Anuncio a) {
        Firebase mFirebase = new Firebase(URL_ANUNCIOS).child(a.getKey()).child("solicitantes").child(currentUser.getKey());
        mFirebase.setValue(null);
        new Firebase(URL_SOLICITUDES).child(currentUser.getKey()).child(a.getKey()).setValue(null);
        userSubRemoved = true;
    }

    public void createNewUserSub(Anuncio a){
        a.getSolicitantes().put(currentUser.getKey(), true);
        Firebase mFirebase = new Firebase(URL_ANUNCIOS).child(a.getKey());
        mFirebase.setValue(a);
        mFirebase = new Firebase(URL_SOLICITUDES).child(currentUser.getKey()).child(a.getKey());
        HashMap<String, Boolean> map = new HashMap();
        map.put(a.getKey(), true);
        mFirebase.setValue(map);
    }

    public void detachFirebaseListeners() {
        mFirebase.removeEventListener(listener);
        listener = null;
        mFirebase = null;
    }
}
