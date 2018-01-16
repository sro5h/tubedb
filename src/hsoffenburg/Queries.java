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
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ubuntu
 */
public class Queries {
    private static final String TAG = Queries.class.getName();
    private static final int POSITION_LAST = -1;
    private static final int POSITION_FIRST = 1;

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
            
            if (position == POSITION_FIRST) {
                results.first();
            }
            
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
    
    public Song previous(String email) {
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
            }
            
            if (position == POSITION_FIRST) {
                results.first();
                
            } else {
                if (results.absolute(position - 1)) {
                    --position;

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
    
    public boolean save(String email, Song song) {
        Connection connection = null;
        Statement statement = null;
        PreparedStatement preStatement = null;
        ResultSet results = null;

        try {
            connection = Globals.getPoolConnection();
            statement = createStatement(connection);
            
            String songQuery = getSongExistsQuery(song);
            System.out.println("Query: " + songQuery);
            
            results = statement.executeQuery(songQuery);
            
            // Does song exist
            if (results.next()) {
                preStatement = getSongUpdate(connection, song);
                preStatement.execute();
                
                preStatement.close();
                preStatement = null;

            } else {
                preStatement = getSongInsert(connection, song);
                preStatement.execute();
                
                preStatement.close();
                preStatement = null;
            }
            
            results.close();
            statement.close();
            
            statement = createStatement(connection);
                 
            String tagQuery = getTagsExistsQuery(email, song);
            System.out.println("Tag query: " + tagQuery);
            
            results = statement.executeQuery(tagQuery);
            
            if (results.next()) {
                preStatement = getTagUpdate(connection, email, song);
                preStatement.execute();
                
                preStatement.close();
                preStatement = null;
                
            } else {
                preStatement = getTagInsert(connection, email, song);
                preStatement.execute();
                
                preStatement.close();
                preStatement = null;
            }
            
            return true;
            
        } catch (SQLException e) {
            Logger.getLogger(TAG)
                    .log(Level.SEVERE, "Save update failed", e);

        } finally {
            try {
                if (results != null) {
                    results.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (preStatement != null) {
                    preStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException e) {
                Logger.getLogger(TAG)
                        .log(Level.SEVERE, "Cleanup failed", e);
            }
        }
        
        return false;
    }

    private String getSongQuery(String email) {
        String query = "";
        query += "SELECT t.url, m.title, m.artist, t.tag, t.gefallen, m.cover ";
        query += "FROM tags t, musics m ";
        query += "WHERE t.email = '" + email + "' and t.url = m.url";
        
        return query;
    }
    
    private String getSongExistsQuery(Song s) {
        String query = "";
        query += "SELECT * ";
        query += "FROM musics m ";
        query += "WHERE m.url = '" + s.url + "'";

        return query;
    }

    private String getTagsExistsQuery(String email, Song s) {
        String query = "";
        query += "SELECT * ";
        query += "FROM tags ";
        query += "WHERE email = '" + email + "' and url = '" + s.url + "'";
        
        return query;
    }
    
    private PreparedStatement getSongInsert(Connection c, Song s) throws SQLException {
        String insert = "";
        insert += "INSERT INTO musics ";
        insert += "VALUES( '" + s.url + "', '" + s.title + "', '" + s.artist + "', ? )";
        
        System.out.println("Musics insert: " + insert);
        
        PreparedStatement st = c.prepareStatement(insert);
        st.setBlob(1, Converter.Icon2Blob(s.cover, c));
        
        return st;
    }
    
    private PreparedStatement getSongUpdate(Connection c, Song s) throws SQLException {
        String update = "";
        update += "UPDATE musics ";
        update += "SET title = '" + s.title + "', artist = '" + s.artist + "', cover = ? ";
        update += "WHERE url = '" + s.url + "'";
        
        System.out.println("Musics update: " + update);
        
        PreparedStatement st = c.prepareStatement(update);
        st.setBlob(1, Converter.Icon2Blob(s.cover, c));
        
        return st;
    }
    
    private PreparedStatement getTagUpdate(Connection c, String email, Song s) throws SQLException {
        String update = "";
        update += "UPDATE tags ";
        update += "SET gefallen = ?, tag = '" + s.tag + "'";
        update += "WHERE url = '" + s.url + "' and email = '" + email + "'";
        
        System.out.println("Tag update: " + update);
        
        PreparedStatement st = c.prepareStatement(update);
        st.setInt(1, s.rating);
        
        return st;
    }
    
    private PreparedStatement getTagInsert(Connection c, String email, Song s) throws SQLException {
        String insert = "";
        insert += "INSERT INTO tags ";
        insert += "VALUES( '" + email + "', '" + s.url + "', ?, '" + s.tag + "')";
        
        System.out.println("Tag insert: " + insert);
        
        PreparedStatement st = c.prepareStatement(insert);
        st.setInt(1, s.rating);
        
        return st;
    }
    
    private Song resultsetToSong(ResultSet set) {
        try {
            Song s = new Song();
            s.url = set.getString(1);
            s.title = set.getString(2);
            s.artist = set.getString(3);
            s.tag = set.getString(4);
            s.rating = set.getInt(5);
            s.cover = Converter.Blob2Icon(set.getBlob(6));
            
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
