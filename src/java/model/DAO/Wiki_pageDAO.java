package model.DAO;

import model.hibernateLang.Wiki_Lang_page;
import org.hibernate.Query;
import org.hibernate.Session;

public class Wiki_pageDAO {

    private Session session;

    public Wiki_pageDAO(Session session) {
        this.session = session;
    }

    public String getWiki_text(String lang) {
        try {
            String peticion = "SELECT old_text FROM Wiki_text WHERE old_id = :id";
            Query query = session.createQuery(peticion);
            query.setParameter("id", getLangPageId(lang));
            return (String) query.uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }

    private Integer getLangPageId(String lang) {
        //SECURITY
        if (lang.length() > 3) {
            return 0;
        }

        String peticion = "FROM Wiki_" + lang + "_page WHERE page_title = 'Lang'";
        Wiki_Lang_page wiki_Lang_page = (Wiki_Lang_page) session.createQuery(peticion).uniqueResult();
        return wiki_Lang_page.getPage_latest();
    }
}
