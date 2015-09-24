package ar.uba.fi.nicodiaz.mascota.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;

/**
 * Created by nicolas on 13/09/15.
 * <p/>
 * From: http://stackoverflow.com/questions/30681905/adding-items-to-endless-scroll-recyclerview-with-progressbar-at-bottom/30691092#30691092
 */
public class AdopcionEndlessAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    private List<AdoptionPet> mascotas;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public AdopcionEndlessAdapter(List<AdoptionPet> mascotas, RecyclerView recyclerView, Context context) {
        this.mascotas = mascotas;
        this.context = context;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v;
        if (viewType == VIEW_ITEM) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_adopcion, parent, false);
            vh = new MascotaViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof MascotaViewHolder) {
            AdoptionPet mascota = mascotas.get(i);
            //((MascotaViewHolder) viewHolder).mascotaImage.setImageDrawable(context.getResources().getDrawable(mascota.getImageResourceId(context)));
            ((MascotaViewHolder) viewHolder).mascotaName.setText(mascota.getName());
            //  ((MascotaViewHolder) viewHolder).mascotaName.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.ic_cat), null);
            ((MascotaViewHolder) viewHolder).mascotaDescription.setText(mascota.getDescription()); // TODO: aca hay que armarse el string como queremos que se muestre, con la edad, la ubicación, etc.
            ((MascotaViewHolder) viewHolder).mascotaName.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.ic_male), null); // TODO: aca hay que elegir la imagen dependiendo del animal y sexo
            ((MascotaViewHolder) viewHolder).currentMascota = mascota;
        } else {
            ((ProgressViewHolder) viewHolder).progressBar.spin();
        }
    }

    public void setLoaded() {
        loading = false;
    }


    @Override
    public int getItemCount() {
        return mascotas == null ? 0 : mascotas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mascotas.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public class MascotaViewHolder extends RecyclerView.ViewHolder {
        public TextView mascotaName;
        public TextView mascotaDescription;
        public ImageView mascotaImage;
        public AdoptionPet currentMascota;

        public MascotaViewHolder(final View itemView) {
            super(itemView);
            mascotaImage = (ImageView) itemView.findViewById(R.id.petImage);
            mascotaName = (TextView) itemView.findViewById(R.id.petName);
            mascotaDescription = (TextView) itemView.findViewById(R.id.petDescription);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressWheel progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressWheel) v.findViewById(R.id.progressBar);
        }
    }
}