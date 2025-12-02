package com.example.workmanaging.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.workmanaging.model.dao.UserActionDao;
import com.example.workmanaging.model.database.AppDatabase;
import com.example.workmanaging.model.entity.UserAction;
import java.util.List;

public class UserActionRepository {
    private UserActionDao mUserActionDao;

    public UserActionRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mUserActionDao = db.userActionDao();
    }

    public LiveData<List<UserAction>> getActionsForUser(int userId) {
        return mUserActionDao.getActionsForUser(userId);
    }

    public void insert(UserAction action) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mUserActionDao.insert(action);
        });
    }
}
