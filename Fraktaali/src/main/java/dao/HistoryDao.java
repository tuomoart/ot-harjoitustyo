package dao;

import java.sql.SQLException;

public interface HistoryDao {
    void saveModification(String settings) throws SQLException;
    String undo() throws SQLException;
}
