package c.proyecto.mvp_models;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_presenters.AdvertsDetailsPresenter;
import c.proyecto.mvp_presenters.ConversationPresenter;
import c.proyecto.mvp_presenters.MainPresenter;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Message;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.pojo.MessagePojoWithoutAnswer;
import c.proyecto.pojo.Usuario;

public class MessagesFirebaseManager {

    private static final String URL_CONVERSACIONES = "https://proyectofinaldam.firebaseio.com/conversaciones/";
    private static final String URL_MSG_SIN_RESP = "https://proyectofinaldam.firebaseio.com/mensajesEnviadosSinRespuesta/";
    private static final String URL_USERS = "https://proyectofinaldam.firebaseio.com/usuarios/";
    private static final int MESSAGES_LIMIT_CONVER = 20;

    private static Firebase mFirebaseReceivedMessages, mFirebaseConversations, mFirebaseMessagesWithoutAnswer;
    private static ValueEventListener mListenerMessagesWithoutAnswer;
    private static ChildEventListener mListenerConversation, mListenerReceivedMessages;
    private HashMap<Query, ChildEventListener> listenersInternosConvers, listenersInternosMessagesSinResp, listenerInternosMessages;

    private MyPresenter presenter;
    private Usuario currentUser;

    public MessagesFirebaseManager(MyPresenter presenter, Usuario currentUser) {
        this.presenter = presenter;
        this.currentUser = currentUser;
        listenerInternosMessages = new HashMap<>();
        listenersInternosMessagesSinResp = new HashMap<>();
        listenersInternosConvers = new HashMap<>();
    }

