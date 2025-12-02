package com.example.workmanaging.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.workmanaging.model.entity.UserAction;
import com.example.workmanaging.repository.UserActionRepository;
import java.util.List;

public class UserActionViewModel extends AndroidViewModel {
    private UserActionRepository mRepository;

    public UserActionViewModel(@NonNull Application application) {
        super(application);
        mRepository = new UserActionRepository(application);
    }

    public LiveData<List<UserAction>> getActionsForUser(int userId) {
        return mRepository.getActionsForUser(userId);
    }

    public void insert(UserAction action) {
        mRepository.insert(action);
    }
}
