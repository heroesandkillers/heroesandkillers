package control.ajax;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ActionContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.*;
import model.DAO.UsuarioDAO;
import model.hibernate.HibernateUtil;
import model.hibernate.Usuario;
import org.hibernate.Session;

public class logout extends ActionSupport {

    public String mapaJSON = "";

    public String execute() throws Exception {
        try {
            Map login = ActionContext.getContext().getSession();
            int userId = (Integer) ActionContext.getContext().getSession().get("usuario");

            phpbb_logout(userId);
            login.remove("usuario");

        } catch (Exception e) {
            mapaJSON = "error = " + e;
        }
        return SUCCESS;
    }

    public void phpbb_logout(int id) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        Usuario usuario = usuarioDAO.getUsuario(id);
        int phpbb_id = usuario.getPhpbb_user().getUser_id();

        Connection conn;
        String userName = "hak";
        String password = "&MOVy1PV";
        String url = "jdbc:mysql://localhost:3306/hak";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, userName, password);

            String peticion = "DELETE FROM phpbb_sessions WHERE session_user_id = " + phpbb_id;
            Statement st = conn.createStatement();
            st.executeUpdate(peticion);

        } catch (Exception e) {
            System.out.println("error: phpbb_logout in logout.java: " + e);
        }

        session.close();
    }
}
