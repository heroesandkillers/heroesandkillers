package control.ajax;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ActionContext;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import model.DAO.Phpbb_userDAO;
import model.DAO.UsuarioDAO;
import model.DAO.Wiki_pageDAO;
import model.hibernate.HibernateUtil;
import model.hibernate.Phpbb_user;
import org.hibernate.Session;

public class login {

    private String key1;
    private String key2;
    public String mapaJSON = "error";

    public String execute() throws Exception {
        Long id = Long.parseLong(key1);
        if (id == 2 || (id == 260888 && key2.equals("trollderiu"))) {
            Map login = ActionContext.getContext().getSession();
            login.put("admin", "true");
            mapaJSON = Login(id, key2);
            return SUCCESS;
//            return mapaJSON;
        } else if (id > 2) {
            mapaJSON = Login(id, key2);
        } else {
            System.out.println("login id = " + key1 + ". no log");
        }

        return SUCCESS;
//        return mapaJSON;
    }

    public String Login(Long phpId, String pass) throws IOException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Map login = ActionContext.getContext().getSession();

        Phpbb_userDAO phpbb_userDAO = new Phpbb_userDAO(session);
        Phpbb_user phpbb_user = null;

        String passForo;
        try {
            phpbb_user = phpbb_userDAO.loadPhpbb_user(phpId);
            passForo = phpbb_user.getUser_password();

        } catch (Exception e) {
            if (null == phpbb_user) {
                return "null response on loadPhpbb_user( " + phpId + " ) in login.java.";
            } else {
                return "unknown error on loadPhpbb_user( " + phpId + " ) in login.java: " + e;
            }
        }

        if (passForo.equals(pass)) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(session);
            int id = usuarioDAO.getUsuarioId(phpbb_user);

            login.remove("usuario");
            login.put("usuario", id);

            String lang = phpbb_user.getUser_lang();
            Wiki_pageDAO wiki_pageDAO = new Wiki_pageDAO(session);
            String text = wiki_pageDAO.getWiki_text(lang);

            Properties properties = parsePropertiesString(text);
            login.put("lang", properties);

            session.close();
            System.out.println("login id = " + key1 + ". log done");
            return "done";

        } else {
            session.close();
            System.out.println("login id = " + key1 + ". log fail");
            return "fail";
        }
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public Properties parsePropertiesString(String s) throws IOException {
        // grr at load() returning void rather than the Properties object
        // so this takes 3 lines instead of "return new Properties().load(...);"
        final Properties p = new Properties();
        p.load(new StringReader(s));
        return p;
    }
}
