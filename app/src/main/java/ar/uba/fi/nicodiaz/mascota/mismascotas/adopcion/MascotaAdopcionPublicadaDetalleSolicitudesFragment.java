package ar.uba.fi.nicodiaz.mascota.mismascotas.adopcion;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionRequest;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.RequestService;
import ar.uba.fi.nicodiaz.mascota.utils.RequestEndlessAdapter;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;

/**
 * Created by nicolas on 14/09/15.
 */
public class MascotaAdopcionPublicadaDetalleSolicitudesFragment extends Fragment {

    private static final String TAG = "MisAdopcionesPublicadasFragment";

    private Context activity;
    private View mainView;
    private TextView emptyView;
    private ArrayList<AdoptionRequest> list;
    private RecyclerView listView;
    private RequestEndlessAdapter listAdapter;

    private class RequestListLoader extends AsyncTask<Void, Void, Boolean> {

        private LinearLayout linlaHeaderProgress;
        private List<AdoptionRequest> resultList;

        public RequestListLoader(View view) {
            linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
            resultList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            list.clear();
            listAdapter.notifyDataSetChanged();
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            listAdapter.reset();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Pet pet = PetServiceFactory.getInstance().getSelectedPet();
            resultList = RequestService.getInstance().getAdoptionRequests((AdoptionPet) pet);
            if (resultList == null)
                return false;
            return !(resultList.isEmpty());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            linlaHeaderProgress.setVisibility(View.GONE);
            if (result) {
                for (AdoptionRequest adoptionRequest : resultList) {
                    list.add(adoptionRequest);
                }
                listAdapter.notifyDataSetChanged();
            }
            checkEmptyList();
            listAdapter.setLoaded();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();

        // View:
        mainView = inflater.inflate(R.layout.fragment_mascota_encontrada_publicada_detalle_solicitudes, container, false);
        mainView.setTag(TAG);

        // ListView:
        emptyView = (TextView) mainView.findViewById(R.id.empty_view);

        list = new ArrayList<>();

        listView = (RecyclerView) mainView.findViewById(R.id.list_request);
        listView.setLayoutManager(new LinearLayoutManager(activity));
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setHasFixedSize(true);

        listAdapter = new RequestEndlessAdapter(list, listView, activity);
        listAdapter.setOnConfirmListener(new RequestEndlessAdapter.OnConfirmListener() {
            @Override
            public void doAction() {
                refresh();
            }
        });
        listAdapter.setOnIgnoreListener(new RequestEndlessAdapter.OnIgnoreListener() {
            @Override
            public void doAction() {
                refresh();
            }
        });
        listView.setAdapter(listAdapter);

        // Cargando la lista de mascotas:
        new RequestListLoader(mainView).execute();

        return mainView;
    }

    private void refresh() {
        checkEmptyList();
    }

    private void checkEmptyList() {
        if (!WaitForInternet.isConnected(activity)) {
            Toast.makeText(activity, "Revise su conexión a Internet", Toast.LENGTH_SHORT).show();
        }
        if (list.isEmpty()) {
            listView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}

