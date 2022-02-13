package com.fahmi.imagemachine.ViewModel.Activities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fahmi.imagemachine.ViewModel.Database.Repository;

public class QrScannerActivityViewModel extends AndroidViewModel {

    private final Repository repository;

    public QrScannerActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public LiveData<Integer> getIdOfMachine(int qrCodeNumber){
        return repository.getByQrCode(qrCodeNumber);
    }
}
