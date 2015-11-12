package ar.uba.fi.nicodiaz.mascota.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.mascotasgenerales.DenounceCommentActivity;
import ar.uba.fi.nicodiaz.mascota.mascotasgenerales.NewCommentActivity;
import ar.uba.fi.nicodiaz.mascota.model.Comment;
import ar.uba.fi.nicodiaz.mascota.model.CommentService;
import ar.uba.fi.nicodiaz.mascota.model.UserService;

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

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public Button denounceButton;
        private View colorBand;
        public TextView authorTextView;
        public TextView commentTextView;
        public TextView hiddenCommentsCountTextView;
        public TextView dateTextView;
        public Button replyButton;
        private View view;

        private final String[] indColors = {"#00B3A2", "#005E53"};

        public CommentViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            authorTextView = (TextView) itemView.findViewById(R.id.author_textview);
            commentTextView = (TextView) itemView.findViewById(R.id.comment_textview);
            dateTextView = (TextView) itemView.findViewById(R.id.date_textview);
            colorBand = itemView.findViewById(R.id.color_band);
            hiddenCommentsCountTextView = (TextView) itemView.findViewById(R.id.hidden_comments_count_textview);
            replyButton = (Button) itemView.findViewById(R.id.reply_button);
            replyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Comment comment = (Comment) getItemAt(getAdapterPosition());
                    Intent i = new Intent(mContext, NewCommentActivity.class);
                    i.putExtra("Parent", comment.id);
                    mContext.startActivity(i);
                }
            });
            denounceButton = (Button) itemView.findViewById(R.id.denounce_button);
            denounceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Comment comment = (Comment) getItemAt(getAdapterPosition());
                    Intent i = new Intent(mContext, DenounceCommentActivity.class);
                    CommentService.getInstance().setSelectedComment(comment.getCommentDB());
                    mContext.startActivity(i);
                }
            });
        }

        public void setColorBandColor(int indentation) {
            int colorId = (indentation % indColors.length);
            int color = Color.parseColor(indColors[colorId]); // TODO: esto puede pinchar, maximo 6 comentarios anidados?
            colorBand.setBackgroundColor(color);
        }

        public void setPaddingLeft(int paddingLeft) {
            view.setPadding(paddingLeft, 0, 0, 0);
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
        if (mListener != null) {
            v.setOnClickListener(mListener);
        }
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

                // El usuario actual no se puede denunciar a sí mismo:
                if (comment.author.equals(UserService.getInstance().getUser().getName()) || comment.text.equals("[Mensaje eliminado por un moderador]")) {
                    commentViewHolder.denounceButton.setVisibility(View.GONE);
                }

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
