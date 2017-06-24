package model.DAO;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionContext;
import java.util.*;
import model.hibernate.Batalla;
import model.hibernate.Division;
import model.hibernate.HibernateUtil;
import model.hibernate.MensajeJuego;
import model.hibernate.Usuario;
import model.jsonClass.Alineacion;
import model.jsonClass.EstadisticasBatalla;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class BatallaDAO {

    private Session session;
    private int criatLiga = 10;

    public BatallaDAO(Session session) {
        this.session = session;
    }

    public Batalla loadBatalla(long id) {
        Transaction t = session.beginTransaction();
        Batalla batalla = (Batalla) session.load(Batalla.class, id);
        t.commit();
        return batalla;
    }

    public void save(Object object) {
        Transaction t = session.beginTransaction();
        session.save(object);
        t.commit();
    }

    public void update(Object object) {
        Transaction t = session.beginTransaction();
        session.update(object);
        t.commit();
    }

    public void delete(Object object) {
        Transaction t = session.beginTransaction();
        session.delete(object);
        t.commit();
    }

    public void vaciarUltimasBatallas() {

        String peticion = "FROM Batalla WHERE fecha = (SELECT max(fecha) FROM Batalla WHERE calculos != 0)";
        List<Batalla> batallas = session.createQuery(peticion).list();

        for (Batalla batalla : batallas) {
            batalla.setAlinLoc(null);
            batalla.setAlinVis(null);
            batalla.setRes(null);
            batalla.setRes2(null);
            batalla.setRes3(null);
            batalla.setResultado(null);
            batalla.setCalculos(0);
            update(batalla);
        }
    }

    public void vaciarUltimasBatallas(Division division) {
        String peticion = "FROM Batalla WHERE fecha = (SELECT max(fecha) FROM Batalla WHERE calculos != 0 AND division = " + division + ") AND division = " + division;
        List<Batalla> batallas = session.createQuery(peticion).list();

        for (Batalla batalla : batallas) {
            batalla.setAlinLoc(null);
            batalla.setAlinVis(null);
            batalla.setRes(null);
            batalla.setRes2(null);
            batalla.setRes3(null);
            batalla.setResultado(null);
            batalla.setCalculos(0);
            update(batalla);
        }
    }

    public void vaciarBatallas() {

        String peticion = "FROM Batalla";
        List<Batalla> batallas = session.createQuery(peticion).list();

        for (Batalla batalla : batallas) {
            batalla.setAlinLoc(null);
            batalla.setAlinVis(null);
            batalla.setRes(null);
            batalla.setRes2(null);
            batalla.setRes3(null);
            batalla.setCalculos(0);
            update(batalla);
        }
    }

    public boolean crearBatallasCalendario(int division) { // todas las jornadas de la division

        eliminarBatallasCalendario(division);
        vaciarResultadosDivision(division);

        Calendar inicio = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("GMT0");
        inicio.setTimeZone(timeZone);

        inicio.set(Calendar.DAY_OF_MONTH, 1);
        inicio.set(Calendar.HOUR_OF_DAY, 0);
        inicio.set(Calendar.MINUTE, 0);
        inicio.set(Calendar.SECOND, 0);
        inicio.set(Calendar.MILLISECOND, 0);

        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        List<Usuario> grupo = usuarioDAO.getUsuariosDivision(division);

        if (grupo.isEmpty()) {
            return false;
        }

        if (grupo.size() < 10) {
            throw new Error("Invalid number of users (" + grupo.size() + ") in division = " + division);
        }

//        try {
        for (int i = 0; i < 9; i++) {
            crearBatallaCalendario("liga", division, grupo.get(rotar(0, i)), grupo.get(9), inicio, i);
            crearBatallaCalendario("liga", division, grupo.get(rotar(1, i)), grupo.get(rotar(8, i)), inicio, i);
            crearBatallaCalendario("liga", division, grupo.get(rotar(2, i)), grupo.get(rotar(7, i)), inicio, i);
            crearBatallaCalendario("liga", division, grupo.get(rotar(3, i)), grupo.get(rotar(6, i)), inicio, i);
            crearBatallaCalendario("liga", division, grupo.get(rotar(4, i)), grupo.get(rotar(5, i)), inicio, i);
        }

        for (int i = 0; i < 5; i++) {
            crearBatallaAcademia(division, grupo.get(rotar(0, i)), grupo.get(9), inicio, i);
            crearBatallaAcademia(division, grupo.get(rotar(1, i)), grupo.get(rotar(8, i)), inicio, i);
            crearBatallaAcademia(division, grupo.get(rotar(2, i)), grupo.get(rotar(7, i)), inicio, i);
            crearBatallaAcademia(division, grupo.get(rotar(3, i)), grupo.get(rotar(6, i)), inicio, i);
            crearBatallaAcademia(division, grupo.get(rotar(4, i)), grupo.get(rotar(5, i)), inicio, i);
        }

//        } catch (Exception e) {
//            System.out.println("Faltan usuarios en la división = " + division + "?, " + e);
//            DivisionDAO divisionDAO = new DivisionDAO(session);
//            Usuario usuario = grupo.get(0);
//            int posicion = usuario.getPosicion();
//            int posRelativa = posicion + divisionDAO.posicion0Division(divisionDAO.divisionAbsoluta(division));
//            if (grupo.size() < 10 && posRelativa == 1) {
//                int numero = divisionDAO.numUsuariosDivision(division);
//                usuarioDAO.rellenarUsuarios(division, posicion, numero);
//            }
//        }
        usuarioDAO.ordenarPosiciones(division);
        return true;
    }

    // UNUSED!
    public void eliminarBatallaCalendarioAntiguas() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        long limit = cal.getTimeInMillis() / 1000;

        String peticion = "DELETE FROM Batalla WHERE fecha < " + limit;
        session.createQuery(peticion);
    }

    public void eliminarBatallasCalendario(int division) {
        String peticion = "DELETE FROM Batalla WHERE division = " + division;
        session.createQuery(peticion);
    }

    public void eliminarBatallas() {
        String peticion = "DELETE FROM Batalla";
        session.createQuery(peticion);
    }

    public void vaciarResultadosDivision(int division) {
        String peticion = "FROM Usuario WHERE division = " + division;
        List<Usuario> usuarios = session.createQuery(peticion).list();
        for (Usuario usuario : usuarios) {
            usuario.setPuntos("0,0-0");
        }
    }

    public boolean crearBatallasRepesca(int division) {
        long dia = getLastDiaBatalla(division);

        Calendar calendar = Calendar.getInstance();
        if (dia > 0) {
            calendar.setTimeInMillis(dia * 1000);
        } else {
            Date today = new Date();
            calendar.setTime(today);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 27);

        int dia1 = (int) (calendar.getTimeInMillis() / 1000);

        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        DivisionDAO divisionDAO = new DivisionDAO(session);

        int posicion = divisionDAO.posicionAbsoluta(division, 2);

        int divAbsoluta = divisionDAO.divisionAbsoluta(division);
        int divRelativa = divisionDAO.subDivisionRelativa(division);

        int nuevaSuvDiv = divRelativa * 3 + divisionDAO.subDivisionesAnteriores(divAbsoluta + 1);

        int oponente = divisionDAO.posicionAbsoluta(nuevaSuvDiv, 7);
        int oponente2 = divisionDAO.posicionAbsoluta(nuevaSuvDiv + 1, 7);
        int oponente3 = divisionDAO.posicionAbsoluta(nuevaSuvDiv + 2, 7);

        Usuario usuario1 = usuarioDAO.loadUsuarioPosicion(posicion);
        Usuario usuario2 = usuarioDAO.loadUsuarioPosicion(oponente);
        Usuario usuario3 = usuarioDAO.loadUsuarioPosicion(oponente2);
        Usuario usuario4 = usuarioDAO.loadUsuarioPosicion(oponente3);

        //not repesca like div 1.1        
        if (null == usuario2 && null == usuario3 && null == usuario4) {
            return false;
        }

        crearBatallaCalendario("repesca", division, usuario1, usuario4, dia1);
        crearBatallaCalendario("repesca", division, usuario2, usuario3, dia1);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, +1);
        int dia2 = (int) (calendar.getTimeInMillis() / 1000);

        crearBatallaCalendario("repesca", division, usuario3, usuario1, dia2);
        crearBatallaCalendario("repesca", division, usuario4, usuario2, dia2);

        return true;
    }

    public int getLastDiaBatalla(int division) {
        String peticion = "SELECT max(fecha) FROM Batalla WHERE division = " + division + " AND tipo = 'liga'";

        if (session.createQuery(peticion).uniqueResult() == null) {
            return 0;
        } else {
            return ((Integer) session.createQuery(peticion).uniqueResult()).intValue();
        }
    }

    public int rotar(int numero, int operacion) {

        int rotar = numero + operacion;

        if (rotar < 0) {
            rotar = rotar + (criatLiga - 1);
        }
        if (rotar >= criatLiga - 1) {
            rotar = rotar - (criatLiga - 1);
        }

        return rotar;
    }

    public void crearBatallaCalendario(String tipo, int division, Usuario usuario1, Usuario usuario2, Calendar inicio, int jornada) {
        DivisionDAO divisionDAO = new DivisionDAO(session);
        Division div = divisionDAO.getDivision(division);

        Date today = new Date();

        Batalla batalla = new Batalla();
        Batalla batalla2 = new Batalla();

        batalla.setTipo(tipo);
        batalla.setDivision(div);
        batalla2.setTipo(tipo);
        batalla2.setDivision(div);

        if (jornada % 2 == 0) {
            batalla.setEqLoc(usuario1);
            batalla.setEqVis(usuario2);
            batalla2.setEqLoc(usuario2);
            batalla2.setEqVis(usuario1);

        } else {
            batalla.setEqLoc(usuario2);
            batalla.setEqVis(usuario1);
            batalla2.setEqLoc(usuario1);
            batalla2.setEqVis(usuario2);
        }

        Calendar cal = (Calendar) inicio.clone();
        cal.set(Calendar.HOUR_OF_DAY, 17);

        for (int i = 0; i < jornada; i++) {
            if (cal.get(Calendar.DAY_OF_WEEK) == 4 || cal.get(Calendar.DAY_OF_WEEK) == 7) {
                cal.add(Calendar.DATE, 2);
            } else {
                cal.add(Calendar.DATE, 1);
            }
        }

        int fecha = (int) (cal.getTimeInMillis() / 1000);
        batalla.setFecha(fecha);
        if (today.after(cal.getTime())) {
            batalla.setResultado(resultadoRandom(batalla));
            batalla.setCalculos(-1);
        }

        for (int i = 0; i < 9; i++) {
            if (cal.get(Calendar.DAY_OF_WEEK) == 4 || cal.get(Calendar.DAY_OF_WEEK) == 7) {
                cal.add(Calendar.DATE, 2);
            } else {
                cal.add(Calendar.DATE, 1);
            }
        }

        fecha = (int) (cal.getTimeInMillis() / 1000);
        batalla2.setFecha(fecha);
        if (today.after(cal.getTime())) {
            batalla2.setResultado(resultadoRandom(batalla2));
            batalla2.setCalculos(-1);
        }

        save(batalla);
        save(batalla2);
    }

    public void crearBatallaAcademia(int division, Usuario usuario1, Usuario usuario2, Calendar inicio, int jornada) {
        DivisionDAO divisionDAO = new DivisionDAO(session);
        Division div = divisionDAO.getDivision(division);
        Date today = new Date();

        Batalla batalla = new Batalla();

        batalla.setTipo("academia");
        batalla.setDivision(div);

        if (jornada % 2 == 0) {
            batalla.setEqLoc(usuario1);
            batalla.setEqVis(usuario2);

        } else {
            batalla.setEqLoc(usuario2);
            batalla.setEqVis(usuario1);
        }

        Calendar cal = (Calendar) inicio.clone();
        cal.set(Calendar.HOUR_OF_DAY, 17);

        cal.add(Calendar.DAY_OF_WEEK, 1);
        int month = cal.get(Calendar.MONTH);
        cal.add(Calendar.DATE, 7 * jornada);

        if (cal.get(Calendar.MONTH) == month) {
            int fecha = (int) (cal.getTimeInMillis() / 1000);
            batalla.setFecha(fecha);
            if (today.after(cal.getTime())) {
                batalla.setResultado(resultadoRandom(batalla));
                batalla.setCalculos(-1);
            }
            save(batalla);
        }
    }

    public void crearBatallaCalendario(String tipo, int division, Usuario usuario1, Usuario usuario2, int dia) {
        DivisionDAO divisionDAO = new DivisionDAO(session);
        Division div = divisionDAO.getDivision(division);

        Batalla batalla = new Batalla();
        batalla.setTipo(tipo);
        batalla.setDivision(div);
        batalla.setEqLoc(usuario1);
        batalla.setEqVis(usuario2);

        batalla.setFecha(dia);
        save(batalla);
    }

    public String resultadoRandom(Batalla batalla) {
        Usuario usuarioLocal = batalla.getEqLoc();
        Usuario usuarioVisitante = batalla.getEqVis();

        int resultado1 = (int) (Math.random() * 5);
        int resultado2 = (int) (Math.random() * 5);

        int puntosLocal = Integer.parseInt(usuarioLocal.getPuntos().split(",")[0]);
        int puntosVisitante = Integer.parseInt(usuarioVisitante.getPuntos().split(",")[0]);

        if (resultado1 > resultado2) {
            resultado1 = 5;
            puntosLocal++;

        } else {
            resultado2 = 5;
            if (resultado1 == 5) {
                resultado1 = 4;
            }
            puntosVisitante++;
        }

        String[] resultadosLocal = usuarioLocal.getPuntos().split(",")[1].split("-");
        int resLoc1 = Integer.parseInt(resultadosLocal[0]) + resultado1;
        int resLoc2 = Integer.parseInt(resultadosLocal[1]) + resultado2;
        usuarioLocal.setPuntos(puntosLocal + "," + resLoc1 + "-" + resLoc2);
        update(usuarioLocal);

        String[] resultadosVisitante = usuarioVisitante.getPuntos().split(",")[1].split("-");
        int resVis1 = Integer.parseInt(resultadosVisitante[0]) + resultado2;
        int resVis2 = Integer.parseInt(resultadosVisitante[1]) + resultado1;
        usuarioVisitante.setPuntos(puntosVisitante + "," + resVis1 + "-" + resVis2);
        update(usuarioVisitante);

        return resultado1 + "-" + resultado2;
    }

    public List<Batalla> getBatallasAcademia(int usuarioId) {
        String peticionBatallas = "FROM Batalla WHERE tipo = 'academia' AND calculos > 0 AND (eqLoc = '" + usuarioId + "' OR eqVis = '" + usuarioId + "')";
        return session.createQuery(peticionBatallas).list();
    }

    public List<Batalla> getBatallasDivision(int division) {
        String peticionBatallas = "FROM Batalla WHERE division =" + division;
        List<Batalla> batallas = session.createQuery(peticionBatallas).list();
        return batallas;
    }

