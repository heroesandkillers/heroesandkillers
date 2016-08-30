package model.hibernate;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "hak_criaturas")
@org.hibernate.annotations.Table(appliesTo = "hak_criaturas",
indexes = {
    @Index(name = "media"),
    @Index(name = "precio")
})
public class Criatura extends Criaturas implements Serializable{

}