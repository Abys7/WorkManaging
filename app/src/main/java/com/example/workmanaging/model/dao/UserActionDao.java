package com.example.workmanaging.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.workmanaging.model.entity.UserAction;
import java.util.List;

@Dao
public interface UserActionDao {
    @Insert
    void insert(UserAction action);

    @Query("SELECT * FROM user_actions WHERE user_id = :userId ORDER BY timestamp DESC")
    LiveData<List<UserAction>> getActionsForUser(int userId);
}
