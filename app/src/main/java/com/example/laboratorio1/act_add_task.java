package com.example.laboratorio1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.laboratorio1.Models.Category;
import com.example.laboratorio1.Models.Database;
import com.example.laboratorio1.Models.ResultCode;
import com.example.laboratorio1.Models.Task;
import com.example.laboratorio1.UI.DatePickerFragment;
import com.example.laboratorio1.UI.TimePickerFragment;

import java.text.SimpleDateFormat;

public class act_add_task extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText txtName, txtDescription, txtLimitDate;
    Spinner spCategory;
    Button btnAddTask, btnReturn;
    int selectedCategory = 0;
    String date;
    Database db = new Database();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_add_task);
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtLimitDate = findViewById(R.id.txtLimitDate);

        txtName.addTextChangedListener(watcher);
        txtDescription.addTextChangedListener(watcher);
        txtLimitDate.addTextChangedListener(watcher);

        spCategory = findViewById(R.id.spCategory);
        btnAddTask = findViewById(R.id.btnAddTask);
        btnReturn = findViewById(R.id.btnReturn);
        spCategory.setOnItemSelectedListener(this);
        db.openDB(getApplicationContext());
        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, db.getCategories());
        spCategory.setAdapter(categoryAdapter);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = txtName.getText().toString().trim();
                String description = txtDescription.getText().toString().trim();
                String limitDate = txtLimitDate.getText().toString().trim();
                Category category = db.getCategoryById(selectedCategory);
                Task task = new Task(name, description, limitDate, category);
                ResultCode code = db.insertTask(task);
                if (code == ResultCode.OK){
                    showToast("Tarea agregada correctamente");
                    clean();
                    return;
                }

                showToast("Algo salió mal al agregar la tarea...");
            }
        });

        txtLimitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(returnIntent);
            }
        });

    }

    public void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.selectedCategory = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void showDatePicker(){
        DatePickerFragment dateFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                date = year + "/" + (month + 1) + "/" + day;
                showTimePicker();
            }
        });
        dateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePicker(){
        TimePickerFragment timeFragment = TimePickerFragment.getInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                if(date.isEmpty()){
                    txtLimitDate.setText("");
                    return;
                }
                String parsedMinutes = minutes + "";
                if (minutes < 10){
                    parsedMinutes = "0" + minutes;
                }
                txtLimitDate.setText(date + " " + hour + ":" + parsedMinutes);
            }
        });
        timeFragment.show(getSupportFragmentManager(), "timePicker");

    }

    public void clean(){
        txtName.setText("");
        txtDescription.setText("");
        txtLimitDate.setText("");
        spCategory.setSelection(0);

    }
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String name = txtName.getText().toString().trim();
            String description = txtDescription.getText().toString().trim();
            String limitDate = txtLimitDate.getText().toString().trim();
            if(name.isEmpty() || description.isEmpty() || limitDate.isEmpty()){
                btnAddTask.setEnabled(false);
                return;
            }

            btnAddTask.setEnabled(true);

        }
    };
}