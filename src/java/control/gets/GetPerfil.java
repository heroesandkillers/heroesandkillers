package control.gets;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.sql.SQLException;
import model.DAO.MensajeDAO;
import model.DAO.UsuarioDAO;
import model.hibernate.HibernateUtil;
import model.hibernate.Phpbb_user;
import model.hibernate.Usuario;
import org.hibernate.Session;

public class GetPerfil {

    public String mapaJSON = "error";

    public void execute() {
    }

    public String GetPerfil() throws SQLException, ClassNotFoundException {
        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        Gson gson = new Gson();
        mapaJSON = gson.toJson(getPerfil(userId));
        return SUCCESS;
    }

    public Usuario getPerfil(int userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        MensajeDAO mensajeDAO = new MensajeDAO(session);
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        Usuario usuario = usuarioDAO.getUsuario(userId);

        usuario.mensajes = mensajeDAO.getMensajesNuevos(usuario.getId());

        session.flush();
        session.close();

//        Phpbb_user phpbb_user = usuario.getPhpbb_user();
//        usuario.setUsername(phpbb_user.getUsername());
//        String lang = phpbb_user.getUser_lang();
//        Wiki_pageDAO wiki_pageDAO = new Wiki_pageDAO(session, lang);
//        int idPage = wiki_pageDAO.getLangPage("Lang");
//        String text = wiki_pageDAO.getWiki_text(idPage);
        usuario.setPhpbb_user(null);

        return usuario;
    }
}