//    public List<BatallaId> getBatallasDivisionHoy(int division) {
//
//        Date date = new Date();
////        int dias = (int) (date.getTime() / 86400000);
//
//        String peticionBatallas = "FROM Batalla WHERE fecha = " + date + " AND division = " + division;
//        List<Batalla> batallas = session.createQuery(peticionBatallas).list();
//
//        List<BatallaId> batallasId = new ArrayList();
//
//        for (int i = 0; i < batallas.size(); i++) {
//
//            BatallaId batallaId = new BatallaId();
//
//            batallaId.setId(batallas.get(i).getId());
//            batallaId.setIdLoc(batallas.get(i).getEquipoLocalId());
//            batallaId.setIdVis(batallas.get(i).getEquipoVisitanteId());
//            batallaId.setRes(batallas.get(i).getResultados());
//
//            batallasId.add(batallaId);
//        }
//
//        return batallasId;
//    }
    public List[] getBatallasUsuarioNotNull(int id) {
        String peticion1 = "SELECT res FROM Batalla WHERE eqLocId = " + id + " AND res != null";
        List<String> resultados1 = session.createQuery(peticion1).list();

        String peticion2 = "SELECT res FROM Batalla WHERE eqVisId = " + id + " AND res != null";
        List<String> resultados2 = session.createQuery(peticion2).list();

        List[] array = {resultados1, resultados2};
        return array;
    }

    public List<Batalla> getBatallasCalculo(int division) {
        int today = (int) (new Date().getTime() / 1000);

        //LIGA con misma DIVISION
        String peticion = "FROM Batalla WHERE calculos = 0 AND division = " + division + " AND fecha <= " + today; //liga y academia
        List<Batalla> batallas = session.createQuery(peticion).list(); //without limit

        if (batallas.isEmpty()) {//ACADEMIA con DIVISION diferente            
            peticion = "FROM Batalla WHERE division = (SELECT min(division) FROM Batalla WHERE tipo = 'liga' AND calculos = 0 AND division <> " + division + " AND fecha <= " + today + ")"
                    + " AND tipo = 'liga' AND calculos = 0 AND fecha <= " + today;
            batallas = session.createQuery(peticion).setMaxResults(5).list();

            if (batallas.isEmpty()) {//LIGA cualquiera
                peticion = "FROM Batalla WHERE division = (SELECT min(division) FROM Batalla WHERE tipo = 'liga' AND calculos = 0 AND fecha <= " + today + ")"
                        + " AND tipo = 'liga' AND calculos = 0 AND fecha <= " + today;
                batallas = session.createQuery(peticion).setMaxResults(5).list();

                if (batallas.isEmpty()) {//cualquiera DENUNCIADA
                    peticion = "FROM Batalla WHERE division = (SELECT min(division) FROM Batalla WHERE den > 0 AND calculos < 5)"
                            + " AND den > 0 AND calculos < 5";
                    batallas = session.createQuery(peticion).setMaxResults(5).list();

                    if (batallas.isEmpty()) {//ACADEMIA con cualquier DIVISION
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.HOUR_OF_DAY, -2);
                        int fecha = (int) (cal.getTimeInMillis() / 1000);

                        peticion = "FROM Batalla WHERE division = (SELECT min(division) FROM Batalla WHERE tipo = 'academia' AND calculos = 0 AND fecha <= " + fecha + ")"
                                + " AND tipo = 'academia' AND calculos = 0 AND fecha <= " + fecha;
                        batallas = session.createQuery(peticion).setMaxResults(5).list();
                    }
                }
            }
        }

        Gson gson = new Gson();
        CriaturaDAO criaturaDAO = new CriaturaDAO(session);

        boolean error = false;
        for (Batalla batalla : batallas) {
            if (batalla.getCalculos() == 0) {
                if (batalla.getEqLoc() != null && batalla.getEqVis() != null) {
                    batalla.setAlinLoc(gson.toJson(criaturaDAO.guardarEquipo(batalla.getTipo(), batalla.getEqLoc())));
                    batalla.setAlinVis(gson.toJson(criaturaDAO.guardarEquipo(batalla.getTipo(), batalla.getEqVis())));
//                    update(batalla);
                    Query query = session.createQuery("update Batalla set alinLoc = :alinLoc, alinVis = :alinVis WHERE id = :id");
                    query.setParameter("alinLoc", batalla.getAlinLoc());
                    query.setParameter("alinVis", batalla.getAlinVis());
                    query.setParameter("id", batalla.getId());
                    int result = query.executeUpdate();
                    System.out.println("update batallas result = " + result);

                } else {
                    batallas.remove(batalla);
                    //session.evict(batalla);
                    //delete(batalla);
                    error = true;
                    break;
                }
            }
            session.evict(batalla);

            //hide options to make anonimous
            batalla.setDivision(null);
            batalla.setEqLoc(null);
            batalla.setEqVis(null);
        }
        if (error) {
            System.out.println("error: perdida de usuarios ( batalla.getEqLoc() || batalla.getEqVis() == null ). Las batallas han sido eliminadas.");
            throw new Error("error: perdida de usuarios ( batalla.getEqLoc() || batalla.getEqVis() == null ). Las batallas han sido eliminadas.");
        }
        return batallas;
    }

    public List<Batalla> getBatallasUsuario(int division, int id) {
        String peticion = "SELECT * FROM Batalla WHERE division = " + division + " AND tipo = 'liga' AND (eqLoc = " + id + " OR eqVis = " + id + ")";
//        String peticion = "FROM Batalla";
        List<Batalla> list = new ArrayList();
        Query query = session.createQuery(peticion);
        if (query != null) {
            list = query.list();
        }
        return list;
    }

