package com.test.models;

/**
 * Modèle Project pour les tests Sprint 8
 * Démontre le binding automatique avec plusieurs attributs de types différents
 */
public class Project {
    private int id;
    private String title;
    private String description;
    private double budget;
    private boolean active;

    // Constructeur vide (requis pour la réflexion)
    public Project() {
    }

    // Constructeur avec paramètres
    public Project(int id, String title, String description, double budget, boolean active) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.active = active;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", budget=" + budget +
                ", active=" + active +
                '}';
    }
}
