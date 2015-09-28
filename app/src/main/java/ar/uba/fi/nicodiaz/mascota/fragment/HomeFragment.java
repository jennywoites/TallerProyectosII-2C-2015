package ar.uba.fi.nicodiaz.mascota.fragment;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.LoginActivity;
import ar.uba.fi.nicodiaz.mascota.MascotaDetalleActivity;
import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.PetService;
import ar.uba.fi.nicodiaz.mascota.utils.AdopcionEndlessAdapter;
import ar.uba.fi.nicodiaz.mascota.utils.ParseProxyObject;

/**
 * Created by nicolas on 14/09/15.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private List<AdoptionPet> list;
    private RecyclerView listView;
    private TextView emptyView;
    private AdopcionEndlessAdapter listAdapter;

    private Context activity;
    private View mainView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();

        // Toolbar:
        setHasOptionsMenu(true);

        // View:
        mainView = inflater.inflate(R.layout.fragment_home, container, false);
        mainView.setTag(TAG);


        // Cargando la lista de mascotas:
        class InitialPetLoader extends AsyncTask<Void, Void, Boolean> {
            private LinearLayout linlaHeaderProgress;
            private View view;

            public InitialPetLoader(View view) {
                linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
                this.view = view;
            }

            @Override
            protected void onPreExecute() {
                linlaHeaderProgress.setVisibility(View.VISIBLE);

                // ListView
                listView = (RecyclerView) view.findViewById(R.id.list_adoption);
                listView.setLayoutManager(new LinearLayoutManager(activity));
                listView.setItemAnimator(new DefaultItemAnimator());
                listView.setHasFixedSize(true);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                list = PetService.getInstance().getAdoptionPets(0);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                listAdapter = new AdopcionEndlessAdapter(list, listView, activity);
                listAdapter.setOnItemClickListener(new AdopcionEndlessAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        // TODO: aca se maneja el click sobre un item de la lista:

                        Intent i = new Intent(activity, MascotaDetalleActivity.class);
                        ArrayList<String> urlPhotos = new ArrayList<String>();
                        AdoptionPet adoptionPet = list.get(position);
                        if (adoptionPet.getPicture() != null) {
                            urlPhotos.add(adoptionPet.getPicture().getUrl());
                        }
                        ParseProxyObject ppo = new ParseProxyObject(adoptionPet);
                        i.putExtra("Pet", ppo);
                        i.putStringArrayListExtra("UrlPhotos", urlPhotos);
                        startActivity(i);
                        getActivity().overridePendingTransition(R.anim.slide_in_1, R.anim.slide_out_1);
                    }
                });

                listView.setAdapter(listAdapter);
                linlaHeaderProgress.setVisibility(View.GONE);
                emptyView = (TextView) view.findViewById(R.id.empty_view);

                checkEmptyList();
            }

        }

        InitialPetLoader loader = new InitialPetLoader(mainView);
        loader.execute();

        return mainView;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        ParseUser.logOut();
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
