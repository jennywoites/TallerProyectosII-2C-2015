package ar.uba.fi.nicodiaz.mascota.MisMascotas;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.utils.RequestEndlessAdapter;

/**
 * Created by nicolas on 14/09/15.
 */
public class MascotaAdopcionPublicadaDetalleSolicitudesFragment extends Fragment {

    private static final String TAG = "MisAdopcionesPublicadasFragment";

    private Context activity;
    private View mainView;
    private TextView emptyView;
    private ArrayList<String> list; // TODO: crear estructura de base de datos para solicitudes
    private RecyclerView listView;
    private RequestEndlessAdapter listAdapter;

    private class RequestListLoader extends AsyncTask<Void, Void, Boolean> {

        private LinearLayout linlaHeaderProgress;
        private List<String> resultList; // TODO: crear estructura de base de datos para solicitudes

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
            resultList = new ArrayList<>();
            resultList.add("Foo");// TODO: hacer metodo para obtener las mascotas de adopcion que YO solicité adoptar.
            return !(resultList.isEmpty());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            linlaHeaderProgress.setVisibility(View.GONE);
            if (result) {
                for (String solicitud : resultList) { // TODO: crear estructura de base de datos para solicitudes
                    list.add(solicitud);
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
        mainView = inflater.inflate(R.layout.fragment_mascota_adopcion_publicada_detalle_solicitudes, container, false);
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
        Log.d(String.valueOf(Log.DEBUG), list.toString());
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
}

