package c.proyecto.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by Cristina on 23/03/2016.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {

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

    public MessagesAdapter(boolean isAConversation) {
        mDatos = new ArrayList<>();
        this.isAConversation = isAConversation;
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessagesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, final int position) {
        holder.onBind(mDatos.get(position));
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

            if (!isAConversation)
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listenerItemClick.onItemClick(mDatos.get(mDatos.indexOf(m)));
                    }
                });
        }
    }

    //Manejo del Adaptador
    public void addItem(MessagePojo m) {
        boolean stop = false;
        for (int i = 0; !stop && i < mDatos.size(); i++)
            if (m.getKey().equals(mDatos.get(i).getKey()))
                stop = true;

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
