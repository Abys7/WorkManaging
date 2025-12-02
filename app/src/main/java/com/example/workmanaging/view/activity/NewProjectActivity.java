package com.example.workmanaging.view.activity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.workmanaging.R;
import com.example.workmanaging.model.entity.Cliente;
import com.example.workmanaging.model.entity.JobStatus;
import com.example.workmanaging.model.entity.Progetto;
import com.example.workmanaging.model.entity.UserAction;
import com.example.workmanaging.viewmodel.ClienteViewModel;
import com.example.workmanaging.viewmodel.ProgettoViewModel;
import com.example.workmanaging.viewmodel.UserActionViewModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewProjectActivity extends AppCompatActivity {

    private EditText etTitle, etDesc;
    private TextView tvStartDate, tvEndDate;
    private Spinner spinnerClients;
    private ProgettoViewModel progettoViewModel;
    private ClienteViewModel clienteViewModel;
    private UserActionViewModel userActionViewModel;
    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();
    private List<Cliente> clientList = new ArrayList<>();
    private static final String PREFS_NAME = "WorkManagingPrefs";
    private static final String KEY_USER_ID = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        etTitle = findViewById(R.id.et_project_title);
        etDesc = findViewById(R.id.et_project_desc);
        tvStartDate = findViewById(R.id.tv_start_date);
        tvEndDate = findViewById(R.id.tv_end_date);
        spinnerClients = findViewById(R.id.spinner_clients);
        ImageButton btnSave = findViewById(R.id.btn_save);

        progettoViewModel = new ViewModelProvider(this).get(ProgettoViewModel.class);
        clienteViewModel = new ViewModelProvider(this).get(ClienteViewModel.class);
        userActionViewModel = new ViewModelProvider(this).get(UserActionViewModel.class);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        int userId = prefs.getInt(KEY_USER_ID, -1);

        if (userId == -1) {
            finish();
            return;
        }

        setupSpinners(userId);
        setupDatePickers();

        btnSave.setOnClickListener(v -> saveProject(userId));
    }

    private void setupSpinners(int userId) {
        clienteViewModel.getClientsForUser(userId).observe(this, clients -> {
            clientList = clients;
            List<String> clientNames = new ArrayList<>();
            clientNames.add("None"); // Option for no client
            for (Cliente c : clients) {
                clientNames.add(c.nome);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clientNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerClients.setAdapter(adapter);
        });
    }

    private void setupDatePickers() {
        tvStartDate.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                startCalendar.set(year, month, dayOfMonth);
                updateLabel(tvStartDate, startCalendar);
            }, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        tvEndDate.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                endCalendar.set(year, month, dayOfMonth);
                updateLabel(tvEndDate, endCalendar);
            }, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateLabel(TextView textView, Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        textView.setText(sdf.format(calendar.getTime()));
    }

    private void saveProject(int userId) {
        String title = etTitle.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        
        if (title.isEmpty()) {
            Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show();
            return;
        }

        Progetto newProject = new Progetto();
        newProject.userId = userId;
        newProject.titolo = title;
        newProject.descrizione = desc;

        if (!tvStartDate.getText().toString().equals("Select Start Date")) {
            newProject.inizio = startCalendar.getTime();
        }
        if (!tvEndDate.getText().toString().equals("Select End Date")) {
            newProject.scadenza = endCalendar.getTime();
        }

        int selectedPosition = spinnerClients.getSelectedItemPosition();
        if (selectedPosition > 0 && !clientList.isEmpty()) {
            newProject.clienteId = clientList.get(selectedPosition - 1).clienteId;
        } else {
            newProject.clienteId = null;
        }

        newProject.stato = calculateStatus(newProject.inizio, newProject.scadenza);

        progettoViewModel.insert(newProject, id -> {
            // Log the action
            UserAction action = new UserAction();
            action.userId = userId;
            action.actionType = "CREATE_PROJECT";
            action.referenceId = (int) id;
            action.timestamp = new Date();
            action.title = "New Project: " + title;
            action.subtitle = newProject.stato != null ? newProject.stato.name() : "";
            action.status = newProject.stato != null ? newProject.stato.name() : "";
            
            userActionViewModel.insert(action);

            runOnUiThread(() -> {
                Toast.makeText(this, "Project saved", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private JobStatus calculateStatus(Date start, Date end) {
        Date now = new Date();
        if (end != null && now.after(end)) {
            return JobStatus.COMPLETATO;
        } else if (start != null && now.after(start)) {
            return JobStatus.IN_CORSO;
        } else {
            return JobStatus.NON_INIZIATO;
        }
    }
}
