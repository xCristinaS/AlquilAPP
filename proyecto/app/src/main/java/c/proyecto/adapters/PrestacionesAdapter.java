package c.proyecto.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import c.proyecto.R;
import c.proyecto.pojo.Prestacion;


public class PrestacionesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Prestacion> mDatos;
    private IPrestacionAdapter mListener;
    private View emptyView;

    public interface IPrestacionAdapter{
        void onPrestacionClicked();
        void onEmptyViewClicked();
    }

    public PrestacionesAdapter(List<Prestacion> prestaciones, IPrestacionAdapter listener){
        mDatos = prestaciones;
        this.mListener = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prestacion, parent, false);
        return new PrestacionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PrestacionViewHolder)holder).onBind(mDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatos.size();
    }

    public List<Prestacion> getmDatos() {
        return mDatos;
    }

    class PrestacionViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imgPrestacion;

        public PrestacionViewHolder(View itemView) {
            super(itemView);
            imgPrestacion = (ImageView) itemView.findViewById(R.id.imgPrestacion);
        }
        public void onBind(Prestacion prestacion){
            imgPrestacion.setImageResource(prestacion.getIdDrawable());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onPrestacionClicked();
                }
            });
        }
    }

    public void actualizarAdapter(){
        notifyDataSetChanged();
        checkIfEmpty();
    }
    public void replaceAll(List<Prestacion> prestaciones){
        mDatos.clear();
        mDatos.addAll(prestaciones);
        actualizarAdapter();
    }
    private void checkIfEmpty() {
        if(emptyView != null)
            emptyView.setVisibility(getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }
    // Establece la empty view para la lista.
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEmptyViewClicked();
            }
        });
        // Muestra la empty view si la lista está vacía.
        checkIfEmpty();
    }
}
