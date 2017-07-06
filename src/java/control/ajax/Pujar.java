package control.ajax;

import model.DAO.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import model.hibernate.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.*;

public class Pujar extends ActionSupport {

    private long id;
    private long puja;
    private String result = "error";

    public String execute() throws Exception {
        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        Session session = HibernateUtil.getSessionFactory().openSession();

        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);

        String[] pujaAnterior = criaturaDAO.pujaCriaturaMazmorra(id);
        int VendedorId = Integer.parseInt(pujaAnterior[0]);
        Usuario comprador = usuarioDAO.loadUsuario(userId);
        long oro = comprador.getOro();

        CriaturaMazmorra criatura = criaturaDAO.loadCriaturaMazmorra(id);

        if (null == criatura) {
            result = "id: '" + id + "' incorrecta.";
            return SUCCESS;
        }

        long precio = Long.parseLong(pujaAnterior[1]);
        long precioMin = precio;
        //null can cause error
        Long precioMax = criatura.getPujaMax();
        if (null == precioMax) {
            precioMax = 0L;
        }

//        Date date = new Date();
//        int time = (int) (date.getTime() / 1000);
//        int pujaResuelta = time - 259200;
        String peticion = "SELECT count(*) FROM Criatura WHERE usuario = " + comprador.getId();
        if (((Number) session.createQuery(peticion).uniqueResult()).intValue() > 19) {
            session.close();
            result = "Has llegado al límite de criaturas!";
            return SUCCESS;
        }

        if (criatura.getTiempoVenta().equals(new Date())) {
            session.close();
            result = "El tiempo de puja excedido..";
            return SUCCESS;
        }

        Usuario usuarioVendedor;

        if (0 == precioMax) {

            if (puja < precio) {
                session.close();
                result = "Han pujado antes q tu una cifra superior!";
                return SUCCESS;
            }

        } else {

            precioMin = (long) (precio * 1.02);
            usuarioVendedor = usuarioDAO.loadUsuario(VendedorId);

            if (usuarioVendedor.getId() == comprador.getId()) {

                if (puja > precio && oro + precio > puja) {
                    if (criatura.getUsuario() == null) {
                        session.close();
                        result = "No puedes pujar por una criatura que hayas vendido";
                        return SUCCESS;
                    } else {
                        comprador.setOro(oro - (puja - precioMax));
                        criatura.setPujaMax(puja);
                        criaturaDAO.update(criatura);
                        usuarioDAO.update(comprador);
                        session.close();
                        result = "Has actualizado tu puja";
                        return SUCCESS;
                    }
                } else {
                    session.close();
                    result = "No puedes pujar con un valor menor al de subasta";
                    return SUCCESS;
                }
            }

            if (puja < precioMin) {
                session.close();
                result = "Han pujado antes q tu una cifra superior!";
                return SUCCESS;
            }

            if (puja >= precioMin && puja <= precioMax) {

                criatura.setPujas(criatura.getPujas() + userId + "," + puja + ";");
                criatura.setPujas(criatura.getPujas() + VendedorId + "," + puja + ";");
                criaturaDAO.update(criatura);
                session.close();
                result = "Tu puja no ha sido suficientemente alta!";
                return SUCCESS;
            }
            //devolver oro antiguo comprador
            usuarioVendedor.setOro(usuarioVendedor.getOro() + precio);
            usuarioDAO.update(usuarioVendedor);
        }

        if (oro < puja) {
            session.close();
            result = "Ya no tienes oro suficiente para pujar!";
            return SUCCESS;
        }

        comprador.setOro(oro - puja);
        criatura.setUsuario(comprador);

        Gson gson = new Gson();
        List<Long> lista = gson.fromJson(comprador.getPujas(), new TypeToken<List<Long>>() {
        }.getType());

        try {
            if (!lista.contains(criatura.getId())) {
                lista.add(criatura.getId());
            }
        } catch (Exception e) {
            lista = new ArrayList();
            lista.add(criatura.getId());
        }
        comprador.setPujas(gson.toJson(lista));

        criatura.setPujas(criatura.getPujas() + userId + ":"
                + comprador.getPhpbb_user().getUsername() + ","
                + precioMin + ";");
        criatura.setPujaMax(puja);

        criaturaDAO.update(criatura);
        usuarioDAO.update(comprador);

        session.close();
        result = "En este momento tienes la puja más alta!";
        return SUCCESS;
    }

    public void setPuja(long puja) {
        this.puja = puja;
    }

    public long getPuja() {
        return puja;
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
