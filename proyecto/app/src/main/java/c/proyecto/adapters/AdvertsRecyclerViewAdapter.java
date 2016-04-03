package c.proyecto.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import c.proyecto.R;
import c.proyecto.activities.ConversationActivity;
import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;
import c.proyecto.presenters.MainPresenter;


public class AdvertsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnAdapterItemLongClick {
        void setAdapterAllowMultiDeletion(AdvertsRecyclerViewAdapter adaptador);
        void onItemLongClick();
        void desactivarMultiseleccion();
    }

    public interface OnAdapterItemClick {
        void onItemClick(Anuncio anuncio, int advertType);
    }

    public static final int ADAPTER_TYPE_SUBS = 0;
    public static final int ADAPTER_TYPE_ADVS = 1;
    public static final int ADAPTER_TYPE_MY_ADVS = 2;

    private int adapter_type;
    private List<Anuncio> mDatos;
    private OnAdapterItemLongClick listenerLongClick;
    private OnAdapterItemClick listenerItemClick;
    private View emptyView;
    private MainPresenter presenter;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray();
    private boolean multiDeletionModeActivated = false;
    private static Usuario user;


    public AdvertsRecyclerViewAdapter(int adapter_type, MainPresenter presenter, Usuario user) {
        mDatos = new ArrayList<>();
        this.presenter = presenter;
        this.adapter_type = adapter_type;
        this.user = user;
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
                    listenerItemClick.onItemClick(mDatos.get(position), adapter_type);
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

    public void removeSelections() {
        List<Integer> seleccionados = getSelectedItemsPositions();
        Collections.sort(seleccionados, Collections.reverseOrder());
        for (int i = 0; i < seleccionados.size(); i++) {
            int pos = seleccionados.get(i);
            removeItem(pos);
        }
    }

    public List<Integer> getSelectedItemsPositions() {
        List<Integer> items = new ArrayList<>(mSelectedItems.size());
        for (int i = 0; i < mSelectedItems.size(); i++) {
            items.add(mSelectedItems.keyAt(i));
        }
        return items;
    }

    private void removeItem(int pos) {
        if (adapter_type == ADAPTER_TYPE_MY_ADVS)
            presenter.removeUserAdvert(mDatos.get(pos));
        else
            presenter.removeUserSub(mDatos.get(pos), user);
        removeItem(mDatos.get(pos));
        notifyItemRemoved(pos);
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
        private TextView lblPoblacion, lblProvincia, lblDireccion, lblNumero, lblPrecio;

        public AnuncioViewHolder(final View itemView) {
            super(itemView);
            lblPoblacion = (TextView) itemView.findViewById(R.id.lblPoblacion);
            lblProvincia = (TextView) itemView.findViewById(R.id.lblProvincia);
            lblDireccion = (TextView) itemView.findViewById(R.id.lblDireccion);
            lblNumero = (TextView) itemView.findViewById(R.id.lblNumero);
            lblPrecio = (TextView) itemView.findViewById(R.id.lblPrecio);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
        }

        public void onBind(final Anuncio anuncio) {
            if (anuncio != null) {
                lblDireccion.setText(anuncio.getDireccion());
                lblNumero.setText(String.valueOf(anuncio.getNumero()));
                lblPoblacion.setText(anuncio.getPoblacion());
                lblProvincia.setText(anuncio.getProvincia());
                lblPrecio.setText(String.valueOf(anuncio.getPrecio()));
                if (anuncio.getImagenes().size() > 0)
                    Picasso.with(itemView.getContext()).load(anuncio.getImagenes().get(0)).into(imgAvatar);
            }
        }
    }

    static class MiAnuncioViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgAvatar;
        private TextView lblPoblacion, lblProvincia, lblDireccion, lblNumero, lblSubs;

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
                if (anuncio.getImagenes().size() > 0)
                    Picasso.with(itemView.getContext()).load(anuncio.getImagenes().get(0)).into(imgAvatar);
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
