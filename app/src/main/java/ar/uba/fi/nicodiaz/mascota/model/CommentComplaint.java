package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Juan Manuel Romera on 11/11/2015.
 */
@ParseClassName("DenunciaComentario")
public class CommentComplaint extends ParseObject {

    public static final String INFORMED = "denunciado";
    public static final String INFORMER = "denunciante";
    public static final String INFO = "masInfo";
    public static final String COMMENT = "comentario";
    public static final String KIND = "tipo";

    public CommentComplaint() {
    }

    public void setInformed(User user) {
        put(INFORMED, user.getParseUser());
    }

    public User getInformed() {
        ParseUser parseUser = getParseUser(INFORMED);
        try {
            return new User(parseUser.fetchIfNeeded());
        } catch (ParseException e) {
            return null;
        }
    }

    public void setInformer(User user) {
        put(INFORMER, user.getParseUser());
    }

    public User getInformer() {
        ParseUser parseUser = getParseUser(INFORMER);
        try {
            return new User(parseUser.fetchIfNeeded());
        } catch (ParseException e) {
            return null;
        }
    }

    public void setInfo(String info) {
        put(INFO, info);
    }

    public String getInfo() {
        return getString(INFO);
    }

    public void setComment(CommentDB commentDB) {
        put(COMMENT, commentDB);
    }

    public CommentDB getCommentDB() {
        CommentDB commentDB = (CommentDB) getParseObject(COMMENT);
        try {
            return commentDB.fetchIfNeeded();
        } catch (ParseException e) {
            return null;
        }
    }

    public void setKind(String kind) {
        put(KIND, kind);
    }

    public String getKind() {
        return getString(KIND);
    }

}

