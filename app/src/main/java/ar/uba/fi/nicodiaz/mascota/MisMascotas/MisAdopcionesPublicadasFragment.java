package ar.uba.fi.nicodiaz.mascota.MisMascotas;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.MascotasGenerales.AdopcionPublicarActivity;
import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.PetService;
import ar.uba.fi.nicodiaz.mascota.utils.AdopcionEndlessAdapter;

/**
 * Created by nicolas on 14/09/15.
 */
public class MisAdopcionesPublicadasFragment extends Fragment {

    private static final String TAG = "MisAdopcionesPublicadasFragment";

    private Context activity;
    private View mainView;
    private TextView emptyView;
    private ArrayList<Pet> list;
    private RecyclerView listView;
    private AdopcionEndlessAdapter listAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean hayMas;

    private class LoadMorePets extends AsyncTask<Integer, Void, Boolean> {

        List<? extends Pet> resultList;

        @Override
        protected void onPreExecute() {
            list.add(null);
            listAdapter.notifyItemInserted(list.size() - 1);
        }

        @Override
        protected Boolean doInBackground(Integer... currentPage) {
            resultList = PetService.getInstance().getAdoptionPetsByUser(currentPage[0]);
            return !resultList.isEmpty();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            list.remove(list.size() - 1);
            listAdapter.notifyItemRemoved(list.size());
            if (!result) {
                hayMas = false;
                return;
            }

            for (Pet pet : resultList) {
                list.add(pet);
                listAdapter.notifyItemInserted(list.size() - 1);
            }
            listAdapter.setLoaded();
        }
    }

    private class PetListLoader extends AsyncTask<Void, Void, Boolean> {

        private LinearLayout linlaHeaderProgress;
        private List<? extends Pet> resultList;

        public PetListLoader(View view) {
            linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
            resultList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            list.clear();
            listAdapter.notifyDataSetChanged();
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            listAdapter.reset();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            resultList = PetService.getInstance().getAdoptionPetsByUser(0);
            return !(resultList.isEmpty());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            linlaHeaderProgress.setVisibility(View.GONE);
            if (result) {
                for (Pet pet : resultList) {
                    list.add(pet);
                }
                listAdapter.notifyDataSetChanged();
            }
            checkEmptyList();
            listAdapter.setLoaded();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();

        // View:
        mainView = inflater.inflate(R.layout.fragment_mis_adopciones_publicadas, container, false);
        mainView.setTag(TAG);

        // FAB:
        FloatingActionButton FAB = (FloatingActionButton) mainView.findViewById(R.id.FAB_agregar_adopcion);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevaAdopcion();
            }
        });

        // ListView:
        emptyView = (TextView) mainView.findViewById(R.id.empty_view);

        list = new ArrayList<>();

        listView = (RecyclerView) mainView.findViewById(R.id.list_adoption);
        listView.setLayoutManager(new LinearLayoutManager(activity));
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setHasFixedSize(true);

        listAdapter = new AdopcionEndlessAdapter(list, listView, activity);
        listAdapter.setOnItemClickListener(new AdopcionEndlessAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent i = new Intent(activity, MascotaAdopcionPublicadaDetalleActivity.class);
                Pet pet = list.get(position);
                PetService.getInstance().setSelectedPet(pet);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_1, R.anim.slide_out_1);
            }
        });
        listAdapter.setOnLoadMoreListener(new AdopcionEndlessAdapter.OnLoadMoreListener() {
            @Override
            public boolean onLoadMore(int currentPage) {
                if (hayMas) {
                    new LoadMorePets().execute(currentPage);
                    return true;
                }
                return false;
            }
        });

        listView.setAdapter(listAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.ColorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                applyQuery();
            }
        });

        // Cargando la lista de mascotas:
        applyQuery();


        return mainView;
    }

    private void applyQuery() {
        hayMas = true;
        new PetListLoader(mainView).execute();
    }

    private void nuevaAdopcion() {
        Intent i = new Intent(activity, AdopcionPublicarActivity.class);
        startActivity(i);
    }

    private void checkEmptyList() {
        if (list.isEmpty()) {
            listView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
