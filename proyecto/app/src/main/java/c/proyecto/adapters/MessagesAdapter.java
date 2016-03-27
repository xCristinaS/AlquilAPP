package c.proyecto.adapters;

import android.app.ActionBar;
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
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import c.proyecto.R;
import c.proyecto.pojo.MessagePojo;
import de.hdodenhof.circleimageview.CircleImageView;


public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    public MessagesAdapter(boolean isAConversation, String keyCurrentUser) {
        mDatos = new ArrayList<>();
        mKeyCurrentUser = keyCurrentUser;
        this.isAConversation = isAConversation;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View anuncioView;
        RecyclerView.ViewHolder viewHolder;

        if (!isAConversation) {
            anuncioView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
            viewHolder = new MessagesViewHolder(anuncioView);
        } else {
            anuncioView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sended_message, parent, false);
            viewHolder = new ChatViewHolder(anuncioView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (!isAConversation)
            ((MessagesViewHolder) holder).onBind(mDatos.get(position));
        else
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
            SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            //long dia = TimeUnit.DAYS.toMillis(1);
            //Date hoy = new Date();
            if (m.getEmisor().getFoto() != null)
                Picasso.with(itemView.getContext()).load(m.getEmisor().getFoto()).into(imgEmisor);
            else
                Picasso.with(itemView.getContext()).load(R.drawable.default_user).into(imgEmisor);

            lblTituloAnuncio.setText(m.getTituloAnuncio());
            lblContenido.setText(m.getContenido());
            lblNombreEmisor.setText(m.getEmisor().getNombre());
            //if (hoy.getTime() - m.getFecha().getTime() > dia)
            lblFecha.setText(fecha.format(m.getFecha()));
            //else
            //  lblFecha.setText(hora.format(m.getFecha()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenerItemClick.onItemClick(mDatos.get(mDatos.indexOf(m)));
                }
            });
        }
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        private final CardView cvItemMessage;
        private final RelativeLayout groupItemMessage;
        private TextView lblMessage, lblDate;


        public ChatViewHolder(View itemView) {
            super(itemView);
            lblMessage = (TextView) itemView.findViewById(R.id.lblMessage);
            lblDate = (TextView) itemView.findViewById(R.id.lblDate);
            cvItemMessage = (CardView) itemView.findViewById(R.id.cvItemMessage);
            groupItemMessage = (RelativeLayout) itemView.findViewById(R.id.groupItemMessage);
        }

        public void onBind(final MessagePojo m) {
            SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            SimpleDateFormat hora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            //long dia = TimeUnit.DAYS.toMillis(1);
            //Date hoy = new Date();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) groupItemMessage.getLayoutParams();
            //Colorea el cardView dependiendo de si es un mensaje mio o no
            if (m.getEmisor().getKey().equals(mKeyCurrentUser)){
                cvItemMessage.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorAccent));
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
            else{
                cvItemMessage.setCardBackgroundColor(itemView.getResources().getColor(R.color.colorPrimary));
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }
            groupItemMessage.setLayoutParams(params);


            lblMessage.setText(m.getContenido());
            //if (hoy.getTime() - m.getFecha().getTime() > dia)
            lblDate.setText(fecha.format(m.getFecha()));
            //else
            //  lblFecha.setText(hora.format(m.getFecha()));
        }
    }

    //Manejo del Adaptador
    public void addItem(MessagePojo m) {
        boolean stop = false;
        for (int i = 0; !stop && i < mDatos.size(); i++)
            if (mDatos.get(i).getKey().equals(m.getKey()))
                stop = true;

        if (!stop && !isAConversation)
            for (int i = 0; i < mDatos.size(); i++)
                if (mDatos.get(i).getEmisor().getKey().equals(m.getEmisor().getKey())) {
                    mDatos.remove(mDatos.get(i));
                    notifyItemRemoved(i);
                }

        if (!stop)
            mDatos.add(m);

        if (isAConversation && mDatos.size() == 20)
            listenerConverManager.removeMessage(mDatos.remove(0));
        //notifyItemInserted(0);
        Collections.sort(mDatos);
        notifyDataSetChanged();
    }

    public void removeItem(MessagePojo m) {
        int position = mDatos.indexOf(m);
        mDatos.remove(m);
        notifyItemRemoved(position);
    }
}
