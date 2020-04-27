package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for using SQLite-database for event history
 * Implements the HistoryDao-interface
 * 
 * @author tuomoart
 */
public class SQLiteHistoryDao implements HistoryDao {
    private final String dbName;
    
    /**
     * Create new SQLiteHistoryDao-object
     * 
     * @param dbName name of the database-file
     * @throws SQLException 
     */
    public SQLiteHistoryDao(String dbName) throws SQLException {
        
        this.dbName = dbName;
        
        createTables();
    }
    
    /**
     * Method for saving new modification event
     * 
     * @param settings correctly formatted string containing settings
     * @throws SQLException 
     */
    @Override
    public void saveModification(String settings) throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + this.dbName)) {
            PreparedStatement p = db.prepareStatement("INSERT INTO History (settings) VALUES (?);");
            p.setString(1, settings);
            p.executeUpdate();
        }
    }
    
    /**
     * Method for getting the latest previous settings
     * 
     * @return String containing the settings
     * @throws SQLException 
     */
    @Override
    public String undo() throws SQLException {
        String response = "";
        
        removeLastFromHistory();
        
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + this.dbName)) {
            PreparedStatement p = db.prepareStatement("SELECT settings FROM History ORDER BY id DESC LIMIT 1;");
            ResultSet r = p.executeQuery();
            response = r.getString("settings");
        }
        
        return response;
    }
    
    /**
     * Method for erasing all events from history
     * 
     * @throws SQLException 
     */
    @Override
    public void empty() throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + this.dbName)) {
            Statement s = db.createStatement();
            
            s.execute("DELETE FROM History;");
        }
    }
    
    private void removeLastFromHistory() throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + this.dbName)) {
            PreparedStatement p = db.prepareStatement("DELETE FROM History WHERE id=(SELECT id FROM History ORDER BY id DESC LIMIT 1)");
            p.executeUpdate();
        }
    }

    private void createTables() throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + this.dbName)) {
            Statement s = db.createStatement();
            
            s.execute("CREATE TABLE IF NOT EXISTS History (id INTEGER PRIMARY KEY, settings TEXT);");
        }
    }
}
