package c.proyecto.mvp_models;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_presenters.MainPresenter;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Usuario;

public class AdvertsFirebaseManager {

    private static final String URL_ANUNCIOS = "https://proyectofinaldam.firebaseio.com/anuncios/";
    private static final String URL_SOLICITUDES = "https://proyectofinaldam.firebaseio.com/solicitudes/";

    private static boolean userSubRemoved = false;
    private static Firebase mFirebase;
    private static ChildEventListener listener;

    private Usuario currentUser;
    private MyPresenter presenter;

    public AdvertsFirebaseManager(MyPresenter presenter, Usuario currentUser) {
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
                    String subKey = (String) data.getValue(HashMap.class).keySet().iterator().next();
                    firebaseAdvertSub.child(subKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ((MainPresenter) presenter).subHasBeenObtained(dataSnapshot.getValue(Anuncio.class));
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
        createListeners();
        mFirebase.addChildEventListener(listener);
    }

    private void createListeners() {
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
                        ((MainPresenter) presenter).userAdvertHasBeenObtained(a);
                    else if (!a.getSolicitantes().containsKey(currentUser.getKey()))
                        ((MainPresenter) presenter).advertHasBeenObtained(a);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Anuncio a = dataSnapshot.getValue(Anuncio.class);
                    if (dataSnapshot.getKey().contains(currentUser.getKey()))
                        ((MainPresenter) presenter).userAdvertHasBeenModified(a);
                    else if (!userSubRemoved && a.getSolicitantes().containsKey(currentUser.getKey())) {
                        ((MainPresenter) presenter).subHasBeenModified(a);
                        ((MainPresenter) presenter).removeAdvert(a);
                    } else if (userSubRemoved) {
                        ((MainPresenter) presenter).advertHasBeenObtained(a);
                        ((MainPresenter) presenter).removeSub(a);
                    } else
                        ((MainPresenter) presenter).adverHasBeenModified(a);

                    userSubRemoved = false;
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Anuncio a = dataSnapshot.getValue(Anuncio.class);
                    if (a.getSolicitantes().containsKey(currentUser.getKey()))
                        ((MainPresenter) presenter).removeSub(a);
                    else
                        ((MainPresenter) presenter).removeAdvert(a);

                    ((MainPresenter) presenter).sendAdvertHasBeenRemovedBroadcast();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            };
        }
    }

    public void removeUserAdvert(Anuncio a) {
        Firebase mFirebase = new Firebase(URL_ANUNCIOS + a.getKey() + "/");
        mFirebase.setValue(null);
        for (String keySolicitante : a.getSolicitantes().keySet())
            new Firebase(URL_SOLICITUDES).child(keySolicitante).child(a.getKey()).setValue(null);
    }

    public void removeUserSub(final Anuncio a) {
        Firebase mFirebase = new Firebase(URL_ANUNCIOS).child(a.getKey()).child("solicitantes").child(currentUser.getKey());
        mFirebase.setValue(null);
        new Firebase(URL_SOLICITUDES).child(currentUser.getKey()).child(a.getKey()).setValue(null);
        userSubRemoved = true;
    }

    public void createNewUserSub(Anuncio a) {
        a.getSolicitantes().put(currentUser.getKey(), true);
        Firebase mFirebase = new Firebase(URL_ANUNCIOS).child(a.getKey());
        mFirebase.setValue(a);
        mFirebase = new Firebase(URL_SOLICITUDES).child(currentUser.getKey()).child(a.getKey());
        HashMap<String, Boolean> map = new HashMap();
        map.put(a.getKey(), true);
        mFirebase.setValue(map);
    }

    public void filterRequest(final String[] tipoVivienda, final int minPrice, final int maxPrice, final int minSize, final int maxSize) {
        final ArrayList<Anuncio> filteredAdverts = new ArrayList<>();

        new Firebase(URL_ANUNCIOS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Anuncio a;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    a = data.getValue(Anuncio.class);
                    if (!a.getAnunciante().equals(currentUser.getKey()) && (tipoVivienda[0] != null && a.getTipo_vivienda().equals(tipoVivienda[0]) || tipoVivienda[1] != null && a.getTipo_vivienda().equals(tipoVivienda[1]) || tipoVivienda[2] != null && a.getTipo_vivienda().equals(tipoVivienda[2])) && a.getPrecio() >= minPrice && a.getTamanio() >= minSize) {
                        if (maxPrice == 1000 && maxSize == 1000)
                            filteredAdverts.add(a);
                        else if (maxPrice != 1000 && a.getPrecio() <= maxPrice)
                            filteredAdverts.add(a);
                        else if (maxSize != 1000 && a.getTamanio() <= maxSize)
                            filteredAdverts.add(a);
                    }
                }
                ((MainPresenter) presenter).onFilterResponsed(filteredAdverts);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void attachFirebaseListeners() {
        createListeners();
        mFirebase.addChildEventListener(listener);
    }

    public void detachFirebaseListeners() {
        mFirebase.removeEventListener(listener);
        mFirebase = null;
        listener = null;
    }
}
