package ar.uba.fi.nicodiaz.mascota.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.Mascota;

/**
 * Created by nicolas on 13/09/15.
 */
public class AdopcionAdapter extends RecyclerView.Adapter<AdopcionAdapter.ViewHolder> {
    private List<Mascota> mascotas;
    private int rowLayout;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AdopcionAdapter(List<Mascota> mascotas, int rowLayout, Context context) {
        this.mascotas = mascotas;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public AdopcionAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Mascota mascota = mascotas.get(i);
        viewHolder.mascotaName.setText(mascota.name);
        viewHolder.mascotaImage.setImageDrawable(context.getResources().getDrawable(mascota.getImageResourceId(context)));
        viewHolder.currentMascota = mascota;
    }

    @Override
    public int getItemCount() {
        return mascotas == null ? 0 : mascotas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mascotaName;
        public ImageView mascotaImage;
        public Mascota currentMascota;

        public ViewHolder(final View itemView) {
            super(itemView);
            mascotaName = (TextView) itemView.findViewById(R.id.petName);
            mascotaImage = (ImageView) itemView.findViewById(R.id.petImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }
    }
}
