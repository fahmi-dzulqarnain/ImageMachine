package com.fahmi.imagemachine.ViewModel.Database.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fahmi.imagemachine.Model.Database.MachineEntity;

import java.util.List;

@Dao
public interface MachineDao {
    @Insert
    void insert(MachineEntity machine);

    @Delete
    void delete(MachineEntity machine);

    @Update
    void update(MachineEntity machine);

    @Query("UPDATE machine_table SET machine_images = :imageArray WHERE machineId = :machineId")
    void updateById(int machineId, String imageArray);

    @Query("SELECT * FROM machine_table WHERE machineId = :machineid")
    LiveData<MachineEntity> getById(int machineid);

    @Query("SELECT machineId FROM machine_table WHERE machine_qr_code = :qrCodeNumber")
    LiveData<Integer> getByQrCode(int qrCodeNumber);

    @Query("SELECT * FROM machine_table")
    LiveData<List<MachineEntity>> getAllAsc();
}
