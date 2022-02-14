package com.fahmi.imagemachine.View.Activities;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fahmi.imagemachine.Model.Database.MachineEntity;
import com.fahmi.imagemachine.Model.RecyclerView.Thumbnail;
import com.fahmi.imagemachine.R;
import com.fahmi.imagemachine.View.Dialogs.DeleteConfirmationDialog;
import com.fahmi.imagemachine.View.RecyclerView.ImageListAdapter;
import com.fahmi.imagemachine.ViewModel.Activities.MachineDetailActivityViewModel;
import com.fahmi.imagemachine.ViewModel.Utils.FileUtils;
import com.fahmi.imagemachine.ViewModel.Utils.StatusBarUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MachineDetailActivity extends AppCompatActivity {

    private MachineDetailActivityViewModel viewModel;
    private int machineId, machineImagesCount;
    private MachineEntity machineEntity;
    private String machineImagesBefore;
    private LinearLayout layoutNoImage;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_detail);
        StatusBarUtil.setTransparent(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        TextView txtMachineName, txtMachineType, txtMachineQrCode, txtLastMaintenance;
        txtMachineName = findViewById(R.id.txtMachineName);
        txtMachineType = findViewById(R.id.txtMachineType);
        txtMachineQrCode = findViewById(R.id.txtQrCodeNumber);
        txtLastMaintenance = findViewById(R.id.txtLastMaintenance);

        FloatingActionButton btnAddMachineImage, btnEditMachine, btnDeleteMachine;
        btnAddMachineImage = findViewById(R.id.btnAddMachineImage);
        btnEditMachine = findViewById(R.id.btnEditMachine);
        btnDeleteMachine = findViewById(R.id.btnDeleteMachine);

        CardView layoutDeleteOption = findViewById(R.id.layoutDeleteOption);
        LinearLayout btnDeleteImage, btnCancelDeleteImage;
        btnDeleteImage = findViewById(R.id.btnDeleteImage);
        btnCancelDeleteImage = findViewById(R.id.btnCancelDeleteImage);

        viewModel = new ViewModelProvider(this).get(MachineDetailActivityViewModel.class);
        context = this;

        RecyclerView rvMachineImages = findViewById(R.id.rvMachineImages);
        layoutNoImage = findViewById(R.id.layoutNoImage);
        ImageListAdapter adapter = new ImageListAdapter(new ImageListAdapter.ImageDiff(), width, viewModel, this);
        rvMachineImages.setAdapter(adapter);
        rvMachineImages.setLayoutManager(new GridLayoutManager(this, 2));

        Intent intent = getIntent();
        machineId = intent.getIntExtra("MACHINE_ID", 0);

        viewModel.getMachineById(machineId).observe(this, machine -> {
            if (machine != null){
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                String lastMaintenance = "Last maintenance on:\n" + formatter.format(machine.lastMaintenanceDate);
                String machineQrCode = "QR Code Number: " + machine.machineQrCode;
                machineImagesBefore = machine.machineImages;
                machineEntity = machine;

                txtMachineName.setText(machine.machineName);
                txtMachineType.setText(machine.machineType);
                txtMachineQrCode.setText(machineQrCode);
                txtLastMaintenance.setText(lastMaintenance);

                adapter.submitList(imagePaths());

                if (machineImagesBefore.equals("") || machineImagesBefore.equals("[]"))
                    layoutNoImage.setVisibility(View.VISIBLE);
                else
                    layoutNoImage.setVisibility(View.GONE);
            }
        });

        viewModel.multipleSelect.observe(this, isMultiple -> {
            if (isMultiple){
                layoutDeleteOption.setVisibility(View.VISIBLE);
            } else {
                layoutDeleteOption.setVisibility(View.GONE);
            }
        });

        btnCancelDeleteImage.setOnClickListener((l) -> {
            viewModel.multipleSelect.setValue(false);
            viewModel.imagesArray.clear();
        });

        btnDeleteImage.setOnClickListener((l) -> {
            JSONArray jsonArray = new JSONArray();
            try {
                JSONArray arrayBefore = new JSONArray(machineImagesBefore);
                for (int i = 0; i < arrayBefore.length(); i++){
                    String prevValue = arrayBefore.getString(i);
                    if (!viewModel.imagesArray.contains(prevValue)){
                        jsonArray.put(prevValue);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            viewModel.updateThumbnailImage(machineId, jsonArray.toString());
            viewModel.multipleSelect.setValue(false);
            viewModel.imagesArray.clear();
        });

        btnAddMachineImage.setOnClickListener(v -> {
            if (machineImagesCount < 10){
                Intent getPhotoIntent = new Intent();
                getPhotoIntent.setType("image/*");
                getPhotoIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                getPhotoIntent.setAction(Intent.ACTION_GET_CONTENT);

                galleryResultLauncher.launch(getPhotoIntent);
            } else {
                Toast.makeText(this, "You exceeded the max number of image (10 images)", Toast.LENGTH_LONG).show();
            }
        });

        btnEditMachine.setOnClickListener((l) -> editMachine());
        btnDeleteMachine.setOnClickListener((l) -> deleteMachine());
    }

    void editMachine(){
        Intent editIntent = new Intent(this, AddMachineActivity.class);
        editIntent.putExtra("MACHINE_ID", machineId);
        startActivity(editIntent);
        finish();
    }

    void deleteMachine(){
        DeleteConfirmationDialog dialog = new DeleteConfirmationDialog(this);
        dialog.show();
        dialog.setOnDismissListener((dismiss) -> {
            if (dialog.getDeleteConfirmation()){
                viewModel.deleteMachine(machineEntity);
                finish();
            }
        });
    }

    void toastUrlNotValid(){
        Toast.makeText(context, "Image URL is not valid, please take image from another source or folder", Toast.LENGTH_LONG).show();
    }

    List<Thumbnail> imagePaths(){
        List<Thumbnail> arrayList= new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(machineImagesBefore);
            machineImagesCount = jsonArray.length();
            for (int i = 0; i < machineImagesCount; i++){
                arrayList.add(new Thumbnail(false, jsonArray.getString(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    ActivityResultLauncher<Intent> galleryResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        List<String> arrayString = new ArrayList<>();

                        if ((data != null ? data.getClipData() : null) != null) {
                            ClipData mClipData = data.getClipData();
                            int count = mClipData.getItemCount();
                            if (count <= 10){
                                for (int i = 0; i < count; i++) {
                                    Uri imageUrl = mClipData.getItemAt(i).getUri();
                                    String URL = FileUtils.getPath(context, imageUrl);

                                    if (imageUrl == null || URL == null)
                                        toastUrlNotValid();
                                    else
                                        arrayString.add(URL);
                                }
                            } else {
                                Toast.makeText(this, "Please pick image less than or equal to 10 images", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Uri imageUrl = data != null ? data.getData() : null;
                            String URL = FileUtils.getPath(context, imageUrl);
                            if (imageUrl == null || URL == null)
                                toastUrlNotValid();
                            else
                                arrayString.add(URL);
                        }

                        viewModel.updateThumbnailImage(machineId, machineImagesBefore, arrayString);
                    } else {
                        Toast.makeText(context, "You haven't picked an image", Toast.LENGTH_LONG).show();
                    }
                }
            });
}
