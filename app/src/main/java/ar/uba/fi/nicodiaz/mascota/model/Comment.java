package ar.uba.fi.nicodiaz.mascota.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.utils.MultiLevelExpIndListAdapter;

/**
 * Created by nicolas on 07/10/15.
 */
public class Comment implements MultiLevelExpIndListAdapter.ExpIndData {

    public String author;
    public String text;
    public Date date;
    public int id;

    private int mIndentation;
    private List<Comment> mChildren;
    private boolean mIsGroup;
    private int mGroupSize;
    public int parent;

    public Comment(int id, String author, String text) {
        this.id = id;
        this.author = author;
        this.text = text;
        date = Calendar.getInstance().getTime();
        mChildren = new ArrayList<>();
        parent = -1;
        setIndentation(0);
    }

    public Comment(int id, String author, String text, Date date) {
        this.id = id;
        this.author = author;
        this.text = text;
        this.date = date;
        mChildren = new ArrayList<>();
        setIndentation(0);
        parent = -1;
    }

    @Override
    public List<? extends MultiLevelExpIndListAdapter.ExpIndData> getChildren() {
        return mChildren;
    }

    @Override
    public boolean isGroup() {
        return mIsGroup;
    }

    @Override
    public void setIsGroup(boolean value) {
        mIsGroup = value;
    }

    @Override
    public void setGroupSize(int groupSize) {
        mGroupSize = groupSize;
    }

    public int getGroupSize() {
        return mGroupSize;
    }

    public void addChild(Comment child) {
        mChildren.add(child);
        child.parent = this.id;
        child.setIndentation(getIndentation() + 1);
    }

    public boolean isParent() {
        return parent == -1;
    }

    public boolean isChild() {
        return parent != -1;
    }

    public int getParent() {
        return parent;
    }

    public int getIndentation() {
        return mIndentation;
    }

    private void setIndentation(int indentation) {
        mIndentation = indentation;
    }
}
