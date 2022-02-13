package com.fahmi.imagemachine.Model.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "machine_table")
public class MachineEntity {
    @PrimaryKey
    public int machineId;

    @ColumnInfo(name = "machine_name")
    public String machineName;

    @ColumnInfo(name = "machine_type")
    public String machineType;

    @ColumnInfo(name = "machine_qr_code")
    public int machineQrCode;

    @ColumnInfo(name = "last_maintenance_date")
    public Date lastMaintenanceDate;

    @ColumnInfo(name = "machine_images")
    public String machineImages;

    public MachineEntity(int machineId, String machineName, String machineType,
                         int machineQrCode, Date lastMaintenanceDate, String machineImages) {
        this.machineId = machineId;
        this.machineName = machineName;
        this.machineType = machineType;
        this.machineQrCode = machineQrCode;
        this.lastMaintenanceDate = lastMaintenanceDate;
        this.machineImages = machineImages;
    }
}
