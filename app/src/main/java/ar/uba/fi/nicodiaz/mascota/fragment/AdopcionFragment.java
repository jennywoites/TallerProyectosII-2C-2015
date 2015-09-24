package ar.uba.fi.nicodiaz.mascota.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.uba.fi.nicodiaz.mascota.AdopcionPublicarActivity;
import ar.uba.fi.nicodiaz.mascota.MascotaDetalleActivity;
import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.PetService;
import ar.uba.fi.nicodiaz.mascota.utils.AdopcionEndlessAdapter;
import ar.uba.fi.nicodiaz.mascota.utils.Filter;
import ar.uba.fi.nicodiaz.mascota.utils.SettingsListAdapter;

public class AdopcionFragment extends Fragment {

    private static final String TAG = "AdopcionFragment";

    private List<AdoptionPet> list;
    private RecyclerView listView;
    private TextView emptyView;
    private AdopcionEndlessAdapter listAdapter;

    private Context activity;

    private DrawerLayout drawerLayout;
    private FloatingActionButton FAB;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_adopcion, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_buscar_adopcion:
                buscarAdopcion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void buscarAdopcion() {
        int drawer = GravityCompat.END;
        if (drawerLayout.isDrawerOpen(drawer)) {
            drawerLayout.closeDrawers();
        } else {
            drawerLayout.openDrawer(drawer);
        }
    }

    private void nuevaAdopcion() {
        Intent i = new Intent(activity, AdopcionPublicarActivity.class);
        startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = PetService.getInstance().getAdoptionPets();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();

        // Toolbar:
        setHasOptionsMenu(true);

        // View:
        View view = inflater.inflate(R.layout.fragment_adopcion, container, false);
        view.setTag(TAG);

        // FAB
        FAB = (FloatingActionButton) view.findViewById(R.id.FAB_agregar_adopcion);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevaAdopcion();
            }
        });

        // Sliding Menu
        drawerLayout = (DrawerLayout) view.findViewById(R.id.sliding_layout);

        // ListView
        listView = (RecyclerView) view.findViewById(R.id.list_adoption);

        listView.setLayoutManager(new LinearLayoutManager(activity));
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setHasFixedSize(true);

        listAdapter = new AdopcionEndlessAdapter(list, listView, activity);
        listAdapter.setOnItemClickListener(new AdopcionEndlessAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                // TODO: aca se maneja el click sobre un item de la lista:

                Intent i = new Intent(activity, MascotaDetalleActivity.class);
                i.putExtra("ID", list.get(position).getName()); // TODO: Acá tirarle algun ID para que la activity lo busque en la base de datos y obtenga TODOS los datos.
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_1, R.anim.slide_out_1);
            }
        });

        listAdapter.setOnLoadMoreListener(new AdopcionEndlessAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add progress item
                list.add(null);
                listAdapter.notifyItemInserted(list.size() - 1);

            /*  // remove progress item
                list.remove(list.size() - 1);
                listAdapter.notifyItemRemoved(list.size());

                // add items
                // TODO: aca llamar a la base de datos y pedir 20 elementos mas.

                // Si no hay mas items que cargar, mostrar un toast y salir:
                if (itemsDeBaseDeDatos == null) {
                    Toast.makeText(activity, "No hay mas items", Toast.LENGTH_SHORT).show();
                    return;
                }

                list.add(itemsDeBaseDeDatos);
                listAdapter.notifyItemInserted(list.size());

                listAdapter.setLoaded();
             */

                // TODO: eliminar esto despues, solo testing:
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //remove progress item
                        list.remove(list.size() - 1);
                        listAdapter.notifyItemRemoved(list.size());

                        //add items one by one
/*                        for (int i = 0; i < 10; i++) {
                            list.add(new Mascota("Mascota " + (list.size() + 1), "Agregada", "pikachu"));
                            listAdapter.notifyItemInserted(list.size());
                        }*/
                        listAdapter.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });

        listView.setAdapter(listAdapter);

        emptyView = (TextView) view.findViewById(R.id.empty_view);

        checkEmptyList();


        // Filter:

        filtersList = (ExpandableListView) view.findViewById(R.id.categories);
        filters = Filter.getFilters();
        adapter = new SettingsListAdapter(activity,
                filters, filtersList);
        filtersList.setAdapter(adapter);

        selectedFilter = new HashMap<>();

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
                            selectedFilter.remove(parentName);
                        } else {
                            sub.setText(filter.selection.toString());
                            selectedFilter.put(parentName, filter.selection);
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

                // TODO: aca se debería acceder a los elementos que fueron seleccionados con true y mandarlo a una query.
                Log.i(String.valueOf(Log.INFO), "+++++++++++++");
                for (String parent : selectedFilter.keySet()) {
                    String childrens = "";
                    for (String children : selectedFilter.get(parent)) {
                        childrens += children + "|";
                    }
                    Log.i(String.valueOf(Log.INFO), parent + ": " + childrens);
                }

                Toast.makeText(activity, "Filtro Aplicado", Toast.LENGTH_SHORT).show();

                // aplicar cambios a la lista
                //applyQuery();
            }
        });

        Button cancelFilterButton = (Button) view.findViewById(R.id.filter_button2);
        cancelFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filters = Filter.getFilters();
                adapter = new SettingsListAdapter(activity, filters, filtersList);
                filtersList.setAdapter(adapter);
                selectedFilter = new HashMap<>();
                Toast.makeText(activity, "Filtro Desactivado", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

    private void applyQuery() {
        // TODO: actualizar la lista "list" con los nuevos datos. Quizas haya que crear las clases Mascota acá.
        // list = nueva lista.

        // avisamos que cambió la lista:
        // TODO: supongo que no hay problema al sobreescribir list, es decir, no habria que crear un nuevo listAdapter. A CONFIRMAR.
        listAdapter.notifyDataSetChanged();

        // vemos si hubo algun resultado:
        checkEmptyList();
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

    // Filter:
    private SettingsListAdapter adapter;
    private ExpandableListView filtersList;
    private ArrayList<Filter> filters;
    private Map<String, List<String>> selectedFilter;
}
