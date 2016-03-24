package c.proyecto.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import c.proyecto.R;


public class HuespedesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> mImages;

    public HuespedesAdapter(List<String> images){
        mImages = images;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_huesped, parent, false);
        return new HuespedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Item Huesped normal
        if(position != 0)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        else //Item agregar Huesped
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    class HuespedViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imgHuesped;

        public HuespedViewHolder(View itemView) {
            super(itemView);
            imgHuesped = (ImageView) itemView.findViewById(R.id.imgHuesped);
        }
        public void onBind(String image){
            Picasso.with(itemView.getContext()).load(image).error(R.drawable.default_user).into(imgHuesped);
        }
    }
}
