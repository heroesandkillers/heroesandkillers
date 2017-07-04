package model.hibernate;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "phpbb_users")
public class Phpbb_user implements Serializable {

    private Integer user_id;
    private String username;
    private String user_password;
    private String user_email;
    private String user_lang = "";

    //NOT DEFAULT DB VALUES
    private String user_permissions = "";
    private String user_ip = "";
    private String username_clean = "";
    private String user_birthday = "";
    private String user_lastpage = "";
    private String user_last_confirm_key = "";
    private String user_timezone = "";
    private String user_colour = "";
    private String user_avatar = "";
    private String user_avatar_type = "";
    private String user_sig_bbcode_uid = "";
    private String user_sig_bbcode_bitfield = "";
    private String user_jabber = "";
    private String user_actkey = "";
    private String user_newpasswd = "";
    private String user_form_salt = "";

    //    @GenericGenerator(name="user_id" , strategy="increment")
    @Id
    @GeneratedValue
    @Column(columnDefinition = "MEDIUMINT(8) unsigned", nullable = false)
    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_lang() {
        return user_lang;
    }

    public void setUser_lang(String user_lang) {
        this.user_lang = user_lang;
    }

    //NOT DEFAULT VALUES
//    public String getUser_permissions() {
//        return user_permissions;
//    }
//
//    public void setUser_permissions(String user_permissions) {
//        this.user_permissions = user_permissions;
//    }
}
