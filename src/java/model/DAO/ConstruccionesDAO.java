package model.DAO;

import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ConstruccionesDAO {

    private Session session;

    public ConstruccionesDAO(Session session) {
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

    public long getPrecioFortaleza(int nivel) {
        return 1000 * (nivel + 1);
    }

    public long getPrecioAcedemia(int nivel) {
        return 1000 * (nivel + 1);
    }

    public long getPrecioTaberna(int nivel) {
        return 1000 * (nivel + 1);
    }

    public long getPrecioTorre(int nivel) {
        return 1000 * (nivel + 1);
    }

    public long getPrecioMuralla(int nivel) {
        return 1000 * (nivel + 1);
    }

    public long getPrecioEntorno(int nivel) {
        return 1000 * (nivel + 1);
    }

    public String setConstrucciones() {
        Cons cons = new Cons();
        Gson gson = new Gson();
        return gson.toJson(cons);
    }

    public class Cons {

        public int fort;
        public long fortPrecio = getPrecioFortaleza(0);
        public int acad;
        public long acadPrecio = getPrecioAcedemia(0);
        public int tabe;
        public long tabePrecio = getPrecioTaberna(0);
        public int torr;
        public long torrPrecio = getPrecioTorre(0);
        public int mura;
        public long muraPrecio = getPrecioMuralla(0);
        public int ento;
        public long entoPrecio = getPrecioEntorno(0);
    }
}
