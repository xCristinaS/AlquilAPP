package c.proyecto.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import c.proyecto.interfaces.MyModel;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.pojo.MessagePojoWithoutAnswer;
import c.proyecto.presenters.ConversationPresenter;
import c.proyecto.presenters.MainPresenter;
import retrofit2.http.Body;

/**
 * Created by Cristina on 23/03/2016.
 */
public class Message implements Parcelable, MyModel {

    private static final String URL_CONVERSACIONES = "https://proyectofinaldam.firebaseio.com/conversaciones/";
    private static final String URL_MSG_SIN_RESP = "https://proyectofinaldam.firebaseio.com/mensajesEnviadosSinRespuesta/";
    private static final String URL_USERS = "https://proyectofinaldam.firebaseio.com/usuarios/";
    private static final int MESSAGES_LIMIT_CONVER = 20;

    private long fecha;
    private String contenido;

    private static Firebase mFirebaseReceivedMessages, mFirebaseConversations, mFirebaseMessagesWithoutAnswer;
    private static ValueEventListener mListenerReceivedMessages, mListenerMessagesWithoutAnswer;
    private static ChildEventListener mListenerConversation;

    public Message() {

    }

    public Message(Date fecha, String contenido) {
        this.fecha = fecha.getTime();
        this.contenido = contenido;
    }

    public static void getUserMessages(Usuario user, MainPresenter presenter) {
        getUserMessagesReceived(user, presenter);
        getUserMessagesSendedWithoutAnswer(user, presenter);
    }

    private static void getUserMessagesReceived(final Usuario user, final MainPresenter presenter) {
        if (mFirebaseReceivedMessages == null)
            mFirebaseReceivedMessages = new Firebase(URL_CONVERSACIONES).child(user.getKey());
        if (mListenerReceivedMessages == null)
            mListenerReceivedMessages = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        final MessagePojo mensaje = new MessagePojo();
                        final String emisor_titleAdvert = data.getKey();
                        String[] campos = emisor_titleAdvert.split("_");
                        final String emisorKey = campos[0];
                        String titulo = "";
                        for (int i = 1; i < campos.length; i++)
                            titulo += campos[i] + " ";

                        checkOnFirstMessageResponse(user.getKey(), emisorKey, emisor_titleAdvert);

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
        mFirebaseReceivedMessages.addValueEventListener(mListenerReceivedMessages);
    }

