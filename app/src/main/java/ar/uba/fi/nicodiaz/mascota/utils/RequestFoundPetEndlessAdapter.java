package ar.uba.fi.nicodiaz.mascota.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.FoundRequest;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.FoundPet;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;

/**
 * Created by nicolas on 13/09/15.
 * <p/>
 * From: http://stackoverflow.com/questions/30681905/adding-items-to-endless-scroll-recyclerview-with-progressbar-at-bottom/30691092#30691092
 */
public class RequestFoundPetEndlessAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final RecyclerView view;

    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 1;
    private int currentPage = 1; // TODO: o cero
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    private List<FoundRequest> foundRequestList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnLoadMoreListener {
        boolean onLoadMore(int currentPage);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void reset() {
        loading = true;
        totalItemCount = ((LinearLayoutManager) view.getLayoutManager()).getItemCount();
        lastVisibleItem = ((LinearLayoutManager) view.getLayoutManager()).findLastVisibleItemPosition();
        currentPage = 1; // TODO: o cero
    }

    public RequestFoundPetEndlessAdapter(List<FoundRequest> foundRequestList, RecyclerView recyclerView, Context context) {
        this.foundRequestList = foundRequestList;
        this.context = context;
        this.view = recyclerView;

        if (view.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) view.getLayoutManager();
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
                            if (onLoadMoreListener.onLoadMore(currentPage)) {
                                currentPage++;
                            }
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
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_request_found, parent, false);
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
            MascotaViewHolder view = (MascotaViewHolder) viewHolder;
            FoundRequest foundRequest = foundRequestList.get(i);
            FoundPet foundPet = foundRequest.getFoundPet();

            if (foundPet == null) {
                return;
            }

            ParseFile photoFile = foundPet.getPicture();
            if (photoFile != null) {
                view.mascotaImage.setVisibility(View.VISIBLE);
                view.mascotaImage.setParseFile(photoFile);
                view.mascotaImage.loadInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                    }
                });
            } else {
                view.mascotaImage.setVisibility(View.INVISIBLE);
            }

            view.mascotaDescription.setText(foundPet.getPreviewDescription());

            if (foundPet.isMale()) {
                view.mascotaDescription.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.ic_male), null);
            } else {
                view.mascotaDescription.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.ic_female), null);
            }

            int iconPetID = PetServiceFactory.getInstance().getIconPet(foundPet.getKind());
            if (iconPetID != -1) {
                view.mascotaName.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(iconPetID), null);
            }

            if (foundRequest.isAccepted()) {
                view.requestDescription.setText("Estado Solicitud: Aceptada");
                view.requestStatusIcon.setImageResource(R.drawable.ic_action_approved_white);
            } else if (foundRequest.isPending()) {
                view.requestDescription.setText("Estado Solicitud: Pendiente");
                view.requestStatusIcon.setImageResource(R.drawable.ic_action_time_white);
            } else if (foundRequest.isConfirmed()) {
                view.requestDescription.setText("Estado Solicitud: Reencontrada");
                view.requestStatusIcon.setImageResource(R.drawable.ic_adoption_white);
            } else {
                view.requestDescription.setText("Estado Solicitud: Rechazada");
                view.requestStatusIcon.setImageResource(R.drawable.ic_action_rejected);
            }

            view.currentMascota = foundPet;
        } else {
            ((ProgressViewHolder) viewHolder).progressBar.spin();
        }
    }

    public void setLoaded() {
        loading = false;
    }


    @Override
    public int getItemCount() {
        return foundRequestList == null ? 0 : foundRequestList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return foundRequestList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public class MascotaViewHolder extends RecyclerView.ViewHolder {
        public TextView mascotaName;
        public TextView mascotaDescription;
        public ParseImageView mascotaImage;
        public Pet currentMascota;
        public TextView requestDescription;
        public ImageView requestStatusIcon;

        public MascotaViewHolder(final View itemView) {
            super(itemView);
            mascotaImage = (ParseImageView) itemView.findViewById(R.id.petImage);
            mascotaName = (TextView) itemView.findViewById(R.id.petName);
            mascotaDescription = (TextView) itemView.findViewById(R.id.petDescription);
            requestDescription = (TextView) itemView.findViewById(R.id.requestState);
            requestStatusIcon = (ImageView) itemView.findViewById(R.id.requestStatusIcon);
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