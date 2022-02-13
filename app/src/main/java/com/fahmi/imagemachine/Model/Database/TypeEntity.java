package com.fahmi.imagemachine.Model.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "type_table")
public class TypeEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "type_name")
    public String typeName;

    public TypeEntity(@NonNull String typeName){
        this.typeName = typeName;
    }
}
