package com.example.laboratorio1.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Database {

    private static SQLiteDatabase db;
    private final static String DB_NAME = "task_db";
    private final static String TB_TASK = "task";
    private final static String TB_CATEGORY = "category";


    private final static String CREATE_TB_CATEGORY = "CREATE TABLE IF NOT EXISTS category(" +
            "id INTEGER PRIMARY KEY," +
            "name TEXT NOT NULL," +
            "description TEXT" +
            ");";
    private final static String POPULATE_TB_CATEGORY = "INSERT OR IGNORE INTO category VALUES" +
            "('" + CategoryIds.SALUD + "','Salud', 'Pedientes referentes a la salud, como toma de medicinas')," +
            "('" + CategoryIds.EDUCACION + "','Educacion', 'Actividades relativas a la educacion')," +
            "('" + CategoryIds.EJERCICIO + "','Ejercicio', 'Tareas con respecto al ejercicio')," +
            "('" + CategoryIds.ENTRETENIMIENTO + "','Entretenimiento', 'Actividades para entretenerse');";
    private final static String CREATE_TB_TASK = "CREATE TABLE IF NOT EXISTS task(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL," +
            "description MEDIUMTEXT NOT NULL," +
            "limitDate TEXT NOT NULL," +
            "categoryId INTEGER NOT NULL," +
            "FOREIGN KEY(categoryId) REFERENCES category(id));";



    public ResultCode openDB(Context context){
        try{
            db = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
            db.execSQL(CREATE_TB_CATEGORY);
            db.execSQL(POPULATE_TB_CATEGORY);
            db.execSQL(CREATE_TB_TASK);
            return ResultCode.OK;
        } catch (SQLException e) {
            e.printStackTrace();
            return ResultCode.ERROR;
        }
    }

    public void closeDB(){
        db.close();
    }


    public ResultCode insertTask(Task task){
        ContentValues values = new ContentValues();
        values.put("name", task.getName());
        values.put("description", task.getDescription());
        values.put("limitDate", task.getFormattedLimitDate());
        values.put("categoryId", task.getCategory().getId());

        int res = (int) db.insert(TB_TASK, null, values);

        return res > 0 ? ResultCode.OK : ResultCode.ERROR;
    }
    public Category getCategoryById(int id){
        String parsedId = id + "";
        Cursor cursor = db.query(TB_CATEGORY, new String[] {"id", "name", "description"}, "id = ?", new String[]{parsedId}, null, null, null, null);

        cursor.moveToFirst();

        if(cursor.getCount() == 0){
            return null;
        }

        return new Category(cursor.getInt(0),cursor.getString(1), cursor.getString(2));
    }

    public ArrayList<Category> getCategories(){
        Cursor cursor = db.query(TB_CATEGORY, new String[]{"id", "name", "description"}, null, null, null, null, "id asc");
        cursor.moveToFirst();
        ArrayList<Category> list = new ArrayList<>();

        while(!cursor.isAfterLast()){

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            Category category = new Category(id, name, description);
            list.add(category);
            cursor.moveToNext();
        }
        return list;
    }
    public ArrayList<Task> getTasks(){
        Cursor cursor = db.query(TB_TASK, new String[]{"id", "name", "description", "limitDate", "categoryId"}, null, null, null, null, "id desc");
        cursor.moveToFirst();
        ArrayList<Task> list = new ArrayList<>();
        while(!cursor.isAfterLast()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            String limitDate= cursor.getString(3);
            int categoryId = cursor.getInt(4);
            Category category = getCategoryById(categoryId);

            Task task = new Task(name, description, limitDate, category);
            task.setId(id);

            list.add(task);
            cursor.moveToNext();
        }
        return list;
    }

    public ArrayList<Task> getTasksByCategory(CategoryIds selectedCategory){
        Cursor cursor = db.query(TB_TASK, new String[]{"id", "name", "description", "limitDate", "categoryId"}, "categoryId = ?", new String[]{ selectedCategory.toString()}, null, null, "id desc");
        cursor.moveToFirst();
        ArrayList<Task> list = new ArrayList<>();
        while(!cursor.isAfterLast()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            String limitDate= cursor.getString(3);
            int categoryId = cursor.getInt(4);
            Category category = getCategoryById(categoryId);

            Task task = new Task(name, description, limitDate, category);
            task.setId(id);

            list.add(task);
            cursor.moveToNext();
        }
        return list;
    }


    public ResultCode updateTask(Task task){
        ContentValues values = new ContentValues();
        values.put("name", task.getName());
        values.put("description", task.getDescription());
        values.put("limitDate", task.getFormattedLimitDate());
        values.put("categoryId", task.getCategory().getId());
        int code = db.update(TB_TASK,values, "id = ?", new String[] {task.getId() + ""});

        return code > 0 ? ResultCode.OK : ResultCode.ERROR;
    }
}

