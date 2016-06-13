package c.proyecto.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

    private View emptyView;
    private List<MessagePojo> mDatos, messagesConver;
    private OnMessagesAdapterItemClick listenerItemClick;
    private ConversationManager listenerConverManager;
    private boolean isAConversation, allMessagesObtained;

    private String mKeyCurrentUser;
    private ComparatorMessages messagesComp = new ComparatorMessages();

    public MessagesRecyclerViewAdapter(boolean isAConversation, String keyCurrentUser) {
        mDatos = new ArrayList<>();
        messagesConver = new ArrayList<>();
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
                ((MessagesViewHolder) holder).onBind(mDatos.get(position));
        } else
            ((ChatViewHolder) holder).onBind(mDatos.get(position));
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
                    listenerItemClick.onItemClick(mDatos.get(mDatos.indexOf(m)));
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
                // si el emisor del mensaje que tengo es el mismo que el emisor del mensaje que me viene, y el titulo del anuncio es el mismo en el mensaje que tengo y en el que me viene
                if ((mDatos.get(i).getEmisor().getKey().equals(m.getEmisor().getKey()) || mDatos.get(i) instanceof MessagePojoWithoutAnswer) && mDatos.get(i).getTituloAnuncio().trim().equals(m.getTituloAnuncio().trim())) {
                    mDatos.remove(mDatos.get(i)); // borro ese mensaje
                    opHecha = true; // paro de recorrer la lista
                }

        if (!isAConversation || isAConversation && allMessagesObtained)
            mDatos.add(0, m);
        else if (isAConversation && !allMessagesObtained)
            messagesConver.add(0, m);

        if (isAConversation)
            if (allMessagesObtained && mDatos.size() == LIMIT_MESSAGES) { // si estoy en conversación activity y he el adaptador contiene tantos elementos como indica el límite
                listenerConverManager.removeMessage(mDatos.remove(mDatos.size() - 1)); // borro el primer mensaje que recibí de la bdd
                mDatos.remove(mDatos.size() - 1); // lo borro del adaptador
            } else if (!allMessagesObtained && messagesConver.size() == LIMIT_MESSAGES) {
                listenerConverManager.removeMessage(messagesConver.remove(messagesConver.size() - 1)); // borro el primer mensaje que recibí de la bdd
                messagesConver.remove(messagesConver.size() - 1); // lo borro del adaptador
            }

        Collections.sort(mDatos, messagesComp);

        if (allMessagesObtained)
            notifyDataSetChanged();
        checkIfEmpty();
    }


    public void allMessagesObtained() {
        // todo NENE, PON TU LA BARRA DE CARGA MIENTRAS SE OBTIENEN TODOS LOS MENSAJES AQUÍ. CUANDO ENTRE POR ESTE MÉTODO ES QUE SE HAN OBTENIDO TODOS Y LA PUEDES OCULTAR.
        if (isAConversation) {
            mDatos.addAll(messagesConver);
            Collections.sort(mDatos, messagesComp);
        }
        allMessagesObtained = true;
        notifyDataSetChanged();
        checkIfEmpty();
    }

    public void addAll(ArrayList<MessagePojo> messagesList) {
        Collections.sort(messagesList, messagesComp);
        mDatos.addAll(messagesList);
        notifyDataSetChanged();
        checkIfEmpty();
    }

    public void removeItem(MessagePojo m) {
        int position = mDatos.indexOf(m);
        mDatos.remove(m);
        notifyItemRemoved(position);
        checkIfEmpty();
    }

    public void setAllMessagesObtained(boolean allMessagesObtained) {
        this.allMessagesObtained = allMessagesObtained;
        mDatos.clear();
    }

    public void setEmptyView(View emptyView){
        this.emptyView = emptyView;
        checkIfEmpty();
    }

    private void checkIfEmpty() {
        if(emptyView != null && !isAConversation)
            emptyView.setVisibility(getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }

}
