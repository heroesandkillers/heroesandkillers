package mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.*;
import javax.sql.DataSource;

public class Mysql {

    Connection conn;

    public Mysql() {
        try {
            Context context = new InitialContext();
//            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/hak");
            DataSource dataSource = (DataSource) context.lookup("jdbc/hak");
            conn = dataSource.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List integers(String query, String[] params) {
        ResultSet rs = query(query, params);

        List<Integer> list = new ArrayList();
        try {
            while (rs.next()) {
                list.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public ResultSet query(String query, String[] params) {
        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            for (int i = 0; i < params.length; i++) {
                stmt.setString(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();

            stmt.close();
            return rs;

        } catch (SQLException e) {
            throw new RuntimeException(e);
            //return null;
        }

    }

}
