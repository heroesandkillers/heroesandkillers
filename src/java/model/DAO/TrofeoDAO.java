package model.DAO;

import java.util.List;
import model.hibernate.Trofeo;
import org.hibernate.Query;
import org.hibernate.Session;

public class TrofeoDAO {

    private Session session;

    public TrofeoDAO(Session session) {
        this.session = session;
    }

    public List<Trofeo> getTrofeosUsuario(int id) {
        String peticion = "FROM Trofeo WHERE usuario = :id";
        Query query = session.createQuery(peticion);
        query.setParameter("id", id);
        List<Trofeo> trofeos = query.list();
        return trofeos;
    }
}
