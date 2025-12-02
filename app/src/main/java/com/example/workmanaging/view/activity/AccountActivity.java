package com.example.workmanaging.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.workmanaging.R;
import com.example.workmanaging.viewmodel.UserViewModel;

public class AccountActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private static final String PREFS_NAME = "WorkManagingPrefs";
    private static final String KEY_USER_ID = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        ImageButton btnBackTop = findViewById(R.id.btn_back_top);
        FrameLayout btnBackBottom = findViewById(R.id.btn_back_bottom);
        TextView tvUsername = findViewById(R.id.tv_username);
        TextView tvEmail = findViewById(R.id.tv_email);
        TextView tvProfession = findViewById(R.id.tv_profession);

        btnBackTop.setOnClickListener(v -> finish());
        btnBackBottom.setOnClickListener(v -> finish());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        int userId = prefs.getInt(KEY_USER_ID, -1);

        if (userId != -1) {
            userViewModel.getUserById(userId).observe(this, user -> {
                if (user != null) {
                    tvUsername.setText(user.nomeUtente);
                    tvEmail.setText(user.email);
                    tvProfession.setText(user.professione != null && !user.professione.isEmpty() ? user.professione : "Not specified");
                }
            });
        }
    }
}
