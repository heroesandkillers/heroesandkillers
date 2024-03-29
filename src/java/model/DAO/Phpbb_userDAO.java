package model.DAO;

import java.sql.SQLException;
import model.hibernate.Phpbb_user;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Phpbb_userDAO {

    private Session session;

    public Phpbb_userDAO(Session session) {
        this.session = session;
    }

    public Phpbb_user loadPhpbb_user(long id) throws SQLException {
        String peticion = "FROM Phpbb_user WHERE user_id = :id";
        Query query = session.createQuery(peticion);
        query.setParameter("id", id);
        return (Phpbb_user) query.uniqueResult();
    }

    public Phpbb_user loadPhpbb_user(String user) throws SQLException {
        String peticion = "FROM Phpbb_user WHERE username_clean = :user"; //username_clean is the login phpbb (key)
        Query query = session.createQuery(peticion);
        query.setParameter("user", user);
        return (Phpbb_user) query.uniqueResult();
    }

    public void save(Object object) {
        Transaction t = session.beginTransaction();
        session.save(object);
        t.commit();
    }
}
