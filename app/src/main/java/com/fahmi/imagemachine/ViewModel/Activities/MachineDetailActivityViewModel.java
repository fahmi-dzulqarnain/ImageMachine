package com.fahmi.imagemachine.ViewModel.Activities;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fahmi.imagemachine.Model.Database.MachineEntity;
import com.fahmi.imagemachine.ViewModel.Database.Repository;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MachineDetailActivityViewModel extends AndroidViewModel {

    private final Repository repository;
    public MutableLiveData<Boolean> multipleSelect;
    public List<String> imagesArray;

    public MachineDetailActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        multipleSelect = new MutableLiveData<>();
        imagesArray = new ArrayList<>();
        multipleSelect.setValue(false);
    }

    public LiveData<MachineEntity> getMachineById(int id) {
        return repository.getById(id);
    }

    public void updateThumbnailImage(int machineId, String imageArray){
        repository.update(machineId, imageArray);
    }

    public void updateThumbnailImage(int machineId, String imageArray, List<String> newImage){
        String newArray = imageArray;
        try {
            JSONArray array = new JSONArray();
            if (!imageArray.equals("") && !imageArray.equals("[null]")) array = new JSONArray(imageArray);
            for (String s : newImage) {
                array.put(s);
            }
            newArray = array.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG", "updateThumbnailImage: " + e);
        }
        repository.update(machineId, newArray);
    }

    public void deleteMachine(MachineEntity machine){
        repository.delete(machine);
    }
}
