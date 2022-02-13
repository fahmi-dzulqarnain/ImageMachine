package com.fahmi.imagemachine.View.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.fahmi.imagemachine.R;

import java.io.File;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    ImageView thumbnailImage;
    RelativeLayout layoutSelected;

    private ImageViewHolder(View itemView) {
        super(itemView);
        thumbnailImage = itemView.findViewById(R.id.imgThumbnail);
        layoutSelected = itemView.findViewById(R.id.layoutSelected);
    }

    public void bind(File imgFile) {
        thumbnailImage.setImageURI(Uri.fromFile(imgFile));
    }

    public void isSelected(boolean selected){
        if (selected) layoutSelected.setVisibility(View.VISIBLE);
        else layoutSelected.setVisibility(View.GONE);
    }

    static ImageViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_image, parent, false);
        return new ImageViewHolder(view);
    }
}
