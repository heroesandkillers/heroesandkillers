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
    public Phpbb_user loadPhpbb_user(long id) throws SQLException {
        try {
            String peticion = "FROM Phpbb_user WHERE user_id = " + id;            
            return (Phpbb_user) session.createQuery(peticion).uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Phpbb_user loadPhpbb_user(String user) throws SQLException {
        try {
            String peticion = "FROM Phpbb_user WHERE username = '" + user + "'";
            return (Phpbb_user) session.createQuery(peticion).uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void save(Object object) {
        Transaction t = session.beginTransaction();
        session.save(object);
        t.commit();
    }
}