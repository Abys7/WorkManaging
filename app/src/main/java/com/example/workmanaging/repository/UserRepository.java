package com.example.workmanaging.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.workmanaging.model.dao.UserDao;
import com.example.workmanaging.model.database.AppDatabase;
import com.example.workmanaging.model.entity.User;

public class UserRepository {
    private UserDao mUserDao;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mUserDao = db.userDao();
    }

    public LiveData<User> getUserByEmail(String email) {
        return mUserDao.getUserByEmail(email);
    }
    
    public LiveData<User> getUserByUsername(String username) {
        return mUserDao.getUserByUsername(username);
    }
    
    public LiveData<User> getUserById(int id) {
        return mUserDao.getUserById(id);
    }

    public void insert(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.insert(user);
        });
    }

    public void update(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.update(user);
        });
    }
}
