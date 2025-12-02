package com.example.workmanaging.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.workmanaging.model.entity.User;
import com.example.workmanaging.repository.UserRepository;
import java.security.MessageDigest;

public class UserViewModel extends AndroidViewModel {
    private UserRepository mRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        mRepository = new UserRepository(application);
    }

    public LiveData<User> getUserByEmail(String email) {
        return mRepository.getUserByEmail(email);
    }
    
    public LiveData<User> getUserByUsername(String username) {
        return mRepository.getUserByUsername(username);
    }

    public LiveData<User> getUserById(int id) {
        return mRepository.getUserById(id);
    }

    public void insert(User user) {
        user.password = hashPassword(user.password);
        mRepository.insert(user);
    }
    
    public void update(User user) {
        mRepository.update(user);
    }
    
    public boolean verifyPassword(String inputPassword, String storedHash) {
        return hashPassword(inputPassword).equals(storedHash);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
