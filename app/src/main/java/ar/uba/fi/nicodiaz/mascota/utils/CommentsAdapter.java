package ar.uba.fi.nicodiaz.mascota.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.Comment;

public class CommentsAdapter extends MultiLevelExpIndListAdapter {
    private static final int VIEW_TYPE_ITEM = 0;
    /**
     * This is called when the user click on an item or group.
     */
    private final View.OnClickListener mListener;
    private final Context mContext;
    /**
     * Unit of identation.
     */
    private final int mPaddingDP = 5;

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private View colorBand;
        public TextView authorTextView;
        public TextView commentTextView;
        public TextView hiddenCommentsCountTextView;
        public TextView dateTextView;
        private View view;

        private static final String[] indColors = {"#000000", "#3366FF", "#E65CE6", "#E68A5C", "#00E68A", "#CCCC33"};

        public CommentViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            authorTextView = (TextView) itemView.findViewById(R.id.author_textview);
            commentTextView = (TextView) itemView.findViewById(R.id.comment_textview);
            dateTextView = (TextView) itemView.findViewById(R.id.date_textview);
            colorBand = itemView.findViewById(R.id.color_band);
            hiddenCommentsCountTextView = (TextView) itemView.findViewById(R.id.hidden_comments_count_textview);
        }

        public void setColorBandColor(int indentation) {
            int color = Color.parseColor(indColors[indentation]); // TODO: esto puede pinchar, maximo 6 comentarios anidados?
            colorBand.setBackgroundColor(color);
        }

        public void setPaddingLeft(int paddingLeft) {
            view.setPadding(paddingLeft,0,0,0);
        }
    }

    public CommentsAdapter(Context context, View.OnClickListener listener) {
        super();
        mContext = context;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                int resource = R.layout.recyclerview_item_comments;
                v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
                viewHolder = new CommentViewHolder(v);
                break;
            default:
                throw new IllegalStateException("unknown viewType");
        }
        v.setOnClickListener(mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                CommentViewHolder commentViewHolder = (CommentViewHolder) viewHolder;
                Comment comment = (Comment) getItemAt(position);
                commentViewHolder.authorTextView.setText(comment.author);
                commentViewHolder.commentTextView.setText(comment.text);
                commentViewHolder.dateTextView.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(comment.date));

                if (comment.getIndentation() == 0) {
                    commentViewHolder.colorBand.setVisibility(View.GONE);
                    commentViewHolder.setPaddingLeft(0);
                } else {
                    commentViewHolder.colorBand.setVisibility(View.VISIBLE);
                    commentViewHolder.setColorBandColor(comment.getIndentation());
                    int leftPadding = MultiLevelExpIndListAdapterUtils.getPaddingPixels(mContext, mPaddingDP) * (comment.getIndentation() - 1);
                    commentViewHolder.setPaddingLeft(leftPadding);
                }

                if (comment.isGroup()) {
                    commentViewHolder.hiddenCommentsCountTextView.setVisibility(View.VISIBLE);
                    commentViewHolder.hiddenCommentsCountTextView.setText(Integer.toString(comment.getGroupSize()));
                } else {
                    commentViewHolder.hiddenCommentsCountTextView.setVisibility(View.GONE);
                }
                break;
            default:
                throw new IllegalStateException("unknown viewType");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }
}
