package com.example.laboratorio1.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Task {

    /*Nombre de la tarea, Que hay que hacer, Fecha límite, hora, y seleccionar
            categoría*/
    private int id;
    private String name;
    private String description;
    private Date limitDate;
    private Category category;

    public static final SimpleDateFormat taskDateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    public Task( String name, String description, Date limitDate, Category category) {

        this.name = name;
        this.description = description;
        this.limitDate = limitDate;
        this.category = category;
    }

    public Task( String name, String description, String limitDate, Category category) {

        this.name = name;
        this.description = description;
        this.category = category;
        try{
            this.limitDate = taskDateFormatter.parse(limitDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLimitDate() {
        return limitDate;
    }
    public String getFormattedLimitDate(){
        return taskDateFormatter.format(limitDate);
    }
    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(limitDate, task.limitDate) && Objects.equals(category, task.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, limitDate, category);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", limitDate=" + limitDate +
                ", category=" + category +
                '}';
    }
}
