package DAO;

public interface HistoryDao {
    void saveModification(String settings);
    String undo();
    String getLatest();
}
