package com.fahmi.imagemachine.View.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.fahmi.imagemachine.Model.Database.MachineEntity;
import com.fahmi.imagemachine.R;
import com.fahmi.imagemachine.View.Dialogs.AddTypeDialog;
import com.fahmi.imagemachine.View.Dialogs.EmptyFieldDialog;
import com.fahmi.imagemachine.ViewModel.Activities.AddMachineActivityViewModel;
import com.fahmi.imagemachine.ViewModel.Utils.StatusBarUtil;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AddMachineActivity extends AppCompatActivity {

    private AddMachineActivityViewModel viewModel;
    private String machineImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_machine);
        StatusBarUtil.setTransparent(this);

        EditText edtMachineName, edtMachineQrCode, edtLastMaintenance;
        edtMachineName = findViewById(R.id.edtMachineName);
        AutoCompleteTextView dropdownMachineType = findViewById(R.id.edtMachineType);
        edtMachineQrCode = findViewById(R.id.edtMachineQrCode);
        edtLastMaintenance = findViewById(R.id.edtMachineLastMaintenance);

        Button btnAddMachine, btnCancel;
        btnAddMachine = findViewById(R.id.btnAddMachine);
        btnCancel = findViewById(R.id.btnCancel);

        RelativeLayout btnAddType, btnChooseDate;
        btnAddType = findViewById(R.id.btnAddType);
        btnChooseDate = findViewById(R.id.btnChooseDate);

        MaterialDatePicker.Builder<Long> datePickerBuilder = MaterialDatePicker.Builder.datePicker();
        datePickerBuilder.setTitleText(R.string.last_maintenance_date);
        MaterialDatePicker<Long> datePicker = datePickerBuilder.build();

        Context context = this;
        viewModel = new ViewModelProvider(this).get(AddMachineActivityViewModel.class);

        viewModel.getAllType().observe(this, typeEntities -> {
            List<String> types = new ArrayList<>();
            for (int i = 0; i < typeEntities.size(); i++) { types.add(typeEntities.get(i).typeName); }
            ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, types);
            dropdownMachineType.setAdapter(dropdownAdapter);
        });

        dropdownMachineType.setInputType(InputType.TYPE_NULL);
        edtLastMaintenance.setInputType(InputType.TYPE_NULL);

        btnChooseDate.setOnClickListener((l) ->
            datePicker.show(getSupportFragmentManager(), "MAINTENANCE_DATE_PICKER"));
        final Long[] machineMaintenance = {0L};
        datePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<? super Long>) selection -> {
            edtLastMaintenance.setText(datePicker.getHeaderText());
            machineMaintenance[0] = selection;
        });

        btnAddType.setOnClickListener((l) -> new AddTypeDialog(context, viewModel).show());

        Intent intentUpdate = getIntent();
        int id = intentUpdate.getIntExtra("MACHINE_ID", 0);
        if (id != 0){
            viewModel.getMachineById(id).observe(this, machine ->{
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                String lastMaintenance = formatter.format(machine.lastMaintenanceDate);

                edtMachineName.setText(machine.machineName);
                dropdownMachineType.setText(machine.machineType);
                edtMachineQrCode.setText(String.valueOf(machine.machineQrCode));
                machineMaintenance[0] = machine.lastMaintenanceDate.getTime();
                edtLastMaintenance.setText(lastMaintenance);
                machineImages = machine.machineImages;

                btnAddMachine.setText(R.string.edit);
            });
        }

        btnAddMachine.setOnClickListener((listener) -> {
            String machineName, machineType, machineQr;
            machineName = edtMachineName.getText().toString();
            machineType = dropdownMachineType.getText().toString();
            machineQr = edtMachineQrCode.getText().toString();

            if (!machineName.equals("") &&
                !machineType.equals("") &&
                !machineQr.equals("") &&
                machineMaintenance[0] != 0L) {

                int machineQrCode = Integer.parseInt(edtMachineQrCode.getText().toString());
                Date lastMaintenance = new Date(machineMaintenance[0]);

                int machineId;
                if (id == 0)
                    machineId = insert(machineName, machineType, machineQrCode, lastMaintenance);
                else
                    machineId = viewModel.updateMachine(new MachineEntity(id, machineName,
                            machineType, machineQrCode, lastMaintenance, machineImages));

                Intent intent = new Intent(this, MachineDetailActivity.class);
                intent.putExtra("MACHINE_ID", machineId);
                startActivity(intent);
                finish();
            } else {
                new EmptyFieldDialog(this).show();
            }
        });

        btnCancel.setOnClickListener((l) -> finish());
    }

    private int insert(String machineName, String machineType, int machineQrCode, Date lastMaintenance){
        int randomId = new Random().nextInt(Integer.MAX_VALUE);
        viewModel.insertMachine(new MachineEntity(
                randomId, machineName, machineType, machineQrCode, lastMaintenance, ""));
        return randomId;
    }
}