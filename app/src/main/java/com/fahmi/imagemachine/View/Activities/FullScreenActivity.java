package com.fahmi.imagemachine.View.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.fahmi.imagemachine.R;
import com.fahmi.imagemachine.ViewModel.Helper.Serializable;

public class FullScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        ImageView imgFullScreen = findViewById(R.id.imgFullScreen);
        imgFullScreen.setImageURI(Uri.fromFile(Serializable.imageFile));
    }
}