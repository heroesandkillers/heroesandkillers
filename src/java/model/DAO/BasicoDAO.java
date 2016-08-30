package model.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class BasicoDAO {

    private Session session;

    public BasicoDAO(Session session) {
        this.session = session;
    }

    public void update(Object object) {
        Transaction t = session.beginTransaction();
        session.update(object);
        t.commit();
    }

    public void save(Object object) {
        Transaction t = session.beginTransaction();
        session.save(object);
        t.commit();
    }
}
