package ar.uba.fi.nicodiaz.mascota.mismascotas.perdidas;

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
import ar.uba.fi.nicodiaz.mascota.model.MissingPet;
import ar.uba.fi.nicodiaz.mascota.model.MissingRequest;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.RequestService;
import ar.uba.fi.nicodiaz.mascota.utils.RequestMisPerdidasEndlessAdapter;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;

/**
 * Created by nicolas on 14/09/15.
 */
public class MascotaPerdidaPublicadaDetalleSolicitudesFragment extends Fragment {

    private static final String TAG = "MisPerdidasPublicadasFragment";

    private Context activity;
    private View mainView;
    private TextView emptyView;
    private ArrayList<MissingRequest> list;
    private RecyclerView listView;
    private RequestMisPerdidasEndlessAdapter listAdapter;

    private class RequestListLoader extends AsyncTask<Void, Void, Boolean> {

        private LinearLayout linlaHeaderProgress;
        private List<MissingRequest> resultList;

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
            resultList = RequestService.getInstance().getMissingRequests((MissingPet) pet);
            if (resultList == null)
                return false;
            return !(resultList.isEmpty());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            linlaHeaderProgress.setVisibility(View.GONE);
            if (result) {
                for (MissingRequest missingRequest : resultList) {
                    list.add(missingRequest);
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
        mainView = inflater.inflate(R.layout.fragment_mascota_perdida_publicada_detalle_solicitudes, container, false);
        mainView.setTag(TAG);

        // ListView:
        emptyView = (TextView) mainView.findViewById(R.id.empty_view);

        list = new ArrayList<>();

        listView = (RecyclerView) mainView.findViewById(R.id.list_request);
        listView.setLayoutManager(new LinearLayoutManager(activity));
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setHasFixedSize(true);

        listAdapter = new RequestMisPerdidasEndlessAdapter(list, listView, activity);
        listAdapter.setOnConfirmListener(new RequestMisPerdidasEndlessAdapter.OnConfirmListener() {
            @Override
            public void doAction() {
                refresh();
            }
        });
        listAdapter.setOnIgnoreListener(new RequestMisPerdidasEndlessAdapter.OnIgnoreListener() {
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

