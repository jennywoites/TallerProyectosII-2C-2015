package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by nicolas on 08/10/15.
 */
public class CommentService {

    private static CommentService ourInstance = new CommentService();
    private static List<CommentDB> lastListComments = new ArrayList<>();


    public static CommentService getInstance() {
        return ourInstance;
    }

    private CommentService() {

    }

    public int getCount(String petID) {
        ParseQuery<CommentDB> query = ParseQuery.getQuery(CommentDB.class);
        query.whereEqualTo(CommentDB.PET_ID, petID);
        try {
            return query.count();
        } catch (ParseException e) {
            return 0;
        }
    }

    public List<Comment> getComments(String petID) {
        List<CommentDB> list = new ArrayList<>();
        ParseQuery<CommentDB> query = ParseQuery.getQuery(CommentDB.class);
        query.whereEqualTo(CommentDB.PET_ID, petID);
        try {
            list = query.find();
        } catch (ParseException e) {
            return null;
        }

        return generateListOfComments(list);
    }

    public static List<Comment> generateListOfComments(List<CommentDB> listDB) {
        lastListComments = listDB;
        List<Comment> comments = new ArrayList<>();
        HashMap<String, List<Comment>> hash = new HashMap<>();

        for (CommentDB commentDB : listDB) {
            String text = "";
            if (commentDB.getAuthor().isBanned() || commentDB.isBanned()) {
                text = "[Mensaje eliminado por un moderador]";
            } else {
                text = commentDB.getText();
            }
            if (commentDB.getParentID().equals("-1")) { // Is top parent
                Comment comment = new Comment(commentDB.getObjectId(), commentDB.getAuthor().getName(), text, commentDB.getDate());
                comments.add(comment); // Add it to the list
            } else { // Is a child of some comment
                if (hash.get(commentDB.getParentID()) == null) {
                    hash.put(commentDB.getParentID(), new ArrayList<Comment>());
                }
                hash.get(commentDB.getParentID()).add(new Comment(commentDB.getObjectId(), commentDB.getAuthor().getName(), text, commentDB.getDate())); // temporary to a hash
            }
        }

        ListIterator<Comment> iter = comments.listIterator();
        while (iter.hasNext()) {
            Comment actual = iter.next();
            String idActual = actual.id;
            if (hash.get(idActual) != null) { // Tiene hijos
                for (Comment child : hash.get(idActual)) { // Agrego esos hijos al actual
                    actual.addChild(child);
                    iter.add(child);
                }
                for (int i = 0; i < hash.get(idActual).size(); i++) {
                    iter.previous(); // returns the added element
                }
            }
        }
        return comments;
    }

    public void save(CommentDB comment, Pet pet) {
        comment.saveInBackground();
        PushService.getInstance().sendCommentNotification(comment, pet, lastListComments);
    }
}
