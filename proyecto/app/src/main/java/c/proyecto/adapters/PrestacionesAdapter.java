package c.proyecto.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import c.proyecto.R;


public class PrestacionesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Integer> mIds;
    private IPrestacionAdapter mListener;

    public interface IPrestacionAdapter{
        void onPrestacionClicked();
    }

    public PrestacionesAdapter(List<Integer> idPrestaciones, IPrestacionAdapter listener){
        mIds = idPrestaciones;
        this.mListener = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prestacion, parent, false);
        return new PrestacionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PrestacionViewHolder)holder).onBind(mIds.get(position));
    }

    @Override
    public int getItemCount() {
        return mIds.size();
    }

    class PrestacionViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imgPrestacion;
        private final TextView lblNombre;

        public PrestacionViewHolder(View itemView) {
            super(itemView);
            imgPrestacion = (ImageView) itemView.findViewById(R.id.imgPrestacion);
            lblNombre = (TextView) itemView.findViewById(R.id.lblNombre);
        }
        public void onBind(int idImg){
            imgPrestacion.setImageResource(idImg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onPrestacionClicked();
                }
            });
        }
    }
}
