package c.proyecto.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Usuario;
import c.proyecto.mvp_presenters.MainPresenter;


public class AdvertsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnAdapterItemLongClick {
        void setAdapterAllowMultiDeletion(AdvertsRecyclerViewAdapter adaptador);

        void onItemLongClick();

        void desactivarMultiseleccion();
    }

    public interface OnAdapterItemClick {
        void onItemClick(Anuncio anuncio, int advertType);
    }

    public interface OnSubsIconClick {
        void onSubsItemClick(View itemView, Anuncio anuncio);
    }

    public static final int ADAPTER_TYPE_SUBS = 0;
    public static final int ADAPTER_TYPE_ADVS = 1;
    public static final int ADAPTER_TYPE_MY_ADVS = 2;

    private int adapter_type;
    private List<Anuncio> mDatos;
    private OnAdapterItemLongClick listenerLongClick;
    private OnAdapterItemClick listenerItemClick;
    private OnSubsIconClick listenerSubsClick;
    private HuespedesAdapter.OnUserSubClick listenerUserSubClick;
    private View emptyView;
    private MainPresenter mPresenter;
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray();
    private boolean multiDeletionModeActivated = false;
    private Usuario user;
    private boolean filtersApplied;
    private RecyclerView rvSolicitantes;
    private AlertDialog solicitantesDialog;

    public AdvertsRecyclerViewAdapter(int adapter_type, MainPresenter presenter, Usuario u) {
        mDatos = new ArrayList<>();
        mPresenter = presenter;
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

    public void disableMultiDeletionMode() {
        multiDeletionModeActivated = false;
    }

    public void setListenerLongClick(OnAdapterItemLongClick listenerLongClick) {
        this.listenerLongClick = listenerLongClick;
    }

    public void setListenerItemClick(OnAdapterItemClick listenerItemClick) {
        this.listenerItemClick = listenerItemClick;
    }

    public void setListenerSubsClick(OnSubsIconClick listenerSubsClick) {
        this.listenerSubsClick = listenerSubsClick;
    }

    public void setListenerUserSubClick(HuespedesAdapter.OnUserSubClick listenerUserSubClick) {
        this.listenerUserSubClick = listenerUserSubClick;
    }

    private static int getAnchoPantalla(Context context) {
        Point point = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(point);
        return point.x;
    }

    public void closeSolicitantesDialog() {
        solicitantesDialog.dismiss();
    }

    @Override
    public int getItemCount() {
        return mDatos.size();
    }

    public int getAdapter_type() {
        return adapter_type;
    }


    /////////////////////////////////////////////    VIEWHOLDERS    //////////////////////////////////////////////
    class AnuncioViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgAvatar;
        private ProgressBar prbAnuncio;
        private TextView lblTituloAnuncio, lblLocalizacion, lblPrecio;
        private int anchoAproxImgAvatar;
        private String formatPrecio;

        public AnuncioViewHolder(final View itemView) {
            super(itemView);
            lblTituloAnuncio = (TextView) itemView.findViewById(R.id.lblTituloAnuncio);
            lblLocalizacion = (TextView) itemView.findViewById(R.id.lblLocalizacion);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
            prbAnuncio = (ProgressBar) itemView.findViewById(R.id.prbAnuncio);
            lblPrecio = (TextView) itemView.findViewById(R.id.lblPrecio);
            anchoAproxImgAvatar = getAnchoPantalla(itemView.getContext()) / 2;
        }

        public void onBind(final Anuncio anuncio) {
            formatPrecio = "%.2f";
            if (anuncio != null) {
                prbAnuncio.setVisibility(View.VISIBLE);
                lblTituloAnuncio.setText(anuncio.getTitulo());
                lblLocalizacion.setText(anuncio.getPoblacion());

                //Si el precio no tiene decimales, el número será mostrado sin 0  Ej: 10.00 -> 10
                if(anuncio.getPrecio() % 1 == 0)
                    formatPrecio = "%.0f";
                lblPrecio.setText(String.format(formatPrecio + "%s", anuncio.getPrecio(), Constantes.MONEDA));

                if (anuncio.getImagenes().size() > 0) {
                    for (String img : anuncio.getImagenes().keySet())
                        if (img.equals(Constantes.FOTO_PRINCIPAL)) // Si la key es de la imagen principal, cargo la foto
                            Picasso.with(itemView.getContext()).load(anuncio.getImagenes().get(img)).resize(anchoAproxImgAvatar, imgAvatar.getLayoutParams().height).centerCrop().into(imgAvatar, new ImageLoadedCallback(prbAnuncio));
                }
            }
        }
    }

    class MiAnuncioViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout groupSuscritos;
        private ImageView imgAvatar, imgSuscritos;
        private ProgressBar prbAnuncio;
        private TextView lblTituloAnuncio, lblLocalizacion, lblSubs;
        private int anchoAproxImgAvatar;

        public MiAnuncioViewHolder(View itemView) {
            super(itemView);
            lblTituloAnuncio = (TextView) itemView.findViewById(R.id.lblTituloAnuncio);
            lblLocalizacion = (TextView) itemView.findViewById(R.id.lblLocalizacion);
            lblSubs = (TextView) itemView.findViewById(R.id.lblSubs);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
            anchoAproxImgAvatar = getAnchoPantalla(itemView.getContext()) / 2;
            prbAnuncio = (ProgressBar) itemView.findViewById(R.id.prbAnuncio);
            groupSuscritos = (LinearLayout) itemView.findViewById(R.id.groupSuscritos);
            imgSuscritos = (ImageView) itemView.findViewById(R.id.imgSuscritos);
        }

        public void onBind(final Anuncio anuncio) {
            if (anuncio != null) {
                prbAnuncio.setVisibility(View.VISIBLE);
                lblTituloAnuncio.setText(anuncio.getTitulo());
                lblLocalizacion.setText(anuncio.getPoblacion());
                lblSubs.setText(String.valueOf(anuncio.getSolicitantes().size()));
                if (anuncio.getImagenes().size() > 0) {
                    for (String img : anuncio.getImagenes().keySet())
                        if (img.equals(Constantes.FOTO_PRINCIPAL)) // Si la key es de la imagen principal, cargo la foto
                            Picasso.with(itemView.getContext()).load(anuncio.getImagenes().get(img)).resize(anchoAproxImgAvatar, imgAvatar.getLayoutParams().height).centerCrop().into(imgAvatar, new ImageLoadedCallback(prbAnuncio));
                }

                if (anuncio.isSubsChanged()) {
                    imgSuscritos.setColorFilter(itemView.getResources().getColor(R.color.colorAccent));
                    lblSubs.setTextColor(itemView.getResources().getColor(R.color.colorAccent));
                } else {
                    imgSuscritos.clearColorFilter();
                    lblSubs.setTextColor(itemView.getResources().getColor(R.color.colorGrayLightText));
                }

                groupSuscritos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listenerSubsClick.onSubsItemClick(itemView, anuncio);
                        anuncio.setSubsChanged(false);
                        imgSuscritos.clearColorFilter();
                        lblSubs.setTextColor(Color.BLACK);
                        mPresenter.updateAdvert(anuncio);
                    }
                });
            }
        }
    }

    public void solicitantesObtained(View itemView, ArrayList<Usuario> listaSolicitantes, Anuncio anuncio) {
        if (itemView != null)
            showSolicitantesDialog(itemView, listaSolicitantes, anuncio);
        else
            updateSolicitantesDialog(listaSolicitantes);
    }

    private void showSolicitantesDialog(View itemView, ArrayList<Usuario> listaSolicitantes, Anuncio anuncio) {
        solicitantesDialog = new AlertDialog.Builder(itemView.getContext()).create();
        View dialogView = View.inflate(itemView.getContext(), R.layout.dialog_solicitantes, null);
        solicitantesDialog.setView(dialogView);
        solicitantesDialog.setCanceledOnTouchOutside(true);
        solicitantesDialog.setTitle("Solicitantes");

        rvSolicitantes = (RecyclerView) dialogView.findViewById(R.id.rvSolicitantes);
        rvSolicitantes.setAdapter(new HuespedesAdapter(listaSolicitantes, anuncio));
        ((HuespedesAdapter) rvSolicitantes.getAdapter()).setListener(listenerUserSubClick);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
        rvSolicitantes.setLayoutManager(mLayoutManager);
        rvSolicitantes.setItemAnimator(new DefaultItemAnimator());
        rvSolicitantes.setOverScrollMode(View.OVER_SCROLL_NEVER);

        //Comprueba que el último item añadido no se muestre en pantalla, por lo tanto activará el scroll.
        //Para NotifyDataSetChange, porque cada vez que se actualiza la lista se borran todos y se vuelven a introducir.
        rvSolicitantes.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            int mUltimoItem = -2;
            @Override
            public void onChildViewAttachedToWindow(View view) {
                int ultimoItemVisible = mLayoutManager.findLastCompletelyVisibleItemPosition();

                if(ultimoItemVisible > mUltimoItem){
                    mUltimoItem = ultimoItemVisible;
                    rvSolicitantes.setOverScrollMode(View.OVER_SCROLL_NEVER);
                }
                else
                    rvSolicitantes.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                mUltimoItem = -2;
            }

        });
        solicitantesDialog.show();
        Point boundsScreen = new Point();
        ((WindowManager) itemView.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(boundsScreen);

        solicitantesDialog.getWindow().setLayout((int) (boundsScreen.x * Constantes.PORCENTAJE_PANTALLA), WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void updateSolicitantesDialog(ArrayList<Usuario> listaSolicitantes) {
        if (rvSolicitantes != null)
            ((HuespedesAdapter) rvSolicitantes.getAdapter()).updateData(listaSolicitantes);
    }

    //Manejo del Adaptador
    public void addItem(Anuncio a) {
        boolean stop = false;
        if (adapter_type != ADAPTER_TYPE_ADVS || !filtersApplied) {
            for (int i = 0; !stop && i < mDatos.size(); i++)
                if (mDatos.get(i).getKey().equals(a.getKey()))
                    stop = true;

            if (!stop)
                mDatos.add(0, a);
            notifyItemInserted(mDatos.indexOf(a));
            notifyDataSetChanged();
        }
    }

    public void addItems(ArrayList<Anuncio> filteredAdverts) {
        filtersApplied = true;
        mDatos.clear();
        mDatos.addAll(filteredAdverts);
        notifyDataSetChanged();
    }

    private void removeItem(int pos) {
        if (adapter_type == ADAPTER_TYPE_MY_ADVS)
            mPresenter.removeUserAdvert(mDatos.get(pos));
        else
            mPresenter.removeUserSub(mDatos.get(pos));
        removeItem(mDatos.get(pos));
    }

    public void removeItem(Anuncio a) {
        boolean stop = false;
        int pos = mDatos.indexOf(a);
        if (pos >= 0)
            mDatos.remove(a);
        else {
            for (int i = 0; !stop && i < mDatos.size(); i++)
                if (mDatos.get(i).getKey().equals(a.getKey())) {
                    mDatos.remove(i);
                    stop = true;
                    pos = i;
                }
        }
        notifyItemRemoved(pos);
        notifyDataSetChanged();
    }

    public void replaceItem(Anuncio a) {
        boolean stop = false;
        for (int i = 0; !stop && i < mDatos.size(); i++)
            if (a.getKey().equals(mDatos.get(i).getKey())) {
                Anuncio aux = mDatos.remove(i);
                mDatos.add(i, a);
                stop = true;
                if (aux.getSolicitantes().size() != a.getSolicitantes().size()) {
                    mPresenter.getSolicitantes(null, a); // para actualizar el dialogo de solicitantes.
                    a.setSubsChanged(true);
                }
            }
        if (!stop && adapter_type != ADAPTER_TYPE_ADVS)
            mDatos.add(a);
        notifyDataSetChanged();
    }

    public Anuncio getAdvert(int position) {
        return mDatos.get(position);
    }

    public void removeFilter() {
        setFiltersApplied(false);
        mDatos.clear();
    }

    public void setFiltersApplied(boolean filtersApplied) {
        this.filtersApplied = filtersApplied;
    }

    private class ImageLoadedCallback implements Callback {
        ProgressBar progressBar;

        public ImageLoadedCallback(ProgressBar progBar) {
            progressBar = progBar;
        }

        @Override
        public void onSuccess() {
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onError() {

        }
    }
}