//    public Usuario getUltimoEnemigo(Usuario yo) {
//        Batalla batalla = getUltimaBatalla(yo);
//
//        if (yo == batalla.getEqLoc()) {
//            return batalla.getEqVis();
//        } else {
//            return batalla.getEqLoc();
//        }
//    }
    public Batalla getUltimaBatalla() {
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        Usuario user = usuarioDAO.getUsuario();
        return getUltimaBatalla(user);
    }

    public Batalla getUltimaBatalla(Usuario user) {
        int now = (int) (new Date().getTime() / 1000);

        String peticion = "FROM Batalla WHERE fecha = (SELECT max(fecha) FROM Batalla WHERE fecha < " + now + " eqLoc = " + user.getId() + " OR eqLoc = " + user.getId() + ") "
                + "AND (eqLoc = " + user.getId() + " OR eqLoc = " + user.getId() + ")";
        return (Batalla) session.createQuery(peticion).uniqueResult();
    }

    public Batalla getBatalla(long id) {
        String peticion = "FROM Batalla WHERE id = " + id;
        return (Batalla) session.createQuery(peticion).uniqueResult();
    }

    public int getCountMapa(int division) {
        String peticion = "SELECT count(*) FROM Batalla WHERE division = " + division;
        int count = (int) ((Long) session.createQuery(peticion).uniqueResult()).longValue();
        return count;
    }

