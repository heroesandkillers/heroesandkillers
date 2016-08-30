package control.gets;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import model.hibernate.HibernateUtil;
import model.hibernate.Usuario;
import org.hibernate.Session;

public class GetPerfilContacto extends ActionSupport {

    private int id;
    public String mapaJSON = "";

    @Override
    public String execute() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        String peticion = "FROM Usuario WHERE id=" + id;
        Usuario usuario = (Usuario) session.createQuery(peticion).uniqueResult();
        session.close();

        if (usuario != null) {
            usuario.div = usuario.getDivision().getDivision();
            usuario.setDivision(null);
            usuario.setOro(null);
            usuario.setAlin(null);
            usuario.setAlinAcad(null);
            usuario.setEntreno(null);
            usuario.setEstad(null);
            usuario.setFichajes(null);
            usuario.setAcademia(null);
        }
        Gson gson = new Gson();
        mapaJSON = gson.toJson(usuario);
        return SUCCESS;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
