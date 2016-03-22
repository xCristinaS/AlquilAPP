package c.proyecto.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import c.proyecto.R;
import c.proyecto.models.Anuncio;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnAdapterItemLongClick {
        void setAdapterAllowMultiDeletion(MyRecyclerViewAdapter adaptador);
        void onItemLongClick();
        void desactivarMultiseleccion();
    }

    public interface  OnAdapterItemClick {

    }

    public static final int ADAPTER_TYPE_SUBS = 0;
    public static final int ADAPTER_TYPE_ADVS = 1;
    public static final int ADAPTER_TYPE_MY_ADVS = 2;

    private int adapter_type;
    private List<Anuncio> mDatos;
    private OnAdapterItemLongClick listenerLongClick;
    private OnAdapterItemClick listenerItemClick;
    private View emptyView;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray();
    private boolean multiDeletionModeActivated = false;


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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AnuncioViewHolder)
            ((AnuncioViewHolder) holder).onBind(mDatos.get(position));
        else
            ((MiAnuncioViewHolder) holder).onBind(mDatos.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!multiDeletionModeActivated) {
                    //listenerItemClick.onItemClick(alumno);
                } else {
                    if (holder.itemView.isActivated()) {
                        holder.itemView.setActivated(false);
                        mSelectedItems.put(position, false);
                    } else {
                        holder.itemView.setActivated(true);
                        mSelectedItems.put(position, true);
                    }
                }
            }
        });

        if (adapter_type == ADAPTER_TYPE_MY_ADVS || adapter_type == ADAPTER_TYPE_SUBS) {
            holder.itemView.setActivated(mSelectedItems.get(position, false));
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listenerLongClick != null) {
                        if (!multiDeletionModeActivated) {
                            multiDeletionModeActivated = true;
                            mSelectedItems.put(position, true);
                            holder.itemView.setActivated(true);
                            listenerLongClick.onItemLongClick();
                        }
                        return true;
                    } else
                        return false;
                }
            });
        }
    }

    public void clearAllSelections() {
        if (mSelectedItems.size() > 0) {
            mSelectedItems.clear();
            notifyDataSetChanged();
        }
    }

    public boolean removeSelections() {
        boolean resp = true;
        List<Integer> seleccionados = getSelectedItemsPositions();
        Collections.sort(seleccionados, Collections.reverseOrder());
        for (int i = 0; i < seleccionados.size(); i++) {
            int pos = seleccionados.get(i);
            if (removeItem(pos))
                resp = false;
        }
        return resp;
    }

    public List<Integer> getSelectedItemsPositions() {
        List<Integer> items = new ArrayList<>(mSelectedItems.size());
        for (int i = 0; i < mSelectedItems.size(); i++) {
            items.add(mSelectedItems.keyAt(i));
        }
        return items;
    }

    private boolean removeItem(int pos) {

        return false;
    }

    public void disableMultiDeletionMode() {
        multiDeletionModeActivated = false;
    }

    public void setListenerLongClick(OnAdapterItemLongClick listenerLongClick) {
        this.listenerLongClick = listenerLongClick;
    }

    public void setListenerItemClick(OnAdapterItemClick listenerItemClick) {
        this.listenerItemClick = listenerItemClick;
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
    public void addItem(Anuncio a){
        boolean stop = false;
        for (int i = 0; !stop && i < mDatos.size(); i++)
            if (a.getKey().equals(mDatos.get(i).getKey()))
                stop = true;

        if (!stop)
            mDatos.add(a);
        notifyItemInserted(mDatos.indexOf(a));
    }

    public void removeItem(Anuncio a){
        int position = mDatos.indexOf(a);
        mDatos.remove(a);
        notifyItemRemoved(position);
    }

    public void replaceItem(Anuncio a){
        boolean stop = false;
        for (int i = 0; !stop && i < mDatos.size(); i++)
            if (a.getKey().equals(mDatos.get(i).getKey())) {
                mDatos.remove(i);
                mDatos.add(i, a);
                stop = true;
            }
        notifyDataSetChanged();
    }

    public Anuncio getAdvert(int position){
        return mDatos.get(position);
    }
}
