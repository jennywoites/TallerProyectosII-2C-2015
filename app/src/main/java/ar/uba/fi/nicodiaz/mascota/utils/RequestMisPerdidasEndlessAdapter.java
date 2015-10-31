package ar.uba.fi.nicodiaz.mascota.utils;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.MissingPet;
import ar.uba.fi.nicodiaz.mascota.model.MissingPetState;
import ar.uba.fi.nicodiaz.mascota.model.MissingRequest;
import ar.uba.fi.nicodiaz.mascota.model.PushService;
import ar.uba.fi.nicodiaz.mascota.model.RequestService;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.service.api.PetService;
import ar.uba.fi.nicodiaz.mascota.utils.Email.EmailHelper;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;

/**
 * Created by nicolas on 13/09/15.
 * <p/>
 * From: http://stackoverflow.com/questions/30681905/adding-items-to-endless-scroll-recyclerview-with-progressbar-at-bottom/30691092#30691092
 */
public class RequestMisPerdidasEndlessAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final RecyclerView view;

    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 1;
    private int currentPage = 1; // TODO: o cero
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    private List<MissingRequest> requestList;
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

    public RequestMisPerdidasEndlessAdapter(List<MissingRequest> list, RecyclerView recyclerView, Context context) {
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

            MissingRequest missingRequest = requestList.get(i);
            view.userName.setText(missingRequest.getRequestingUser().getName());
            view.userUbication.setText(missingRequest.getRequestingUser().getAddress().getSubLocality());
            view.message.setText(missingRequest.getMessage());
            view.requestDate.setText(missingRequest.getDate());

            User requestingUser = missingRequest.getRequestingUser();
            if (requestingUser.isMale()) {
                view.userPhoto.setImageResource(R.drawable.user_male);
            } else if (requestingUser.isFemale()) {
                view.userPhoto.setImageResource(R.drawable.user_female);
            }

            /*final ParseImageView imageView = new ParseImageView(context);
            ParseFile photoFile = missingRequest.getRequestingUser().getProfilePicture();
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

            MissingRequest request = requestList.get(view.getAdapterPosition());

            if (request.isPending()) {
                view.status.setText("Pendiente");
                view.status_icon.setImageResource(R.drawable.ic_action_time);
                view.aceptConfirmButtons.setVisibility(View.VISIBLE);
                view.successFailButtons.setVisibility(View.GONE);
                view.bottomDivider.setVisibility(View.VISIBLE);
            } else if (request.isAccepted()) { // Posible
                view.status.setText("Posible");
                view.status_icon.setImageResource(R.drawable.ic_action_approved);
                view.aceptConfirmButtons.setVisibility(View.GONE);
                view.successFailButtons.setVisibility(View.VISIBLE);
                view.bottomDivider.setVisibility(View.GONE);
            } else if (request.isConfirmed()) { // Encontrada
                view.status.setText("Encontrada");
                view.status_icon.setImageResource(R.drawable.ic_action_approved);
                view.aceptConfirmButtons.setVisibility(View.GONE);
                view.successFailButtons.setVisibility(View.GONE);
                view.bottomDivider.setVisibility(View.GONE);
            } else {
                view.status.setText("NoSeVe");
                view.status_icon.setImageResource(R.drawable.ic_action_rejected);
                view.aceptConfirmButtons.setVisibility(View.GONE);
                view.successFailButtons.setVisibility(View.GONE);
                view.bottomDivider.setVisibility(View.GONE);
            }


            view.currentRequest = missingRequest;
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
        public MissingRequest currentRequest;
        public TextView userName;
        public TextView userUbication;
        public TextView requestDate;
        public ImageView userPhoto;
        public TextView message;
        public LinearLayout aceptConfirmButtons;
        public LinearLayout successFailButtons;
        public Button confirmButton;
        public Button ignoreButton;
        public Button success;
        public Button fail;

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
            success = (Button) itemView.findViewById(R.id.success_button);
            fail = (Button) itemView.findViewById(R.id.fail_button);
            aceptConfirmButtons = (LinearLayout) itemView.findViewById(R.id.buttonsAceptIgnore);
            successFailButtons = (LinearLayout) itemView.findViewById(R.id.buttonsSuccessFail);

            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("¿Podría ser esta su mascota?")
                            .setCancelable(false)
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {

                                    RequestService requestService = RequestService.getInstance();
                                    PetService petService = PetServiceFactory.getInstance();
                                    for (MissingRequest request : requestList) {
                                        request.ignore();
                                        requestService.save(request);
                                    }

                                    int index = getAdapterPosition();
                                    MissingRequest missingRequestOK = requestList.get(index);
                                    missingRequestOK.accept();
                                    requestService.save(missingRequestOK, requestList);

                                    EmailHelper.sendEmail((Activity) context, "MascotaPerdidaEncontrada", missingRequestOK.getMissingPet().getOwner(), missingRequestOK.getRequestingUser());

                                    //Reservamos la mascota como posible
                                    MissingPet missingPet = missingRequestOK.getMissingPet();
                                    missingPet.setState(MissingPetState.POSSIBLE);
                                    petService.saveMissingPet(missingPet);

                                    // Actualizamos la vista:
                                    requestList.clear(); // Saco todos
                                    requestList.add(missingRequestOK); // Menos la que aceptó
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
                    builder.setMessage("¿Realmente quiere rechazar esta solicitud?")
                            .setCancelable(false)
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    int index = getAdapterPosition();
                                    MissingRequest request = requestList.get(index);
                                    request.reject();
                                    RequestService.getInstance().save(request);
                                    PushService.getInstance().sendRejectRequestMissingPet(request.getMissingPet(), request.getRequestingUser());
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
            success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("¿Marcarla como encontrada?")
                            .setCancelable(false)
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {

                                    RequestService requestService = RequestService.getInstance();
                                    PetService petService = PetServiceFactory.getInstance();

                                    int index = getAdapterPosition();
                                    MissingRequest missingRequestOK = requestList.get(index);
                                    missingRequestOK.confirm();
                                    requestService.save(missingRequestOK, requestList);

                                    MissingPet missingPet = missingRequestOK.getMissingPet();

                                    // ponemos en rechazadas a las ignoradas:
                                    List<MissingRequest> requests = requestService.getAllMissingRequests(missingPet);
                                    for (MissingRequest request : requests) {
                                        if (request.isIgnored()) {
                                            request.reject();
                                            requestService.save(request);
                                        }
                                    }

                                    //Confirmo la mascota como encontrada
                                    missingPet.setState(MissingPetState.FOUND);
                                    petService.saveMissingPet(missingPet);

                                    // Actualizamos la vista:
                                    requestList.clear();
                                    requestList.add(missingRequestOK);
                                    notifyDataSetChanged();

                                    if (onConfirmListener != null) {
                                        onConfirmListener.doAction();
                                    }
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            fail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("¿No era tu mascota?")
                            .setCancelable(false)
                            .setPositiveButton("No era", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    int index = getAdapterPosition();
                                    MissingRequest actualRequest = requestList.get(index);
                                    actualRequest.reject();
                                    actualRequest.saveInBackground();

                                    // Republicamos la mascota:
                                    PetService petService = PetServiceFactory.getInstance();
                                    MissingPet pet = actualRequest.getMissingPet();
                                    pet.setState(MissingPetState.PUBLISHED);
                                    petService.saveMissingPet(pet);

                                    // Actualizamos la vista:
                                    requestList.remove(index); // quitamos la actual
                                    // ponemos en pendiente a las ignoradas y las agregamos a la lista:
                                    RequestService requestService = RequestService.getInstance();
                                    List<MissingRequest> requests = requestService.getAllMissingRequests(pet);
                                    for (MissingRequest request : requests) {
                                        if (request.isIgnored()) {
                                            request.pend();
                                            requestService.save(request);
                                            requestList.add(request);
                                        }
                                    }
                                    notifyDataSetChanged();
                                    if (onIgnoreListener != null) {
                                        onIgnoreListener.doAction();
                                    }
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
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
