package model.DAO;

import java.sql.SQLException;
import model.hibernate.Phpbb_user;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Phpbb_userDAO {

    private Session session;

    public Phpbb_userDAO(Session session) {
        this.session = session;
    }

    public Phpbb_user loadPhpbb_user(long id) {
        String peticion = "FROM Phpbb_user WHERE user_id = " + id;
        return (Phpbb_user) session.createQuery(peticion).uniqueResult();

    }

    public Phpbb_user loadPhpbb_user(String user) {
        String peticion = "FROM Phpbb_user WHERE username = '" + user + "'";
        return (Phpbb_user) session.createQuery(peticion).uniqueResult();
    }

    public void save(Object object) {
        Transaction t = session.beginTransaction();
        session.save(object);
        t.commit();
    }
}
