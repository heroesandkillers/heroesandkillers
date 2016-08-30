package model.hibernateLang;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "wiki_es_page")
public class Wiki_es_page extends Wiki_Lang_page implements Serializable{

}
