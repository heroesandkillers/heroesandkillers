package model.hibernateLang;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "wiki_en_page")
public class Wiki_en_page extends Wiki_Lang_page implements Serializable{

}