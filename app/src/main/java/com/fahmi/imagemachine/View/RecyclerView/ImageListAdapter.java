package com.fahmi.imagemachine.View.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.fahmi.imagemachine.Model.RecyclerView.Thumbnail;
import com.fahmi.imagemachine.View.Activities.FullScreenActivity;
import com.fahmi.imagemachine.ViewModel.Helper.Serializable;
import com.fahmi.imagemachine.ViewModel.Activities.MachineDetailActivityViewModel;

import java.io.File;
import java.util.Objects;

public class ImageListAdapter extends ListAdapter<Thumbnail, ImageViewHolder> {

    int screenWidth;
    MachineDetailActivityViewModel viewModel;
    LifecycleOwner owner;

    public ImageListAdapter(@NonNull DiffUtil.ItemCallback<Thumbnail> diffCallback, int screenWidth,
                            MachineDetailActivityViewModel viewModel, LifecycleOwner owner){
        super(diffCallback);
        this.screenWidth = screenWidth;
        this.viewModel = viewModel;
        this.owner = owner;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ImageViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Thumbnail thumbnail = getItem(position);
        File imageFile = new File(thumbnail.imageURL);
        Context context = holder.itemView.getContext();

        holder.thumbnailImage.getLayoutParams().height = screenWidth / 2;
        holder.thumbnailImage.getLayoutParams().width = screenWidth / 2;

        holder.bind(imageFile);
        holder.thumbnailImage.setOnClickListener((l) -> {
            if (Objects.requireNonNull(viewModel.multipleSelect.getValue())){
                thumbnail.isSelected = !thumbnail.isSelected;
                holder.isSelected(thumbnail.isSelected);

                if (thumbnail.isSelected){
                    viewModel.imagesArray.add(thumbnail.imageURL);
                } else {
                    viewModel.imagesArray.remove(thumbnail.imageURL);
                }

            } else {
                Serializable.imageFile = imageFile;
                context.startActivity(new Intent(context, FullScreenActivity.class));
            }
        });

        holder.thumbnailImage.setOnLongClickListener((l) -> {
            viewModel.multipleSelect.setValue(true);
            thumbnail.isSelected = true;
            holder.isSelected(true);
            viewModel.imagesArray.add(thumbnail.imageURL);
            return true;
        });

        viewModel.multipleSelect.observe(owner, isMultiple -> {
            if (!isMultiple) holder.isSelected(false);
        });
    }

    public static class ImageDiff extends DiffUtil.ItemCallback<Thumbnail> {

        @Override
        public boolean areItemsTheSame(@NonNull Thumbnail oldItem, @NonNull Thumbnail newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Thumbnail oldItem, @NonNull Thumbnail newItem) {
            return oldItem.imageURL.equals(newItem.imageURL);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
