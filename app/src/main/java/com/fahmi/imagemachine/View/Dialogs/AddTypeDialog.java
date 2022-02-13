package com.fahmi.imagemachine.View.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.fahmi.imagemachine.Model.Database.TypeEntity;
import com.fahmi.imagemachine.R;
import com.fahmi.imagemachine.ViewModel.Activities.AddMachineActivityViewModel;

public class AddTypeDialog extends Dialog {
    public AddTypeDialog(@NonNull Context context, AddMachineActivityViewModel viewModel) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_type);

        EditText edtNewType = findViewById(R.id.edtNewMachineType);
        Button btnAddNewType, btnCancelAdd;
        btnAddNewType = findViewById(R.id.btnAddType);
        btnCancelAdd = findViewById(R.id.btnCancelAdd);

        btnAddNewType.setOnClickListener((l) -> {
            String newType = edtNewType.getText().toString();
            if (!newType.equals(""))
                viewModel.insertType(new TypeEntity(newType));
            dismiss();
        });

        btnCancelAdd.setOnClickListener((l) -> dismiss());
    }
}
