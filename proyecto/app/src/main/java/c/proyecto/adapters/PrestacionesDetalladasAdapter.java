package c.proyecto.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import c.proyecto.R;
import c.proyecto.pojo.Prestacion;


public class PrestacionesDetalladasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private final List<Prestacion> prestaciones;


    public PrestacionesDetalladasAdapter(List<Prestacion> prestaciones){
        this.prestaciones = prestaciones;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prestacion_detallado, parent, false);
        return new PrestacionDetalladaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PrestacionDetalladaViewHolder)holder).onBind(prestaciones.get(position));
    }

    @Override
    public int getItemCount() {
        return prestaciones.size();
    }

    class PrestacionDetalladaViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imgPrestacion;
        private final TextView lblNombre;

        public PrestacionDetalladaViewHolder(View itemView) {
            super(itemView);
            imgPrestacion = (ImageView) itemView.findViewById(R.id.imgPrestacion);
            lblNombre = (TextView) itemView.findViewById(R.id.lblNombre);
        }
        public void onBind(Prestacion prestacion){
            imgPrestacion.setImageResource(itemView.getResources().getIdentifier(prestacion.getNameDrawable(), "drawable", itemView.getContext().getPackageName()));
            lblNombre.setText(prestacion.getNombre());
        }
    }
}
