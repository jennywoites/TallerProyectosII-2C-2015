package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by nicolas on 08/10/15.
 */
@ParseClassName("Comentarios")
public class CommentDB extends ParseObject {

    public static final String AUTHOR = "author";
    public static final String TEXT = "text";
    public static final String PET_ID = "petId";
    public static final String PARENT_ID = "parentId";
    public static final String BANNED = "banned";

    public CommentDB() {
    }


    public String getObjetID() {
        return getObjectId();
    }

    public User getAuthor() {
        ParseUser parseUser = getParseUser(AUTHOR);
        try {
            return new User(parseUser.fetchIfNeeded());
        } catch (ParseException e) {
            return null;
        }
    }

    public void setAuthor(User author) {
        put(AUTHOR, author.getParseUser());
    }

    public String getText() {
        return getString(TEXT);
    }

    public void setText(String text) {
        put(TEXT, text);
    }

    public Date getDate() {
        return getCreatedAt();
    }

    public String getPetId() {
        return getString(PET_ID);
    }

    public void setPetId(String petId) {
        put(PET_ID, petId);
    }

    public String getParentID() {
        return getString(PARENT_ID);
    }

    public void setParentID(String parentID) {
        put(PARENT_ID, parentID);
    }

    public void setBanned(boolean banned) {
        put(BANNED, banned);
    }

    public boolean getBanned() {
        return getBoolean(BANNED);
    }

    public boolean isBanned()
    {
        return getBanned();
    }
}
