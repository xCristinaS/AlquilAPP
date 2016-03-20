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

    private boolean isMyAdv;
    private List<Anuncio> mDatos;
    private View emptyView;


    public MyRecyclerViewAdapter(boolean isMyAdv){
        mDatos = new ArrayList<>();
        this.isMyAdv = isMyAdv;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View anuncioView;
        RecyclerView.ViewHolder viewHolder;

        if(isMyAdv){
            anuncioView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anuncio, parent, false);
            viewHolder = new AnuncioViewHolder(anuncioView);
        }else{
            anuncioView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mi_anuncio, parent, false);
            viewHolder = new MiAnuncioViewHolder(anuncioView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof AnuncioViewHolder)
            ((AnuncioViewHolder) holder).onBind(mDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatos.size();
    }

    //VIEWHOLDERS
    static class AnuncioViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imgAvatar;
        private final TextView lblPoblacion;
        private final TextView lblProvincia;
        private final TextView lblCalle;
        private final TextView lblNumero;
        private final TextView lblPrecio;

        public AnuncioViewHolder(View itemView) {
            super(itemView);
            lblPoblacion = (TextView) itemView.findViewById(R.id.lblPoblacion);
            lblProvincia = (TextView) itemView.findViewById(R.id.lblProvincia);
            lblCalle = (TextView) itemView.findViewById(R.id.lblCalle);
            lblNumero = (TextView) itemView.findViewById(R.id.lblNumero);
            lblPrecio = (TextView) itemView.findViewById(R.id.lblPrecio);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
        }
        public void onBind(Anuncio anuncio){
            lblCalle.setText(anuncio.getDireccion());
        }
    }
    static class MiAnuncioViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imgAvatar;
        private final TextView lblPoblacion;
        private final TextView lblProvincia;
        private final TextView lblCalle;
        private final TextView lblNumero;
        private final TextView lblSubs;

        public MiAnuncioViewHolder(View itemView) {
            super(itemView);
            lblPoblacion = (TextView) itemView.findViewById(R.id.lblPoblacion);
            lblProvincia = (TextView) itemView.findViewById(R.id.lblProvincia);
            lblCalle = (TextView) itemView.findViewById(R.id.lblCalle);
            lblNumero = (TextView) itemView.findViewById(R.id.lblNumero);
            lblSubs = (TextView) itemView.findViewById(R.id.lblSubs);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
        }
        public void onBind(Anuncio anuncio){

        }
    }

    //Manejo del Adaptador
    public void replaceAll(List<Anuncio> anuncios){
        mDatos.clear();
        mDatos.addAll(anuncios);
        notifyDataSetChanged();
    }

    public List<Anuncio> getmDatos() {
        return mDatos;
    }

    public void setmDatos(List<Anuncio> mDatos) {
        this.mDatos = mDatos;
        notifyDataSetChanged();
    }
}
