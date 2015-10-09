package ar.uba.fi.nicodiaz.mascota.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import ar.uba.fi.nicodiaz.mascota.utils.CommentDB;

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

    public List<Comment> getComments(int petId) {
        // TODO: bajar de base de datos:
        List<CommentDB> list = new ArrayList<>();

        /**
         * El id no importa en que orden este, supuestamente se debe ir sumando de a 1 cada vez que se agregue un comentario nuevo a esta mascota...
         *
         * 0 - Este es un comentario.
         *   1 - Este es un hijo del anterior
         *   3 - Este es otro hijo de 0
         *     5 - Este es un hijo de 3
         * 2 - Este es otro comentario
         *   4 - Este es un hijo del anterior
         *     8- Este es un hijo de 4
         *       9 - Este es un ultimo comentario
         *   7 - Este es otro hijo de 2
         * 6 - Este es un ultimo comentario
         */

        CommentDB comment = new CommentDB();
        comment.author = "Nico";
        comment.text = "0 - Este es un comentario.";
        comment.parent = -1;
        comment.date = new Date();
        comment.id = 0;
        list.add(comment);

        comment = new CommentDB();
        comment.author = "Jenny";
        comment.text = "1 - Este es un hijo del anterior.";
        comment.parent = 0;
        comment.date = new Date();
        comment.id = 1;
        list.add(comment);

        comment = new CommentDB();
        comment.author = "Jenny";
        comment.text = "5 - Este es un hijo del 3.";
        comment.parent = 3;
        comment.date = new Date();
        comment.id = 5;
        list.add(comment);



        comment = new CommentDB();
        comment.author = "Juan";
        comment.text = "6 - Este es un ultimo comentario.";
        comment.parent = -1;
        comment.date = new Date();
        comment.id = 6;
        list.add(comment);

        comment = new CommentDB();
        comment.author = "Jenny";
        comment.text = "4 - Este es un hijo del anterior.";
        comment.parent = 2;
        comment.date = new Date();
        comment.id = 4;
        list.add(comment);

        comment = new CommentDB();
        comment.author = "Ramiro";
        comment.text = "2 - Este es otro comentario.";
        comment.parent = -1;
        comment.date = new Date();
        comment.id = 2;
        list.add(comment);

        comment = new CommentDB();
        comment.author = "Jenny";
        comment.text = "8 -Este es un hijo del 4.";
        comment.parent = 4;
        comment.date = new Date();
        comment.id = 8;
        list.add(comment);

        comment = new CommentDB();
        comment.author = "Jenny";
        comment.text = "7 - Este es otro hijo del 2.";
        comment.parent = 2;
        comment.date = new Date();
        comment.id = 7;
        list.add(comment);



        comment = new CommentDB();
        comment.author = "Juan";
        comment.text = "9 - Este es un ultimo comentario.";
        comment.parent = 8;
        comment.date = new Date();
        comment.id = 9;
        list.add(comment);

        comment = new CommentDB();
        comment.author = "Juan";
        comment.text = "3 - Este es otro hijo de 0.";
        comment.parent = 0;
        comment.date = new Date();
        comment.id = 3;
        list.add(comment);

        return generateListOfComments(list);
    }

    public static List<Comment> generateListOfComments(List<CommentDB> listDB) {
        List<Comment> comments = new ArrayList<>();
        HashMap<Integer, List<Comment>> hash = new HashMap<>();

        for (CommentDB commentDB : listDB) {
            if (commentDB.parent == -1) { // Is top parent
                Comment comment = new Comment(commentDB.id, commentDB.author, commentDB.text, commentDB.date);
                comments.add(comment); // Add it to the list
            } else { // Is a child of some comment
                if (hash.get(commentDB.parent) == null) {
                    hash.put(commentDB.parent, new ArrayList<Comment>());
                }
                hash.get(commentDB.parent).add(new Comment(commentDB.id, commentDB.author, commentDB.text, commentDB.date)); // temporary to a hash
            }
        }

        ListIterator<Comment> iter = comments.listIterator();
        while (iter.hasNext()) {
            Comment actual = iter.next();
            int idActual = actual.id;
            if (hash.get(idActual) != null) { // Tiene hijos
                for (Comment child : hash.get(idActual)) { // Agrego esos hijos al actual
                    actual.addChild(child);
                    iter.add(child);
                }
                for (int i=0; i < hash.get(idActual).size(); i++) {
                    iter.previous(); // returns the added element
                }
            }
        }

        // sort by date
        Collections.sort(comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment lhs, Comment rhs) {
                if (lhs.date == null || rhs.date == null) {
                    return 0;
                }
                return lhs.date.compareTo(rhs.date);
            }
        });

        return comments;
    }

    // TODO: quizas nunca usemos esta funcion
    public static List<CommentDB> generateListOfCommentsDB(List<Comment> comments) {
        List<CommentDB> listDB = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDB commentDB = new CommentDB();
            commentDB.author = comment.author;
            commentDB.text = comment.text;
            commentDB.date = comment.date;
            commentDB.id = comment.id;
            commentDB.parent = comment.parent;
            listDB.add(commentDB);
        }
        return listDB;
    }
}
