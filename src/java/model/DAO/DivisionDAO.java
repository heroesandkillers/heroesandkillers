package model.DAO;

import java.util.ArrayList;
import java.util.List;
import model.hibernate.Division;
import model.hibernate.HibernateUtil;
import model.hibernate.Usuario;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DivisionDAO {

    private Session session;
    final static public int subDiv = 3;
    final static public int leagueUsers = 10;

    public DivisionDAO(Session session) {
        this.session = session;
    }

    private Division loadDivision(int division) {
        Transaction t = session.beginTransaction();
        Division object = (Division) session.load(Division.class, division);
        t.commit();
        return object;
    }

    //OBJETO DIVISION
    public Division getDivision(int division) {
        String peticion = "FROM Division WHERE division = :division ";
        Query query = session.createQuery(peticion);
        query.setParameter("division", division);
        Division div = (Division) query.uniqueResult();

        if (div == null) {
            System.out.println("division " + division + " not have Division Object: setting new Division()");
            div = new Division();
            div.setDivision(division);
            save(div);
        }
        return div;
    }

    private void save(Object object) {
        Transaction t = session.beginTransaction();
        session.save(object);
        t.commit();
    }

    private void update(Object object) {
        Transaction t = session.beginTransaction();
        session.update(object);
        t.commit();
    }

    public int numSubDivisiones() {
        String peticion = "SELECT max(division) FROM Division";
        int divisiones = Integer.parseInt(session.createQuery(peticion).uniqueResult().toString());
        return divisiones;
    }

    public Division subDivision(int division, int posRelativa) {

        int subDivision;
        int posLista = (int) ((posRelativa - 1) / Math.pow(subDiv, division - 1));
        int subDivisionRelativa = (int) (posRelativa - posLista * Math.pow(subDiv, division - 1));

        int subDivisionesAnteriores = 0;
        for (int i = 1; i < division; i++) {
            subDivisionesAnteriores = subDivisionesAnteriores + (int) Math.pow(subDiv, i - 1);
        }

        if (subDivisionesAnteriores > 0) {
            subDivision = subDivisionesAnteriores + subDivisionRelativa;
        } else {
            subDivision = 1;
        }
        return getDivision(subDivision);
    }

    public int subDivisionesAnteriores(int divAbs) {
        int subDivAnt = 0;
        for (int i = 1; i < divAbs; i++) {
            subDivAnt = subDivAnt + subDivisiones(i);
        }
        return subDivAnt;
    }

    public int subDivisionRelativa(int subdivision) {

        int divAbs = divisionAbsoluta(subdivision);

        int subDivAnt = subDivisionesAnteriores(divAbs);

        return subdivision - subDivAnt;
    }

    private int subDivisiones(int division) {
        int subDivisionesAnteriores = 0;
        for (int i = 1; i <= division; i++) {
            subDivisionesAnteriores = subDivisionesAnteriores + (int) Math.pow(subDiv - 1, i - 1);
        }
        return subDivisionesAnteriores;
    }

//    private List<Integer> numerosDivision(int division) {
//
//        List<Integer> numerosDivision = new ArrayList<Integer>();
//
//        int partesDivision = (int) Math.pow(subDiv, division - 1);
//
//        int numeroDivision = 1;
//
//        for (int i = 0; i < division - 1; i++) {
//            numeroDivision = numeroDivision + (int) Math.pow(subDiv, i);
//        }
//
//        for (int i = 0; i < partesDivision; i++) {
//            numerosDivision.add(numeroDivision);
//            numeroDivision++;
//        }
//
//        return numerosDivision;
//    }

    public int divisionAbsoluta(int subdivision) {

        int division = 0;
        int resultado = 0;
        boolean siguiente = true;
        while (siguiente == true) {
            division++;
            if (resultado + (int) Math.pow(subDiv, division) > subdivision) {
                siguiente = false;
            } else {
                resultado = resultado + (int) Math.pow(subDiv, division);
            }
        }
        return division;
    }

    public int divisionAbsolutaByPosicion(int posicion) {
        int division = 0;
        boolean superior = true;
        int resultado = 0;

        while (superior == true) {
            division++;

            for (int i = 0; i < division; i++) {
                resultado = (int) (leagueUsers * (Math.pow(subDiv, i)));
            }

            if (resultado >= posicion) {
                superior = false;
            }
        }
        return division;
    }

    public int numUsuariosDivision(int division) {
        return (int) (leagueUsers * (Math.pow(subDiv, division - 1)));
    }

    public int posicion0Division(int division) {
        int posRelativa = 0;
        for (int i = 0; i < division - 1; i++) {
            posRelativa = posRelativa - (int) (leagueUsers * (Math.pow(subDiv, i)));
        }
        return posRelativa;
    }

    public int posicionAbsoluta(int divisionRelativa, int posicionLiga) {

        int division = 0;
        int resultado = 0;
        int posicion = 0;
        boolean siguiente = true;
        while (siguiente == true) {

            if (resultado + (int) Math.pow(subDiv, division) >= divisionRelativa) {
                siguiente = false;
            } else {
                resultado = resultado + (int) Math.pow(subDiv, division);
                posicion = posicion + leagueUsers * (int) Math.pow(subDiv, division);
            }
            division++;
        }

        int subdivisiones = subDivisiones(division);
        int subdivision = divisionRelativa - resultado;
        posicion = posicion + (posicionLiga - 1) * subdivisiones + subdivision;

        return posicion;
    }

//    public int maxDivision() {
//
//        int subdivision = numSubDivisiones();
//        int division = 0;
//        int resultado = 0;
//        boolean siguiente = true;
//        while (siguiente == true) {
//            division++;
//            if (resultado + (int) Math.pow(subDiv, division) > subdivision) {
//                siguiente = false;
//            } else {
//                resultado = resultado + (int) Math.pow(subDiv, division);
//            }
//        }
//        return division;
//    }

    public void setMapa(int division, String mapa, byte[] back) {
        Division div = loadDivision(division);
        if (mapa != null) {
            div.setMapa(mapa);
        } else {
            div.setBack(back);
        }
        update(div);
    }

//    public int getEmptyDivision() {
//        int division = 0;
//        long count = 10;
//        while (count > 9) {
//            division++;
//            String peticion = "SELECT count(*) FROM Usuario WHERE division = :division";
//            Query query = session.createQuery(peticion);
//            query.setParameter("division", division);
//            count = (Long) session.createQuery(peticion).uniqueResult();
//        }
//        return division;
//    }

    public void checkDivision(int div, int users) {
        if (users < leagueUsers) {
            int add = users - leagueUsers;
            CriaturaDAO criaturaDAO = new CriaturaDAO(session);

            for (int i = 0; i < add; i++) {
                Usuario usuario = new Usuario();
                DivisionDAO divisionDAO = new DivisionDAO(session);
                usuario.setDivision(divisionDAO.getDivision(div));
                usuario.setActivo(0);
                save(usuario);
                for (int j = 0; j < 10; j++) {
                    criaturaDAO.crearCriatura(usuario);
                }
                for (int k = 0; k < 10; k++) {
                    criaturaDAO.crearCriaturaAcademia(usuario);
                }
            }
        }
    }

}