    private static void checkOnFirstMessageResponse(final String userKey, final String emisorKey, final String emisor_titleAdvert) {
        final String title = emisor_titleAdvert.substring(emisor_titleAdvert.indexOf("_") + 1, emisor_titleAdvert.length());
        new Firebase(URL_MSG_SIN_RESP).child(userKey).child(emisorKey).child(title).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap map = dataSnapshot.getValue(HashMap.class);
                if (map != null) {
                    String msgPreviamenteEnviado = (String) map.keySet().iterator().next();
                    if ((userKey + "_" + title).equals(msgPreviamenteEnviado))
                        new Firebase(URL_MSG_SIN_RESP).child(userKey).child(emisorKey).child(title).setValue(null);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private static void getUserMessagesSendedWithoutAnswer(final Usuario user, final MainPresenter presenter) {
        new Firebase(URL_MSG_SIN_RESP).child(user.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final String receptor = data.getKey();
                    new Firebase(URL_MSG_SIN_RESP).child(user.getKey()).child(receptor).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data2 : dataSnapshot.getChildren())
                                new Firebase(URL_MSG_SIN_RESP).child(user.getKey()).child(receptor).child(data2.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        HashMap map = dataSnapshot.getValue(HashMap.class);
                                        String msgEnviado = (String) map.keySet().iterator().next();
                                        final String tituloAnuncio = dataSnapshot.getKey().trim().replace("_", " ");

                                        new Firebase(URL_CONVERSACIONES).child(receptor).child(msgEnviado).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                    Message m = data.getValue(Message.class);
                                                    final MessagePojoWithoutAnswer mensaje = new MessagePojoWithoutAnswer();
                                                    mensaje.setContenido(m.getContenido());
                                                    mensaje.setFecha(new Date(m.getFecha()));
                                                    mensaje.setKey(dataSnapshot.getKey());
                                                    mensaje.setEmisor(user);
                                                    mensaje.setTituloAnuncio(tituloAnuncio);
                                                    new Firebase(URL_USERS).child(receptor).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            mensaje.setKeyReceptor(dataSnapshot.getValue(Usuario.class).getKey());
                                                            presenter.userMessageHasBeenObtained(mensaje);
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

                                        new Firebase(URL_CONVERSACIONES).child(receptor).child(msgEnviado).addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                Message m = dataSnapshot.getValue(Message.class);
                                                final MessagePojoWithoutAnswer mensaje = new MessagePojoWithoutAnswer();
                                                mensaje.setContenido(m.getContenido());
                                                mensaje.setFecha(new Date(m.getFecha()));
                                                mensaje.setKey(dataSnapshot.getKey());
                                                mensaje.setEmisor(user);
                                                mensaje.setTituloAnuncio(tituloAnuncio);
                                                new Firebase(URL_USERS).child(receptor).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        mensaje.setKeyReceptor(dataSnapshot.getValue(Usuario.class).getKey());
                                                        presenter.userMessageHasBeenObtained(mensaje);
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
                                        });
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
        });
    }

    public static void getUserConversation(final Usuario u, final MessagePojo m, final ConversationPresenter presenter) {
        String nodoAsunto = m.getEmisor().getKey() + "_" + m.getTituloAnuncio().trim().replace(" ", "_");

        if (m instanceof MessagePojoWithoutAnswer)
            mFirebaseConversations = new Firebase(URL_CONVERSACIONES).child(m.getKeyReceptor()).child(nodoAsunto);
        else
            mFirebaseConversations = new Firebase(URL_CONVERSACIONES).child(u.getKey()).child(nodoAsunto);

        mListenerConversation = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessagePojo mensaje;
                if (m instanceof MessagePojoWithoutAnswer) {
                    mensaje = new MessagePojoWithoutAnswer();
                    mensaje.setKeyReceptor(m.getKeyReceptor());
                    mensaje.setEmisor(u);
                } else {
                    mensaje = new MessagePojo();
                    mensaje.setKeyReceptor(u.getKey());
                    mensaje.setEmisor(m.getEmisor());
                }
                mensaje.setKey(dataSnapshot.getKey());
                mensaje.setTituloAnuncio(m.getTituloAnuncio());
                Message mAux = dataSnapshot.getValue(Message.class);
                mensaje.setFecha(new Date(mAux.getFecha()));
                mensaje.setContenido(mAux.getContenido());
                presenter.messageHasBeenObtained(mensaje);

                String nodoAsunto2 = u.getKey() + "_" + m.getTituloAnuncio().trim().replace(" ", "_");
                nodoAsunto2 = nodoAsunto2.substring(0, nodoAsunto2.length());
                Query f;
                if (m instanceof MessagePojoWithoutAnswer)
                    f = new Firebase(URL_CONVERSACIONES).child(u.getKey()).child(nodoAsunto2).limitToLast(MESSAGES_LIMIT_CONVER);
                else
                    f = new Firebase(URL_CONVERSACIONES).child(m.getEmisor().getKey()).child(nodoAsunto2).limitToLast(MESSAGES_LIMIT_CONVER);
                f.addChildEventListener(new ChildEventListener() {
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
        mFirebaseConversations.limitToLast(MESSAGES_LIMIT_CONVER).addChildEventListener(mListenerConversation);
    }

    public static void sendMessage(MessagePojo m, String keyReceptor, boolean isFirstMessageSended) {
        String nodoAsunto = m.getEmisor().getKey() + "_" + m.getTituloAnuncio().trim().replace(" ", "_");
        nodoAsunto = nodoAsunto.substring(0, nodoAsunto.length());
        new Firebase(URL_CONVERSACIONES).child(keyReceptor).child(nodoAsunto).push().setValue(new Message(m.getFecha(), m.getContenido()));
        if (isFirstMessageSended) {
            Map<String, Boolean> map = new HashMap();
            map.put(nodoAsunto, true);
            new Firebase(URL_MSG_SIN_RESP).child(m.getEmisor().getKey()).child(keyReceptor).child(m.getTituloAnuncio().replace(" ", "_")).setValue(map);
        }
    }

    public static void removeMessage(MessagePojo m) {
        String nodoAsunto = m.getEmisor().getKey() + "_" + m.getTituloAnuncio().replace(" ", "_");
        nodoAsunto = nodoAsunto.substring(0, nodoAsunto.length());
        new Firebase(URL_CONVERSACIONES).child(m.getKeyReceptor()).child(nodoAsunto).child(m.getKey()).setValue(null);
    }

    public static void detachConversationListeners() {
        if (mFirebaseConversations != null) {
            mFirebaseConversations.removeEventListener(mListenerConversation);
            mListenerConversation = null;
            mFirebaseConversations = null;
        }
    }

    public static void detachMessagesListeners() {
        if (mFirebaseReceivedMessages != null) {
            mFirebaseReceivedMessages.removeEventListener(mListenerReceivedMessages);
            mListenerReceivedMessages = null;
            mFirebaseReceivedMessages = null;
        }

        if (mFirebaseMessagesWithoutAnswer != null) {
            mFirebaseMessagesWithoutAnswer.removeEventListener(mListenerMessagesWithoutAnswer);
            mListenerMessagesWithoutAnswer = null;
            mFirebaseMessagesWithoutAnswer = null;
        }
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
