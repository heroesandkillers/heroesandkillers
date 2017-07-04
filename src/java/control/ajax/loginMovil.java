package control.ajax;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ActionContext;
import control.gets.GetAll;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.*;
import model.DAO.Phpbb_userDAO;
import model.DAO.UsuarioDAO;
import model.hibernate.HibernateUtil;
import model.hibernate.Phpbb_user;
import model.hibernate.Usuario;
import org.hibernate.Session;

public class loginMovil extends ActionSupport {

    private String key1;
    private String key2;
    public String mapaJSON = "error: ";

    public String execute() throws Exception {
        mapaJSON += " execute() key1";

        Map login = ActionContext.getContext().getSession();
        Session session = HibernateUtil.getSessionFactory().openSession();

        Phpbb_userDAO phpbb_userDAO = new Phpbb_userDAO(session);
        Phpbb_user yo = null;
        try {
            yo = phpbb_userDAO.loadPhpbb_user(key1);
        } catch (Exception e) {
            mapaJSON += e.getMessage();
            return SUCCESS;
        }

        PhpBB phpbb = new PhpBB();

        if (null == yo) {
            mapaJSON += " El usuario no existe. ";
            if (key1.indexOf("@gmail.") > -1) {

                String[] arr = key1.split("@");
                String username = arr[0];

                //LOAD USER ANDROID TYPE
                yo = phpbb_userDAO.loadPhpbb_user(username);
                if (null == yo) {
                    //CREATE PHPBB USER FROM ANDROID LOGIN
                    yo = new Phpbb_user();
                    yo.setUsername(username);
                    yo.setUsername_clean(username); //HAVE TO BE UNIQUE AND IS STRING FOR LOGIN!
                    yo.setUser_email(key1);
                    yo.setUser_password(phpbb.phpbb_hash(key2));
                    phpbb_userDAO.save(yo);
                }

            } else {
                mapaJSON += " incorrecto (1)";
                return SUCCESS;
            }
        }

        String passForo;
        boolean result = false;
        passForo = yo.getUser_password();
        result = phpbb.phpbb_hash(key2) == passForo;

        //ACTIVAR EN CASO DE NO HABER FORO
//            if (loginUser.equals("prueba")) {
//                result = true;
//            }
//            yo = new Phpbb_user();
//            yo.setUser_id(1);
//            yo.setUsername(loginUser);
        if (!result) {
            mapaJSON = "incorrecto (2)";
            //mapaJSON = "incorrecto " + phpbb.phpbb_hash(key2) + " == " + passForo;
            return SUCCESS;
        }

        //LOGUED
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        Usuario usuario = usuarioDAO.loadUsuarioPhpbb(yo);

        if (usuario == null) {
            mapaJSON = "duplicate username";
            return SUCCESS;
        }

        login.remove("usuario");
        login.put("usuario", usuario.getId());
        login.put("idioma", "es_ES");

        GetAll getAll = new GetAll();
        mapaJSON = getAll.getAll(usuario.getId());

        session.close();
        return SUCCESS;
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }
    /**
     * Port of phpBB3 password handling to Java. See
     * phpBB3/includes/functions.php
     *
     * @author lars
     */
    private static final int PHP_VERSION = 5;
//    private String itoa64 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
//    public String phpbb_hash(String password) {
//        String random_state = unique_id();
//        String random = "";
//        int count = 6;
//
//        if (random.length() < count) {
//            random = "";
//
//            for (int i = 0; i < count; i += 16) {
//                random_state = md5(unique_id() + random_state);
//                random += pack(md5(random_state));
//            }
//            random = random.substring(0, count);
//        }
//
//        String hash = _hash_crypt_private(password, _hash_gensalt_private(random, itoa64));
//        if (hash.length() == 34) {
//            return hash;
//        }
//
//        return md5(password);
//    }
//    private String unique_id() {
//        return unique_id("c");
//    }
//    private String unique_id(String extra) {
//        // TODO Generate something random here.
//        return "1234567890abcdef";
//    }
//    private String _hash_gensalt_private(String input, String itoa64) {
//        return _hash_gensalt_private(input, itoa64, 6);
//    }
//    private String _hash_gensalt_private(String input, String itoa64, int iteration_count_log2) {
//        if (iteration_count_log2 < 4 || iteration_count_log2 > 31) {
//            iteration_count_log2 = 8;
//        }
//
//        String output = "$H$";
//        output += itoa64.charAt(Math.min(iteration_count_log2 + ((PHP_VERSION >= 5) ? 5 : 3), 30));
//        output += _hash_encode64(input, 6);
//
//        return output;
//    }
//    /**
//     * Encode hash
//     */
//    private String _hash_encode64(String input, int count) {
//        String output = "";
//        int i = 0;
//
//        do {
//            int value = input.charAt(i++);
//            output += itoa64.charAt(value & 0x3f);
//
//            if (i < count) {
//                value |= input.charAt(i) << 8;
//            }
//
//            output += itoa64.charAt((value >> 6) & 0x3f);
//
//            if (i++ >= count) {
//                break;
//            }
//
//            if (i < count) {
//                value |= input.charAt(i) << 16;
//            }
//
//            output += itoa64.charAt((value >> 12) & 0x3f);
//
//            if (i++ >= count) {
//                break;
//            }
//
//            output += itoa64.charAt((value >> 18) & 0x3f);
//        } while (i < count);
//
//        return output;
//    }
//    String _hash_crypt_private(String password, String setting) {
//        String output = "*";
//
//        // Check for correct hash
//        if (!setting.substring(0, 3).equals("$H$")) {
//            return output;
//        }
//
//        int count_log2 = itoa64.indexOf(setting.charAt(3));
//        if (count_log2 < 7 || count_log2 > 30) {
//            return output;
//        }
//
//        int count = 1 << count_log2;
//        String salt = setting.substring(4, 12);
//        if (salt.length() != 8) {
//            return output;
//        }
//
//        String m1 = md5(salt + password);
//        String hash = pack(m1);
//        do {
//            hash = pack(md5(hash + password));
//        } while (--count > 0);
////        String hash = md5(salt + password);
////        do {
////            hash = md5(hash + password);
////        } while (--count > 0);
//
//        output = setting.substring(0, 12);
//        output += _hash_encode64(hash, 16);
//
//        return output;
//    }
//    public boolean phpbb_check_hash(String password, String hash) {
//        if (hash.length() == 34) {
//            return _hash_crypt_private(password, hash).equals(hash);
//        } else {
//            return md5(password).equals(hash);
//        }
//    }
//    public String md5(String data) {
//        try {
//            byte[] bytes = data.getBytes("ISO-8859-1");
//            MessageDigest md5er = MessageDigest.getInstance("MD5");
//            byte[] hash = md5er.digest(bytes);
//            return bytes2hex(hash);
//        } catch (GeneralSecurityException e) {
//            throw new RuntimeException(e);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    int hexToInt(char ch) {
        if (ch >= '0' && ch <= '9') {
            return ch - '0';
        }

        ch = Character.toUpperCase(ch);
        if (ch >= 'A' && ch <= 'F') {
            return ch - 'A' + 0xA;
        }

        throw new IllegalArgumentException("Not a hex character: " + ch);
    }

    private String bytes2hex(byte[] bytes) {
        StringBuffer r = new StringBuffer(32);
        for (int i = 0; i < bytes.length; i++) {
            String x = Integer.toHexString(bytes[i] & 0xff);
            if (x.length() < 2) {
                r.append("0");
            }
            r.append(x);
        }
        return r.toString();
    }

    String pack(String hex) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < hex.length(); i += 2) {
            char c1 = hex.charAt(i);
            char c2 = hex.charAt(i + 1);
            char packed = (char) (hexToInt(c1) * 16 + hexToInt(c2));
            buf.append(packed);
        }
        return buf.toString();
    }
}
