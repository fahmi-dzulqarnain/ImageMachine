package com.fahmi.imagemachine.ViewModel.Activities;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fahmi.imagemachine.Model.Database.MachineEntity;
import com.fahmi.imagemachine.Model.Enums.SortMachine;
import com.fahmi.imagemachine.Model.Enums.SortType;
import com.fahmi.imagemachine.ViewModel.Database.Repository;

import java.util.Comparator;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private final Repository repository;
    public SortMachine sortParameter;
    public SortType sortTypeParameter;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        sortParameter = SortMachine.machine_name;
        sortTypeParameter = SortType.ascending;
    }

    public LiveData<List<MachineEntity>> getMachineAsc(){
        return repository.getMachineAsc();
    }

    public void checkPermission(Activity context, String[] permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(context, permission[0]) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(context, permission[1]) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(context, permission, requestCode);
    }

    public static class SortByName implements Comparator<MachineEntity>
    {
        public int compare(MachineEntity a, MachineEntity b) {
            return a.machineName.compareTo(b.machineName);
        }
    }

    public static class SortByNameDesc implements Comparator<MachineEntity> {
        public int compare(MachineEntity a, MachineEntity b) {
            return b.machineName.compareTo(a.machineName);
        }
    }

    public static class SortByType implements Comparator<MachineEntity> {
        public int compare(MachineEntity a, MachineEntity b) {
            return a.machineType.compareTo(b.machineType);
        }
    }

    public static class SortByTypeDesc implements Comparator<MachineEntity> {
        public int compare(MachineEntity a, MachineEntity b) {
            return b.machineType.compareTo(a.machineType);
        }
    }

}
