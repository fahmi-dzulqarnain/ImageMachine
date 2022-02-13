package com.fahmi.imagemachine.ViewModel.Activities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fahmi.imagemachine.Model.Database.MachineEntity;
import com.fahmi.imagemachine.Model.Database.TypeEntity;
import com.fahmi.imagemachine.ViewModel.Database.Repository;

import java.util.List;

public class AddMachineActivityViewModel extends AndroidViewModel {

    private final Repository repository;

    public AddMachineActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public LiveData<MachineEntity> getMachineById(int id) {
        return repository.getById(id);
    }

    public LiveData<List<TypeEntity>> getAllType(){
        return repository.getAlltype();
    }

    public void insertMachine(MachineEntity machine){
        repository.insert(machine);
    }

    public void insertType(TypeEntity type){
        repository.insert(type);
    }

    public int updateMachine(MachineEntity machine){
        repository.update(machine);
        return machine.machineId;
    }
}
