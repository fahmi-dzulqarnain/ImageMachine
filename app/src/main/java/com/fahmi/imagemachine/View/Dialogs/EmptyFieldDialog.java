package com.fahmi.imagemachine.View.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.fahmi.imagemachine.R;

public class EmptyFieldDialog extends Dialog {
    public EmptyFieldDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_field_empty);

        Button btnDismiss = findViewById(R.id.btnDismiss);
        btnDismiss.setOnClickListener((l) -> dismiss());
    }
}
