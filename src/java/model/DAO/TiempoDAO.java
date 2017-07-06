package model.DAO;

import java.util.Date;
import java.util.List;
import model.hibernate.Tiempo;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TiempoDAO {

    private Session session;

    public TiempoDAO(Session session) {
        this.session = session;
    }

    public Tiempo loadTimer(long id) {
        Transaction t = session.beginTransaction();

        Tiempo tiempo = (Tiempo) session.load(Tiempo.class, id);

        t.commit();
        return tiempo;
    }

    public Tiempo loadTiempo(String nombreId) {

        String peticion = "FROM Tiempo WHERE nombreId = :nombreId";
        Query query = session.createQuery(peticion);
        query.setParameter("nombreId", nombreId);
        Tiempo tiempo = (Tiempo) query.uniqueResult();

        return tiempo;
    }

    public void createTiempo(String nombreId) {

        String peticion = "FROM Tiempo WHERE nombreId = :nombreId";
        Query query = session.createQuery(peticion);
        query.setParameter("nombreId", nombreId);
        Tiempo tiempo = (Tiempo) query.uniqueResult();

        if (tiempo == null) {
            tiempo = new Tiempo();
            tiempo.setNombreId(nombreId);

            Transaction t = session.beginTransaction();
            session.save(tiempo);
            t.commit();
        } else {
            System.out.println("El tiempo ya existe");
        }
    }

    public void update(Object obj) {
        Transaction t = session.beginTransaction();
        session.update(obj);
        t.commit();
    }

    private boolean comprobar(int editorId, String nombreId) {
        boolean comprobar = false;
        Tiempo tiempo = loadTiempo(nombreId);
        if (tiempo.getEstado().equals("reservado") && tiempo.getEditorId() == editorId) {
            comprobar = true;
        }
        return comprobar;
    }

    public void finalizar(String nombreId) {
        Date date = new Date();
        long time = date.getTime();
        int dias = (int) (time / 86400000);

        Tiempo tiempo = loadTiempo(nombreId);

        tiempo.setEstado("inactivo");
        tiempo.setDia(dias);

        update(tiempo);
    }

    public List<Tiempo> getTiempos() {

        String peticion = "FROM Tiempo";
        List<Tiempo> timers = session.createQuery(peticion).list();

        return timers;
    }
}
