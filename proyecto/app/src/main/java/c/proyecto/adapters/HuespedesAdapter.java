package c.proyecto.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import c.proyecto.R;
import c.proyecto.pojo.Usuario;


public class HuespedesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Usuario> mUsers;

    public HuespedesAdapter(List<Usuario> users){
        mUsers = users;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_huesped, parent, false);
        return new HuespedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((HuespedViewHolder) holder).onBind(mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class HuespedViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imgHuesped;
        private final TextView lblNombre;

        public HuespedViewHolder(View itemView) {
            super(itemView);
            imgHuesped = (ImageView) itemView.findViewById(R.id.imgHuesped);
            lblNombre = (TextView) itemView.findViewById(R.id.lblNombre);
        }
        public void onBind(Usuario user){
            Picasso.with(itemView.getContext()).load(user.getFoto()).error(R.drawable.default_user).into(imgHuesped);
            lblNombre.setText(user.getNombre() + " " + user.getApellidos());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
