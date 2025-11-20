package kr.ac.dankook.ecg_dating.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EcgDataDao {
    @Insert
    void insert(EcgData ecgData);

    @Query("SELECT * FROM ecg_table ORDER BY id DESC")
    List<EcgData> getAllEcgData();

    @Query("SELECT * FROM ecg_table WHERE userId = :userId ORDER BY id DESC")
    List<EcgData> findDataByUserId(String userId);

    @Query("DELETE FROM ecg_table")
    void deleteAll();
}