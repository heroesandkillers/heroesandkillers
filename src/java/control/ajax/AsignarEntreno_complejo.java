//package control.ajax;
//
//import model.DAO.CriaturaDAO.Entreno;
//import model.DAO.UsuarioDAO;
//import com.google.gson.Gson;
//import com.opensymphony.xwork2.ActionContext;
//import com.opensymphony.xwork2.ActionSupport;
//import java.util.Map;
//import model.hibernate.*;
//import org.hibernate.*;
//
//public class AsignarEntreno extends ActionSupport {
//
//    private String manana = null;
//    private String tarde = null;
//    private String noche = null;
//    public String mapaJSON = "error";
//
//    public String execute() throws Exception {
//
//        int fuerza = 0, magia = 0, agilidad = 0, reflejos = 0, constitucion = 0, defensa = 0, reaccion = 0, frescura = 0, oro = 0, xp = 0;
//        String a[] = {manana, tarde, noche};
//
//        for (int i = 0; i < a.length; i++) {
//            
//            if (null == a[i] || a[i].equals("nada")) {
//            } else if (a[i].equals("vago")) {
//                frescura = frescura + 3;
//                defensa = defensa + 2;
//                magia = magia + 1;
//            } else if (a[i].equals("trabajar")) {
//                oro = oro + 3;
//                reaccion = reaccion + 2;
//                defensa = defensa + 1;
//            } else if (a[i].equals("estudiar")) {
//                magia = magia + 3;
//                defensa = defensa + 2;
//                frescura++;
//            } else if (a[i].equals("cazar")) {
//                agilidad = agilidad + 3;
//                reflejos = reflejos + 2;
//                reaccion = reaccion++;
//            } else if (a[i].equals("meditar")) {
//                defensa = defensa + 3;
//                frescura = frescura + 2;
//                magia = magia + 1;
//            } else if (a[i].equals("saquear")) {
//                oro = oro + 3;
//                reaccion = reaccion + 2;
//                fuerza = fuerza + 1;
//            } else if (a[i].equals("nadar")) {
//                constitucion = constitucion + 3;
//                agilidad = agilidad + 2;
//                defensa = defensa + 1;
//            } else {
//                mapaJSON = "se ha producido un error con una acción";
//                return SUCCESS;
//            }
//        }
//
//        Entreno entreno = new Entreno();
//
//        entreno.manana = manana;
//        entreno.tarde = tarde;
//        entreno.noche = noche;
//
//        entreno.fuerza = fuerza;
//        entreno.magia = magia;
//        entreno.agilidad = agilidad;
//        entreno.reflejos = reflejos;
//        entreno.constitucion = constitucion;
//        entreno.defensa = defensa;
//        entreno.reaccion = reaccion;
//        entreno.frescura = frescura;
//        entreno.oro = oro;
//        entreno.xp = xp;
//
//        Gson gson = new Gson();
//        String entr = gson.toJson(entreno);
//        ActionContext ctx = ActionContext.getContext();
//        Map<String, Object> s = ctx.getSession();
//        Object usuario = s.get("usuario");
//        int userId = (Integer) usuario;
//
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
//        usuarioDAO.setEntreno(userId, entr);
//
//        mapaJSON = "";
//        session.close();
//        return SUCCESS;
//    }
//
//    public void setManana(String manana) {
//        this.manana = manana;
//    }
//
//    public String getManana() {
//        return manana;
//    }
//
//    public void setTarde(String tarde) {
//        this.tarde = tarde;
//    }
//
//    public String getTarde() {
//        return tarde;
//    }
//
//    public void setNoche(String noche) {
//        this.noche = noche;
//    }
//
//    public String getNoche() {
//        return noche;
//    }
//}