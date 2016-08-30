package model.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.hibernate.HibernateUtil;
import model.hibernate.Phpbb_user;
import model.hibernate.Trofeo;
import model.hibernate.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class UsuarioDAO {

    private Session session;

    public UsuarioDAO(Session session) {
        this.session = session;
    }

    public int getUsuarioId(Phpbb_user phpbb_user) {
        String peticion = "FROM Usuario WHERE phpbb_user = " + phpbb_user.getUser_id();
        try {
            return ((Usuario) session.createQuery(peticion).uniqueResult()).getId();
        } catch (Exception e) {
            System.out.println("INFO: No existe el usuario phpbb_user con id = " + phpbb_user.getUser_id() + " en el juego. getUsuarioId() en UsuarioDAO");
            return crearUsuario(phpbb_user).getId();
        }
    }

    public Usuario loadUsuario(int id) {
        Transaction t = session.beginTransaction();
        Usuario usuario = (Usuario) session.load(Usuario.class, id);
        t.commit();
        return usuario;
    }

    public Usuario getUsuario(int id) {
        Transaction t = session.beginTransaction();
        Usuario usuario = (Usuario) session.get(Usuario.class, id);
        usuario.username = getName(usuario);
        t.commit();
        return usuario;
    }

    public Usuario getUsuario(String user) {
        String peticion = "FROM Usuario WHERE nombreId='" + user + "'";
        Usuario usuario = (Usuario) session.createQuery(peticion).uniqueResult();
        usuario.username = getName(usuario);
        return usuario;
    }

    public Usuario loadUsuarioPhpbb(Phpbb_user phpbb_user) {
        String peticion = "FROM Usuario WHERE phpbb_user=" + phpbb_user.getUser_id();
        Usuario usuario = (Usuario) session.createQuery(peticion).uniqueResult();

        if (usuario == null) {
            if (!existeUsuario(phpbb_user.getUsername())) {
                usuario = crearUsuario(phpbb_user);
            } else {
                System.out.println("ERROR: Ya existe un usuario con nombre: " + phpbb_user.getUsername() + " en el juego. Imposible generar un nuevo usuario.");
                return usuario;
            }

        }
        return usuario;
    }

    public Usuario loadUsuarioPosicion(int posicion) {
        String peticion = "FROM Usuario WHERE posicion = " + posicion;
        Usuario usuario = (Usuario) session.createQuery(peticion).uniqueResult();
        if (null == usuario) {
            //throw new Error("Usuario no cargado");
            return null;
        }
        return usuario;
    }

    public Usuario getNewUsuario() {
        String peticion = "FROM Usuario WHERE posicion = (SELECT min(posicion) FROM Usuario WHERE activo = 0)";
        Usuario usuario = (Usuario) session.createQuery(peticion).uniqueResult();
        return usuario;
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

//    public void setUsername(int id, String name) {
//        Usuario usuario = loadUsuario(id);
//        usuario.setUsername(name);
//        save(usuario);
//    }
    public List<Usuario> getUsuariosDivision(int division) {
        String peticion = "FROM Usuario WHERE division = " + division;
        try {
            List<Usuario> usuarios = session.createQuery(peticion).list();
            return usuarios;
        } catch (Exception e) {
            throw new Error("La división = " + division + " no existe: " + e);
        }
    }

    public String getEntreno(int userId) {
        Usuario usuario = getUsuario(userId);
        return usuario.getEntreno();
    }

    public String getAlineacion(int userId) {
        Usuario usuario = getUsuario(userId);
        return usuario.getAlin();
    }

    public String getEconomia(int userId) {
        Usuario usuario = getUsuario(userId);
        return usuario.getEconomia();
    }

    public void setEntreno(int userId, String entreno) {
        Usuario usuario = getUsuario(userId);
        usuario.setEntreno(entreno);
        update(usuario);
    }

    public void setAlineacionUsuario(int id, String alineacion) {

        Usuario usuario = (Usuario) session.load(Usuario.class, id);

        usuario.setAlin(alineacion);
        update(usuario);
    }

    public void setAlineacionAcademia(int id, String alineacion) {

        Usuario usuario = (Usuario) session.load(Usuario.class, id);

        usuario.setAlinAcad(alineacion);
        update(usuario);
    }

    public boolean existeUsuario(String user) {
        boolean existe = false;
        String peticion = "FROM Usuario WHERE username='" + user + "'";
        if (session.createQuery(peticion).uniqueResult() != null || user.equals("hak")) {
            existe = true;
        }
        return existe;
    }

    public Usuario crearUsuario(Phpbb_user phpbb_user) {

        DivisionDAO divisionDAO = new DivisionDAO(session);
        int posicion = usuariosActivos() + 1;

        if (posicion == 1) {
            TiempoDAO tiempoDAO = new TiempoDAO(session);
            tiempoDAO.createTiempo("entreno");
        }

        int division = divisionDAO.divisionAbsolutaByPosicion(posicion);
        int posRelativa = posicion + divisionDAO.posicion0Division(division);
        if (posRelativa == 1) {
            String peticion = "FROM Usuario WHERE posicion >= " + posicion;
            if (session.createQuery(peticion).list().isEmpty()) {
                int numero = divisionDAO.numUsuariosDivision(division);
                rellenarUsuarios(division, posicion, numero);
            }
        }
        Usuario usuario = getNewUsuario();
        usuario.setPhpbb_user(phpbb_user);
        usuario.setActivo(1);
        usuario.setOro(5000L);
        usuario.setDivision(divisionDAO.getDivision(division));
        ConstruccionesDAO consDAO = new ConstruccionesDAO(session);
        usuario.setCons(consDAO.setConstrucciones());
        update(usuario);

        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        criaturaDAO.eliminarCriaturas(usuario);
        for (int i = 0; i < 10; i++) {
            criaturaDAO.crearCriatura(usuario);
        }
        for (int i = 0; i < 10; i++) {
            criaturaDAO.crearCriaturaAcademia(usuario);
        }
        return usuario;
    }

    public int usuariosActivos() {
        return ((Number) session.createCriteria(Usuario.class).add(Restrictions.eq("activo", 1)).setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

    public int numUsuarios() {
        return ((Number) session.createCriteria(Usuario.class).setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

    public List<Usuario> getUsuariosActivos() {

        String peticion = "FROM Usuario WHERE activo = 1";
        List<Usuario> usuarios = session.createQuery(peticion).list();

        return usuarios;
    }

    public void eliminarUsuario(int id) {
        System.out.println("eliminarUsuario " + id + " id in UsuarioDAO");
        Transaction t = session.beginTransaction();
        Usuario usuario = (Usuario) session.load(Usuario.class, id);
        session.delete(usuario);
        t.commit();
    }

    public void rellenarUsuarios(int division, int posicion, int numero) {
        int posRelativa = 1;
        CriaturaDAO criaturaDAO = new CriaturaDAO(session);

        for (int i = 0; i < numero; i++) {
            Usuario usuario = new Usuario();
            DivisionDAO divisionDAO = new DivisionDAO(session);
            usuario.setDivision(divisionDAO.subDivision(division, posRelativa));
            usuario.setPosicion(posicion);
            usuario.setId(null);
            usuario.setActivo(0);
            save(usuario);

            for (int j = 0; j < 10; j++) {
                criaturaDAO.crearCriatura(usuario);
            }
            for (int k = 0; k < 10; k++) {
                criaturaDAO.crearCriaturaAcademia(usuario);
            }
            posicion++;
            posRelativa++;
        }
    }

    public void ordenarPosiciones(int division) {

        DivisionDAO divisionDAO = new DivisionDAO(session);
        String peticion = "FROM Usuario WHERE division = " + division;
        try {
            List<Usuario> usuarios = session.createQuery(peticion).list();

            int[] arrayPosiciones = new int[usuarios.size()];

            for (int i = 0; i < arrayPosiciones.length; i++) {
                arrayPosiciones[i] = divisionDAO.posicionAbsoluta(division, i + 1);
            }

            for (int i = 0; i < arrayPosiciones.length; i++) {

                String[] puntosMax = usuarios.get(0).getPuntos().split(",");
                Usuario temp = usuarios.get(0);

                for (int j = 1; j < usuarios.size(); j++) {

                    Usuario usuario = usuarios.get(j);

                    String[] puntuaciones = usuario.getPuntos().split(",");
                    int puntos = Integer.parseInt(usuario.getPuntos().split(",")[0]);

                    int pMax = Integer.parseInt(puntosMax[0]);
                    if (puntos > pMax) {
                        temp = usuario;
                        puntosMax = puntuaciones;

                    } else if (puntos == pMax) {

                        int bajasMax = Integer.parseInt(puntosMax[1].split("-")[0]);
                        int bajas = Integer.parseInt(puntuaciones[1].split("-")[0]);

                        if (bajas > bajasMax) {

                            temp = usuario;
                            puntosMax = puntuaciones;

                        } else if (bajas == bajasMax) {

                            int muertesMax = Integer.parseInt(puntosMax[1].split("-")[0]);
                            int muertes = Integer.parseInt(puntuaciones[1].split("-")[0]);

                            if (muertes > muertesMax) {

                                temp = usuario;
                                puntosMax = puntuaciones;

                            }
                        }
                    }
                }

                temp.setPosicion(arrayPosiciones[i]);
                update(temp);
                usuarios.remove(temp);
            }
        } catch (Exception e) {
            System.out.println("La división = " + division + " no existe en UsuarioDAO.ordenarPosiciones()");
            System.out.println("error = " + e);
        }
    }

//    public void premiarLiga(Batalla batalla) {
//
//        MensajeDAO mensajeDAO
//        Gson gson = new Gson();
//        Usuario usuario;
//
//        int premio = 100000 / batalla.getDivision();
//
//        String res = batalla.getResultados();
//        EstadisticasBatalla estadisticas = gson.fromJson(res, EstadisticasBatalla.class);
//
//        String[] resultado = estadisticas.getResultado().split("-");
//
//        if (Integer.parseInt(resultado[0]) < Integer.parseInt(resultado[1])) {
//            usuario = loadUsuario(batalla.getEquipoLocalId());
//        } else {
//            usuario = loadUsuario(batalla.getEquipoVisitanteId());
//        }
//
//        usuario.setOro(usuario.getOro() + premio);
//        usuario.setEconomia(usuario.getEconomia() + "liga:" + premio + ",");
//        update(usuario);
//    }
    public void copaLiga(int division) {
        Date date = new Date();
        int dias = (int) (date.getTime() / 86400000);

        String peticion = "FROM Usuario WHERE division = " + division + " AND posicion = (SELECT min(posicion) FROM Usuario WHERE division = " + division + ")";
        Usuario usuario = (Usuario) session.createQuery(peticion).uniqueResult();

        Trofeo trofeo = new Trofeo();
        trofeo.setUsuario(usuario);
        trofeo.setTipo("liga");
        trofeo.setRango(division);
        trofeo.setFecha(dias);

        save(trofeo);
    }

    public List<Usuario> getRango(int rangoMin, int rangoMax) {
        return session.createCriteria(Usuario.class).add(Restrictions.ge("posicion", rangoMin)).add(Restrictions.le("posicion", rangoMax)).list();
    }

    public void activarUsuarios() {
        List<Usuario> usuarios = session.createCriteria(Usuario.class).add(Restrictions.eq("activo", 0)).list();
        for (Usuario usuario : usuarios) {
            usuario.setActivo(1);
            update(usuario);
        }
    }

    public String getName(Usuario usuario) {
        if (usuario.getPhpbb_user() != null) {
            return usuario.getPhpbb_user().getUsername();
        } else {
            return "bot_" + usuario.getId();
        }
    }

//    public List<Usuario> getCampeones(int rangoMin, int rangoMax){
//        return session.createCriteria(Usuario.class).add(Restrictions.isNotNull("usuario")).add(Restrictions.ge("posicion", rangoMin)).add(Restrictions.le("posicion", rangoMax)).list();
//    }    
//    public String[] getEconomia(int id, String[] ultimaEconomia) {
//
//        String economia = "";
//        String repetir = "no";
//        boolean manutencion = false;
//
//        for (int i = 0; i < ultimaEconomia.length - 1; i++) {
//            if (ultimaEconomia[i].split(":")[0].equals("manutencion")) {
//                manutencion = true;
//                break;
//            }
//        }
//
//        if (manutencion == false) {
//
//            UsuarioDAO usuarioDAO = new UsuarioDAO(session);
//            CriaturaDAO criaturaDAO = new CriaturaDAO(session);
//            List<Criatura> criaturas = criaturaDAO.getCriaturasUsuario(id);
//
//            int sueldos = 0;
//            for (Criatura criatura : criaturas) {
//                sueldos = sueldos + criatura.getManutencion();
//            }
//
//            Usuario usuario = usuarioDAO.loadUsuario(id);
//            economia = usuario.getEconomia() + "manutencion:" + sueldos + ",";
//            repetir = "si";
//            
//            usuario.setEconomia(economia);
//            usuarioDAO.update(usuario);
//        }
//        return new String[]{economia,repetir};
//    }
}
