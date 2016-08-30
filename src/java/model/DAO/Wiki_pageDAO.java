package model.DAO;

import model.hibernateLang.Wiki_Lang_page;
import org.hibernate.Session;

public class Wiki_pageDAO {

    private Session session;

    public Wiki_pageDAO(Session session) {
        this.session = session;
    }

    public String getWiki_text(String lang) {
        try {
            String peticion = "SELECT old_text FROM Wiki_text WHERE old_id = " + getLangPageId(lang);
            return (String) session.createQuery(peticion).uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getLangPageId(String lang) {
        String peticion = "FROM Wiki_" + lang + "_page WHERE page_title = 'Lang'";
        Wiki_Lang_page wiki_Lang_page = (Wiki_Lang_page) session.createQuery(peticion).uniqueResult();
        return wiki_Lang_page.getPage_latest();
    }
}
