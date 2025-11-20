package kr.ac.dankook.ecg_dating.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {EcgData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EcgDataDao ecgDataDao();
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDBInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "ecg_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}