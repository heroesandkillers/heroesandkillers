package mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.naming.*;
import javax.sql.DataSource;

public class Mysql {

    Connection conn;
    PreparedStatement stmt;

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

    public List integers(String query, int[] params) {
        ResultSet rs = query(query, params);

        List<Integer> list = new ArrayList();
        try {
            while (rs.next()) {
                list.add(rs.getInt(1));
            }
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public ResultSet query(String query, int[] params) {
        try {
            stmt = conn.prepareStatement(query);

            for (int i = 0; i < params.length; i++) {
                stmt.setInt(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();

            return rs;

        } catch (SQLException e) {
            throw new RuntimeException(e);
            //return null;
        }
    }
    
    public void update(String query, int[] params) {
        try {
            stmt = conn.prepareStatement(query);

            for (int i = 0; i < params.length; i++) {
                stmt.setInt(i + 1, params[i]);
            }
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
            //return null;
        }
    }

}
