package com.example.workmanaging.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.workmanaging.model.dao.ProgettoDao;
import com.example.workmanaging.model.database.AppDatabase;
import com.example.workmanaging.model.entity.Progetto;
import java.util.List;

public class ProgettoRepository {
    private ProgettoDao mProgettoDao;

    public ProgettoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mProgettoDao = db.progettoDao();
    }

    public LiveData<List<Progetto>> getProjectsForUser(int userId) {
        return mProgettoDao.getProjectsForUser(userId);
    }

    public void insert(Progetto progetto, OnInsertListener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long id = mProgettoDao.insert(progetto);
            if (listener != null) {
                listener.onInsert(id);
            }
        });
    }

    public interface OnInsertListener {
        void onInsert(long id);
    }

    public void delete(Progetto progetto) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mProgettoDao.delete(progetto);
        });
    }
    
    public void update(Progetto progetto) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mProgettoDao.update(progetto);
        });
    }
}
