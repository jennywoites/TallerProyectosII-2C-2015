package ar.uba.fi.nicodiaz.mascota;

import android.test.AndroidTestCase;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.model.Comment;
import ar.uba.fi.nicodiaz.mascota.model.CommentDB;
import ar.uba.fi.nicodiaz.mascota.model.CommentService;
import ar.uba.fi.nicodiaz.mascota.model.User;

public class TestCommentService extends AndroidTestCase {

    public void testGenerateListOfCommentsEmpty() {
        List<CommentDB> emptyList = new ArrayList<>();
        List<Comment> result = CommentService.generateListOfComments(emptyList);

        assertEquals("La lista no esta vacia.", 0, result.size());
    }

    public void testGenerateListOfCommentsAllParents() {
        ParseObject.registerSubclass(MockCommentDB.class);

        List<CommentDB> parentsList = getDummyOnlyParents();
        List<Comment> result = CommentService.generateListOfComments(parentsList);

        assertEquals("No tiene el mismo tamaño que la lista original.", parentsList.size(), result.size());

        for (Comment comment : result) {
            assertEquals("El comentario tiene padre.", "-1",comment.getParent());
            assertTrue("El comentario no es padre.", comment.isParent());
            assertFalse("El comentario es hijo de alguien.", comment.isChild());
            assertEquals("El comentario tiene hijos.", 0, comment.getChildren().size());
        }
    }

    public void testGenerateListOfCommentsOneParentWithChilds() {
        ParseObject.registerSubclass(MockCommentDB.class);

        List<CommentDB> source = getDummyParentWithChilds();
        List<Comment> result = CommentService.generateListOfComments(source);
        assertEquals("No tiene el mismo tamaño que la lista original.", source.size(), result.size());

        for (Comment comment : result) {

            if (comment.id.equals("padre")) {
                assertEquals("El comentario tiene padre.", "-1", comment.getParent());
                assertTrue("El comentario no es padre.", comment.isParent());
                assertFalse("El comentario es hijo de alguien.", comment.isChild());
                assertEquals("El comentario no tiene hijos.", 1, comment.getChildren().size());
                assertEquals("El comentario no tiene hijos correctos.", "hijo", ((Comment) comment.getChildren().get(0)).id);
            }

            if (comment.id.equals("hijo")) {
                assertEquals("El comentario no tiene padre.", "padre", comment.getParent());
                assertFalse("El comentario es padre.", comment.isParent());
                assertTrue("El comentario es hijo de alguien.", comment.isChild());
                assertEquals("El comentario tiene hijos.", 0, comment.getChildren().size());
            }
        }
    }

    private List<CommentDB> getDummyParentWithChilds() {
        List<CommentDB> list = new ArrayList<>();

        MockCommentDB parent = new MockCommentDB();
        parent.objectID = "padre";
        list.add(parent);

        MockCommentDB child = new MockCommentDB();
        child.objectID = "hijo";
        child.parentID = "padre";
        list.add(child);

        return list;
    }

    private List<CommentDB> getDummyOnlyParents() {
        List<CommentDB> onlyParents = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            MockCommentDB parent = new MockCommentDB();
            onlyParents.add(parent);
        }

        return onlyParents;
    }

    public static class MockUser extends User {

        private String name;

        public MockUser(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static class MockCommentDB extends CommentDB {
        public String objectID;
        public User author;
        public String text;
        public String parentID;
        public Date date;
        public String petID;

        public MockCommentDB() {
            this.author = new MockUser("foo");
            this.objectID = "FOO";
            petID = "bar";
            text = "foobar";
            parentID = "-1";
            date = null;
        }

        public String getObjectId() {
            return this.objectID;
        }

        public User getAuthor() {
            return this.author;
        }

        public void setAuthor(User author) {
            this.author = author;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Date getDate() {
            return this.date;
        }

        public String getPetId() {
            return this.petID;
        }

        public void setPetId(String petId) {
            this.petID = petId;
        }

        public String getParentID() {
            return this.parentID;
        }

        public void setParentID(String parentID) {
            this.parentID = parentID;
        }
    }
}
