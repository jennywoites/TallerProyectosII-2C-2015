package ar.uba.fi.nicodiaz.mascota.model;

import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import ar.uba.fi.nicodiaz.mascota.MyApplication;

/**
 * Created by nicolas on 08/10/15.
 */
public class CommentService {

    private static CommentService ourInstance = new CommentService();

    public static CommentService getInstance() {
        return ourInstance;
    }

    private CommentService() {

    }

    public List<Comment> getComments(String petID) {
        List<CommentDB> list = new ArrayList<>();
        ParseQuery<CommentDB> query = ParseQuery.getQuery(CommentDB.class);
        query.whereEqualTo(CommentDB.PET_ID, petID);
        try {
            list = query.find();
        } catch (ParseException e) {
            Toast.makeText(MyApplication.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return generateListOfComments(list);
    }

    public static List<Comment> generateListOfComments(List<CommentDB> listDB) {
        List<Comment> comments = new ArrayList<>();
        HashMap<String, List<Comment>> hash = new HashMap<>();


        for (CommentDB commentDB : listDB) {
            if (commentDB.getParentID().equals("-1")) { // Is top parent
                Comment comment = new Comment(commentDB.getObjectId(), commentDB.getAuthor().getName(), commentDB.getText(), commentDB.getDate());
                comments.add(comment); // Add it to the list
            } else { // Is a child of some comment
                if (hash.get(commentDB.getParentID()) == null) {
                    hash.put(commentDB.getParentID(), new ArrayList<Comment>());
                }
                hash.get(commentDB.getParentID()).add(new Comment(commentDB.getObjectId(), commentDB.getAuthor().getName(), commentDB.getText(), commentDB.getDate())); // temporary to a hash
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

    // TODO: quizas nunca usemos esta funcion
    public static List<CommentDB> generateListOfCommentsDB(List<Comment> comments) {
        List<CommentDB> listDB = new ArrayList<>();
/*        for (Comment comment : comments) {
            CommentDB commentDB = new CommentDB();
            commentDB.author = comment.author;
            commentDB.text = comment.text;
            commentDB.date = comment.date;
            commentDB.id = comment.id;
            commentDB.parent = comment.parent;
            listDB.add(commentDB);
        }*/
        return listDB;
    }

    public void save(CommentDB comment) {
        comment.saveInBackground();
    }
}
