package ar.uba.fi.nicodiaz.mascota.mascotasgenerales.perdidas;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.utils.Filter;
import ar.uba.fi.nicodiaz.mascota.utils.MissingEndlessAdapter;
import ar.uba.fi.nicodiaz.mascota.utils.SettingsListAdapter;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;

public class PerdidasFragment extends Fragment {
    private static final String TAG = "PerdidasFragment";
    private List<Pet> list;
    private RecyclerView listView;
    private TextView emptyView;
    private boolean hayMas;
    private MissingEndlessAdapter listAdapter;
    private Context activity;
    private DrawerLayout drawerLayout;
    private View mainView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private class LoadMorePets extends AsyncTask<Integer, Void, Boolean> {
        List<? extends Pet> resultList;

        @Override
        protected void onPreExecute() {
            list.add(null);
            listAdapter.notifyItemInserted(list.size() - 1);
        }

        @Override
        protected Boolean doInBackground(Integer... currentPage) {
            if (selectedFilter.isEmpty()) {
                resultList = PetServiceFactory.getInstance().getMissingPets(currentPage[0]);
            } else {
                resultList = PetServiceFactory.getInstance().getMissingPets(currentPage[0], selectedFilter);
            }
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
            if (selectedFilter.isEmpty()) {
                resultList = PetServiceFactory.getInstance().getMissingPets(0);
            } else {
                resultList = PetServiceFactory.getInstance().getMissingPets(0, selectedFilter);
            }
            if (resultList == null)
                return false;

            return !(resultList.isEmpty());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            linlaHeaderProgress.setVisibility(View.GONE);
            if (result) {
                list.addAll(resultList);
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
        ((AppCompatActivity) activity).getSupportActionBar().setSubtitle(null);

        // Toolbar:
        setHasOptionsMenu(true);

        // View:
        mainView = inflater.inflate(R.layout.fragment_missing, container, false);
        mainView.setTag(TAG);

        // FAB
        FloatingActionButton FAB = (FloatingActionButton) mainView.findViewById(R.id.FAB_agregar_perdido);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevaPerdida();
            }
        });

        // Sliding Menu
        drawerLayout = (DrawerLayout) mainView.findViewById(R.id.sliding_layout);

        // Filter:
        createFilterMenu(mainView);

        // ListView
        emptyView = (TextView) mainView.findViewById(R.id.empty_view);

        list = new ArrayList<>();

        listView = (RecyclerView) mainView.findViewById(R.id.list_missing);
        listView.setLayoutManager(new LinearLayoutManager(activity));
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setHasFixedSize(true);

        listAdapter = new MissingEndlessAdapter(list, listView, activity);
        listAdapter.setOnItemClickListener(new MissingEndlessAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent i = new Intent(activity, MascotaPerdidaDetalleActivity.class);
                Pet pet = list.get(position);
                PetServiceFactory.getInstance().setSelectedPet(pet);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_1, R.anim.slide_out_1);
            }
        });

        listAdapter.setOnLoadMoreListener(new MissingEndlessAdapter.OnLoadMoreListener() {
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_missing, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_buscar_perdido:
                buscarPerdido();
                return true;
            case R.id.action_refresh:
                applyQuery();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void buscarPerdido() {
        int drawer = GravityCompat.END;
        if (drawerLayout.isDrawerOpen(drawer)) {
            drawerLayout.closeDrawers();
        } else {
            drawerLayout.openDrawer(drawer);
        }
    }

    private void nuevaPerdida() {
        Intent i = new Intent(activity, PerdidasPublicarActivity.class);
        startActivity(i);
    }

    // Filter:
    private SettingsListAdapter adapter;
    private ExpandableListView filtersList;
    private ArrayList<Filter> filters;
    private Map<String, List<String>> selectedFilter;
    private Map<String, List<String>> tempSelectedFilter;

    private void createFilterMenu(View view) {
        filtersList = (ExpandableListView) view.findViewById(R.id.categories);
        filters = Filter.getPerdidasFilters();
        adapter = new SettingsListAdapter(activity,
                filters, filtersList);
        filtersList.setAdapter(adapter);

        selectedFilter = new HashMap<>();
        tempSelectedFilter = new HashMap<>();

        filtersList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                CheckedTextView checkbox = (CheckedTextView) v.findViewById(R.id.list_item_text_child);
                checkbox.toggle();

                String parentName = filters.get(groupPosition).name;
                String childName = checkbox.getText().toString();


                // find parent view by tag
                View parentView = filtersList.findViewWithTag(parentName);
                if (parentView != null) {
                    TextView sub = (TextView) parentView.findViewById(R.id.list_item_text_subscriptions);

                    if (sub != null) {
                        Filter filter = filters.get(groupPosition);
                        if (checkbox.isChecked()) {
                            // add child filter to parent's selection list
                            filter.selection.add(childName);
                        } else {
                            // remove child filter from parent's selection list
                            filter.selection.remove(childName);
                        }

                        // display selection list
                        //if (filter.selection.isEmpty() || filter.selection.size() == filter.children.size()) {
                        if (filter.selection.isEmpty()) {
                            sub.setText("");
                            tempSelectedFilter.remove(parentName);
                        } else {
                            sub.setText(filter.selection.toString());
                            tempSelectedFilter.put(parentName, filter.selection);
                        }
                    }
                }
                return true;
            }
        });

        Button filterButton = (Button) view.findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                Toast.makeText(activity, "Filtro Aplicado", Toast.LENGTH_SHORT).show();
                // aplicar cambios a la lista
                selectedFilter.clear();

                // Copiar los filtos seleccionados
                for (String key : tempSelectedFilter.keySet()) {
                    List<String> listValue = tempSelectedFilter.get(key);
                    List<String> copiedListValue = new ArrayList<>();
                    copiedListValue.addAll(listValue);
                    selectedFilter.put(key, copiedListValue);
                }
                applyQuery();
            }
        });

        Button cancelFilterButton = (Button) view.findViewById(R.id.filter_button2);
        cancelFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filters = Filter.getPerdidasFilters();
                adapter = new SettingsListAdapter(activity, filters, filtersList);
                filtersList.setAdapter(adapter);
                tempSelectedFilter.clear();
                Toast.makeText(activity, "Filtro Desactivado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
