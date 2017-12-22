/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hsoffenburg;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ubuntu
 */
public class Queries {
    public int count() {
        int count = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;
        
        try {
            connection = Globals.getPoolConnection();
            statement = connection.createStatement();
            
            String query = "SELECT COUNT(*) FROM musics";
            System.out.println("Query: " + query);
            
            results = statement.executeQuery(query);

            results.next();
            count = results.getInt(1);
            
        } catch (SQLException ex) {
            Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
            
        } finally {
            try {
                if (results != null) results.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                
            } catch (SQLException ex) {
                    Logger.getLogger(Queries.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return count;
    }
}