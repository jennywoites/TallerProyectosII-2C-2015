package ar.uba.fi.nicodiaz.mascota.mismascotas.encontradas;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.mascotasgenerales.encontradas.EncontradasPublicarActivity;
import ar.uba.fi.nicodiaz.mascota.model.FoundPetState;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.utils.FoundEndlessAdapter;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;

/**
 * Created by nicolas on 14/09/15.
 */
public class MisEncontradasPublicadasFragment extends Fragment {

    private static final String TAG = "MisEncontradasPublicadasFragment";

    private Context activity;
    private View mainView;
    private TextView emptyView;
    private ArrayList<Pet> list;
    private RecyclerView listView;
    private FoundEndlessAdapter listAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean hayMas;
    private String selectedFilter;

    private class LoadMorePets extends AsyncTask<Integer, Void, Boolean> {

        List<? extends Pet> resultList;

        @Override
        protected void onPreExecute() {
            list.add(null);
            listAdapter.notifyItemInserted(list.size() - 1);
        }

        @Override
        protected Boolean doInBackground(Integer... currentPage) {
            resultList = PetServiceFactory.getInstance().getFoundPetsByUser(currentPage[0], selectedFilter);
            if (resultList == null)
                return false;
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
            resultList = PetServiceFactory.getInstance().getFoundPetsByUser(0, selectedFilter);
            if (resultList == null)
                return false;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.mis_encontradas_publicadas, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_published:
                selectedFilter = FoundPetState.PUBLISHED.toString();
                break;
            case R.id.action_accepted:
                selectedFilter = FoundPetState.RETAINED.toString();
                break;
            case R.id.action_founded:
                selectedFilter = FoundPetState.REENCOTRADA.toString();
                break;
            case R.id.action_hidden:
                selectedFilter = FoundPetState.HIDDEN.toString();
                break;
            default:
                selectedFilter = FoundPetState.PUBLISHED.toString();
                applyQuery();
                return super.onOptionsItemSelected(item);
        }
        applyQuery();
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        setHasOptionsMenu(true);

        selectedFilter = FoundPetState.PUBLISHED.toString();

        // View:
        mainView = inflater.inflate(R.layout.fragment_mis_encontradas_publicadas, container, false);
        mainView.setTag(TAG);

        // FAB:
        FloatingActionButton FAB = (FloatingActionButton) mainView.findViewById(R.id.FAB_agregar_encontrada);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevaEncontrada();
            }
        });

        // ListView:
        emptyView = (TextView) mainView.findViewById(R.id.empty_view);

        list = new ArrayList<>();

        listView = (RecyclerView) mainView.findViewById(R.id.list_encontradas);
        listView.setLayoutManager(new LinearLayoutManager(activity));
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setHasFixedSize(true);

        listAdapter = new FoundEndlessAdapter(list, listView, activity);
        listAdapter.setOnItemClickListener(new FoundEndlessAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent i = new Intent(activity, MascotaEncontradaPublicadaDetalleActivity.class);
                Pet pet = list.get(position);
                PetServiceFactory.getInstance().setSelectedPet(pet);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_1, R.anim.slide_out_1);
            }
        });
        listAdapter.setOnLoadMoreListener(new FoundEndlessAdapter.OnLoadMoreListener() {
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

    private void nuevaEncontrada() {
        Intent i = new Intent(activity, EncontradasPublicarActivity.class);
        startActivity(i);
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