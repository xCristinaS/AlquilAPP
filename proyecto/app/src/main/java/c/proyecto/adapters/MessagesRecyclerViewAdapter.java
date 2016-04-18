package c.proyecto.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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
import c.proyecto.interfaces.IMessageAdapter;
import c.proyecto.pojo.MessageAdapterHeader;
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

    private List<IMessageAdapter> mDatos;
    private OnMessagesAdapterItemClick listenerItemClick;
    private ConversationManager listenerConverManager;
    private boolean isAConversation;
    private String mKeyCurrentUser;
    private MessageAdapterHeader cabeceraMensajesRecibidos, cabeceraMensajesSinRespuesta;
    private ComparatorMessages messagesComp = new ComparatorMessages();

    public MessagesRecyclerViewAdapter(boolean isAConversation, String keyCurrentUser) {
        mDatos = new ArrayList<>();
        mKeyCurrentUser = keyCurrentUser;
        if (!isAConversation) {
            cabeceraMensajesRecibidos = new MessageAdapterHeader("Mensajes recibidos");
            mDatos.add(cabeceraMensajesRecibidos);
            cabeceraMensajesSinRespuesta = new MessageAdapterHeader("Mensajes enviados, sin respuesta");
            mDatos.add(cabeceraMensajesSinRespuesta);
        }
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
            if (viewType == MessageAdapterHeader.TIPO_CABECERA) {
                anuncioView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cabecera_message, parent, false);
                viewHolder = new HeaderViewHolder(anuncioView);
            } else {
                anuncioView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
                viewHolder = new MessagesViewHolder(anuncioView);
            }
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
            else
                ((HeaderViewHolder) holder).onBind((MessageAdapterHeader) mDatos.get(position));
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

    public List<IMessageAdapter> getmDatos() {
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

            if (m instanceof MessagePojoWithoutAnswer){
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

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView lblTipoMessage;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            lblTipoMessage = (TextView) itemView.findViewById(R.id.lblTipoMessage);
        }

        public void onBind(final MessageAdapterHeader tipo) {
            lblTipoMessage.setText(tipo.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleMessages(tipo);
                }
            });
        }
    }



    private void toggleMessages(MessageAdapterHeader tipo) {
        int posicion = mDatos.indexOf(tipo);
        if (tipo.getHiddenChildren() == null) {
            int desde = posicion + 1;
            int cuantos = 0;
            ArrayList<MessagePojo> hijosOcultos = new ArrayList<>();
            while (desde < mDatos.size() && mDatos.get(desde) instanceof MessagePojo) {
                hijosOcultos.add((MessagePojo) mDatos.get(desde));
                mDatos.remove(desde);
                cuantos++;
            }
            tipo.setHiddenChildren(hijosOcultos);
            notifyItemRangeRemoved(desde, cuantos);
            //viewHolder.imgIndicador.setImageResource(R.drawable.ic_arrow_drop_down);
        } else {
            int indice = posicion + 1;
            for (MessagePojo m : tipo.getHiddenChildren()) {
                mDatos.add(indice, m);
                indice++;
            }
            notifyItemRangeInserted(posicion + 1, indice - posicion - 1);
            tipo.setHiddenChildren(null);
            //viewHolder.imgIndicador.setImageResource(R.drawable.ic_arrow_drop_up);
        }
    }

    //Manejo del Adaptador
    public void addItem(MessagePojo m) {
        boolean stop = false, isMessageWithoutAnswer = false, opHecha = false;
        for (int i = 0; !stop && i < mDatos.size(); i++)
            if (mDatos.get(i) instanceof MessagePojo)
                if (((MessagePojo) mDatos.get(i)).getKey().equals(m.getKey()))
                    stop = true;

        if (!stop && !isAConversation) {
            for (int i = 0; !opHecha && i < mDatos.size(); i++)
                if (mDatos.get(i) instanceof MessagePojo)
                    if (((MessagePojo) mDatos.get(i)).getEmisor().getKey().equals(m.getEmisor().getKey()) && ((MessagePojo) mDatos.get(i)).getTituloAnuncio().equals(m.getTituloAnuncio())) {
                        mDatos.remove(mDatos.get(i));
                        notifyItemRemoved(i);
                        opHecha = true;
                    }

            opHecha = false;
            for (int i = mDatos.size()-1; !opHecha && i >= 0; i--)
                if (mDatos.get(i) instanceof MessagePojoWithoutAnswer) {
                    if (((MessagePojoWithoutAnswer) mDatos.get(i)).getEmisor().getKey().equals(m.getKeyReceptor()) && ((MessagePojoWithoutAnswer) mDatos.get(i)).getTituloAnuncio().trim().equals(m.getTituloAnuncio().trim())) {
                        mDatos.remove(mDatos.get(i));
                        notifyItemRemoved(i);
                        opHecha = true;
                    }
                }
        }

        if (m instanceof MessagePojoWithoutAnswer)
            isMessageWithoutAnswer = true;

        if (!stop) {
            if (isMessageWithoutAnswer)
                mDatos.add(mDatos.indexOf(cabeceraMensajesSinRespuesta) + 1, m);
            else
                mDatos.add(mDatos.indexOf(cabeceraMensajesRecibidos) + 1, m);
        }

        if (isAConversation && mDatos.size() == LIMIT_MESSAGES)
            listenerConverManager.removeMessage((MessagePojo) mDatos.remove(1));

        if (!isAConversation) {
            List<MessagePojo> aux = new ArrayList<>(), aux2 = new ArrayList<>();
            int hasta = mDatos.indexOf(cabeceraMensajesSinRespuesta);
            for (int i = mDatos.indexOf(cabeceraMensajesRecibidos) + 1; i < hasta; i++)
                if (mDatos.get(i) instanceof MessagePojo)
                    aux.add((MessagePojo) mDatos.get(i));

            Collections.sort(aux, messagesComp);

            hasta = mDatos.size();
            for (int i = mDatos.indexOf(cabeceraMensajesSinRespuesta) + 1; i < hasta; i++)
                if (mDatos.get(i) instanceof MessagePojo)
                    aux2.add((MessagePojo) mDatos.get(i));

            Collections.sort(aux2, messagesComp);

            mDatos.clear();
            mDatos.add(cabeceraMensajesRecibidos);
            mDatos.add(cabeceraMensajesSinRespuesta);
            mDatos.addAll(mDatos.indexOf(cabeceraMensajesRecibidos) + 1, aux);
            mDatos.addAll(mDatos.indexOf(cabeceraMensajesSinRespuesta) + 1, aux2);
        } else {
            List<MessagePojo> aux = new ArrayList<>();
            for (int i = 0; i < mDatos.size(); i++)
                if (mDatos.get(i) instanceof MessagePojo)
                    aux.add((MessagePojo) mDatos.get(i));

            Collections.sort(aux, messagesComp);
            mDatos.clear();
            mDatos.addAll(aux);
        }

        notifyDataSetChanged();
    }

    public void removeItem(MessagePojo m) {
        int position = mDatos.indexOf(m);
        mDatos.remove(m);
        notifyItemRemoved(position);
    }
}
