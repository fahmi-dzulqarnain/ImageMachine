package com.fahmi.imagemachine.ViewModel.Database.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fahmi.imagemachine.Model.Database.TypeEntity;

import java.util.List;

@Dao
public interface TypeDao {
    @Insert
    void insert(TypeEntity type);

    @Delete
    void delete(TypeEntity type);

    @Update
    void update(TypeEntity type);

    @Query("SELECT * FROM type_table ORDER BY type_name ASC")
    LiveData<List<TypeEntity>> getAll();
}
