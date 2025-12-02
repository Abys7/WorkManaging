package com.example.workmanaging.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "user_actions")
public class UserAction {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "action_type")
    public String actionType;

    @ColumnInfo(name = "reference_id")
    public int referenceId;

    @ColumnInfo(name = "timestamp")
    public Date timestamp;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "subtitle")
    public String subtitle;
    
    @ColumnInfo(name = "status")
    public String status;

    public UserAction() {}
}
