package ar.uba.fi.nicodiaz.mascota.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
         *   7 - Este es otro hijo de 2
         * 6 - Este es un ultimo comentario
         */

        CommentDB comment = new CommentDB();
        comment.author = "Nico";
        comment.text = "Este es un comentario.";
        comment.parent = -1;
        comment.date = new Date();
        comment.id = 0;
        list.add(comment);

        comment = new CommentDB();
        comment.author = "Jenny";
        comment.text = "Este es un hijo del anterior.";
        comment.parent = 0;
        comment.date = new Date();
        comment.id = 1;
        list.add(comment);

        comment = new CommentDB();
        comment.author = "Juan";
        comment.text = "Este es otro hijo de 0.";
        comment.parent = 0;
        comment.date = new Date();
        comment.id = 3;
        list.add(comment);

        comment = new CommentDB();
        comment.author = "Jenny";
        comment.text = "Este es un hijo del 3.";
        comment.parent = 3;
        comment.date = new Date();
        comment.id = 5;
        list.add(comment);

        comment = new CommentDB();
        comment.author = "Ramiro";
        comment.text = "Este es otro comentario.";
        comment.parent = -1;
        comment.date = new Date();
        comment.id = 2;
        list.add(comment);

        comment = new CommentDB();
        comment.author = "Jenny";
        comment.text = "Este es un hijo del anterior.";
        comment.parent = 2;
        comment.date = new Date();
        comment.id = 4;
        list.add(comment);

        comment = new CommentDB();
        comment.author = "Jenny";
        comment.text = "Este es un hijo del 4.";
        comment.parent = 4;
        comment.date = new Date();
        comment.id = 8;
        list.add(comment);

        comment = new CommentDB();
        comment.author = "Jenny";
        comment.text = "Este es otro hijo del 2.";
        comment.parent = 2;
        comment.date = new Date();
        comment.id = 7;
        list.add(comment);

        comment = new CommentDB();
        comment.author = "Juan";
        comment.text = "Este es un ultimo comentario.";
        comment.parent = -1;
        comment.date = new Date();
        comment.id = 6;
        list.add(comment);

        return generateListOfComments(list);
    }

    public static List<Comment> generateListOfComments(List<CommentDB> listDB) {
        /**
         * TODO: quizas convenga usar un hash aca... no se. Si usamos esto, estamos OBLIGADOS a que desde la base de datos,
         * los comentarios vengan ordenados, es decir: padre -> hijos. Nunca deberia aparecer un hijo antes que su padre.
         * Esto podría complicar las cosas... quizas con un hash se simplifique. No tuve tiempo de pensarlo.
         */

        List<Comment> comments = new ArrayList<>();
        for (CommentDB commentDB : listDB) {
            Comment comment = new Comment(commentDB.id, commentDB.author, commentDB.text, commentDB.date);
            comments.add(comment);
            if (commentDB.parent != -1) {
                for (Comment aux : comments) {
                    if (aux.id == commentDB.parent) {
                        aux.addChild(comment);
                        break;
                    }
                }
            }
        }
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
