package c.proyecto.mvp_models;

import android.text.TextUtils;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_presenters.ConversationPresenter;
import c.proyecto.mvp_presenters.MainPresenter;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Prestacion;
import c.proyecto.pojo.Usuario;

public class AdvertsFirebaseManager {

    private static final String URL_ANUNCIOS = "https://proyectofinaldam.firebaseio.com/anuncios/";
    private static final String URL_ANUNCIOS_USUARIOS = "https://proyectofinaldam.firebaseio.com/anuncios_usuarios/";
    private static final String URL_SOLICITUDES = "https://proyectofinaldam.firebaseio.com/solicitudes/";
    private static final String URL_LOCATIONS = "https://proyectofinaldam.firebaseio.com/Locations/";

    private static boolean userSubRemoved = false;
    private static Firebase mFirebase;
    private static ChildEventListener listener;
    private static GeoQuery geoQuery;

    private Usuario currentUser;
    private MyPresenter presenter;

    public AdvertsFirebaseManager(MyPresenter presenter, Usuario currentUser) {
        this.presenter = presenter;
        this.currentUser = currentUser;
    }

    public void publishNewAdvert(Anuncio a) { // crea un nuevo anuncio en la rama /anuncios/keyAnuncio/valorAnuncio --> keyAnuncio = numeroIdentificadoUsuarioQuePublica_numeroIdentificadorAnuncio
        Firebase mFirebase = new Firebase(URL_ANUNCIOS + a.getKey() + "/");
        mFirebase.setValue(a);

        GeoFire g = new GeoFire(new Firebase(URL_LOCATIONS));
        g.setLocation(a.getKey(), new GeoLocation(a.getLats().getLatitude(), a.getLats().getLongitude()));

        HashMap<String, Boolean> mapAux = new HashMap<>();
        mapAux.put(a.getKey(), true);
        new Firebase(URL_ANUNCIOS_USUARIOS).child(currentUser.getKey()).child(a.getKey()).setValue(mapAux);
    }

