package com.fahmi.imagemachine.View.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fahmi.imagemachine.R;

public class MachineViewHolder extends RecyclerView.ViewHolder {
    TextView txtMachineName, txtMachineType;
    LinearLayout rowMachine;

    private MachineViewHolder(View itemView) {
        super(itemView);
        txtMachineName = itemView.findViewById(R.id.txtMachineName);
        txtMachineType = itemView.findViewById(R.id.txtMachineType);
        rowMachine = itemView.findViewById(R.id.rowMachine);
    }

    public void bind(String machineName, String machineType) {
        txtMachineName.setText(machineName);
        txtMachineType.setText(machineType);
    }

    static MachineViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_machine, parent, false);
        return new MachineViewHolder(view);
    }
}
