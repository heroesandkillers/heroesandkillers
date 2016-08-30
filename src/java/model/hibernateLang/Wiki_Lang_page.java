package model.hibernateLang;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Wiki_Lang_page implements Serializable {

    private Integer page_id;
    private String page_title;
    private Integer page_latest;

    @Id
    @GeneratedValue
    @Column(columnDefinition = "INT(10) unsigned", nullable = false)
    public Integer getPage_id() {
        return page_id;
    }

    public void setPage_id(Integer page_id) {
        this.page_id = page_id;
    }

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    public String getPage_title() {
        return page_title;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }

    @Column(columnDefinition = "INT(10) unsigned", nullable = false)
    public Integer getPage_latest() {
        return page_latest;
    }

    public void setPage_latest(Integer page_latest) {
        this.page_latest = page_latest;
    }
}
