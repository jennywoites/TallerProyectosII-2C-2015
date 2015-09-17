package ar.uba.fi.nicodiaz.mascota.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ar.uba.fi.nicodiaz.mascota.R;

/**
 * Created by nicolas on 14/09/15.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private Context activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();

        // Toolbar:
        setHasOptionsMenu(true);

        // View:
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        view.setTag(TAG);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // TODO: si hay que guardar algo, hacerlo aca
        // savedInstanceState.putSerializable(algo);
        super.onSaveInstanceState(savedInstanceState);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
