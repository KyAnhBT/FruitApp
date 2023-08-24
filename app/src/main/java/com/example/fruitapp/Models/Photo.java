package com.example.fruitapp.Models;

public class Photo {
    private String name;
    private int idImage;
    private int idRaw;

    public Photo(String name, int idImage, int idRaw) {
        this.name = name;
        this.idImage = idImage;
        this.idRaw = idRaw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public int getIdRaw() {
        return idRaw;
    }

    public void setIdRaw(int idRaw) {
        this.idRaw = idRaw;
    }
}
