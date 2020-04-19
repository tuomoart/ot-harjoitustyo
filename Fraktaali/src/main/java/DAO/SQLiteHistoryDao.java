/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

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
    
    public SQLiteHistoryDao(String dbName) {
        this.dbName=dbName;
        
        createTables();
    }
    
    @Override
    public void saveModification(String settings) {
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + this.dbName)) {
            PreparedStatement p = db.prepareStatement("INSERT INTO History (settings) VALUES (?);");
            p.setString(1,settings);
            p.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    @Override
    public String undo() {
        String response = "";
        
        removeLastFromHistory();
        
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + this.dbName)) {
            PreparedStatement p = db.prepareStatement("SELECT settings FROM History ORDER BY id DESC LIMIT 1;");
            ResultSet r = p.executeQuery();
            response = r.getString("settings");
        } catch (SQLException e) {
            System.out.println(e);
            //TODO Replace exception return to return default settings
        }
        
        return response;
    }
    
    @Override
    public String getLatest() {
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + this.dbName)) {
            PreparedStatement p = db.prepareStatement("SELECT settings FROM History ORDER BY id DESC LIMIT 1;");
            ResultSet r = p.executeQuery();
            return r.getString("settings");
        } catch (SQLException e) {
            System.out.println(e);
            return "";
        }
    }
    
    private void removeLastFromHistory() {
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + this.dbName)) {
            PreparedStatement p = db.prepareStatement("DELETE FROM History WHERE id=(SELECT id FROM History ORDER BY id DESC LIMIT 1)");
            p.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void createTables() {
        try (Connection db = DriverManager.getConnection("jdbc:sqlite:" + this.dbName)) {
            Statement s = db.createStatement();
            
            s.execute("CREATE TABLE IF NOT EXISTS History (id INTEGER PRIMARY KEY, settings TEXT);");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
