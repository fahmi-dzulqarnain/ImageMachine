package com.fahmi.imagemachine.View.Activities;

import static com.fahmi.imagemachine.ViewModel.Helper.Serializable.PERMISSION_REQUEST;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fahmi.imagemachine.Model.Enums.SortMachine;
import com.fahmi.imagemachine.Model.Enums.SortType;
import com.fahmi.imagemachine.R;
import com.fahmi.imagemachine.View.RecyclerView.MachineListAdapter;
import com.fahmi.imagemachine.ViewModel.Activities.MainActivityViewModel;
import com.fahmi.imagemachine.ViewModel.Utils.StatusBarUtil;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private MachineListAdapter adapter;
    private LinearLayout layoutNoData;
    private MainActivityViewModel viewModel;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureStatusBar();

        ImageView iconMachineList, iconAddMachine, iconQrCode;
        iconMachineList = findViewById(R.id.iconMachineList);
        iconAddMachine = findViewById(R.id.iconAddMachine);
        iconQrCode = findViewById(R.id.iconQrCode);

        ChipGroup chipGroupChoice = findViewById(R.id.choiceSortBy);
        ImageView btnAscDesc = findViewById(R.id.btnAscDesc);
        btnAscDesc.setTag(R.drawable.icon_descending_sorting);
        Typeface ubuntuRegular = Typeface.createFromAsset(getAssets(), "font/Ubuntu-Regular.ttf");
        Chip chipMachineName, chipMachineType;
        chipMachineName = findViewById(R.id.chipMachineName);
        chipMachineType = findViewById(R.id.chipMachineType);
        chipMachineName.setTypeface(ubuntuRegular);
        chipMachineType.setTypeface(ubuntuRegular);

        RecyclerView rvOfMachines = findViewById(R.id.rvOfMachines);
        layoutNoData = findViewById(R.id.layoutNoData);
        adapter = new MachineListAdapter(new MachineListAdapter.MachineDiff());
        rvOfMachines.setAdapter(adapter);
        rvOfMachines.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvOfMachines.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        btnAscDesc.setOnClickListener((view) -> {
            ImageView imageView = (ImageView) view;
            assert(R.id.btnAscDesc == imageView.getId());

            Integer integer = (Integer) imageView.getTag();
            integer = integer == null ? 0 : integer;

            switch(integer) {
                case R.drawable.icon_ascending_sorting:
                    imageView.setImageResource(R.drawable.icon_descending_sorting);
                    imageView.setTag(R.drawable.icon_descending_sorting);
                    viewModel.sortTypeParameter = SortType.ascending;
                    break;
                case R.drawable.icon_descending_sorting:
                default:
                    imageView.setImageResource(R.drawable.icon_ascending_sorting);
                    imageView.setTag(R.drawable.icon_ascending_sorting);
                    viewModel.sortTypeParameter = SortType.descending;
                    break;
            }

            showRecyclerViewData();
        });

        chipGroupChoice.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = chipGroupChoice.findViewById(checkedId);

            if (chip.getText().toString().equals("Machine Name")){
                viewModel.sortParameter = SortMachine.machine_name;
            } else {
                viewModel.sortParameter = SortMachine.machine_type;
            }

            showRecyclerViewData();
        });
        chipMachineName.setChecked(true);

        viewModel.checkPermission(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, PERMISSION_REQUEST);

        iconAddMachine.setOnClickListener((listener) -> startActivity(new Intent(MainActivity.this, AddMachineActivity.class)));
        iconQrCode.setOnClickListener((listener) -> startActivity(new Intent(MainActivity.this, QrScannerActivity.class)));

        iconMachineList.setColorFilter(Color.parseColor("#355368"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter.getItemCount() == 0) layoutNoData.setVisibility(View.VISIBLE);
        else layoutNoData.setVisibility(View.GONE);
    }

    private void showRecyclerViewData(){
        viewModel.getMachineAsc().observe(this, machines -> {

            if (viewModel.sortTypeParameter == SortType.descending){
                if (viewModel.sortParameter == SortMachine.machine_name)
                    Collections.sort(machines, new MainActivityViewModel.SortByNameDesc());
                else
                    Collections.sort(machines, new MainActivityViewModel.SortByTypeDesc());
            } else if (viewModel.sortTypeParameter == SortType.ascending){
                if (viewModel.sortParameter == SortMachine.machine_name)
                    Collections.sort(machines, new MainActivityViewModel.SortByName());
                else
                    Collections.sort(machines, new MainActivityViewModel.SortByType());
            }

            adapter.submitList(machines);
            if (machines.size() == 0) layoutNoData.setVisibility(View.VISIBLE);
            else layoutNoData.setVisibility(View.GONE);
        });

    }

    private void configureStatusBar(){
        StatusBarUtil.setTransparent(this);
        if (getResources().getConfiguration().uiMode == Configuration.UI_MODE_NIGHT_YES) {
            StatusBarUtil.setDarkMode(this);
        } else if (getResources().getConfiguration().uiMode == Configuration.UI_MODE_NIGHT_NO ||
                getResources().getConfiguration().uiMode == Configuration.UI_MODE_NIGHT_UNDEFINED) {
            StatusBarUtil.setLightMode(this);
        }
    }
}