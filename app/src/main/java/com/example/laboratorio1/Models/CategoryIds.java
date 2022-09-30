package com.example.laboratorio1.Models;

public enum CategoryIds {

    SALUD(0), EDUCACION(1), EJERCICIO(2), ENTRETENIMIENTO(3);
    public final int id;


    CategoryIds(int i) {
        this.id = i;
    }

    @Override
    public String toString() {
        return id + "";
    }

    public static CategoryIds get(int id) {
        for (CategoryIds categoryId : values()) {
            if (categoryId.id == id) return categoryId;
        }
        return null;
    }
}
