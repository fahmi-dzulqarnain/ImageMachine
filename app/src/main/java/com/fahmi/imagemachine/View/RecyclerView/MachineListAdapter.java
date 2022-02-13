package com.fahmi.imagemachine.View.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.fahmi.imagemachine.Model.Database.MachineEntity;
import com.fahmi.imagemachine.View.Activities.MachineDetailActivity;

public class MachineListAdapter extends ListAdapter<MachineEntity, MachineViewHolder> {

    public MachineListAdapter(@NonNull DiffUtil.ItemCallback<MachineEntity> diffCallback){
        super(diffCallback);
    }

    @NonNull
    @Override
    public MachineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MachineViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MachineViewHolder holder, int position) {
        MachineEntity machineEntity = getItem(position);
        Context context = holder.itemView.getContext();
        holder.bind(machineEntity.machineName, machineEntity.machineType);
        holder.rowMachine.setOnClickListener((l) -> {
            Intent intent = new Intent(context, MachineDetailActivity.class);
            intent.putExtra("MACHINE_ID", machineEntity.machineId);
            context.startActivity(intent);
        });
    }

    public static class MachineDiff extends DiffUtil.ItemCallback<MachineEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull MachineEntity oldItem, @NonNull MachineEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MachineEntity oldItem, @NonNull MachineEntity newItem) {
            return oldItem.machineName.equals(newItem.machineName);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
