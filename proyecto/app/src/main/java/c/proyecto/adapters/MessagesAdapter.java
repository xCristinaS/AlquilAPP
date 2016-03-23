package c.proyecto.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import c.proyecto.R;
import c.proyecto.models.Anuncio;
import c.proyecto.pojo.MessagePojo;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Cristina on 23/03/2016.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {

    List<MessagePojo> mDatos;

    public MessagesAdapter() {
        mDatos = new ArrayList<>();
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessagesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        holder.onBind(mDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatos.size();
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

        public void onBind(MessagePojo m) {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            if (m.getFotoEmisor() != null)
                Picasso.with(itemView.getContext()).load(m.getFotoEmisor()).into(imgEmisor);
            else
                Picasso.with(itemView.getContext()).load(R.drawable.default_user).into(imgEmisor);
            lblTituloAnuncio.setText(m.getTituloAnuncio());
            lblContenido.setText(m.getContenido());
            lblNombreEmisor.setText(m.getNombreEmisor());
//            lblFecha.setText(formato.format(m.getFecha()));
        }
    }

    //Manejo del Adaptador
    public void addItem(MessagePojo m) {
        mDatos.add(m);
        notifyItemInserted(mDatos.indexOf(m));
    }

    public void removeItem(MessagePojo m) {
        int position = mDatos.indexOf(m);
        mDatos.remove(m);
        notifyItemRemoved(position);
    }
}
