package com.example.laboratorio1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.laboratorio1.Models.Category;
import com.example.laboratorio1.Models.CategoryIds;
import com.example.laboratorio1.Models.Database;
import com.example.laboratorio1.Models.Task;
import com.example.laboratorio1.UI.DatePickerFragment;
import com.example.laboratorio1.UI.TimePickerFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button btnAdd;
    ListView taskList;
    Spinner spCategory;
    Database db;
    ArrayList<Task> taskArrayList;
    private final String EMPTY_VIEW_MSG = "Parece que no hay tareas pendientes...\nCrea algunas con el botón añadir o presiona aquí";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        taskList = findViewById(R.id.taskList);
        spCategory = findViewById(R.id.spCategory);
        db = new Database();
        db.openDB(getApplicationContext());
        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add("Todos");
        for (Category category : db.getCategories()){
            categoryList.add(category.getName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryList);

        spCategory.setAdapter(spinnerAdapter);
        spCategory.setOnItemSelectedListener(this);

        View view = getEmptyTextView();

        ViewGroup viewGroup= ( ViewGroup)taskList.getParent();
        viewGroup.addView(view);
        taskList.setEmptyView(view);
        updateList();



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              toAddActivity();
            }
        });

        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


               Task selectedTask = taskArrayList.get(i);
               Bundle bundle = new Bundle();
               bundle.putString("name", selectedTask.getName());
               bundle.putString("description", selectedTask.getDescription());
               bundle.putString("limitDate", selectedTask.getFormattedLimitDate());
               bundle.putInt("id", selectedTask.getId());
               bundle.putInt("categoryId", selectedTask.getCategory().getId());

               Intent modifyIntent = new Intent(getApplicationContext(), act_update_task.class);
               modifyIntent.putExtras(bundle);

               startActivity(modifyIntent);
            }
        });
    }


    private void toAddActivity(){
        Intent toAddIntent = new Intent(getApplicationContext(), act_add_task.class);

        startActivity(toAddIntent);
    }
    private View getEmptyTextView(){

        TextView tvEmpty = new TextView(getApplicationContext());
        tvEmpty.setTextSize(16);
        tvEmpty.setTextColor(getResources().getColor(R.color.black));

        tvEmpty.setText(EMPTY_VIEW_MSG);
        tvEmpty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = 100;
        layoutParams.bottomMargin = 100;


        tvEmpty.setLayoutParams(layoutParams);
        tvEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddActivity();
            }
        });
        return tvEmpty;


    }
    @Override
    protected void onRestart() {
        super.onRestart();

        updateList();
    }

    public void updateList(){
        taskArrayList =  db.getTasks();
        TaskAdapter adapter = new TaskAdapter(getApplicationContext(), taskArrayList);
        taskList.setAdapter(adapter);
    }

    public void updateList(ArrayList<Task> list){
        TaskAdapter adapter = new TaskAdapter(getApplicationContext(), list);
        taskList.setAdapter(adapter);
        taskArrayList = list;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int trueSelection = i - 1;
        CategoryIds categoryId = CategoryIds.get(trueSelection);
        if(categoryId != null){
            updateList(db.getTasksByCategory(CategoryIds.get(trueSelection)));
            return;
        }
        updateList();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}