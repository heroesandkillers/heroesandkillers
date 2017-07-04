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

    //@GenericGenerator(name="user_id" , strategy="increment")
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

    //NOT DEFAULT VALUES (Alt + Insert)
    public String getUser_permissions() {
        return user_permissions;
    }

    public String getUser_ip() {
        return user_ip;
    }

    public String getUsername_clean() {
        return username_clean;
    }

    public String getUser_birthday() {
        return user_birthday;
    }

    public String getUser_lastpage() {
        return user_lastpage;
    }

    public String getUser_last_confirm_key() {
        return user_last_confirm_key;
    }

    public String getUser_timezone() {
        return user_timezone;
    }

    public String getUser_colour() {
        return user_colour;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public String getUser_avatar_type() {
        return user_avatar_type;
    }

    public String getUser_sig_bbcode_uid() {
        return user_sig_bbcode_uid;
    }

    public String getUser_sig_bbcode_bitfield() {
        return user_sig_bbcode_bitfield;
    }

    public String getUser_jabber() {
        return user_jabber;
    }

    public String getUser_actkey() {
        return user_actkey;
    }

    public String getUser_newpasswd() {
        return user_newpasswd;
    }

    public String getUser_form_salt() {
        return user_form_salt;
    }

    //
    public void setUser_permissions(String user_permissions) {
        this.user_permissions = user_permissions;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    public void setUsername_clean(String username_clean) {
        this.username_clean = username_clean;
    }

    public void setUser_birthday(String user_birthday) {
        this.user_birthday = user_birthday;
    }

    public void setUser_lastpage(String user_lastpage) {
        this.user_lastpage = user_lastpage;
    }

    public void setUser_last_confirm_key(String user_last_confirm_key) {
        this.user_last_confirm_key = user_last_confirm_key;
    }

    public void setUser_timezone(String user_timezone) {
        this.user_timezone = user_timezone;
    }

    public void setUser_colour(String user_colour) {
        this.user_colour = user_colour;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public void setUser_avatar_type(String user_avatar_type) {
        this.user_avatar_type = user_avatar_type;
    }

    public void setUser_sig_bbcode_uid(String user_sig_bbcode_uid) {
        this.user_sig_bbcode_uid = user_sig_bbcode_uid;
    }

    public void setUser_sig_bbcode_bitfield(String user_sig_bbcode_bitfield) {
        this.user_sig_bbcode_bitfield = user_sig_bbcode_bitfield;
    }

    public void setUser_jabber(String user_jabber) {
        this.user_jabber = user_jabber;
    }

    public void setUser_actkey(String user_actkey) {
        this.user_actkey = user_actkey;
    }

    public void setUser_newpasswd(String user_newpasswd) {
        this.user_newpasswd = user_newpasswd;
    }

    public void setUser_form_salt(String user_form_salt) {
        this.user_form_salt = user_form_salt;
    }

}