//    public List<Batalla> getUltimasBatallasDivision(int division) {
//
//        String peticion = "FROM Batalla WHERE fecha = (SELECT max(fecha) FROM Batalla WHERE division = " + division + ") AND division = " + division;
//        List<Batalla> batallas = session.createQuery(peticion).list();
//
//        return batallas;
//    }
//    public List<Batalla> getUltimasBatallasDivision(int division, int dia) {
//
//        String peticion = "FROM Batalla WHERE fecha = (SELECT max(fecha) FROM Batalla WHERE LOWER(fecha) = " + dia + " AND division = " + division + ") AND division = " + division;
//        List<Batalla> batallas = session.createQuery(peticion).list();
//
//        return batallas;
//    }
//    public List<Batalla> getProximasBatallas(int division, int dias) {//
//        Date fecha = (Date) session.createCriteria(Batalla.class).add(Restrictions.eq("division", division)).add(Restrictions.ge("fecha", dias)).setProjection(Projections.min("fecha")).uniqueResult();
//        return session.createCriteria(Batalla.class).add(Restrictions.eq("division", division)).add(Restrictions.eq("fecha", fecha)).list();
//    }
//    public List<Batalla> getUltimasBatallasRealizadas(int division) {
//
//        Date date = new Date();
//        int hoy = (int) (date.getTime() / 86400000);
//        Calendar calendar = GregorianCalendar.getInstance();
//        calendar.setTime(date);
//        int hours = calendar.get(Calendar.HOUR_OF_DAY);
//        Date fecha;
//
//        Criteria criteria = session.createCriteria(Batalla.class).add(Restrictions.eq("division", division)).add(Restrictions.isNotNull("resultado"));
//        if (session.createCriteria(Batalla.class).add(Restrictions.eq("division", division)).add(Restrictions.isNull("resultado")).setProjection(Projections.min("fecha")).uniqueResult() != null) {
//            fecha = (Date) session.createCriteria(Batalla.class).add(Restrictions.eq("division", division)).add(Restrictions.isNotNull("resultado")).setProjection(Projections.max("fecha")).uniqueResult();
//        } else {
//            fecha = ((Integer) session.createCriteria(Batalla.class).add(Restrictions.eq("division", division)).setProjection(Projections.max("fecha")).uniqueResult()).intValue();
//        }
//
//        if (hours >= 18 && hours < 20 && fecha == hoy) {
//            fecha = ((Integer) criteria.add(Restrictions.lt("fecha", fecha)).setProjection(Projections.max("fecha")).uniqueResult()).intValue();
//        } else {
//            fecha = ((Integer) criteria.add(Restrictions.le("fecha", fecha)).setProjection(Projections.max("fecha")).uniqueResult()).intValue();
//        }
//        return session.createCriteria(Batalla.class).add(Restrictions.eq("division", division)).add(Restrictions.eq("fecha", fecha)).list();
//    }
    public List<Batalla> getCalendarioDivision(int division) {
        String peticion = "FROM Batalla WHERE division = " + division + " ORDER BY fecha ASC";
        List<Batalla> batallas = session.createQuery(peticion).list();
        if (batallas.isEmpty()) {
            CheckCalendarioMapa(division);
            return getCalendarioDivision(division);
        }

        return batallas;
    }

    public int setResultadoBatalla(int usuarioId, long id, EstadisticasBatalla estadisticas) {

        Gson gson = new Gson();
        String resultado = estadisticas.getResultado();
        CriaturaDAO criaturaDAO = new CriaturaDAO(session);

        String peticion = "FROM Batalla WHERE id = " + id;
        Batalla batalla = (Batalla) session.createQuery(peticion).uniqueResult();

        if (batalla.getCalculos() == 0) { //es el primer resultado
            estadisticas.setCalculos(usuarioId + ",");
            batalla.setRes(gson.toJson(estadisticas));
            batalla.setResultado(estadisticas.resultado);

            if (!batalla.getTipo().equals("academia")) {
                asignarResultados(batalla, resultado);
                criaturaDAO.asignarMuertes("liga", estadisticas.getMuertes());

            } else if (batalla.getTipo().equals("academia")) {

                List<Alineacion> local = gson.fromJson(batalla.getAlinLoc(), new TypeToken<List<Alineacion>>() {
                }.getType());

                List<Alineacion> visitante = gson.fromJson(batalla.getAlinVis(), new TypeToken<List<Alineacion>>() {
                }.getType());

                criaturaDAO.destapar(local, visitante);
                criaturaDAO.asignarMuertes("academia", estadisticas.getMuertes());
            }

        } else { //no es el primer resultado
            setResultado(batalla, estadisticas, resultado, usuarioId);
        }

        batalla.setCalculos(batalla.getCalculos() + 1);
        update(batalla);
        return batalla.getDivision().getDivision();
    }

    public void setResultado(Batalla batalla, EstadisticasBatalla estadisticas, String resultado, int usuarioId) {
        Gson gson = new Gson();

        if (estadisticas.getResultado().equals(resultado)) {
            estadisticas.setCalculos(estadisticas.getCalculos() + "," + usuarioId);
            batalla.setRes(gson.toJson(estadisticas));

        } else { //no es el mismo resultado q batalla.Resultados
            if (batalla.getRes2() == null) { //Resultados2 esta vacio
                estadisticas.setCalculos(usuarioId + "");
                batalla.setRes2(gson.toJson(estadisticas));
            } else { //Resultados2 NO esta vacio
                String jsonBatalla2 = batalla.getRes2();
                EstadisticasBatalla estadisticas2 = gson.fromJson(jsonBatalla2, EstadisticasBatalla.class);

                if (estadisticas2.getResultado().equals(resultado)) {
                    estadisticas2.setCalculos(estadisticas2.getCalculos() + "," + usuarioId);
                } else { //Resultados2 NO es el mismo resultado

                    if (batalla.getRes3() == null) { //Resultados3 esta vacio
                        estadisticas.setCalculos(usuarioId + "");
                        batalla.setRes3(gson.toJson(estadisticas));

                    } else { //Resultados3 NO esta vacio
                        String jsonBatalla3 = batalla.getRes3();
                        EstadisticasBatalla estadisticas3 = gson.fromJson(jsonBatalla3, EstadisticasBatalla.class);

                        if (estadisticas3.getResultado().equals(resultado)) {
                            estadisticas3.setCalculos(estadisticas3.getCalculos() + "," + usuarioId);

                            if (estadisticas3.getCalculos().split(",").length > estadisticas2.getCalculos().split(",").length) { //Resultados3 tiene más cálculos que Resultado2

                                batalla.setRes3(batalla.getRes2());
                                batalla.setRes2(gson.toJson(estadisticas3));

                                corregirResultados(batalla.getId(), estadisticas2, estadisticas3);
                            } else {
                                batalla.setRes3(gson.toJson(estadisticas3));
                            }
                        }
                    }
                }

                if (estadisticas2.getCalculos().split(",").length > estadisticas.getCalculos().split(",").length) { //Resultados2 tiene más cálculos que Resultado1

                    batalla.setRes2(batalla.getRes());
                    batalla.setRes(gson.toJson(estadisticas2));

                    corregirResultados(batalla.getId(), estadisticas, estadisticas2);
                } else {
                    batalla.setRes2(gson.toJson(estadisticas2));
                }
            }
        }
    }

    public void asignarResultados(Batalla batalla, String resultado) {
        MensajeDAO mensajeDAO = new MensajeDAO(session);

        String[] arrayResultado = resultado.split("-");
        int premio = 100 / batalla.getDivision().getDivision();

        int resultado1 = Integer.parseInt(arrayResultado[0]);
        int resultado2 = Integer.parseInt(arrayResultado[1]);

        Usuario local = batalla.getEqLoc();
        Usuario visitante = batalla.getEqVis();

        int puntos, p1 = 0, p2 = 0;
        String[] p, ratio;
        if (resultado1 > resultado2) {
            p1 = 1;
            long oroLocal;
            try {
                oroLocal = local.getOro();
            } catch (Exception e) {
                oroLocal = 0;
            }
            local.setOro(oroLocal + premio + 50);
            if (visitante.getPhpbb_user() != null) {
                mensajeDAO.setPremioBatalla(local, visitante.getPhpbb_user().getUsername(), premio);
            }

        } else {
            p2 = 1;
            long oroVisitante;
            try {
                oroVisitante = visitante.getOro();
            } catch (Exception e) {
                oroVisitante = 0;
            }
            visitante.setOro(oroVisitante + premio + 50);
            if (local.getPhpbb_user() != null) {
                mensajeDAO.setPremioBatalla(visitante, local.getPhpbb_user().getUsername(), premio);
            }
        }

        p = local.getPuntos().split(",");
        puntos = Integer.parseInt(p[0]);
        ratio = p[1].split("-");
        local.setPuntos((puntos + p1) + "," + (Integer.parseInt(ratio[0]) + resultado1) + "-" + (Integer.parseInt(ratio[1]) + resultado2));
        update(local);

        p = visitante.getPuntos().split(",");
        puntos = Integer.parseInt(p[0]);
        ratio = p[1].split("-");
        visitante.setPuntos((puntos + p2) + "," + (Integer.parseInt(ratio[0]) + resultado2) + "-" + (Integer.parseInt(ratio[1]) + resultado1));
        update(visitante);
    }

    public void corregirResultados(long id, EstadisticasBatalla anterior, EstadisticasBatalla nuevo) {
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);

        String peticion = "FROM Batalla WHERE id = " + id;
        Batalla batalla = (Batalla) session.createQuery(peticion).uniqueResult();

        String[] arrayAnterior = anterior.getResultado().split("-");
        String[] arrayNuevo = nuevo.getResultado().split("-");

        int anterior1 = Integer.parseInt(arrayAnterior[0]);
        int anterior2 = Integer.parseInt(arrayAnterior[1]);
        int nuevo1 = Integer.parseInt(arrayNuevo[0]);
        int nuevo2 = Integer.parseInt(arrayNuevo[1]);

        int p1 = 0, p2 = 0;
        String puntos1 = "", puntos2 = "";

        Usuario local = batalla.getEqLoc();
        Usuario visitante = batalla.getEqVis();

        if (batalla.getTipo().equals("liga")) {
            puntos1 = local.getPuntos();
            puntos2 = visitante.getPuntos();

        }

        if (anterior1 > anterior2 && nuevo1 < nuevo2) {
            p1 = Integer.parseInt(puntos1.split(",")[0]) - 1;
            p2 = Integer.parseInt(puntos2.split(",")[0]) + 1;
        }

        if (anterior1 < anterior2 && nuevo1 > nuevo2) {
            p1 = Integer.parseInt(puntos1.split(",")[0]) + 1;
            p2 = Integer.parseInt(puntos2.split(",")[0]) - 1;
        }

        puntos1 = p1 + "," + local.getPuntos().split(",")[1];
        puntos2 = p2 + "," + visitante.getPuntos().split(",")[1];

        if (batalla.getTipo().equals("liga")) {
            local.setPuntos(puntos1);
            visitante.setPuntos(puntos2);

        }

        update(local);
        update(visitante);

        if (batalla.getTipo().equals("liga")) {
            usuarioDAO.ordenarPosiciones(batalla.getDivision().getDivision()); //ordenar posiciones
        }
    }

