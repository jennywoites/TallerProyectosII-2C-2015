package ar.uba.fi.nicodiaz.mascota.MascotasGenerales;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.Comment;
import ar.uba.fi.nicodiaz.mascota.model.CommentService;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.PetService;
import ar.uba.fi.nicodiaz.mascota.utils.CommentsAdapter;

public class MascotaDetalleComentariosFragment extends Fragment {

    private static final String GROUPS_KEY = "groups_key";

    private CommentsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView emptyView;
    private List<Comment> comments;

    @Override
    public void onResume() {
        super.onResume();
        reloadComments();
    }

    private void reloadComments() {
        Log.d(String.valueOf(Log.DEBUG), "reloading");
        if (comments != null) {
            comments.clear();
            mAdapter.clear();
            Pet selectedPet = PetService.getInstance().getSelectedPet();
            comments = CommentService.getInstance().getComments(selectedPet.getID());
            mAdapter.addAll(comments);
            checkEmptyList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mascota_detalle_comentarios, container, false);
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.comments_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CommentsAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mAdapter);

        Pet selectedPet = PetService.getInstance().getSelectedPet();
        comments = CommentService.getInstance().getComments(selectedPet.getID());
        mAdapter.addAll(comments);
        checkEmptyList();

        if (savedInstanceState != null) {
            List<Integer> groups = savedInstanceState.getIntegerArrayList(GROUPS_KEY);
            mAdapter.restoreGroups(groups);
        }

        return rootView;
    }

    private void checkEmptyList() {
        if (comments.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putIntegerArrayList(GROUPS_KEY, mAdapter.saveGroups());
        super.onSaveInstanceState(outState);
    }
}