    public void initializeFirebaseListeners() {
        Firebase firebaseSubs = new Firebase(URL_SOLICITUDES).child(currentUser.getKey());
        final Firebase fAdverts = new Firebase(URL_ANUNCIOS);
        firebaseSubs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String subKey = (String) data.getValue(HashMap.class).keySet().iterator().next();
                    fAdverts.child(subKey).addListenerForSingleValueEvent(new ValueEventListener() {
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

        // Obtener los anuncios que ha publicado el usuario
        new Firebase(URL_ANUNCIOS_USUARIOS).child(currentUser.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String advertKey = (String) data.getValue(HashMap.class).keySet().iterator().next();
                    fAdverts.child(advertKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ((MainPresenter) presenter).userAdvertHasBeenObtained(dataSnapshot.getValue(Anuncio.class));
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

        GeoFire g = new GeoFire(new Firebase(URL_LOCATIONS));
        g.removeLocation(a.getKey());

        new Firebase(URL_ANUNCIOS_USUARIOS).child(currentUser.getKey()).child(a.getKey()).setValue(null);

        for (String keySolicitante : a.getSolicitantes().keySet())
            new Firebase(URL_SOLICITUDES).child(keySolicitante).child(a.getKey()).setValue(null);
    }

    public void removeUserSub(final Anuncio a) {
        a.setSubsChanged(true);
        Firebase mFirebase = new Firebase(URL_ANUNCIOS).child(a.getKey()).child("solicitantes").child(currentUser.getKey());
        mFirebase.setValue(null);
        new Firebase(URL_SOLICITUDES).child(currentUser.getKey()).child(a.getKey()).setValue(null);
        userSubRemoved = true;
    }

    public void createNewUserSub(Anuncio a) {
        a.getSolicitantes().put(currentUser.getKey(), true);
        a.setSubsChanged(true);
        Firebase mFirebase = new Firebase(URL_ANUNCIOS).child(a.getKey());
        mFirebase.setValue(a);
        mFirebase = new Firebase(URL_SOLICITUDES).child(currentUser.getKey()).child(a.getKey());
        HashMap<String, Boolean> map = new HashMap();
        map.put(a.getKey(), true);
        mFirebase.setValue(map);
    }


    public void getAdvertFromTitle(String tituloAnuncio) {
        new Firebase(URL_ANUNCIOS).orderByChild("titulo").equalTo(tituloAnuncio.trim()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Anuncio a = dataSnapshot.getChildren().iterator().next().getValue(Anuncio.class);
                ((ConversationPresenter) presenter).advertObtained(a);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void getAdvertClickedFromMap(String advertKey) {
        new Firebase(URL_ANUNCIOS).child(advertKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Anuncio a = dataSnapshot.getValue(Anuncio.class);
                ((MainPresenter) presenter).advertClickedFromMapObtained(a);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void filterRequest(final String[] tipoVivienda, final int minPrice, final int maxPrice, final int minSize, final int maxSize, final ArrayList<Prestacion> prestaciones, final String provincia, final String poblacion) {
        final ArrayList<Anuncio> filteredAdverts = new ArrayList<>();

        new Firebase(URL_ANUNCIOS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Anuncio a;
                boolean agregar;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    a = data.getValue(Anuncio.class);
                    if (!a.getAnunciante().equals(currentUser.getKey()) && a.getTamanio() >= minSize) {
                        agregar = anuncioCumpleFiltroPoblacion(poblacion, a);
                        if (agregar && !TextUtils.isEmpty(provincia))
                            agregar = anuncioCumpleFiltroProvincia(provincia, a);
                        if (agregar)
                            agregar = anuncioCumpleFiltroTipoVivienda(tipoVivienda, a);
                        if (agregar)
                            agregar = anuncioCumpleFiltroPrecio(minPrice, maxPrice, a);
                        if (agregar)
                            agregar = anuncioCumpleFiltroTamanio(minSize, maxSize, a);
                        if (prestaciones.size() > 0 && agregar)
                            agregar = anuncioCumpleFiltroPrestaciones(prestaciones, a);
                        if (agregar)
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

    private boolean anuncioCumpleFiltroPoblacion(String poblacion, Anuncio a) {
        boolean r = false;
        if (a.getPoblacion().equalsIgnoreCase(poblacion) || a.getPoblacion().toLowerCase().contains(poblacion.toLowerCase()))
            r = true;
        return r;
    }

    private boolean anuncioCumpleFiltroProvincia(String provincia, Anuncio a) {
        boolean r = false;
        if (a.getProvincia().equalsIgnoreCase(provincia) || a.getProvincia().toLowerCase().contains(provincia.toLowerCase()))
            r = true;
        return r;
    }

    private boolean anuncioCumpleFiltroTipoVivienda(String[] tipoVivienda, Anuncio a) {
        boolean r = false;
        if (tipoVivienda[0] == null && tipoVivienda[1] == null && tipoVivienda[2] == null)
            r = true;
        else if ((tipoVivienda[0] != null && a.getTipo_vivienda().equals(tipoVivienda[0]) || tipoVivienda[1] != null && a.getTipo_vivienda().equals(tipoVivienda[1]) || tipoVivienda[2] != null && a.getTipo_vivienda().equals(tipoVivienda[2])))
            r = true;
        return r;
    }

    private boolean anuncioCumpleFiltroPrecio(int minPrice, int maxPrice, Anuncio a) {
        boolean r = false;
        if (a.getPrecio() >= minPrice && (maxPrice == 1000 || a.getPrecio() <= maxPrice))
            r = true;
        return r;
    }

    private boolean anuncioCumpleFiltroTamanio(int minSize, int maxSize, Anuncio a) {
        boolean r = false;
        if (a.getTamanio() >= minSize && (maxSize == 1000 || a.getTamanio() <= maxSize))
            r = true;
        return r;
    }

    private boolean anuncioCumpleFiltroPrestaciones(ArrayList<Prestacion> prestaciones, Anuncio a) {
        boolean r = true, encontrada, salir = false;
        int i, j;
        if (a.getPrestaciones().size() == 0)
            r = false;
        else {
            if (a.getPrestaciones().size() >= prestaciones.size()) // si el anuncio tiene la misma cantidad de prestaciones o más que la lista de prestaciones por las que filtrar
                for (i = 0; !salir && i < prestaciones.size(); i++) { // voy cogiendo las prestaciones que debe tener el anuncio
                    encontrada = false;
                    for (j = 0; !encontrada && j < a.getPrestaciones().size(); j++) // recorro la lista de prestaciones del anuncio
                        if (prestaciones.get(i).equals(a.getPrestaciones().get(j))) // si encuentra la prestación por la que se tiene que filtrar en la lista de prestaciones del anuncio
                            encontrada = true; // el centinela indica que la encontró y pasa a la siguiente prestación a buscar

                    if (!encontrada) { // si después de recorrer la lista de prestaciones del anuncio no encontró la prestación por la que hay que filtrar
                        salir = true; // el centinela indica que se debe salir del bucle
                        r = false; // el resultado es false
                    }
                }
        }
        return r;
    }

    public void getLocations(GeoLocation centerPosition, double radius) {
        GeoFire g = new GeoFire(new Firebase(URL_LOCATIONS));
        final Firebase f = new Firebase(URL_ANUNCIOS);
        geoQuery = g.queryAtLocation(centerPosition, radius);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, final GeoLocation location) {
                f.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Anuncio a = dataSnapshot.getValue(Anuncio.class);
                        if (!a.getKey().contains(currentUser.getKey()))
                            ((MainPresenter) presenter).locationObtained(a, location);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(FirebaseError error) {

            }
        });
    }

    public void detachGeoLocationListener() {
        if (geoQuery != null)
            geoQuery.removeAllListeners();
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
