package kr.ac.dankook.ecg_dating.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List; // [중요] 리스트 기능을 위해 필요

@Dao
public interface EcgDataDao {

    @Insert
    void insert(EcgData ecgData);

    // 모든 데이터 가져오기
    @Query("SELECT * FROM ecg_table ORDER BY id DESC")
    List<EcgData> getAllEcgData();

    // 특정 유저 데이터 가져오기
    @Query("SELECT * FROM ecg_table WHERE userId = :userId ORDER BY measuredAt ASC")
    List<EcgData> getAllDataForUser(String userId);

    // 특정 유저 평균값 가져오기
    @Query("SELECT AVG(hrv) FROM ecg_table WHERE userId = :userId")
    double getAverageHrv(String userId);

    @Query("DELETE FROM ecg_table")
    void deleteAll();
}