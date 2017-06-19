package control.gets;

import com.google.gson.Gson;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import control.ajax.CalendarioAction;
import control.ajax.CriaturasAction;
import control.ajax.DivisionesAction;
import model.DAO.Phpbb_userDAO;
import model.DAO.UsuarioDAO;
import model.hibernate.HibernateUtil;
import model.hibernate.Phpbb_user;
//import model.DAO.DivisionDAO;
//import model.DAO.UsuarioDAO;
//import model.hibernate.Division;
//import model.hibernate.HibernateUtil;
import model.hibernate.Usuario;
import org.hibernate.Session;
//import org.hibernate.Session;

public class GetAll extends ActionSupport {

    public String mapaJSON = "error";

    @Override
    public String execute() {
        int userId;
        try {
            userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        } catch (Exception e) {
            Gson gson = new Gson();
            mapaJSON = "ActionContext.getContext().getSession().get('usuario') = " + e + "." + gson.toJson(ActionContext.getContext().getSession());
            if (e.toString().contains("java.lang.NullPointerException")) {

            }
            return SUCCESS;
        }
        
        try {
            mapaJSON = getAll(userId);
        } catch (Exception e) {
            mapaJSON = e.toString();
        }
        return SUCCESS;
    }

    public String getAll(int userId) {

        Gson gson = new Gson();
        All all = new All();

        GetPerfil getPerfil = new GetPerfil();
        Usuario perfilUsuario = getPerfil.getPerfil(userId);
        int division;

        try {
            division = perfilUsuario.getDivision().getDivision();
        } catch (Exception e) {
            System.out.println("Usuario '" + perfilUsuario.getId() + "' has empty división: " + e + ": setting new division...");

            return "error";

//            Session session = HibernateUtil.getSessionFactory().openSession();
//            DivisionDAO divisionDAO = new DivisionDAO(session);
//            division = divisionDAO.getEmptyDivision();
//            Division divisionObject = divisionDAO.getDivision(division);
//            perfilUsuario.setDivision(divisionObject);
//            UsuarioDAO usuarioDAO = new UsuarioDAO(session);
//            usuarioDAO.update(perfilUsuario);
        }
        all.perfil = gson.toJson(perfilUsuario);

        CalendarioAction calAction = new CalendarioAction();

        all.calendario = gson.toJson(calAction.getCalendario(userId));
        all.calendarioDivision = gson.toJson(calAction.getCalendarioDivision(division)); //makes error DIVISION ?

        DivisionesAction divAction = new DivisionesAction();
        all.divisiones = divAction.getDivisiones();

        GetLiga getLiga = new GetLiga();
        all.liga = gson.toJson(getLiga.getLiga(division));

        CriaturasAction criatAction = new CriaturasAction();
        all.equipo = gson.toJson(criatAction.getEquipo(userId));
        all.equipoAcademia = gson.toJson(criatAction.getEquipoAcademia(userId));

        return gson.toJson(all);
    }

    public class All {

        public String perfil;
        public String calendario;
        public String calendarioDivision;
        public Integer divisiones;
        public String liga;
        public String equipo;
        public String equipoAcademia;
    }
}
