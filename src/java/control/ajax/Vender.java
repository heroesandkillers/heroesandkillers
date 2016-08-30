package control.ajax;

import model.DAO.*;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import model.hibernate.*;
import java.util.Date;
import org.hibernate.*;

public class Vender extends ActionSupport {

    private long id;
    private String result = "";

    public String execute() throws Exception {
        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        Session session = HibernateUtil.getSessionFactory().openSession();

        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        Usuario usuario = usuarioDAO.loadUsuario(userId);
        
        String peticion = "SELECT count(*) FROM Criatura WHERE usuario = " + userId;
        if (((Long) session.createQuery(peticion).uniqueResult()).intValue() < 6) {

            session.close();
            result = "No puedes tener menos de 5 criaturas!";
            return SUCCESS;
        }

        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        Criatura criatura = criaturaDAO.loadCriatura(id);

        if (criatura.getUsuario().getId() == userId) {
            criaturaDAO.toCriaturaMazmorra(criatura);

            long precio = criatura.getPrecio();
            long OroTotal = usuario.getOro() + precio;

            usuario.setOro(OroTotal);
            usuarioDAO.update(usuario);

            Traspaso traspaso = new Traspaso();
            traspaso.setFecha(new Date());
            traspaso.setCriatura(criatura);
            traspaso.setVendedor(usuario);
            traspaso.setValor(precio);
            traspaso.setPrecio(precio);
//            traspaso.setEdad(criatura.getEdad());

            criaturaDAO.save(traspaso);
        } else {
            session.close();
            result = "Ya no eres dueño de esa criatura";
            return SUCCESS;
        }

        session.close();
        result = "";
        return SUCCESS;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
