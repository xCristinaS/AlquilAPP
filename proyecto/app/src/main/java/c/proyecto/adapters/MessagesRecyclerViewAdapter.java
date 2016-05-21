package c.proyecto.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import c.proyecto.R;
import c.proyecto.pojo.Message;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.pojo.MessagePojoWithoutAnswer;
import c.proyecto.pojo.Usuario;
import c.proyecto.utils.ComparatorMessages;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LIMIT_MESSAGES = 40;

    public interface OnMessagesAdapterItemClick {
        void onItemClick(MessagePojo mensaje);
    }

    public interface ConversationManager {
        void removeMessage(MessagePojo m);
    }

    private List<MessagePojo> mDatos;
    private OnMessagesAdapterItemClick listenerItemClick;
    private ConversationManager listenerConverManager;
    private boolean isAConversation;
    private String mKeyCurrentUser;
    private ComparatorMessages messagesComp = new ComparatorMessages();

    public MessagesRecyclerViewAdapter(boolean isAConversation, String keyCurrentUser) {
        mDatos = new ArrayList<>();
        mKeyCurrentUser = keyCurrentUser;
        this.isAConversation = isAConversation;
    }

    @Override
    public int getItemViewType(int position) {
        return mDatos.get(position).getType(mKeyCurrentUser);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View anuncioView;
        RecyclerView.ViewHolder viewHolder;

        if (!isAConversation) {
            anuncioView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
            viewHolder = new MessagesViewHolder(anuncioView);
        } else {
            if (viewType == MessagePojo.TIPO_RECIBIDO) {
                anuncioView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received_message, parent, false);
                viewHolder = new ChatViewHolder(anuncioView);
            } else {
                anuncioView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sended_message, parent, false);
                viewHolder = new ChatViewHolder(anuncioView);
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (!isAConversation) {
            if (holder instanceof MessagesViewHolder)
                ((MessagesViewHolder) holder).onBind((MessagePojo) mDatos.get(position));
        } else if (mDatos.get(position) instanceof MessagePojo)
            ((ChatViewHolder) holder).onBind((MessagePojo) mDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatos.size();
    }

    public void setListenerItemClick(OnMessagesAdapterItemClick listenerItemClick) {
        this.listenerItemClick = listenerItemClick;
    }

    public void setListenerConverManager(ConversationManager listenerConverManager) {
        this.listenerConverManager = listenerConverManager;
    }

    public List<MessagePojo> getmDatos() {
        return mDatos;
    }

    class MessagesViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imgEmisor;
        private TextView lblNombreEmisor, lblFecha, lblContenido, lblTituloAnuncio;

        public MessagesViewHolder(View itemView) {
            super(itemView);
            imgEmisor = (CircleImageView) itemView.findViewById(R.id.imgEmisor);
            lblNombreEmisor = (TextView) itemView.findViewById(R.id.lblNombreEmisor);
            lblFecha = (TextView) itemView.findViewById(R.id.lblFecha);
            lblContenido = (TextView) itemView.findViewById(R.id.lblMessage);
            lblTituloAnuncio = (TextView) itemView.findViewById(R.id.lblTituloAnuncio);
        }

        public void onBind(final MessagePojo m) {
            SimpleDateFormat fecha = new SimpleDateFormat("dd/MM", Locale.getDefault());
            SimpleDateFormat hora = new SimpleDateFormat("HH:mm", Locale.getDefault());

            // Consigue el long del día de hoy a las 00:00
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            Date hoy = cal.getTime();

            //Si la fecha del mensaje es posterior a las 00:00 de hoy, se mostrará solo la hora del mensaje
            if (m.getFecha().getTime() >= hoy.getTime())
                lblFecha.setText(hora.format(m.getFecha()));
            else//sino la fecha
                lblFecha.setText(fecha.format(m.getFecha()));

            if (m instanceof MessagePojoWithoutAnswer) {
                Usuario receptor = ((MessagePojoWithoutAnswer) m).getReceptor();
                if (receptor.getFoto() != null)
                    Picasso.with(itemView.getContext()).load(receptor.getFoto()).into(imgEmisor);
                else
                    Picasso.with(itemView.getContext()).load(R.drawable.default_user).into(imgEmisor);
            } else {
                if (m.getEmisor().getFoto() != null)
                    Picasso.with(itemView.getContext()).load(m.getEmisor().getFoto()).into(imgEmisor);
                else
                    Picasso.with(itemView.getContext()).load(R.drawable.default_user).into(imgEmisor);
            }

            lblTituloAnuncio.setText(m.getTituloAnuncio());
            lblContenido.setText(m.getContenido());

            if (m instanceof MessagePojoWithoutAnswer)
                lblNombreEmisor.setText(((MessagePojoWithoutAnswer) m).getReceptor().getNombre());
            else
                lblNombreEmisor.setText(m.getEmisor().getNombre());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenerItemClick.onItemClick((MessagePojo) mDatos.get(mDatos.indexOf(m)));
                }
            });
        }
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        private TextView lblMessage, lblDate;


        public ChatViewHolder(View itemView) {
            super(itemView);
            lblMessage = (TextView) itemView.findViewById(R.id.lblMessage);
            lblDate = (TextView) itemView.findViewById(R.id.lblDate);
        }

        public void onBind(final MessagePojo m) {
            SimpleDateFormat fecha = new SimpleDateFormat("dd/MM", Locale.getDefault());
            SimpleDateFormat hora = new SimpleDateFormat("HH:mm", Locale.getDefault());

            // Consigue el long del día de hoy a las 00:00
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            Date hoy = cal.getTime();

            //Si la fecha del mensaje es posterior a las 00:00 de hoy, se mostrará solo la hora del mensaje
            if (m.getFecha().getTime() >= hoy.getTime())
                lblDate.setText(hora.format(m.getFecha()));
            else//sino la fecha
                lblDate.setText(fecha.format(m.getFecha()));

            lblMessage.setText(m.getContenido());
        }
    }

    //Manejo del Adaptador
    public void addItem(MessagePojo m) {
        boolean opHecha = false;

        if (!isAConversation) // si no estoy en conversationActivity y recibo un nuevo mensaje
            for (int i = 0; !opHecha && i < mDatos.size(); i++) // recorro los mensajes que tengo en mDatos
                if (mDatos.get(i) instanceof MessagePojo) // esta comprobación es para que no pete cuando llegue a un elemento "cabecera"
                    // si el emisor del mensaje que tengo es el mismo que el emisor del mensaje que me viene, y el titulo del anuncio es el mismo en el mensaje que tengo y en el que me viene
                    if (((MessagePojo) mDatos.get(i)).getEmisor().getKey().equals(m.getEmisor().getKey()) && ((MessagePojo) mDatos.get(i)).getTituloAnuncio().equals(m.getTituloAnuncio())) {
                        mDatos.remove(mDatos.get(i)); // borro ese mensaje
                        opHecha = true; // paro de recorrer la lista
                    }

        mDatos.add(0, m); // lo agrego detrás de la cabecera de mensajes sin respuesta

        if (isAConversation && mDatos.size() == LIMIT_MESSAGES) { // si estoy en conversación activity y he el adaptador contiene tantos elementos como indica el límite
            listenerConverManager.removeMessage((MessagePojo) mDatos.remove(0)); // borro el primer mensaje que recibí de la bdd
            mDatos.remove(0); // lo borro del adaptador
            notifyItemRemoved(0); // notifico
        }
        Collections.sort(mDatos, messagesComp);
        notifyDataSetChanged();
        //orderData(); // ORDENAR LOS MENSAJES DEL ADAPTADOR
    }
