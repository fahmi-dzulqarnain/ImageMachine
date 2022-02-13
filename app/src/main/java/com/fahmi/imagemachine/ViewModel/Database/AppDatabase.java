package com.fahmi.imagemachine.ViewModel.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.fahmi.imagemachine.Model.Database.MachineEntity;
import com.fahmi.imagemachine.Model.Database.TypeEntity;
import com.fahmi.imagemachine.ViewModel.Database.Daos.MachineDao;
import com.fahmi.imagemachine.ViewModel.Database.Daos.TypeDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {MachineEntity.class, TypeEntity.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract MachineDao machineDao();
    public abstract TypeDao typeDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AppDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (AppDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .addMigrations(MIGRATION_1_2).build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `type_table` (`type_name` TEXT NOT NULL, PRIMARY KEY(`type_name`))");
        }
    };
}
