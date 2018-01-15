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
    private static final String TAG = Queries.class.getName();
    private static final int POSITION_LAST = -1;

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
            
        } catch (SQLException e) {
            Logger.getLogger(TAG)
                    .log(Level.SEVERE, "Count query failed", e);
            
        } finally {
            try {
                if (results != null) results.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                
            } catch (SQLException e) {
                    Logger.getLogger(TAG)
                            .log(Level.SEVERE, "Cleanup failed", e);
            }
        }

        return count;
    }
    
    public boolean login(String email, String password) {
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;
        
        try {
            connection = Globals.getPoolConnection();
            statement = connection.createStatement();
            
            String query = "SELECT * " +
                           "FROM users u " +
                           "WHERE u.email = '" + email + "' " +
                           "AND u.passwd = '" + password +"'";
            System.out.println("Query: " + query);
            
            results = statement.executeQuery(query);
            
            // If an entry was returned, the login was successful
            return results.next();
            
            } catch (SQLException e) {
            Logger.getLogger(TAG)
                    .log(Level.SEVERE, "Login query failed", e);
            
        } finally {
            try {
                if (results != null) results.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                
            } catch (SQLException e) {
                    Logger.getLogger(TAG)
                            .log(Level.SEVERE, "Cleanup failed", e);
            }
        }
        
        return false;
    }
    
    public boolean register(String email, String password) {
        Connection connection = null;
        Statement statement = null;
        
        try {
            connection = Globals.getPoolConnection();
            statement = connection.createStatement();
            
            String query = "INSERT INTO users (email, passwd) " +
                           "VALUES ('" + email + "', '" + password + "')";
            System.out.println("Query: " + query);
            
            return statement.executeUpdate(query) == 1;
            
        } catch (SQLException e) {
            Logger.getLogger(TAG)
                    .log(Level.SEVERE, "Register query failed", e);
            
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                
            } catch (SQLException e) {
                    Logger.getLogger(TAG)
                            .log(Level.SEVERE, "Cleanup failed", e);
            }
        }
        
        return false;
    }
    
    public Song first(String email) {
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;
        
        try {
            connection = Globals.getPoolConnection();
            statement = createStatement(connection);
            
            String query = getSongQuery(email);            
            System.out.println("Query: " + query);
            
            results = statement.executeQuery(query);
            
            results.first();
            position = 1;
            return resultsetToSong(results);
            
        } catch (SQLException e) {
            Logger.getLogger(TAG)
                    .log(Level.SEVERE, "First query failed", e);
        
        } finally {
            try {
                if (results != null) results.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                
            } catch (SQLException e) {
                Logger.getLogger(TAG)
                        .log(Level.SEVERE, "Cleanup failed", e);
            }
        }
        
        return null;
    }
    
    public Song last(String email) {
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        try {
            connection = Globals.getPoolConnection();
            statement = createStatement(connection);

            String query = getSongQuery(email);
            System.out.println("Query: " + query);

            results = statement.executeQuery(query);

            results.last();
            position = POSITION_LAST;
            return resultsetToSong(results);

        } catch (SQLException e) {
            Logger.getLogger(TAG)
                    .log(Level.SEVERE, "First query failed", e);

        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException e) {
                Logger.getLogger(TAG)
                        .log(Level.SEVERE, "Cleanup failed", e);
            }
        }

        return null;
    }
    
    public Song next(String email) {
        Connection connection = null;
        Statement statement = null;
        ResultSet results = null;

        try {
            connection = Globals.getPoolConnection();
            statement = createStatement(connection);
            
            String query = getSongQuery(email);
            System.out.println("Query: " + query);
            
            results = statement.executeQuery(query);
            
            if (position == POSITION_LAST) {
                results.last();
                
            } else {
                if (results.absolute(position + 1)) {
                    ++position;
                    
                } else {
                    results.absolute(position);
                }
            }
            
            return resultsetToSong(results);
            
    } catch (SQLException e) {
            Logger.getLogger(TAG)
                    .log(Level.SEVERE, "First query failed", e);

        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException e) {
                Logger.getLogger(TAG)
                        .log(Level.SEVERE, "Cleanup failed", e);
            }
        }
        
        return null;
    }
    
    private String getSongQuery(String email) {
        String query = "";
        query += "SELECT t.url, m.title, m.artist, t.tag, t.gefallen, m.cover ";
        query += "FROM tags t, musics m ";
        query += "WHERE t.email = '" + email + "' and t.url = m.url";
        
        return query;
    }
    
    private Song resultsetToSong(ResultSet set) {
        try {
            Song s = new Song();
            s.url = set.getString(1);
            s.title = set.getString(2);
            s.artist = set.getString(3);
            s.tag = set.getString(4);
            s.rating = set.getInt(5);
            
            return s;
        
        } catch(SQLException e) {
            Logger.getLogger(TAG)
                    .log(Level.SEVERE, "Could not convert the set to a song", e);
        }
        
        return null;
    }
    
    private Statement createStatement(Connection connection) throws SQLException {
        return connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
    }
    
    private int position = 1;
}
