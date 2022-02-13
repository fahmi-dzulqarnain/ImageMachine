package com.fahmi.imagemachine.ViewModel.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.fahmi.imagemachine.Model.Database.MachineEntity;
import com.fahmi.imagemachine.Model.Database.TypeEntity;
import com.fahmi.imagemachine.ViewModel.Database.Daos.MachineDao;
import com.fahmi.imagemachine.ViewModel.Database.Daos.TypeDao;

import java.util.List;

public class Repository {

    private final MachineDao machineDao;
    private final TypeDao typeDao;

    public Repository(Application application){
        AppDatabase database = AppDatabase.getDatabase(application);
        machineDao = database.machineDao();
        typeDao = database.typeDao();
    }

    public LiveData<List<MachineEntity>> getMachineAsc(){
        return machineDao.getAllAsc();
    }

    public LiveData<List<TypeEntity>> getAlltype(){
        return typeDao.getAll();
    }

    public LiveData<MachineEntity> getById(int machineId) {
        return machineDao.getById(machineId);
    }

    public LiveData<Integer> getByQrCode(int qrCodeNumber) {
        return machineDao.getByQrCode(qrCodeNumber);
    }

    public void insert(MachineEntity machineEntity){
        AppDatabase.databaseWriteExecutor.execute(() -> machineDao.insert(machineEntity));
    }

    public void update(MachineEntity machineEntity){
        AppDatabase.databaseWriteExecutor.execute(() -> machineDao.update(machineEntity));
    }

    public void update(int machineId, String machineImages){
        AppDatabase.databaseWriteExecutor.execute(() -> machineDao.updateById(machineId, machineImages));
    }

    public void delete(MachineEntity machineEntity){
        AppDatabase.databaseWriteExecutor.execute(() -> machineDao.delete(machineEntity));
    }

    public void insert(TypeEntity typeEntity){
        AppDatabase.databaseWriteExecutor.execute(() -> typeDao.insert(typeEntity));
    }

}
