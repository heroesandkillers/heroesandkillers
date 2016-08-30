package model.DAO;

import java.util.List;
import model.hibernate.Traspaso;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class TraspasoDAO {

    private Session session;

    public TraspasoDAO(Session session) {
        this.session = session;
    }

    public Traspaso loadTraspaso(long criaturaId) {
        return (Traspaso) session.createCriteria(Traspaso.class).add(Restrictions.eq("criaturaId", criaturaId)).uniqueResult();
    }
    
    public List<Traspaso> getTraspasos(int usuarioId) {
        String peticion = "FROM Traspaso WHERE comprador = " + usuarioId + " OR vendedor = " + usuarioId;
        return session.createQuery(peticion).list();
    }
}