/*
    private void orderData() {
        if (!isAConversation) { // si no estoy en conversationActivity, los mensajes deben aparecer en orden de más reciente a más antiguos, para ello:
            List<MessagePojo> aux = new ArrayList<>(), aux2 = new ArrayList<>(); // creo dos listas auxiliares. La primera para ordenar los mensajes recibidos. La segunda para ordenar los mensajes enviados sin respuesta
            int hasta = mDatos.indexOf(cabeceraMensajesSinRespuesta);  // obtengo el índice de la cabecera de mensajesSinRespuesta, porque ahí acaban los mensajes recibidos

            for (int i = mDatos.indexOf(cabeceraMensajesRecibidos) + 1; i < hasta; i++) // recorro los mensajes que hay entre la cabecera de mensajes recibidos y la de mensajes enviados sin respuesta
                if (mDatos.get(i) instanceof MessagePojo)
                    aux.add((MessagePojo) mDatos.get(i)); // los agrego a la primera lista auxiliar

            Collections.sort(aux, messagesComp); // ordeno esa lista

            for (int i = mDatos.indexOf(cabeceraMensajesSinRespuesta) + 1; i < mDatos.size(); i++) // recorro los mensajes desde la cabecera de mensajes sin respuesta hasta el final
                if (mDatos.get(i) instanceof MessagePojo)
                    aux2.add((MessagePojo) mDatos.get(i)); // los agrego a la segunda lista auxiliar.

            Collections.sort(aux2, messagesComp); // ordeno esa segunda lista

            mDatos.clear(); // limpio el adaptador
            mDatos.add(cabeceraMensajesRecibidos); // agrego la cabecera de mensajes recibidos
            mDatos.addAll(aux); // agrego la lista ordenada de los mensajes recibidos
            mDatos.add(cabeceraMensajesSinRespuesta); // agrego la cabecera de mensajes sin respuesta
            mDatos.addAll(aux2); // agrego la lista ordenada de los mensajes enviados sin respuestas
        } else { // si estoy en conversationActivity
            List<MessagePojo> aux = new ArrayList<>(); // me creo una lista auxiliar
            for (int i = 0; i < mDatos.size(); i++) // recorro todos los mensajes
                if (mDatos.get(i) instanceof MessagePojo)
                    aux.add((MessagePojo) mDatos.get(i)); // los agrego a la lista auxiliar. Este paso lo tengo que hacer porque el comparator compara MessagesPojo.

            Collections.sort(aux, messagesComp); // ordeno la lista
            mDatos.clear(); // limpio el contenido de mDatos
            mDatos.addAll(aux); // agrego toda la lista auxiliar con los mensajes de la conversación ya ordenada
        }
        notifyDataSetChanged();
    }
    */

    public void removeItem(MessagePojo m) {
        int position = mDatos.indexOf(m);
        mDatos.remove(m);
        notifyItemRemoved(position);
    }
}
