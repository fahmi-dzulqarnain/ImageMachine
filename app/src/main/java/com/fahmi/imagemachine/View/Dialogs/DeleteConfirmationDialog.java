package com.fahmi.imagemachine.View.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.fahmi.imagemachine.R;

public class DeleteConfirmationDialog extends Dialog {

    private boolean deleteConfirmation = false;

    public DeleteConfirmationDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete_confirmation);

        Button btnYes, btnNo;
        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);

        btnYes.setOnClickListener((l) -> {
            deleteConfirmation = true;
            dismiss();
        });

        btnNo.setOnClickListener((l) -> dismiss());
    }

    public boolean getDeleteConfirmation(){
        return deleteConfirmation;
    }
}
