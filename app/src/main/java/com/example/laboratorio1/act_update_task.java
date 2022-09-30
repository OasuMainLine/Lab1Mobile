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

public class act_update_task extends AppCompatActivity {
    EditText txtId, txtName, txtDescription, txtLimitDate;
    Spinner spCategory;
    Button btnModifyTask, btnReturn;
    String date;
    int selectedCategory = 0;
    Database db = new Database();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_update_task);
        txtId = findViewById(R.id.txtId);
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtLimitDate = findViewById(R.id.txtLimitDate);

        btnModifyTask = findViewById(R.id.btnModifyTask);
        btnReturn = findViewById(R.id.btnReturn);
        txtName.addTextChangedListener(watcher);
        txtDescription.addTextChangedListener(watcher);
        txtLimitDate.addTextChangedListener(watcher);

        db.openDB(getApplicationContext());
        Bundle bundle = this.getIntent().getExtras();


        spCategory = findViewById(R.id.spCategory);
        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(getApplicationContext(), android.R.layout.simple_spinner_item, db.getCategories());
        spCategory.setAdapter(categoryAdapter);
        txtId.setText(bundle.getInt("id") + "");
        txtName.setText(bundle.getString("name"));
        txtDescription.setText(bundle.getString("description"));
        txtLimitDate.setText(bundle.getString("limitDate"));
        spCategory.setSelection(bundle.getInt("categoryId"));
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        txtLimitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        btnModifyTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = Integer.parseInt(txtId.getText().toString().trim());
                String name = txtName.getText().toString().trim();
                String description = txtDescription.getText().toString().trim();
                String limitDate = txtLimitDate.getText().toString().trim();
                Category category = db.getCategoryById(selectedCategory);
                Task task = new Task(name, description, limitDate, category);
                task.setId(id);
                if(db.updateTask(task) == ResultCode.OK){
                    showToast("Tarea actualizada con exito");

                    returnToMain();
                    clean();
                    return;
                }
                showToast("Problemas al actualizar la tarea");
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToMain();
            }
        });
    }

    public void returnToMain(){
        Intent returnIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(returnIntent);
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
    public void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
                btnModifyTask.setEnabled(false);
                return;
            }

            btnModifyTask.setEnabled(true);

        }
    };
}