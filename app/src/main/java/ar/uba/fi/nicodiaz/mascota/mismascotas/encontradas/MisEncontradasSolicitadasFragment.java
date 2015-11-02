package ar.uba.fi.nicodiaz.mascota.mismascotas.encontradas;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.mascotasgenerales.encontradas.MascotaEncontradaDetalleActivity;
import ar.uba.fi.nicodiaz.mascota.model.FoundRequest;
import ar.uba.fi.nicodiaz.mascota.model.RequestService;
import ar.uba.fi.nicodiaz.mascota.utils.RequestFoundPetEndlessAdapter;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;

/**
 * Created by nicolas on 14/09/15.
 */
public class MisEncontradasSolicitadasFragment extends Fragment {

    private static final String TAG = "MisEncontradasSolicitadasFragment";

    private Context activity;
    private View mainView;
    private TextView emptyView;
    private ArrayList<FoundRequest> list;
    private RecyclerView listView;
    private RequestFoundPetEndlessAdapter listAdapter;


    private class PetListLoader extends AsyncTask<Void, Void, Boolean> {

        private LinearLayout linlaHeaderProgress;
        private List<FoundRequest> resultList;

        public PetListLoader(View view) {
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
            resultList = RequestService.getInstance().getFoundRequestsByUser(0);
            if (resultList == null)
                return false;
            return !(resultList.isEmpty());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            linlaHeaderProgress.setVisibility(View.GONE);
            if (result) {
                for (FoundRequest request : resultList) {
                    list.add(request);
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

        setHasOptionsMenu(true);

        // View:
        mainView = inflater.inflate(R.layout.fragment_mis_encontradas_solicitadas, container, false);
        mainView.setTag(TAG);

        // ListView:
        emptyView = (TextView) mainView.findViewById(R.id.empty_view);

        list = new ArrayList<>();

        listView = (RecyclerView) mainView.findViewById(R.id.list_encontradas);
        listView.setLayoutManager(new LinearLayoutManager(activity));
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setHasFixedSize(true);

        listAdapter = new RequestFoundPetEndlessAdapter(list, listView, activity);
        listAdapter.setOnItemClickListener(new RequestFoundPetEndlessAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent i = new Intent(activity, MascotaEncontradaSolicitadaDetalleActivity.class);
                FoundRequest foundRequest = list.get(position);
                PetServiceFactory.getInstance().setSelectedPet(foundRequest.getFoundPet());
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_1, R.anim.slide_out_1);
            }
        });

        listView.setAdapter(listAdapter);

        // Cargando la lista de mascotas:
        new PetListLoader(mainView).execute();


        return mainView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
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

