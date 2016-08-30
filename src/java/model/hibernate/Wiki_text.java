package model.hibernate;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wiki_text")
public class Wiki_text implements Serializable {

    private Integer old_id;
    private String old_text;
    private String old_flags;

    @Id
    @GeneratedValue
    @Column(columnDefinition = "INT(10) unsigned", nullable = false)
    public Integer getOld_id() {
        return old_id;
    }

    public void setOld_id(Integer old_id) {
        this.old_id = old_id;
    }

    @Column(columnDefinition = "MEDIUMBLOB", nullable = false)
    public String getOld_text() {
        return old_text;
    }

    public void setOld_text(String old_text) {
        this.old_text = old_text;
    }

    @Column(columnDefinition = "TINYBLOB", nullable = false)
    public String getOld_flags() {
        return old_flags;
    }

    public void setOld_flags(String old_flags) {
        this.old_flags = old_flags;
    }

}
