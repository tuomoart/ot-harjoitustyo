/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author tuomoart
 */
public class SQLiteHistoryDao implements HistoryDao {
    private String dbName;
    
    public SQLiteHistoryDao(String dbName) throws SQLException {
        
        this.dbName = dbName;
        
        createTables();
    }
    
    @Override
    public void saveModification(String settings) throws SQLException {
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + this.dbName)) {
            PreparedStatement p = db.prepareStatement("INSERT INTO History (settings) VALUES (?);");
            p.setString(1, settings);
            p.executeUpdate();
        }
    }
    
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