    public void initializeMessagesListeners() {
        // El listener se inicializa al entrar en la actividad de mensajes. El listener permanecerá a la escucha hasta que se cierre sesión. Entonces:
        if (mFirebaseReceivedMessages == null)
            mFirebaseReceivedMessages = new Firebase(URL_CONVERSACIONES).child(currentUser.getKey()); // conversaciones/keyUser --> para leer todos los mensajer que ha recibido el usuario
        if (mListenerReceivedMessages == null)
            mListenerReceivedMessages = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    final MessagePojo mensaje = new MessagePojo(); // creo el mensajePojo que será agregado al adaptador del recyclerView de mensajes
                    final String emisor_titleAdvert = dataSnapshot.getKey(); // obtengo la key del nodo, que contiene el id de quien mandó el mensaje y el titulo del anuncio sobre el que habló
                    String[] campos = emisor_titleAdvert.split("_"); // separo la cadena en campos
                    final String emisorKey = campos[0]; // la key del emisor es el campo 0
                    String titulo = "";
                    for (int i = 1; i < campos.length; i++) // recorro los campos, exluyendo el 0 que es el id del emisor, y lo concateno con espacios para obtener el titulo original del anuncio
                        titulo += campos[i] + " ";

                    checkOnFirstMessageResponse(currentUser.getKey(), emisorKey, emisor_titleAdvert); // compruebo si el mensaje que estoy leyendo es un mensaje recibido en respuesta a una conversación iniciada por el usuario

                    mensaje.setTituloAnuncio(titulo);
                    new Firebase(URL_USERS).child(emisorKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario emisor = dataSnapshot.getValue(Usuario.class);
                            mensaje.setEmisor(emisor); // asigno el emisor obtenido al mensaje que será devuelto al adaptador
                            mensaje.setKeyReceptor(currentUser.getKey()); // asigno al receptor

                            // obtengo el último mensaje de la conversación, para mostrar el último en la actividad de mensajes


                            // este listener es el que estará a la escucha por si se recibe un nuevo mensaje en una conversación ya existente. El primer listener estará a la escucha y obtendrá los
                            // nuevos mensajes recibidos, éste en cambio obtendra los mensajes recibidos sobre una conversación existente, para mantener el adaptador en la vista de mensajes actualizado
                            Query fInterno = new Firebase(URL_CONVERSACIONES).child(currentUser.getKey()).child(emisor_titleAdvert).limitToLast(1);
                            ChildEventListener listenerInterno = new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    Message m = dataSnapshot.getValue(Message.class);
                                    mensaje.setContenido(m.getContenido());
                                    mensaje.setFecha(new Date(m.getFecha()));
                                    mensaje.setKey(dataSnapshot.getKey());
                                    ((MainPresenter) presenter).userMessageHasBeenObtained(mensaje);
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
                            fInterno.addChildEventListener(listenerInterno);
                            listenerInternosMessages.put(fInterno, listenerInterno);
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
        mFirebaseReceivedMessages.addChildEventListener(mListenerReceivedMessages);
    }

    // compruebo si el mensaje que he recibido es un mensaje en respuesta a una conversación iniciada por el usuario. Si es así, elimino el mensaje de la lista de mensajes enviados sin respuesta.
    private void checkOnFirstMessageResponse(final String userKey, final String emisorKey, final String emisor_titleAdvert) {
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

    public void getUserMessages() {
        getUserMessagesReceived();
        getUserMessagesSendedWithoutAnswer();
    }

    private void getUserMessagesReceived() {
        mFirebaseReceivedMessages.removeEventListener(mListenerReceivedMessages);
        mFirebaseReceivedMessages.addChildEventListener(mListenerReceivedMessages);
    }

    private void getUserMessagesSendedWithoutAnswer() {
        // Obtengo los mensajes que el usuario ha enviado y que aún no tienen respuesta
        new Firebase(URL_MSG_SIN_RESP).child(currentUser.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final String keyReceptor = data.getKey(); // obtengo el receptor
                    // voy a la rama de mensajes que le he enviado a ese receptor
                    new Firebase(URL_MSG_SIN_RESP).child(currentUser.getKey()).child(keyReceptor).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data2 : dataSnapshot.getChildren())
                                // leo los anuncios sobre los que le he enviado el mensaje a ese receptor
                                new Firebase(URL_MSG_SIN_RESP).child(currentUser.getKey()).child(keyReceptor).child(data2.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        HashMap map = dataSnapshot.getValue(HashMap.class);
                                        String msgEnviado = (String) map.keySet().iterator().next();
                                        // obtengo la referencia del mensaje que tiene que tener el receptor. Es decir, la referencia del mensaje que le envió en usuario
                                        final String tituloAnuncio = dataSnapshot.getKey().trim().replace("_", " ");

                                        // me voy a la rama de mensajes del receptor y obtengo el último mensaje que le envío el usuario
                                        // este listener sirve para mantener actualizado el adaptador. Es decir, si el usuario envía un nuevo mensaje y el receptor sigue sin responder que aparezca ese último
                                        // mensaje enviado
                                        Query fInterno = new Firebase(URL_CONVERSACIONES).child(keyReceptor).child(msgEnviado).limitToLast(1);
                                        ChildEventListener listenerInterno = new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                Message m = dataSnapshot.getValue(Message.class);
                                                final MessagePojoWithoutAnswer mensaje = new MessagePojoWithoutAnswer();
                                                mensaje.setContenido(m.getContenido());
                                                mensaje.setFecha(new Date(m.getFecha()));
                                                mensaje.setKey(dataSnapshot.getKey());
                                                mensaje.setEmisor(currentUser);
                                                mensaje.setTituloAnuncio(tituloAnuncio);
                                                new Firebase(URL_USERS).child(keyReceptor).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Usuario receptor = dataSnapshot.getValue(Usuario.class);
                                                        mensaje.setReceptor(receptor);
                                                        mensaje.setKeyReceptor(receptor.getKey());
                                                        ((MainPresenter) presenter).userMessageHasBeenObtained(mensaje);
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
                                        fInterno.addChildEventListener(listenerInterno);
                                        listenersInternosMessagesSinResp.put(fInterno, listenerInterno);
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

    // obtengo la conversación entera sobre un anuncio concreto
    public void getUserConversation(final MessagePojo m) {
        String nodoAsunto;
        if (mFirebaseConversations != null)
            mFirebaseConversations.limitToLast(MESSAGES_LIMIT_CONVER).removeEventListener(mListenerConversation);
        nodoAsunto = m.getEmisor().getKey() + "_" + m.getTituloAnuncio().trim().replace(" ", "_");
        if (m instanceof MessagePojoWithoutAnswer)
            mFirebaseConversations = new Firebase(URL_CONVERSACIONES).child(m.getKeyReceptor()).child(nodoAsunto);
        else
            mFirebaseConversations = new Firebase(URL_CONVERSACIONES).child(currentUser.getKey()).child(nodoAsunto);

        mListenerConversation = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessagePojo mensaje;
                if (m instanceof MessagePojoWithoutAnswer) {
                    mensaje = new MessagePojoWithoutAnswer();
                    mensaje.setKeyReceptor(m.getKeyReceptor());
                    mensaje.setEmisor(currentUser);
                } else {
                    mensaje = new MessagePojo();
                    mensaje.setKeyReceptor(currentUser.getKey());
                    mensaje.setEmisor(m.getEmisor());
                }
                mensaje.setKey(dataSnapshot.getKey());
                mensaje.setTituloAnuncio(m.getTituloAnuncio());
                Message mAux = dataSnapshot.getValue(Message.class);
                mensaje.setFecha(new Date(mAux.getFecha()));
                mensaje.setContenido(mAux.getContenido());
                ((ConversationPresenter) presenter).messageHasBeenObtained(mensaje);

                String nodoAsunto2 = currentUser.getKey() + "_" + m.getTituloAnuncio().trim().replace(" ", "_");
                nodoAsunto2 = nodoAsunto2.substring(0, nodoAsunto2.length());

                if (listenersInternosConvers.size() == 0) {
                    Query fInterno = new Firebase(URL_CONVERSACIONES).child(m.getEmisor().getKey()).child(nodoAsunto2);
                    ChildEventListener listenerInterno = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            MessagePojo mensaje = new MessagePojo();
                            mensaje.setKey(dataSnapshot.getKey());
                            mensaje.setKeyReceptor(m.getEmisor().getKey());
                            mensaje.setEmisor(currentUser);
                            mensaje.setTituloAnuncio(m.getTituloAnuncio());
                            Message mAux = dataSnapshot.getValue(Message.class);
                            mensaje.setFecha(new Date(mAux.getFecha()));
                            mensaje.setContenido(mAux.getContenido());
                            ((ConversationPresenter) presenter).messageHasBeenObtained(mensaje);
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
                    fInterno.limitToLast(MESSAGES_LIMIT_CONVER).addChildEventListener(listenerInterno);
                    listenersInternosConvers.put(fInterno, listenerInterno);
                }
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

    public void sendMessage(MessagePojo m, String keyReceptor, boolean isFirstMessageSended) {
        String nodoAsunto = m.getEmisor().getKey() + "_" + m.getTituloAnuncio().trim().replace(" ", "_");
        nodoAsunto = nodoAsunto.substring(0, nodoAsunto.length());
        new Firebase(URL_CONVERSACIONES).child(keyReceptor).child(nodoAsunto).push().setValue(new Message(m.getFecha(), m.getContenido()));
        if (isFirstMessageSended) {
            Map<String, Boolean> map = new HashMap();
            map.put(nodoAsunto, true);
            new Firebase(URL_MSG_SIN_RESP).child(m.getEmisor().getKey()).child(keyReceptor).child(m.getTituloAnuncio().replace(" ", "_")).setValue(map);
        }
    }

    public void removeMessage(MessagePojo m) {
        String nodoAsunto = m.getEmisor().getKey() + "_" + m.getTituloAnuncio().replace(" ", "_");
        nodoAsunto = nodoAsunto.substring(0, nodoAsunto.length());
        new Firebase(URL_CONVERSACIONES).child(m.getKeyReceptor()).child(nodoAsunto).child(m.getKey()).setValue(null);
    }


    public void getMessageIfConverExist(Anuncio anuncio) {
        Firebase f = new Firebase(URL_CONVERSACIONES).child(currentUser.getKey()).child(anuncio.getAnunciante() + "_" + anuncio.getTitulo().trim());
        f.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MessagePojo m = null;
                if (dataSnapshot.getValue() != null)
                    m = dataSnapshot.getChildren().iterator().next().getValue(MessagePojo.class);
                ((AdvertsDetailsPresenter) presenter).messageIfConverExistObtained(m);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void detachConversationListeners() {
        if (mFirebaseConversations != null) {
            mFirebaseConversations.removeEventListener(mListenerConversation);
            mListenerConversation = null;
            mFirebaseConversations = null;
        }

        for (Query f : listenersInternosConvers.keySet())
            f.removeEventListener(listenersInternosConvers.get(f));

        listenersInternosConvers.clear();
        listenersInternosConvers = null;
    }

    public void detachMessagesListeners() {
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

        for (Query f : listenerInternosMessages.keySet())
            f.removeEventListener(listenerInternosMessages.get(f));

        listenerInternosMessages.clear();
        listenerInternosMessages = null;

        for (Query f : listenersInternosMessagesSinResp.keySet())
            f.removeEventListener(listenersInternosMessagesSinResp.get(f));

        listenersInternosMessagesSinResp.clear();
        listenersInternosMessagesSinResp = null;
    }
}
