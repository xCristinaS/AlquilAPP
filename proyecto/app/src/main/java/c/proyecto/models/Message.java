package c.proyecto.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Date;
import java.util.Iterator;

import c.proyecto.pojo.MessagePojo;
import c.proyecto.presenters.ConversationPresenter;
import c.proyecto.presenters.MainPresenter;

/**
 * Created by Cristina on 23/03/2016.
 */
public class Message implements Parcelable {

    private static final String URL_CONVERSACIONES = "https://proyectofinaldam.firebaseio.com/conversaciones/";
    private static final String URL_USERS = "https://proyectofinaldam.firebaseio.com/usuarios/";

    private long fecha;
    private String contenido;

    private static Firebase mFirebase;
    private static ValueEventListener mListener;
    private static Firebase mFirebaseConversations;
    private static ChildEventListener mListenerConversation;


    public Message() {

    }

    public Message(Date fecha, String contenido) {
        this.fecha = fecha.getTime();
        this.contenido = contenido;
    }

    public static void getUserMessages(final Usuario user, final MainPresenter presenter) {
        mFirebase = new Firebase(URL_CONVERSACIONES).child(user.getKey());
        mListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final MessagePojo mensaje = new MessagePojo();
                    final String emisor_titleAdvert = data.getKey();
                    String[] campos = emisor_titleAdvert.split("_");
                    String emisorKey = campos[0];
                    String titulo = "";
                    for (int i = 1; i < campos.length; i++)
                        titulo += campos[i] + " ";

                    mensaje.setTituloAnuncio(titulo);
                    new Firebase(URL_USERS).child(emisorKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario emisor = dataSnapshot.getValue(Usuario.class);
                            mensaje.setEmisor(emisor);
                            mensaje.setKeyReceptor(user.getKey());

                            new Firebase(URL_CONVERSACIONES).child(user.getKey()).child(emisor_titleAdvert).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        Message m = data.getValue(Message.class);
                                        mensaje.setContenido(m.getContenido());
                                        mensaje.setFecha(new Date(m.getFecha()));
                                        mensaje.setKey(dataSnapshot.getKey());
                                    }
                                    presenter.userMessageHasBeenObtained(mensaje);
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });

                            new Firebase(URL_CONVERSACIONES).child(user.getKey()).child(emisor_titleAdvert).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    Message m = dataSnapshot.getValue(Message.class);
                                    mensaje.setContenido(m.getContenido());
                                    mensaje.setFecha(new Date(m.getFecha()));
                                    mensaje.setKey(dataSnapshot.getKey());
                                    presenter.userMessageHasBeenObtained(mensaje);
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
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
        };
        mFirebase.addValueEventListener(mListener);
    }

    public static void getUserConversation(final Usuario u, final MessagePojo m, final ConversationPresenter presenter) {
        String nodoAsunto = m.getEmisor().getKey() + "_" + m.getTituloAnuncio().replace(" ", "_");
        nodoAsunto = nodoAsunto.substring(0, nodoAsunto.length() - 1);
        mFirebaseConversations = new Firebase(URL_CONVERSACIONES).child(u.getKey()).child(nodoAsunto);
        mListenerConversation = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessagePojo mensaje = new MessagePojo();
                mensaje.setKey(dataSnapshot.getKey());
                mensaje.setKeyReceptor(u.getKey());
                mensaje.setEmisor(m.getEmisor());
                mensaje.setTituloAnuncio(m.getTituloAnuncio());
                Message mAux = dataSnapshot.getValue(Message.class);
                mensaje.setFecha(new Date(mAux.getFecha()));
                mensaje.setContenido(mAux.getContenido());
                presenter.messageHasBeenObtained(mensaje);

                String nodoAsunto2 = u.getKey() + "_" + m.getTituloAnuncio().replace(" ", "_");
                nodoAsunto2 = nodoAsunto2.substring(0, nodoAsunto2.length() - 1);
                new Firebase(URL_CONVERSACIONES).child(m.getEmisor().getKey()).child(nodoAsunto2).limitToLast(10).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        MessagePojo mensaje = new MessagePojo();
                        mensaje.setKey(dataSnapshot.getKey());
                        mensaje.setKeyReceptor(m.getEmisor().getKey());
                        mensaje.setEmisor(u);
                        mensaje.setTituloAnuncio(m.getTituloAnuncio());
                        Message mAux = dataSnapshot.getValue(Message.class);
                        mensaje.setFecha(new Date(mAux.getFecha()));
                        mensaje.setContenido(mAux.getContenido());
                        presenter.messageHasBeenObtained(mensaje);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        mFirebaseConversations.limitToLast(10).addChildEventListener(mListenerConversation);
    }

    public static void sendMessage(MessagePojo m, String keyReceptor) {
        String nodoAsunto = m.getEmisor().getKey() + "_" + m.getTituloAnuncio().replace(" ", "_");
        nodoAsunto = nodoAsunto.substring(0, nodoAsunto.length() - 1);
        new Firebase(URL_CONVERSACIONES).child(keyReceptor).child(nodoAsunto).push().setValue(new Message(m.getFecha(), m.getContenido()));
    }

    public static void removeMessage(MessagePojo m) {
        String nodoAsunto = m.getEmisor().getKey() + "_" + m.getTituloAnuncio().replace(" ", "_");
        nodoAsunto = nodoAsunto.substring(0, nodoAsunto.length() - 1);
        new Firebase(URL_CONVERSACIONES).child(m.getKeyReceptor()).child(nodoAsunto).child(m.getKey()).setValue(null);
    }

    public static void detachConversationListeners() {
        mFirebaseConversations.removeEventListener(mListenerConversation);
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.fecha);
        dest.writeString(this.contenido);
    }

    protected Message(Parcel in) {
        this.fecha = in.readLong();
        this.contenido = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
