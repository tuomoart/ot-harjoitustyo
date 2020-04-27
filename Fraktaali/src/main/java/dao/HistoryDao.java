package dao;

import java.sql.SQLException;

/**
 * Interface for using action history
 * 
 * @author tuomoart
 */
public interface HistoryDao {
    
    /**
     * Method for saving new modification event
     * 
     * @param settings correctly formatted string containing settings
     * @throws SQLException 
     */
    void saveModification(String settings) throws SQLException;
    
    /**
     * Method for getting the latest previous settings
     * 
     * @return String containing the settings
     * @throws SQLException 
     */
    String undo() throws SQLException;
    
    /**
     * Method for erasing all events from history
     * 
     * @throws SQLException 
     */
    void empty() throws SQLException;
}
