package com.fahmi.imagemachine.View.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.ScanMode;
import com.fahmi.imagemachine.R;
import com.fahmi.imagemachine.ViewModel.Activities.QrScannerActivityViewModel;

public class QrScannerActivity extends AppCompatActivity {

    private CodeScanner codeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        QrScannerActivityViewModel viewModel = new ViewModelProvider(this).get(QrScannerActivityViewModel.class);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        setupCodeScanner(scannerView);

        codeScanner.setDecodeCallback(result -> runOnUiThread(() ->
                viewModel.getIdOfMachine(Integer.parseInt(result.getText())).observe(this, id -> {
                    Intent intent = new Intent(this, MachineDetailActivity.class);
                    intent.putExtra("MACHINE_ID", id);
                    startActivity(intent);
                    finish();
        })));

        scannerView.setOnClickListener(view -> codeScanner.startPreview());
    }

    private void setupCodeScanner(CodeScannerView scannerView){
        codeScanner = new CodeScanner(this, scannerView);

        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);
        codeScanner.setScanMode(ScanMode.SINGLE);
        codeScanner.setAutoFocusEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}