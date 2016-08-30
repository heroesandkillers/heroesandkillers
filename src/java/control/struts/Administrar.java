package control.struts;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import model.DAO.BatallaDAO;
import model.DAO.CriaturaDAO;
import model.DAO.DivisionDAO;
import model.DAO.UsuarioDAO;
import model.hibernate.Batalla;
import model.hibernate.Criatura;
import model.hibernate.CriaturaAcademia;
import model.hibernate.CriaturaMazmorra;
import model.hibernate.CriaturaPrecio;
import model.hibernate.Division;
import model.hibernate.HibernateUtil;
import model.hibernate.MensajeJuego;
import model.hibernate.MensajePrensa;
import model.hibernate.MensajeUsuario;
import model.hibernate.Tiempo;
import model.hibernate.Traspaso;
import model.hibernate.Trofeo;
import model.hibernate.Usuario;
import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

public class Administrar extends ActionSupport {

    private Session session;
    private String admin;
    private String inicio;
    private String fin;

    public String execute() throws Exception {
        try {
            admin = ActionContext.getContext().getSession().get("admin").toString();
            if (admin.equals("true")) {
                return SUCCESS;
            } else {
                addActionError("NO TIENES PERMISOS PARA ESTO");
                return ERROR;
            }
        } catch (Exception e) {
            addActionError("NO TIENES PERMISOS PARA ESTO");
            return ERROR;
        }
    }

    public String probarEntreno() {
        session = HibernateUtil.getSessionFactory().openSession();

        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        criaturaDAO.entreno();

        session.close();
        return SUCCESS;
    }

    public String probarMazmorras() {
        session = HibernateUtil.getSessionFactory().openSession();

        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        criaturaDAO.llenarMazmorras();

        session.close();
        return SUCCESS;
    }

    public String llenarMazmorras() { //100 a saco
        session = HibernateUtil.getSessionFactory().openSession();

        CriaturaDAO criaturaDAO = new CriaturaDAO(session);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 3);
        int date = (int) (c.getTimeInMillis() / 1000);

        for (int i = 0; i < 100; i++) {
            criaturaDAO.crearCriatura(date);
            date -= 2500;
        }

        session.close();
        return SUCCESS;
    }

    public String vaciarUltimasBatallas() {
        session = HibernateUtil.getSessionFactory().openSession();

        BatallaDAO batallaDAO = new BatallaDAO(session);
        batallaDAO.vaciarUltimasBatallas();

        session.close();
        return SUCCESS;
    }

    public String activarUsuarios() {
        session = HibernateUtil.getSessionFactory().openSession();

        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        usuarioDAO.activarUsuarios();

        session.close();
        return SUCCESS;
    }

    public String reiniciar() throws Exception {
        addActionMessage("error");
        session = HibernateUtil.getSessionFactory().openSession();
        AnnotationConfiguration config = new AnnotationConfiguration();

        config.addAnnotatedClass(Tiempo.class);
        config.addAnnotatedClass(Division.class);
        config.addAnnotatedClass(Usuario.class);
        config.addAnnotatedClass(Batalla.class);
        config.addAnnotatedClass(Criatura.class);
        config.addAnnotatedClass(Trofeo.class);
        config.addAnnotatedClass(Traspaso.class);
        config.addAnnotatedClass(CriaturaAcademia.class);
        config.addAnnotatedClass(CriaturaMazmorra.class);
        config.addAnnotatedClass(CriaturaPrecio.class);
        config.addAnnotatedClass(MensajeUsuario.class);
        config.addAnnotatedClass(MensajeJuego.class);
        config.addAnnotatedClass(MensajePrensa.class);

        config.configure();
        new SchemaUpdate(config).execute(true, true);
        addActionMessage("Se han reiniciado las tablas");

        session.close();
        return SUCCESS;
    }

    public String reiniciarPrecios() throws Exception {
        session = HibernateUtil.getSessionFactory().openSession();

        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        criaturaDAO.llenarPrecios();

        addActionMessage("Se han reiniciado los precios");
        session.close();
        return SUCCESS;
    }

    public String anularBatallas() {
        session = HibernateUtil.getSessionFactory().openSession();

        BatallaDAO batallaDAO = new BatallaDAO(session);
        batallaDAO.anularBatallas();

        session.close();
        return SUCCESS;
    }

    public String eliminarPuntuaciones() {
        session = HibernateUtil.getSessionFactory().openSession();

        DivisionDAO divisionDAO = new DivisionDAO(session);
        int divisiones = divisionDAO.numSubDivisiones();

        BatallaDAO batallaDAO = new BatallaDAO(session);
        for (int i = 1; i < divisiones + 1; i++) {
            batallaDAO.vaciarResultadosDivision(i);
        }

        session.close();
        return SUCCESS;
    }

    public String eliminarBatallas() {
        session = HibernateUtil.getSessionFactory().openSession();

        BatallaDAO batallaDAO = new BatallaDAO(session);
        batallaDAO.eliminarBatallas();

        session.close();
        return SUCCESS;
    }

    public String vaciarBatallas() {
        session = HibernateUtil.getSessionFactory().openSession();

        BatallaDAO batallaDAO = new BatallaDAO(session);
        batallaDAO.vaciarBatallas();

        session.close();
        return SUCCESS;
    }

    public String rectificarPremios() throws ParseException {
        session = HibernateUtil.getSessionFactory().openSession();

        BatallaDAO batallaDAO = new BatallaDAO(session);

        SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
        Date dInicio = f.parse(inicio);
        Date dFin = f.parse(fin);
        int start = (int) (dInicio.getTime() / 1000);
        int end = (int) (dFin.getTime() / 1000);
        batallaDAO.rectificarPremios(start, end);

        session.close();
        return SUCCESS;
    }

    public String saveEvol() throws ParseException {
        session = HibernateUtil.getSessionFactory().openSession();

        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        criaturaDAO.saveEvol();

        session.close();
        return SUCCESS;
    }

    public String updateEvol() throws ParseException {
        session = HibernateUtil.getSessionFactory().openSession();

        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        criaturaDAO.updateEvol();

        session.close();
        return SUCCESS;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }
}
