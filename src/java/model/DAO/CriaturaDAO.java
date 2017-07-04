package model.DAO;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.*;
import model.hibernate.*;
import model.jsonClass.Alineacion;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class CriaturaDAO {

    private Session session;
    String[] raza = {"humano", "goblin", "muerto"};
    String[] mutacion = {"alas", "puas", "ojos", "cuernos", "escamas", "marca"};
    String[] elemento = {"fuego", "rayo", "hielo", "veneno", "piedra", "naturaleza"};

    public CriaturaDAO(Session session) {
        this.session = session;
    }

    public List<Criatura> obtenerTodasCriaturas() {
        Transaction t = session.beginTransaction();

        String peticion = "FROM Criatura";
        List criaturas = session.createQuery(peticion).list();

        t.commit();
        return criaturas;
    }

    public Criatura loadCriatura(long id) {
        Transaction t = session.beginTransaction();
        Criatura criatura = (Criatura) session.load(Criatura.class, id);
        t.commit();
        return criatura;
    }

    public CriaturaMazmorra loadCriaturaMazmorra(long id) {
        Transaction t = session.beginTransaction();
        CriaturaMazmorra criatura = (CriaturaMazmorra) session.get(CriaturaMazmorra.class, id);
        t.commit();
        return criatura;
    }

    public CriaturaAcademia loadCriaturaAcademia(long id) {
        Transaction t = session.beginTransaction();
        CriaturaAcademia criatura = (CriaturaAcademia) session.load(CriaturaAcademia.class, id);
        t.commit();
        return criatura;
    }

    public CriaturaPrecio loadCriaturaPrecio(long id) {
        Transaction t = session.beginTransaction();
        CriaturaPrecio criatura = (CriaturaPrecio) session.load(CriaturaPrecio.class, id);
        t.commit();
        return criatura;
    }

    public void save(Object object) {
        Transaction t = session.beginTransaction();
        session.save(object);
        t.commit();
    }

    public void update(Object criatura) {
        Transaction t = session.beginTransaction();
        session.update(criatura);
        t.commit();
    }

    public void delete(Object criatura) {
        System.out.println("criaturaDelete = " + criatura);
        Transaction t = session.beginTransaction();
        System.out.println("zzz");
        session.delete(criatura);
        System.out.println("xxx");
        t.commit();
        System.out.println("vvv");
    }

    public List<CriaturaMazmorra> getMazmorrasRango(String peticionAtributos) {
        System.out.println("peticionAtributos = " + peticionAtributos);
        
        //if not creatures
        llenarMazmorras();

        //if empty query -> nothing
        if ("".equals(peticionAtributos)) {
            return new ArrayList();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, 4);
        String peticion = "FROM CriaturaMazmorra WHERE tiempoVenta BETWEEN " + new Date().getTime() / 1000 + " AND " + calendar.getTimeInMillis() / 1000;

        String[] arrayPeticiones = peticionAtributos.split(";");

        for (int i = 0; i < arrayPeticiones.length; i++) {
            String peticionJSON = arrayPeticiones[i];
            if (peticionJSON.isEmpty()) {
                continue;
            }
            String[] arrayPeticion = peticionJSON.split(",");
            peticion = peticion + " AND " + arrayPeticion[0] + " BETWEEN " + arrayPeticion[1] + " AND " + arrayPeticion[2];
        }

        peticion = peticion + " ORDER BY tiempoVenta";
        return session.createQuery(peticion).list();
    }

    public List<CriaturaMazmorra> getMisPujas(int id, List<Long> lista) {
        List<CriaturaMazmorra> criaturas = new ArrayList();
        for (int i = 0; i < lista.size(); i++) {
            String peticion = "FROM CriaturaMazmorra WHERE id = " + lista.get(i);
            try {
                criaturas.add((CriaturaMazmorra) session.createQuery(peticion).uniqueResult());
            } catch (Exception e) {
                lista.remove(lista.get(i));
                UsuarioDAO usuarioDAO = new UsuarioDAO(session);
                Usuario usuario = usuarioDAO.loadUsuario(id);
                Gson gson = new Gson();
                usuario.setPujas(gson.toJson(lista));
                update(usuario);
            }
        }
        return criaturas;
    }

//    public List<Criatura> getCriaturasUsuario(Usuario usuario) {
//        Date date = new Date();
//
//        String peticionPujas = "FROM CriaturaMazmorra WHERE usuario = " + usuario.getId() + " AND tiempoVenta < '" + date.getTime() + "'";
//        List<CriaturaMazmorra> criaturasMazmorra = session.createQuery(peticionPujas).list();
//
//        for (int i = 0; i < criaturasMazmorra.size(); i++) {
//
//            CriaturaMazmorra criatura = criaturasMazmorra.get(i);
//            toCriatura(criatura);
//
//            String[] pujas = criatura.getPujas().split(";");
//            long precio = Long.parseLong(pujas[pujas.length].split(",")[1]);
//
//            try {
//                TraspasoDAO traspasoDAO = new TraspasoDAO(session);
//                Traspaso traspaso = traspasoDAO.loadTraspaso(criatura.getId());
//                traspaso.setFecha(date);
//                traspaso.setComprador(usuario);
//                traspaso.setPrecio(precio);
//                update(traspaso);
//
//            } catch (Exception e) {
//            }
//
//            long precioAnterior = precioCriatura(criatura);
//            long nuevoPrecio = (long) ((precio + precioAnterior) / 2);
//
//            String peticion = criaturaPrecioIgual(criatura);
//            CriaturaPrecio criatPrecio = (CriaturaPrecio) session.createQuery(peticion).uniqueResult();
//            criatPrecio.setPrecio(nuevoPrecio);
//            update(criatPrecio);
//
//
//            String des = criaturaPrecioPeor(criatura) + " AND precio > " + nuevoPrecio;
//            List<CriaturaPrecio> desfasados1 = session.createQuery(des).list();
//            for (CriaturaPrecio desfasado : desfasados1) {
//                desfasado.setPrecio(nuevoPrecio);
//                update(desfasado);
//            }
//
//            String des2 = criaturaPrecioMejor(criatura) + " AND precio < " + nuevoPrecio;
//            List<CriaturaPrecio> desfasados2 = session.createQuery(des2).list();
//            for (CriaturaPrecio desfasado : desfasados2) {
//                desfasado.setPrecio(nuevoPrecio);
//                update(desfasado);
//            }
//        }
//    }
    public List<Criatura> getCriaturasUsuario(int id) {
        String peticion = "FROM Criatura WHERE usuario = " + id;
        List<Criatura> criaturas = session.createQuery(peticion).list();
        return criaturas;
    }

    public List<CriaturaAcademia> getAcademiaUsuario(int usuarioId) {
        String peticion = "FROM CriaturaAcademia WHERE usuario = " + usuarioId;
        List<CriaturaAcademia> lista = session.createQuery(peticion).list();

        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        Usuario usuario = usuarioDAO.loadUsuario(usuarioId);
        if (lista.size() < 10) {
            for (int i = 0; i < 10 - lista.size(); i++) {
                crearCriaturaAcademia(usuario);
            }
            lista = session.createQuery(peticion).list();
        }
        return lista;
    }

//    public List<Criatura> getCriaturasUsuario(String nombreId) {
//        String peticion = "FROM Usuario WHERE nombreId='" + nombreId + "'";
//        Usuario usuario = (Usuario) session.createQuery(peticion).uniqueResult();
//        return getCriaturasUsuario(usuario);
//    }
    public String[] pujaCriaturaMazmorra(long id) {

        CriaturaMazmorra criatura = loadCriaturaMazmorra(id);

        if (null == criatura) {
            System.out.println("wrong creature id for pujaCriaturaMazmorra()");
            return null;
        }

        String pujas = criatura.getPujas();
        String[] arrayPujas = pujas.split(";");
        String comprador = arrayPujas[arrayPujas.length - 1];
        String[] arrayComprador = comprador.split(",");

        return arrayComprador;
    }

    public void crearCriatura(Usuario usuario) {

        Criaturas criatura = new Criatura();
        criatura = randomCriatura(criatura, 15, 20, 5);
        criatura = setEvol(criatura);
        if (Math.random() * 4 < 3) {
            criatura.setElemento(null);
        }
        if (Math.random() * 4 < 3) {
            criatura.setMutacion(null);
        }
        long precio = precioCriatura(criatura);

        criatura.setPrecio(precio);
        criatura.setUsuario(usuario);
        save(criatura);
    }

    public void crearCriatura(Integer date) {

        CriaturaMazmorra criatura = (CriaturaMazmorra) crearCriaturaMazmorra();
        criatura.setTiempoVenta(date);

        save((CriaturaMazmorra) criatura);
    }

    public Criaturas crearCriaturaMazmorra() {

        Criaturas criatura = new CriaturaMazmorra();
        criatura = randomCriatura(criatura, 20, 20, 5);
        criatura = setEvol(criatura);
        if (Math.random() * 4 < 3) {
            criatura.setElemento(null);
        }
        if (Math.random() * 4 < 3) {
            criatura.setMutacion(null);
        }
        long precio = (long) (precioCriatura(criatura) * 0.8);

        if (precio < 50) {
            precio = 50;
        }

        CriaturaMazmorra criatMazmorra = (CriaturaMazmorra) criatura;
        criatMazmorra.setPujas("0," + precio + ";");

        return criatMazmorra;
    }

    public void crearCriaturaAcademia(Usuario usuario) {
        CriaturaAcademia criatura = new CriaturaAcademia();
        criatura = (CriaturaAcademia) randomCriatura(criatura, 25, 17, 1);
        criatura = (CriaturaAcademia) setEvol(criatura, 2);
        criatura.setClases("");
        criatura.setUsuario(usuario);
        save(criatura);
    }

    public void eliminarCriaturas(Usuario usuario) {
        session.createQuery("delete FROM Criatura WHERE usuario = " + usuario.getId()).executeUpdate();
        session.createQuery("delete FROM CriaturaAcademia WHERE usuario = " + usuario.getId()).executeUpdate();
    }

    public Criaturas randomCriatura(Criaturas criatura, int num, int edadInicial, int max) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -edadInicial);
        calendar.add(Calendar.DAY_OF_WEEK, -(int) (Math.random() * max * 30));

        criatura.setAspecto((int) (Math.random() * 3) + ";" + (int) (Math.random() * 3) + ";" + (int) (Math.random() * 3) + ";" + (int) (Math.random() * 3) + ";" + (int) (Math.random() * 3) + ";" + (int) (Math.random() * 3) + ";" + (int) (Math.random() * 3));

        criatura.setSexo(1);

        int razaInt = (int) (Math.random() * raza.length);
        criatura.setRaza(raza[razaInt]);
        criatura.setNombre(nombre(razaInt));

        criatura.setMutacion(mutacion[(int) (Math.random() * mutacion.length)]);
        criatura.setElemento(elemento[(int) (Math.random() * elemento.length)]);

        double fuerza = (int) (num - Math.sqrt(Math.random() * Math.pow(num, 2))) + 1.9999;
        criatura.setFuerza(fuerza);
        double magia = (int) (num - Math.sqrt(Math.random() * Math.pow(num, 2))) + 1.9999;
        criatura.setMagia(magia);
        double agilidad = (int) (num - Math.sqrt(Math.random() * Math.pow(num, 2))) + 1.9999;
        criatura.setAgilidad(agilidad);
        double reflejos = (int) (num - Math.sqrt(Math.random() * Math.pow(num, 2))) + 1.9999;
        criatura.setReflejos(reflejos);
        double constitucion = (int) (num - Math.sqrt(Math.random() * Math.pow(num, 2))) + 1.9999;
        criatura.setConstitucion(constitucion);
        double defensa = (int) (num - Math.sqrt(Math.random() * Math.pow(num, 2))) + 1.9999;
        criatura.setDefensa(defensa);
        double reaccion = (int) (num - Math.sqrt(Math.random() * Math.pow(num, 2))) + 1.9999;
        criatura.setReaccion(reaccion);

        int media = (int) fuerza + (int) magia + (int) agilidad + (int) reflejos + (int) constitucion + (int) defensa + (int) reaccion;
        criatura.setMedia(media);

        double xp = 0;
        criatura.setXp(xp);
        criatura.setEdad(calendar.getTimeInMillis());

        String clase;
        if (fuerza * 1.2 > magia) {
            if (constitucion * 1.2 > fuerza) {
                clase = "barbaro";
            } else {
                clase = "arquero";
            }
        } else {
            clase = "mago";
        }

        criatura.setClases(clase + ":100;");
        return criatura;
    }

    public String nombre(int num) {
        //http://www.wizards.com
        String[] nombres;
        String[] apellidos;

        switch (num) {
            case 0: //humano
                nombres = new String[]{"Aquiles", "Hector", "Hércules", "Heracles", "Jasón", "Teseo", "Filemón", "Orfeo", "Ulises", "Agamenón", "Milciades", "Temístocles", "Leónidas", "Pausanias", "Minos"};
                apellidos = new String[]{"Tigersoul", "Shieldheart", "Droverson", "Shieldheart", "Arroway", "Pegason", "Wyvernjack", "Swordhand", "Chorster", "Urthadar"};

                break;
            case 1: //goblin
                nombres = new String[]{"Ricendithas", "Zanhorn", "Ricfire", "Jamroar", "Yenkas", "Wallannan", "Xavtumal", "Jamdak", "Vicril", "Norroar", "Shalannan", "Norlamin", "Petaver", "Thoice", "Frulamin"};
                apellidos = new String[]{"Laughshield", "Goblinsfoe", "Gladdenstone", "Homeforger", "Lightouch", "Beestinger", "Goldweaver", "Shortankard", "Alerteyes", "Mongothsbeard", "Trickfoot", "Cupshigh"};

                break;
            case 2: //muerto
            default:
                nombres = new String[]{"Keveak", "Petgretor", "Frunan", "Otirry", "Seaward", "Elneiros", "Seanan", "Zanice", "Xavkul", "Cruamros", "Norril", "Walnan", "Marroar", "Thokul", "Minos", "Adoril"};
                apellidos = new String[]{"Wolfswift", "Falconsflight", "Spelloyal", "Dragonsbane", "Loyalar", "Waveharp", "Droverson", "Milner", "Spelloyal"};

        }

        String nombre = nombres[(int) (nombres.length * Math.random())];
        String apellido = apellidos[(int) (apellidos.length * Math.random())];
//        int nombre = (int) (100 * Math.random());
//        int apellido = (int) (100 * Math.random());

        return nombre + " " + apellido;
    }

    public List<Criaturas> guardarEquipo(String tipo, Usuario usuario) {

        List<Criaturas> criaturas = new ArrayList();

        String peticionEquipo;
        List<Criaturas> equipo;
        String alineacionString;

        if (!tipo.equals("academia")) {
            peticionEquipo = "FROM Criatura WHERE usuario = " + usuario.getId();
            equipo = session.createQuery(peticionEquipo).list();
            alineacionString = usuario.getAlin();
        } else {
            peticionEquipo = "FROM CriaturaAcademia WHERE usuario = " + usuario.getId();
            equipo = session.createQuery(peticionEquipo).list();

            if (equipo.isEmpty()) {
                for (int i = 0; i < 10; i++) {
                    crearCriaturaAcademia(usuario);
                }
                peticionEquipo = "FROM CriaturaAcademia WHERE usuario = " + usuario.getId();
                equipo = session.createQuery(peticionEquipo).list();
            }
            alineacionString = usuario.getAlinAcad();
        }

        Gson gson = new Gson();
        List<Alineacion> alineacion = new ArrayList();

        if (alineacionString != null && !alineacionString.equals("")) {
            try {
                alineacion = gson.fromJson(alineacionString, new TypeToken<List<Alineacion>>() {
                }.getType());
            } catch (Exception e) {
            }
        }

        long[] ids = new long[5];
        int[] posiciones = {-1, -1, -1, -1, -1};

        long id;
        int posicion;
        String actitud;
        String clase;
        for (int i = 0; i < alineacion.size(); i++) {
            id = alineacion.get(i).getId();
            ids[i] = id;
            posicion = alineacion.get(i).getPosicion();
            posiciones[i] = posicion;
            clase = alineacion.get(i).getClase();
            actitud = alineacion.get(i).getActitud();

            try {
                criaturas.add(criaturasBatalla(tipo, id, posicion, actitud, clase));

            } catch (Exception e) {
                System.out.println("La criatura de alineacion ya no es del propietario? = " + e + " (CriaturaDAO.java)");
            }
        }

        // INICIO DE ASIGNACION ALINEACION AUTOMATICO
        List<Long> idList = new ArrayList<Long>();
        List<Integer> posicionList = new ArrayList<Integer>();

        for (int i = 0; i < ids.length; i++) {
            idList.add(ids[i]);
        }
        for (int i = 0; i < posiciones.length; i++) {
            posicionList.add(posiciones[i]);
        }

        int contador;
        boolean asignado;
        boolean posicionVacia;

        while (criaturas.size() < 5) {

            asignado = false;
            id = 0;
            posicion = 0;

            for (int j = 0; j < 15; j++) { //asignar posicion
                posicionVacia = true;
                for (int p = 0; p < posicionList.size(); p++) {
                    if (posicionList.get(p) == j) {
                        posicionVacia = false;
                        p = posicionList.size();
                    }
                }
                if (posicionVacia) {
                    posicion = j;
                    break;
                }
            }

            contador = 0;
            while (asignado == false) {

                id = equipo.get(contador).getId();
                asignado = true;

                for (int num = 0; num < idList.size(); num++) {
                    if (id == idList.get(num)) {
                        asignado = false;
                        break;
                    }
                }
                contador++;
                if (contador == equipo.size()) {
                    break;
                }
            }
            if (asignado) {
                posicionList.add(posicion);
                idList.add(id);
                String claseFuerte;
                try {
                    claseFuerte = equipo.get(contador).getClases().split(";")[0].split(":")[0];
                    if (claseFuerte.equals("")) {
                        claseFuerte = "barbaro";
                    }
                } catch (Exception e) {
                    claseFuerte = "barbaro";
                }
                criaturas.add(criaturasBatalla(tipo, id, posicion, "moderado", claseFuerte));
            } else {
                break;
            }
        }

        for (Criaturas criatura : criaturas) {
            criatura.setUsuario(null);
        }
        return criaturas;
    }

    public Criaturas criaturasBatalla(String tipo, long id, int posicion, String actitud, String clase) {
        System.out.println("111");
        String peticionCriatura;
        if (!tipo.equals("academia")) {
            peticionCriatura = "FROM Criatura WHERE id = " + id;
        } else {
            peticionCriatura = "FROM CriaturaAcademia WHERE id = " + id;
        }

        Criaturas criatura = (Criaturas) session.createQuery(peticionCriatura).uniqueResult();

        criatura.setClases(xpClases(criatura.getClases(), clase));

        criatura.setXp(criatura.getXp() + 0.3);
        criatura.setFrescura(criatura.getFrescura() - 5);
        criatura.setBatallas(criatura.getBatallas() + 1);
//        update(criatura);

        session.evict(criatura);
        System.out.println("222");
        return convertir(criatura, posicion, actitud, clase);
    }

    public String xpClases(String clasesString, String clase) {

        String[] clases = {};
        String nuevasClases = "";
        boolean nuevo = true;

        if (clasesString != null && !clasesString.equals("")) {
            clases = clasesString.split(";");

            //cambiar clases
            for (int i = 0; i < clases.length; i++) {
                String clasePorcentaje = clases[i].split(":")[0];
                int porcentaje = Integer.parseInt(clases[i].split(":")[1]);

                if (clase.equals(clasePorcentaje)) {
                    porcentaje = porcentaje + 10;
                    if (porcentaje > 100) {
                        porcentaje = 100;
                    }
                    nuevo = false;
                } else {
                    porcentaje = porcentaje - 10 / clases.length;
                    if (porcentaje < 0) {
                        porcentaje = 0;
                    }
                }

                clases[i] = clasePorcentaje + ":" + porcentaje;
            }
        }

        if (nuevo) {
            clases = push(clases, clase + ":10;");
        } else {
            //ordenar clases        
            for (int i = 0; i < clases.length; i++) {
                for (int j = 0; j < clases.length; j++) {
                    if (Integer.parseInt(clases[i].split(":")[1]) > Integer.parseInt(clases[j].split(":")[1])) {
                        String nexo = clases[i];
                        clases[i] = clases[j];
                        clases[j] = nexo;
                    }
                }
            }
        }

        //montar string
        for (int i = 0; i < clases.length; i++) {
            nuevasClases = nuevasClases + clases[i] + ";";
        }

        return nuevasClases;
    }

    private static String[] push(String[] array, String push) {
        String[] longer = new String[array.length + 1];
        System.arraycopy(array, 0, longer, 0, array.length);
        longer[array.length] = push;
        return longer;
    }

    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }
            if (Character.digit(s.charAt(i), radix) < 0) {
                return false;
            }
        }
        return true;
    }

    public long[] toLong(String[] num) {

        long[] lista;
        lista = new long[6];

        for (int i = 0; i < 6; i++) {
            if (isInteger(num[i])) {
                lista[i] = Long.parseLong(num[i]);
            } else {
                lista[i] = 0;
            }
        }
        return lista;
    }

    public Criaturas convertir(Criaturas criatura, int posicion, String actitud, String clase) {

        int x = (int) (posicion / 5);
        int y = posicion - (x * 5);

        criatura.setFuerza((double) Math.round(criatura.getFuerza()));
        criatura.setMagia((double) Math.round(criatura.getMagia()));
        criatura.setAgilidad((double) Math.round(criatura.getAgilidad()));
        criatura.setReflejos((double) Math.round(criatura.getReflejos()));
        criatura.setConstitucion((double) Math.round(criatura.getConstitucion()));
        criatura.setDefensa((double) Math.round(criatura.getDefensa()));
        criatura.setReaccion((double) Math.round(criatura.getReaccion()));
        criatura.setXp((double) Math.round(criatura.getXp()));
        criatura.setEvolucion(null);
        criatura.setPrecio(null);

        criatura.actitud = actitud;
        criatura.clase = clase;
        criatura.x = x + "";
        criatura.y = y + "";

        return criatura;
    }

    public void comprobarEntreno(int editorId) {
        Date date = new Date();
        long time = date.getTime();
        int dias = (int) (time / 86400000);

        TiempoDAO tiempoDAO = new TiempoDAO(session);

        int fechaEntreno = tiempoDAO.loadTiempo("entreno").getDia();

        if (fechaEntreno < dias - 7) {
            Tiempo tiempo = tiempoDAO.loadTiempo("entreno");

            if (!tiempo.getEstado().equals("reservado")) {

                tiempo.setEstado("reservado");
                tiempo.setEditorId(editorId);
                tiempoDAO.update(tiempo);

                CriaturaDAO criaturaDAO = new CriaturaDAO(session);
                criaturaDAO.entrenar(editorId);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, 2);

                Date aaa = calendar.getTime();
                int dia = (int) (aaa.getTime() / 86400000);

                tiempo.setEstado("");
                tiempo.setDia(dia);
                tiempoDAO.update(tiempo);
            }
        }
    }

    public void entrenar(int editorId) {
        TiempoDAO tiempoDAO = new TiempoDAO(session);
        Tiempo tiempo = tiempoDAO.loadTiempo("entreno");

        if (tiempo.getEditorId() == editorId) {
            entreno();
            //llenarMazmorras();
        }
    }

    public double valEntreno(double num) {
        if (num > 100) {
            num = 100;
        } else if (num < 0) {
            num = 0;
        }
        return num;
    }

    public void entreno() {
        System.out.println("entreno()");

        Gson gson = new Gson();
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        List<Usuario> usuarios = usuarioDAO.getUsuariosActivos();

        for (Usuario usuario : usuarios) {
            System.out.println("entreno() en usuario " + usuario.getId());
            List<Criatura> criaturas = getCriaturasUsuario(usuario.getId());

            Entreno entreno;

            try {
                entreno = gson.fromJson(usuario.getEntreno(), Entreno.class);

                for (Criatura rs : criaturas) {

                    int edad = getEdad(rs.getEdad());

                    double entrenos[] = new double[7];
                    String clase = getClase(rs.getClases());

                    if (clase.equals("barbaro")) {
                        entrenos[0] = 2.5;
                        entrenos[1] = 1;
                        entrenos[2] = 1.5;
                        entrenos[3] = 1;
                        entrenos[4] = 2;
                        entrenos[5] = 0.5;
                        entrenos[6] = 1.5;
                    } else if (clase.equals("arquero")) {
                        entrenos[0] = 2;
                        entrenos[1] = 1;
                        entrenos[2] = 2.5;
                        entrenos[3] = 1.5;
                        entrenos[4] = 0.5;
                        entrenos[5] = 1;
                        entrenos[6] = 1.5;
                    } else if (clase.equals("mago")) {
                        entrenos[0] = 1;
                        entrenos[1] = 2.5;
                        entrenos[2] = 2;
                        entrenos[3] = 0.5;
                        entrenos[4] = 1;
                        entrenos[5] = 1.5;
                        entrenos[6] = 1.5;
                    } else if (clase.equals("paladin")) {
                        entrenos[0] = 1;
                        entrenos[1] = 1.5;
                        entrenos[2] = 0.5;
                        entrenos[3] = 2;
                        entrenos[4] = 2.5;
                        entrenos[5] = 1;
                        entrenos[6] = 1.5;
                    } else if (clase.equals("shinobi")) {
                        entrenos[0] = 1.5;
                        entrenos[1] = 0.5;
                        entrenos[2] = 1;
                        entrenos[3] = 2.5;
                        entrenos[4] = 1;
                        entrenos[5] = 2;
                        entrenos[6] = 1.5;
                    } else if (clase.equals("healer")) {
                        entrenos[0] = 0.5;
                        entrenos[1] = 2;
                        entrenos[2] = 1;
                        entrenos[3] = 1;
                        entrenos[4] = 1.5;
                        entrenos[5] = 2.5;
                        entrenos[6] = 1.5;
                    } else {
                        System.out.println("error con el nombre de clase = " + clase);
                    }

                    double fuerza = valEntreno(rs.getFuerza() + entreno.fuerza * (500.0 / (edad * edad)) * entrenos[0] / 10.0);
                    double magia = valEntreno(rs.getMagia() + entreno.magia * (500.0 / (edad * edad)) * entrenos[1] / 10.0);
                    double agilidad = valEntreno(rs.getAgilidad() + entreno.agilidad * (500.0 / (edad * edad)) * entrenos[2] / 10.0);
                    double reflejos = valEntreno(rs.getReflejos() + entreno.reflejos * (500.0 / (edad * edad)) * entrenos[3] / 10.0);
                    double constitucion = valEntreno(rs.getConstitucion() + entreno.constitucion * (500.0 / (edad * edad)) * entrenos[4] / 10.0);
                    double defensa = valEntreno(rs.getDefensa() + entreno.defensa * (500.0 / (edad * edad)) * entrenos[5] / 10.0);
                    double reaccion = valEntreno(rs.getReaccion() + entreno.reaccion * (500.0 / (edad * edad)) * entrenos[6] / 10.0);
//                    int frescura = (int) valEntreno(rs.getFrescura() + (((double) entreno.frescura * 3)));
//                    int moral = (int) valEntreno(rs.getMoral() + (((double) entreno.moral * 3)));

                    long precio = precioCriatura(rs);
                    rs.setPrecio(precio);

                    rs.setFuerza(fuerza);
                    rs.setMagia(magia);
                    rs.setAgilidad(agilidad);
                    rs.setReflejos(reflejos);
                    rs.setConstitucion(constitucion);
                    rs.setDefensa(defensa);
                    rs.setReaccion(reaccion);
                    rs.setMedia((int) fuerza + (int) magia + (int) agilidad + (int) reflejos + (int) constitucion + (int) defensa + (int) reaccion);
//                    rs.setFrescura(frescura);
//                    rs.setMoral(moral);

                    rs = (Criatura) setEvol(rs);

                    update(rs);

                }
            } catch (Exception e) {
                System.out.println("ENTRENAMIENTO ERRONEO = " + e);
                entreno = new Entreno();
                usuario.setEntreno(gson.toJson(entreno));
                update(usuario);
            }
        }
    }

    public String getClase(String clases) {
        try {
            return clases.split(";")[0].split(":")[0];
        } catch (Exception e) {
            return "barbaro";
        }
    }

    public Integer getNivel(Double xp) {
        return (int) Math.sqrt(xp);
    }

    public Criaturas setEvol(Criaturas criatura) {

        Gson gson = new Gson();
        List<Evolucion> evolucion;
        try {
            evolucion = gson.fromJson(criatura.getEvolucion(), new TypeToken<List<Evolucion>>() {
            }.getType());
            evolucion.remove(0);
        } catch (Exception e) {
            evolucion = new ArrayList<Evolucion>();
            for (int i = 0; i < 10; i++) {
                evolucion.add(new Evolucion());
            }
        }

        return evol(criatura, evolucion);
    }

    public Criaturas setEvol(Criaturas criatura, int num) {
        Gson gson = new Gson();

        List<Evolucion> evolucion;
        try {
            evolucion = gson.fromJson(criatura.getEvolucion(), new TypeToken<List<Evolucion>>() {
            }.getType());
            evolucion.remove(0);
        } catch (Exception e) {
            evolucion = new ArrayList<Evolucion>();
            for (int i = 0; i < num; i++) {
                evolucion.add(new Evolucion());
            }
        }

        return evol(criatura, evolucion);
    }

    public Criaturas evol(Criaturas criatura, List<Evolucion> evolucion) {
        Gson gson = new Gson();

        Evolucion evol = new Evolucion();
        evol.fu = (int) (double) criatura.getFuerza();
        evol.ma = (int) (double) criatura.getMagia();
        evol.ag = (int) (double) criatura.getAgilidad();
        evol.rf = (int) (double) criatura.getReflejos();
        evol.co = (int) (double) criatura.getConstitucion();
        evol.df = (int) (double) criatura.getDefensa();
        evol.rc = (int) (double) criatura.getReaccion();
        evol.fr = (int) criatura.getFrescura();
        evol.mo = (int) criatura.getMoral();
        evol.xp = (int) (double) criatura.getXp();

        evolucion.add(evol);
        criatura.setEvolucion(gson.toJson(evolucion));
        return criatura;
    }

    public void saveEvol() {

        String peticion = "FROM Criatura WHERE evolucion IS NULL";
        List<Criatura> criaturas = session.createQuery(peticion).list();
        for (Criatura criatura : criaturas) {
            criatura = (Criatura) setEvol(criatura);
            update(criatura);
        }

        String peticionA = "FROM CriaturaAcademia WHERE evolucion IS NULL";
        List<CriaturaAcademia> criaturasA = session.createQuery(peticionA).list();
        for (CriaturaAcademia criatura : criaturasA) {
            criatura = (CriaturaAcademia) setEvol(criatura);
            update(criatura);
        }
    }

    public void updateEvol() {

        String peticion = "FROM Criatura";
        List<Criatura> criaturas = session.createQuery(peticion).list();
        for (Criatura criatura : criaturas) {
            criatura.setEvolucion("");
            criatura = (Criatura) setEvol(criatura);
            update(criatura);
        }

        String peticionA = "FROM CriaturaAcademia";
        List<CriaturaAcademia> criaturasA = session.createQuery(peticionA).list();
        for (CriaturaAcademia criatura : criaturasA) {
            criatura.setEvolucion("");
            criatura = (CriaturaAcademia) setEvol(criatura);
            update(criatura);
        }
    }

    public long precioCriatura(long id) {
        Criaturas criatura = (Criaturas) loadCriatura(id);
        return precioCriatura(criatura);
    }

    public long precioCriatura(Criaturas criatura) {

        long precio = 0;

        String peticion = "SELECT precio " + criaturaPrecioIgual(criatura);

        if (session.createQuery(peticion).setMaxResults(1).uniqueResult() != null) {
            precio = (Long) session.createQuery(peticion).uniqueResult();

        } else {

            String peticion2 = "SELECT max(precio) " + criaturaPrecioPeor(criatura);
            Object obj = session.createQuery(peticion2).uniqueResult();

            if (obj != null) {
                precio = (Long) obj;
            }
            if (precio < 50) {
                precio = 50;
            }

            CriaturaPrecio criatPrecio = new CriaturaPrecio();

            int edad = getEdad(criatura.getEdad());

            criatPrecio.setEdad(edad);
            criatPrecio.setSexo(criatura.getSexo());
            criatPrecio.setRaza(criatura.getRaza());
            criatPrecio.setElemento(criatura.getElemento());
            criatPrecio.setMutacion(criatura.getMutacion());

            criatPrecio.setFuerza((int) (criatura.getFuerza() / 5));
            criatPrecio.setMagia((int) (criatura.getMagia() / 5));
            criatPrecio.setAgilidad((int) (criatura.getAgilidad() / 5));
            criatPrecio.setReflejos((int) (criatura.getReflejos() / 5));
            criatPrecio.setConstitucion((int) (criatura.getConstitucion() / 5));
            criatPrecio.setDefensa((int) (criatura.getDefensa() / 5));
            criatPrecio.setReaccion((int) (criatura.getReaccion() / 5));
            criatPrecio.setXp((int) (criatura.getXp() / 5));

            criatPrecio.setPrecio(precio);
            criatPrecio.setTiempoVenta(new Date());

            save(criatPrecio);
        }

        return precio;
    }

    public int getEdad(Long edad) {
        Calendar now = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(edad);

        int meses = 0;
        cal.add(Calendar.MONTH, 1);
        while (cal.getTimeInMillis() < now.getTimeInMillis()) {
            meses++;
            cal.add(Calendar.MONTH, 1);
        }
        return meses;
    }

    public String criaturaPrecioIgual(Criaturas criatura) {
        return "FROM CriaturaPrecio WHERE sexo = " + criatura.getSexo() + " AND raza = '" + criatura.getRaza() + "' AND elemento = '" + criatura.getElemento()
                + "' AND mutacion = '" + criatura.getMutacion() + "' AND fuerza = " + (int) (criatura.getFuerza() / 5) + " AND magia = " + (int) (criatura.getMagia() / 5)
                + " AND agilidad = " + (int) (criatura.getAgilidad() / 5) + " AND reflejos = " + (int) (criatura.getReflejos() / 5) + " AND constitucion = " + (int) (criatura.getConstitucion() / 5)
                + " AND defensa = " + (int) (criatura.getDefensa() / 5) + " AND reaccion = " + (int) (criatura.getReaccion() / 5) + " AND xp = " + (int) (criatura.getXp() / 5);
//                + " AND edad = " + (int) (criatura.getEdad() / 30);
    }

    public String criaturaPrecioMejor(Criaturas criatura) {
        return "FROM CriaturaPrecio WHERE sexo = " + criatura.getSexo() + " AND raza = '" + criatura.getRaza() + "' AND elemento = '" + criatura.getElemento()
                + "' AND mutacion = '" + criatura.getMutacion() + "' AND fuerza >= " + (int) (criatura.getFuerza() / 5) + " AND magia >= " + (int) (criatura.getMagia() / 5)
                + " AND agilidad >= " + (int) (criatura.getAgilidad() / 5) + " AND reflejos >= " + (int) (criatura.getReflejos() / 5) + " AND constitucion >= " + (int) (criatura.getConstitucion() / 5)
                + " AND defensa >= " + (int) (criatura.getDefensa() / 5) + " AND reaccion >= " + (int) (criatura.getReaccion() / 5) + " AND xp >= " + (int) (criatura.getXp() / 5);
//                + " AND edad <= " + (int) (criatura.getEdad() / 30);
    }

    public String criaturaPrecioPeor(Criaturas criatura) {
        return "FROM CriaturaPrecio WHERE sexo = " + criatura.getSexo() + " AND raza = '" + criatura.getRaza() + "' AND elemento = '" + criatura.getElemento()
                + "' AND mutacion = '" + criatura.getMutacion() + "' AND fuerza <= " + (int) (criatura.getFuerza() / 5) + " AND magia <= " + (int) (criatura.getMagia() / 5)
                + " AND agilidad <= " + (int) (criatura.getAgilidad() / 5) + " AND reflejos <= " + (int) (criatura.getReflejos() / 5) + " AND constitucion <= " + (int) (criatura.getConstitucion() / 5)
                + " AND defensa <= " + (int) (criatura.getDefensa() / 5) + " AND reaccion <= " + (int) (criatura.getReaccion() / 5) + " AND xp <= " + (int) (criatura.getXp() / 5);
//                + " AND edad >= " + (int) (criatura.getEdad() / 30);
    }

    public void llenarMazmorras() {

//        String qUsuarios = "SELECT count(*) FROM Usuario WHERE activo = 1";
//        int numUsuarios = ((Number) session.createQuery(qUsuarios).uniqueResult()).intValue();
//        if (numUsuarios < 100) {
//        numUsuarios = 100;
//        }
        //ALLWAYS MIN 100 CREATURES SELLING
        String qCriaturas = "SELECT count(*) FROM CriaturaMazmorra WHERE tiempoVenta > " + new Date().getTime() / 1000;
        int numCriaturas = ((Number) session.createQuery(qCriaturas).uniqueResult()).intValue();

        if (numCriaturas > 90) {
            return;
        }

        int create = 100 - numCriaturas;

        int intervalo = 604800 / create;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, intervalo);

        for (int i = 0; i < create; i++) {
            crearCriatura((int) (calendar.getTimeInMillis() / 1000));
        }

        //LIMPIEZA:
        //
        //SELECCIONAR CRIATURAS QUE NO HAN SIDO COMPRADAS
        String peticionEliminar = "FROM CriaturaMazmorra WHERE usuario IS NULL AND tiempoVenta < " + new Date().getTime() / 1000;
        List<CriaturaMazmorra> criaturas = session.createQuery(peticionEliminar).list();

        //REDUCIR PRECIO CRIATURAS PARA SIGUIENTE VUELTA
        for (CriaturaMazmorra criatura : criaturas) {

            String[] pujas = criatura.getPujas().split(";");
            long precio = Long.parseLong(pujas[pujas.length].split(",")[1]);

            String peores = criaturaPrecioPeor(criatura) + " AND precioMax > " + precio;

            if (session.createQuery(peores).list() != null) {
                List<CriaturaPrecio> criaturasGuardar = session.createQuery(peores).list();

                for (int i = 0; i < criaturasGuardar.size(); i++) {

                    CriaturaPrecio criaturaPrecio = criaturasGuardar.get(i);
                    criaturaPrecio.setPrecio(precio);
                    update(criaturaPrecio);

                    //LIMPIAR SESION CADA 50 CRIATURAS CAMBIADAS
                    if (i % 50 == 0) {
                        session.flush();
                        session.clear();
                    }
                }
            }

            delete(criatura);
        }
    }

    public int numCriaturas() {
        return ((Number) session.createCriteria(Criatura.class).add(Restrictions.isNotNull("usuario")).add(Restrictions.eq("tiempoVenta", 0)).setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

    public List<Criatura> getRango(int min, int max) {
        return session.createCriteria(Criatura.class).add(Restrictions.isNotNull("usuario")).add(Restrictions.eq("tiempoVenta", 0)).addOrder(Order.desc("media")).setFirstResult(min).setMaxResults(max - min).list();
    }

    public void asignarMuertes(String tipo, String muertes) {

        String[] arrayMuertes = muertes.split(",");

        if (arrayMuertes.length > 1) {
            for (int i = 0; i < arrayMuertes.length - 1; i++) {

                String[] muerte = arrayMuertes[i].split(":");

                Criaturas asesino, victima;

                if (!tipo.equals("academia")) {
                    asesino = (Criaturas) loadCriatura(Long.parseLong(muerte[0]));
                    victima = (Criaturas) loadCriatura(Long.parseLong(muerte[1]));
                } else {
                    asesino = (Criaturas) loadCriaturaAcademia(Long.parseLong(muerte[0]));
                    victima = (Criaturas) loadCriaturaAcademia(Long.parseLong(muerte[1]));
                }

                asesino.setBajas(asesino.getBajas() + 1);
                update(asesino);

                victima.setMuertes(victima.getMuertes() + 1);
                update(victima);
            }
        }
    }

    public void destapar(List<Alineacion> alineacionLocal, List<Alineacion> alineacionVisitante) {
        if (null != alineacionLocal) {
            for (Alineacion local : alineacionLocal) {
                CriaturaAcademia criatura = (CriaturaAcademia) loadCriaturaAcademia(local.getId());
                destaparValorCriatura(criatura);
            }
        }
        if (null != alineacionVisitante) {
            for (Alineacion visitante : alineacionVisitante) {
                CriaturaAcademia criatura = (CriaturaAcademia) loadCriaturaAcademia(visitante.getId());
                destaparValorCriatura(criatura);
            }
        }
    }

    public void destaparValorCriatura(CriaturaAcademia criatura) {
        Gson gson = new Gson();
        Destapes destapes;

        destapes = gson.fromJson(criatura.getDestapes(), Destapes.class);
        if (destapes == null) {
            destapes = new Destapes();
            System.out.println("destapes.fu = " + destapes.fu);
        }

        List<String> ocultos = new ArrayList();
        int des = 0;

        if (null != destapes.fu && destapes.fu == 0) {
            des = destapes.desFu;
            ocultos.add("fu");
        }
        if (null != destapes.ma && destapes.ma == 0) {
            if (destapes.desMa > des) {
                ocultos = new ArrayList();
                des = destapes.desMa;
            }
            if (destapes.desMa == des) {
                ocultos.add("ma");
            }
        }
        if (null != destapes.ag && destapes.ag == 0) {
            if (destapes.desAg > des) {
                ocultos = new ArrayList();
                des = destapes.desAg;
            }
            if (destapes.desAg == des) {
                ocultos.add("ag");
            }
        }
        if (null != destapes.rf && destapes.rf == 0) {
            if (destapes.desRf > des) {
                ocultos = new ArrayList();
                des = destapes.desRf;
            }
            if (destapes.desRf == des) {
                ocultos.add("rf");
            }
        }
        if (null != destapes.co && destapes.co == 0) {
            if (destapes.desCo > des) {
                ocultos = new ArrayList();
                des = destapes.desCo;
            }
            if (destapes.desCo == des) {
                ocultos.add("co");
            }
        }
        if (null != destapes.df && destapes.df == 0) {
            if (destapes.desDf > des) {
                ocultos = new ArrayList();
                des = destapes.desDf;
            }
            if (destapes.desDf == des) {
                ocultos.add("df");
            }
        }
        if (null != destapes.rc && destapes.rc == 0) {
            if (destapes.desRc > des) {
                ocultos = new ArrayList();
                des = destapes.desRc;
            }
            if (destapes.desRc == des) {
                ocultos.add("rc");
            }
        }
        if (null != destapes.mut && destapes.mut.equals("")) {
            if (destapes.desMut > des) {
                ocultos = new ArrayList();
                des = destapes.desMut;
            }
            if (destapes.desMut == des) {
                ocultos.add("mut");
            }
        }
        if (null != destapes.ele && destapes.ele.equals("")) {
            if (destapes.desEle > des) {
                ocultos = new ArrayList();
                des = destapes.desEle;
            }
            if (destapes.desEle == des) {
                ocultos.add("ele");
            }
        }

        if (ocultos.size() > 0) {
            String oculto = ocultos.get((int) (Math.random() * ocultos.size()));
            destapes.ultima = oculto;

            if (oculto.equals("fu")) {
                destapes.fu = calculoDestape(criatura.getFuerza(), destapes.desFu);
            } else if (oculto.equals("ma")) {
                destapes.ma = calculoDestape(criatura.getMagia(), destapes.desMa);
            } else if (oculto.equals("ag")) {
                destapes.ag = calculoDestape(criatura.getAgilidad(), destapes.desAg);
            } else if (oculto.equals("rf")) {
                destapes.rf = calculoDestape(criatura.getReflejos(), destapes.desRf);
            } else if (oculto.equals("co")) {
                destapes.co = calculoDestape(criatura.getConstitucion(), destapes.desCo);
            } else if (oculto.equals("df")) {
                destapes.df = calculoDestape(criatura.getDefensa(), destapes.desDf);
            } else if (oculto.equals("rc")) {
                destapes.rc = calculoDestape(criatura.getReaccion(), destapes.desRc);
            } else if (oculto.equals("mut")) {
                if (Math.random() * 4 >= 3) {
                    destapes.mut = criatura.getMutacion();
                } else {
                    destapes.mut = "none";
                }
            } else if (oculto.equals("ele")) {
                if (Math.random() * 4 >= 3) {
                    destapes.ele = criatura.getElemento();
                } else {
                    destapes.ele = "none";
                }
            }

            criatura.setDestapes(gson.toJson(destapes));
            update(criatura);
        }
    }

    public int calculoDestape(double atr, int des) {
        return (int) (Math.random() * atr / (1 + 1 / (des + 1))) + 1;
    }

    public Destapes newDestapes() {
        return new Destapes();
    }

    public void llenarPrecios() {

        int rn = 10; //rango
        int al = 15; //alcance
        int ed = 5; //edad

        CriaturaPrecio criatura;

        for (int h = 0; h < 2; h++) {
            for (int i = 0; i < raza.length; i++) {
                for (int j = 0; j < elemento.length; j++) {
                    for (int k = 0; k < mutacion.length; k++) {
                        for (int l = 0; l < al; l = l + rn) {
                            for (int m = 0; m < al; m = m + rn) {
                                for (int n = 0; n < al; n = n + rn) {
                                    for (int o = 0; o < al; o = o + rn) {
                                        for (int p = 0; p < al; p = p + rn) {
                                            for (int q = 0; q < al; q = q + rn) {
                                                for (int r = 0; r < al; r = r + rn) {
                                                    for (int t = 0; t < ed; t++) {

                                                        criatura = new CriaturaPrecio();

                                                        criatura.setSexo(h);
                                                        criatura.setRaza(raza[i]);
                                                        criatura.setElemento(elemento[j]);
                                                        criatura.setMutacion(mutacion[k]);

                                                        criatura.setFuerza(l);
                                                        criatura.setMagia(m);
                                                        criatura.setAgilidad(n);
                                                        criatura.setReflejos(o);
                                                        criatura.setConstitucion(p);
                                                        criatura.setDefensa(q);
                                                        criatura.setReaccion(r);
                                                        criatura.setXp(0);

                                                        criatura.setEdad(20 + t);

                                                        save((CriaturaPrecio) criatura);

                                                        if (i % 50 == 0) {
                                                            session.flush();
                                                            session.clear();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public void toCriaturaMazmorra(Criaturas criatura) {

        CriaturaMazmorra criatMazmorra = new CriaturaMazmorra();
        criatura.setUsuario(null);
        update(criatura);
        criatMazmorra.setId(criatura.getId());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, 3);
        int date = (int) (calendar.getTimeInMillis() / 1000);

        criatMazmorra.setTiempoVenta(date);

        criatMazmorra.setAspecto(criatura.getAspecto());
        criatMazmorra.setNombre(criatura.getNombre());
        criatMazmorra.setClases(criatura.getClases());

        criatMazmorra.setSexo(criatura.getSexo());
        criatMazmorra.setRaza(criatura.getRaza());
        criatMazmorra.setElemento(criatura.getElemento());
        criatMazmorra.setMutacion(criatura.getMutacion());

        criatMazmorra.setEdad(criatura.getEdad());
        criatMazmorra.setMedia(criatura.getMedia());
        criatMazmorra.setFuerza(criatura.getFuerza());
        criatMazmorra.setMagia(criatura.getMagia());
        criatMazmorra.setAgilidad(criatura.getAgilidad());
        criatMazmorra.setReflejos(criatura.getReflejos());
        criatMazmorra.setConstitucion(criatura.getConstitucion());
        criatMazmorra.setDefensa(criatura.getDefensa());
        criatMazmorra.setReaccion(criatura.getReaccion());
        criatMazmorra.setXp(criatura.getXp());

        criatMazmorra.setPujas("0," + criatura.getPrecio() + ";");
        criatMazmorra.setPujaMax(0L);

        save(criatMazmorra);
    }

    public void toCriatura(Criaturas criatura) {
        Criatura criat = new Criatura();
        criat.setUsuario(null);
        criat.setId(criatura.getId());

        criat.setAspecto(criatura.getAspecto());
        criat.setApodo(criatura.getApodo());
        criat.setClases(criatura.getClases());

        criat.setSexo(criatura.getSexo());
        criat.setRaza(criatura.getRaza());
        criat.setElemento(criatura.getElemento());
        criat.setMutacion(criatura.getMutacion());

        criat.setEdad(criatura.getEdad());
        criat.setFuerza(criatura.getFuerza());
        criat.setMagia(criatura.getMagia());
        criat.setAgilidad(criatura.getAgilidad());
        criat.setReflejos(criatura.getReflejos());
        criat.setConstitucion(criatura.getConstitucion());
        criat.setDefensa(criatura.getDefensa());
        criat.setReaccion(criatura.getReaccion());
        criat.setXp(criatura.getXp());

        delete(criatura);
        update(criat);
    }

    public class Estadisticas {

        public int muertes;
    }

    static public class Entreno {

//        public String manana;
//        public String tarde;
//        public String noche;
        public Integer fuerza = 0;
        public Integer magia = 0;
        public Integer agilidad = 0;
        public Integer reflejos = 0;
        public Integer constitucion = 0;
        public Integer defensa = 0;
        public Integer reaccion = 0;
//        public Integer frescura = 0;
//        public Integer moral = 0;
//        public Integer oro = 0;
//        public Integer xp = 0;
    }

    public class Destapes {

        public Integer fu;
        public Integer desFu;
        public Integer ma;
        public Integer desMa;
        public Integer ag;
        public Integer desAg;
        public Integer rf;
        public Integer desRf;
        public Integer co;
        public Integer desCo;
        public Integer df;
        public Integer desDf;
        public Integer rc;
        public Integer desRc;
        public String mut;
        public Integer desMut;
        public String ele;
        public Integer desEle;
//        public int date = 0;
        public String ultima;
    }

    public class Evolucion {

        public int fu = -1;
        public int ma = -1;
        public int ag = -1;
        public int rf = -1;
        public int co = -1;
        public int df = -1;
        public int rc = -1;
        public int fr = -1;
        public int mo = -1;
        public int xp = -1;
    }
}
