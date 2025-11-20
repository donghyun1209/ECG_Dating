package kr.ac.dankook.ecg_dating.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ecg_table")
public class EcgData {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private double hrv;
    private String userId;
    private long measuredAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getHrv() {
        return hrv;
    }

    public void setHrv(double hrv) {
        this.hrv = hrv;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getMeasuredAt() {
        return measuredAt;
    }

    public void setMeasuredAt(long measuredAt) {
        this.measuredAt = measuredAt;
    }
}
