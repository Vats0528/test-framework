package com.test.models;

/**
 * Modèle Department pour les tests Sprint 8
 * Démontre le binding automatique d'objets avec d'autres objets
 */
public class Department {
    private int id;
    private String name;
    private String location;

    // Constructeur vide (requis pour la réflexion)
    public Department() {
    }

    // Constructeur avec paramètres
    public Department(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    // Getters et Setters
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
