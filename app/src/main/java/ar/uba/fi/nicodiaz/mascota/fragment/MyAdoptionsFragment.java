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
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.MascotaDetalleActivity;
import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.PetService;
import ar.uba.fi.nicodiaz.mascota.utils.AdopcionEndlessAdapter;
import ar.uba.fi.nicodiaz.mascota.utils.ParseProxyObject;

/**
 * Created by nicolas on 14/09/15.
 */
public class MyAdoptionsFragment extends Fragment {

    private static final String TAG = "MyAdoptionsFragment";

    private Context activity;
    private View mainView;
    private TextView emptyView;
    private ArrayList<AdoptionPet> list;
    private RecyclerView listView;
    private AdopcionEndlessAdapter listAdapter;

    private class PetListLoader extends AsyncTask<Void, Void, Boolean> {

        private LinearLayout linlaHeaderProgress;
        private List<AdoptionPet> resultList;

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
            resultList = PetService.getInstance().getAdoptionPetsByUser();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();

        // View:
        mainView = inflater.inflate(R.layout.fragment_my_adoptions, container, false);
        mainView.setTag(TAG);

        // ListView
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
                Intent i = new Intent(activity, MascotaDetalleActivity.class);
                ArrayList<String> urlPhotos = new ArrayList<>();
                AdoptionPet adoptionPet = list.get(position);
                for (ParseFile picture : adoptionPet.getPictures()) {
                    urlPhotos.add(picture.getUrl());
                }
                ArrayList<String> urlVideos = adoptionPet.getVideos();

                ParseProxyObject ppo = new ParseProxyObject(adoptionPet);
                i.putExtra("Pet", ppo);
                i.putStringArrayListExtra("UrlPhotos", urlPhotos);
                i.putStringArrayListExtra("UrlVideos", urlVideos);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_1, R.anim.slide_out_1);
            }
        });

        listView.setAdapter(listAdapter);

        // Cargando la lista de mascotas:
        new PetListLoader(mainView).execute();


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
}
