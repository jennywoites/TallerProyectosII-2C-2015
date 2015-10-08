package ar.uba.fi.nicodiaz.mascota.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.Comment;
import ar.uba.fi.nicodiaz.mascota.utils.CommentsAdapter;

public class MascotaDetalleComentariosFragment extends Fragment {

    private static final String GROUPS_KEY = "groups_key";

    private CommentsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView emptyView;
    private List<Comment> comments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mascota_detalle_comentarios, container, false);
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.comments_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CommentsAdapter(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mRecyclerView.getChildPosition(v);
                mAdapter.toggleGroup(position);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        comments = getDummyComments();
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

    private List<Comment> getDummyComments() {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("poopsliquid", "You'd think that after he was pulled on the ground by the machine he might take a break and reevaluate his choices."));
        comments.add(new Comment("Nizzler", "that's what a skinny little weakling would do. You a skinny little weakling, /u/poopsliquid?"));
        comments.get(0).addChild(comments.get(1));
        comments.add(new Comment("banedes", "Hell yeah get him man. We don't need no more thinks at the gym."));
        comments.get(1).addChild(comments.get(2));
        comments.add(new Comment("Nizzler", "What a scrawny little twerp."));
        comments.get(2).addChild(comments.get(3));
        comments.add(new Comment("Daibhead", "When the machine starts working out on you its time to try something else."));
        comments.get(0).addChild(comments.get(4));
        comments.add(new Comment("bigtfatty", "Got to say I'm pretty impressed he held on though and didn't drop (and possibly break) the weights."));
        comments.get(0).addChild(comments.get(5));

        comments.add(new Comment("wwickeddogg", "Good practice for when you are dragging your dead girlfriends body out of the house with one hand while also trying to keep the dog out of the way with the other hand."));
        comments.add(new Comment("banedes", "Amen been there brother I have been there."));
        comments.get(6).addChild(comments.get(7));
        comments.add(new Comment("GratefulGreg89", "I know right haven't we all?"));
        comments.get(7).addChild(comments.get(8));
        comments.add(new Comment("SoefianB", "I was there yesterday"));
        comments.get(8).addChild(comments.get(9));
        comments.add(new Comment("Aznleroy", "For me it was ABOUT A WEEK AGO. WEEK AGO."));
        comments.get(9).addChild(comments.get(10));
        comments.add(new Comment("neurorgasm", "If I remember correctly, everybody was catching bullet holes that day."));
        comments.get(10).addChild(comments.get(11));
        comments.add(new Comment("HippolyteClio", "I went that long once, never again."));
        comments.get(10).addChild(comments.get(12));
        comments.add(new Comment("heyYOUguys1", "did you just about a week ago yourself?"));
        comments.get(10).addChild(comments.get(13));
        comments.add(new Comment("MrMontage", "Exactly, it's a very functional exercise."));
        comments.get(6).addChild(comments.get(14));

        comments.add(new Comment("CranberryNapalm", "Ah, the Lat Lean-Back Single Plate Hop-Pull. Do you even lift, bros?"));
        comments.add(new Comment("toke81", "Do you even hop and pull?"));
        comments.get(15).addChild(comments.get(16));
        comments.add(new Comment("itza_me", "Nothing beats this rowing machine mastery for me."));
        comments.add(new Comment("All_The_Ragrets", "One... One... One... One... One..."));
        comments.get(17).addChild(comments.get(18));
        comments.add(new Comment("leeboof", "Not even one..."));
        comments.get(18).addChild(comments.get(19));
        return comments;
    }
}
