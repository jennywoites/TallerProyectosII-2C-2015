package ar.uba.fi.nicodiaz.mascota.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPetState;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionRequest;
import ar.uba.fi.nicodiaz.mascota.model.PetService;
import ar.uba.fi.nicodiaz.mascota.model.RequestService;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;

/**
 * Created by nicolas on 13/09/15.
 * <p/>
 * From: http://stackoverflow.com/questions/30681905/adding-items-to-endless-scroll-recyclerview-with-progressbar-at-bottom/30691092#30691092
 */
public class RequestEndlessAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final RecyclerView view;

    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 1;
    private int currentPage = 1; // TODO: o cero
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    private List<AdoptionRequest> requestList;
    private Context context;
    private OnConfirmListener onConfirmListener;
    private OnIgnoreListener onIgnoreListener;

    public interface OnConfirmListener {
        void doAction();
    }

    public interface OnIgnoreListener {
        void doAction();
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public void setOnIgnoreListener(OnIgnoreListener onIgnoreListener) {
        this.onIgnoreListener = onIgnoreListener;
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

    public RequestEndlessAdapter(List<AdoptionRequest> list, RecyclerView recyclerView, Context context) {
        this.requestList = list;
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
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_solicitante_adopcion, parent, false);
            vh = new RequestViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RequestViewHolder) {
            RequestViewHolder view = (RequestViewHolder) viewHolder;

            AdoptionRequest adoptionRequest = requestList.get(i);
            view.userName.setText(adoptionRequest.getRequestingUser().getName());
            view.userUbication.setText(adoptionRequest.getRequestingUser().getAddress().getSubLocality());
            view.message.setText(adoptionRequest.getMessage());
            view.requestDate.setText(adoptionRequest.getDate());

            User requestingUser =  adoptionRequest.getRequestingUser();
            if (requestingUser.isMale()) {
                view.userPhoto.setImageResource(R.drawable.user_male);
            } else if (requestingUser.isFemale()) {
                view.userPhoto.setImageResource(R.drawable.user_female);
            }

            /*final ParseImageView imageView = new ParseImageView(context);
            ParseFile photoFile = adoptionRequest.getRequestingUser().getProfilePicture();
            if (photoFile != null) {
                imageView.setParseFile(photoFile);
                imageView.loadInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        try {
                            view.userPhoto.setImageDrawable(imageView.getDrawable());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }*/

            AdoptionRequest request = requestList.get(view.getAdapterPosition());

            if (request.isPending()) {
                view.status.setText("Pendiente");
                view.status_icon.setImageResource(R.drawable.ic_action_time);
                view.confirmButton.setVisibility(View.VISIBLE);
                view.ignoreButton.setVisibility(View.VISIBLE);
                view.bottomDivider.setVisibility(View.VISIBLE);
            } else if (request.isAccepted()) { // Aceptada
                view.status.setText("Aceptada");
                view.status_icon.setImageResource(R.drawable.ic_action_approved);
                view.confirmButton.setVisibility(View.GONE);
                view.ignoreButton.setVisibility(View.GONE);
                view.bottomDivider.setVisibility(View.GONE);
            }


            view.currentRequest = adoptionRequest;
        } else {
            ((ProgressViewHolder) viewHolder).progressBar.spin();
        }
    }

    public void setLoaded() {
        loading = false;
    }


    @Override
    public int getItemCount() {
        return requestList == null ? 0 : requestList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return requestList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {
        public View bottomDivider;
        public ImageView status_icon;
        public TextView status;
        public AdoptionRequest currentRequest;
        public TextView userName;
        public TextView userUbication;
        public TextView requestDate;
        public ImageView userPhoto;
        public TextView message;
        public Button confirmButton;
        public Button ignoreButton;

        public RequestViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userUbication = (TextView) itemView.findViewById(R.id.user_ubication);
            requestDate = (TextView) itemView.findViewById(R.id.request_date);
            userPhoto = (ImageView) itemView.findViewById(R.id.profile_image);
            message = (TextView) itemView.findViewById(R.id.message);
            status = (TextView) itemView.findViewById(R.id.request_status);
            status_icon = (ImageView) itemView.findViewById(R.id.status_icon);
            bottomDivider = itemView.findViewById(R.id.bottomDivider);
            confirmButton = (Button) itemView.findViewById(R.id.confirm_button);
            ignoreButton = (Button) itemView.findViewById(R.id.ignore_button);

            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("¿Realmente quiere aceptar esta solicitud de adopción?")
                            .setCancelable(false)
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {

                                    RequestService requestService = RequestService.getInstance();
                                    PetService petService = PetService.getInstance();
                                    for (AdoptionRequest request : requestList) {
                                        request.ignore();
                                        requestService.save(request);
                                    }

                                    int index = getAdapterPosition();
                                    AdoptionRequest adoptionRequestOK = requestList.get(index);
                                    adoptionRequestOK.accept();
                                    requestService.save(adoptionRequestOK);

                                    //Confirmo la mascota como adoptada
                                    AdoptionPet adoptionPet = adoptionRequestOK.getAdoptionPet();
                                    adoptionPet.setState(AdoptionPetState.ADOPTED);
                                    petService.saveAdoptionPet(adoptionPet);

                                    // Actualizamos la vista:
                                    requestList.clear(); // Saco todos
                                    requestList.add(adoptionRequestOK); // Menos la que aceptó
                                    notifyDataSetChanged();
                                    if (onConfirmListener != null) {
                                        onConfirmListener.doAction();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            ignoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("¿Realmente quiere rechazar esta solicitud de adopción?")
                            .setCancelable(false)
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    int index = getAdapterPosition();
                                    AdoptionRequest request = requestList.get(index);
                                    request.reject();
                                    request.saveInBackground();
                                    // Actualizamos la vista:
                                    requestList.remove(index);
                                    notifyItemRemoved(index);
                                    if (onIgnoreListener != null) {
                                        onIgnoreListener.doAction();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
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