//    public int getControlTrampas(int usuario) {
//
//        Gson gson = new Gson();
//
//        Date date = new Date();
//        long time = date.getTime();
//        int dias = ((int) (time / 86400000)) - 1;
//
//        String peticion = "FROM Batalla WHERE fecha = " + dias + " AND (equipoLocal = " + usuario + " OR equipoVisitante = " + usuario + ")";
//        Batalla batalla = (Batalla) session.createQuery(peticion).uniqueResult();
//
//        int resultado = 0;
//
//        if (batalla.getResultados2() != null) {
//
//            String[] tramposos = gson.fromJson(batalla.getResultados2(), EstadisticasBatalla.class).getCalculos().split(",");
//
//            if (gson.fromJson(batalla.getResultados(), EstadisticasBatalla.class).getCalculos().split(",").length > tramposos.length) {
//
//                for (int i = 0; i < tramposos.length; i++) {
//                    if (Long.parseLong(tramposos[i]) == usuario) {
//                        resultado = 1;
//                        break;
//                    }
//                }
//
//                if (batalla.getResultados3() != null) {
//                    tramposos = gson.fromJson(batalla.getResultados3(), EstadisticasBatalla.class).getCalculos().split(",");
//                    for (int i = 0; i < tramposos.length; i++) {
//                        if (Long.parseLong(tramposos[i]) == usuario) {
//                            resultado = 1;
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//        return resultado;
//    }
    public void anularBatallas() {

        String peticion = "FROM Batalla";
        List<Batalla> batallas = session.createQuery(peticion).list();

        for (Batalla batalla : batallas) {
            batalla.setCalculos(5);
            batalla.setAlinLoc(null);
            batalla.setAlinVis(null);
            update(batalla);
        }
    }

    public void CheckCalendarioMapa(int division) {
        System.out.println("111");
        if (getCountMapa(division) > 0) {
            System.out.println("222");
            Date date = new Date();
            int dia = (int) (date.getTime() / 86400000);
            int diaBatalla = getLastDiaBatalla(division);
            if (diaBatalla < dia) {
                if (getDiaMes() < 27) {
                    crearBatallasCalendario(division);
                } else if (getDiaMes() == 27) {
                    if (division != 1) {
                        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
                        usuarioDAO.copaLiga(division);

                        boolean resolved = crearBatallasRepesca(division);
                    }
                }
            }
        } else {
            System.out.println("333");
            crearBatallasCalendario(division);
        }
    }

    public int getDiaMes() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void rectificarPremios(int inicio, int fin) {
        String peticion = "FROM MensajeJuego WHERE fecha BETWEEN '" + inicio + "' AND '" + fin + "'";
        List<MensajeJuego> mensajes = session.createQuery(peticion).list();

        Usuario usuario;
        int valor;

        for (MensajeJuego mensaje : mensajes) {
            usuario = mensaje.getReceptor();
            valor = Integer.parseInt(mensaje.getMensaje().split(" ")[5]);

            session.delete(mensaje);
            usuario.setOro(usuario.getOro() - valor);
            update(usuario);
        }
    }

    public void setDenuncia(long id) {
        String peticion = "FROM Batalla WHERE id = " + id;
        Batalla batalla = (Batalla) session.createQuery(peticion).uniqueResult();
        if (batalla.getDen() == null) {
            batalla.setDen(1);
        } else {
            batalla.setDen(batalla.getDen() + 1);
        }
        update(batalla);
    }
}
