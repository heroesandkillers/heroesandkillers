package mysql;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.*;
import javax.sql.DataSource;

public class Mysql {

    Connection conn;

    public Mysql() {
        try {
            Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/myDB");
            conn = dataSource.getConnection();
        } catch (Exception e) {
            //
        }
    }

    public ResultSet query(String query, String[] params) {
        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            for (int i = 0; i < params.length; i++) {
                stmt.setString(i, params[i]);
            }
            ResultSet data = stmt.executeQuery();

            stmt.close();
            return data;

        } catch (SQLException ex) {
            return null;
        }

    }

}
