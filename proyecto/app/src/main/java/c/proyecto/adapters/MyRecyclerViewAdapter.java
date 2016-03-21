package c.proyecto.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import c.proyecto.R;
import c.proyecto.models.Anuncio;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ADAPTER_TYPE_SUBS = 0;
    public static final int ADAPTER_TYPE_ADVS = 1;
    public static final int ADAPTER_TYPE_MY_ADVS = 2;

    private int adapter_type;
    private List<Anuncio> mDatos;
    private View emptyView;


    public MyRecyclerViewAdapter(int adapter_type) {
        mDatos = new ArrayList<>();
        this.adapter_type = adapter_type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View anuncioView;
        RecyclerView.ViewHolder viewHolder;

        if (adapter_type != ADAPTER_TYPE_MY_ADVS) {
            anuncioView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anuncio, parent, false);
            viewHolder = new AnuncioViewHolder(anuncioView);
        } else {
            anuncioView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mi_anuncio, parent, false);
            viewHolder = new MiAnuncioViewHolder(anuncioView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AnuncioViewHolder)
            ((AnuncioViewHolder) holder).onBind(mDatos.get(position));
        else
            ((MiAnuncioViewHolder) holder).onBind(mDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatos.size();
    }

    public int getAdapter_type() {
        return adapter_type;
    }

    //VIEWHOLDERS
    static class AnuncioViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgAvatar;
        private TextView lblPoblacion;
        private TextView lblProvincia;
        private TextView lblDireccion;
        private TextView lblNumero;
        private TextView lblPrecio;

        public AnuncioViewHolder(View itemView) {
            super(itemView);
            lblPoblacion = (TextView) itemView.findViewById(R.id.lblPoblacion);
            lblProvincia = (TextView) itemView.findViewById(R.id.lblProvincia);
            lblDireccion = (TextView) itemView.findViewById(R.id.lblDireccion);
            lblNumero = (TextView) itemView.findViewById(R.id.lblNumero);
            lblPrecio = (TextView) itemView.findViewById(R.id.lblPrecio);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
        }

        public void onBind(Anuncio anuncio) {
            if (anuncio != null) {
                lblDireccion.setText(anuncio.getDireccion());
                lblNumero.setText(String.valueOf(anuncio.getNumero()));
                lblPoblacion.setText(anuncio.getPoblacion());
                lblProvincia.setText(anuncio.getProvincia());
                lblPrecio.setText(String.valueOf(anuncio.getPrecio()));
                // IMG AVATAR
            }
        }
    }

    static class MiAnuncioViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgAvatar;
        private TextView lblPoblacion;
        private TextView lblProvincia;
        private TextView lblDireccion;
        private TextView lblNumero;
        private TextView lblSubs;

        public MiAnuncioViewHolder(View itemView) {
            super(itemView);
            lblPoblacion = (TextView) itemView.findViewById(R.id.lblPoblacion);
            lblProvincia = (TextView) itemView.findViewById(R.id.lblProvincia);
            lblDireccion = (TextView) itemView.findViewById(R.id.lblDireccion);
            lblNumero = (TextView) itemView.findViewById(R.id.lblNumero);
            lblSubs = (TextView) itemView.findViewById(R.id.lblSubs);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
        }

        public void onBind(Anuncio anuncio) {
            if (anuncio != null) {
                lblDireccion.setText(anuncio.getDireccion());
                lblNumero.setText(String.valueOf(anuncio.getNumero()));
                lblPoblacion.setText(anuncio.getPoblacion());
                lblProvincia.setText(anuncio.getProvincia());
                lblSubs.setText(String.valueOf(anuncio.getSolicitantes().size()));
                // IMG AVATAR
            }
        }
    }

    //Manejo del Adaptador
    public void replaceAll(List<Anuncio> anuncios) {
        mDatos.clear();
        mDatos.addAll(anuncios);
        notifyDataSetChanged();
    }

    public List<Anuncio> getmDatos() {
        return mDatos;
    }
}
