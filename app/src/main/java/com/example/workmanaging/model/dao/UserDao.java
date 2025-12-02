package com.example.workmanaging.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.workmanaging.model.entity.User;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM utente WHERE email = :email LIMIT 1")
    LiveData<User> getUserByEmail(String email);

    @Query("SELECT * FROM utente WHERE user_id = :id")
    LiveData<User> getUserById(int id);
    
    @Query("SELECT * FROM utente WHERE nome_utente = :username LIMIT 1")
    LiveData<User> getUserByUsername(String username);
}
