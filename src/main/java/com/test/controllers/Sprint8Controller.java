package com.test.controllers;

import com.framework.annotation.*;
import com.test.models.Employee;
import com.test.models.Department;
import com.test.models.Project;

/**
 * Sprint 8 - Contrôleur de test pour le binding automatique avec réflexion
 * 
 * Démontre:
 * 1. Binding d'objets simples
 * 2. Binding de tableaux d'objets
 * 3. Combinaison avec types primitifs et String
 * 4. Matching automatique entre paramètres HTTP et attributs des objets
 */
@Controller
public class Sprint8Controller {

    /**
     * Test 1: Sauvegarder un employé simple
     * Format des paramètres HTTP:
     * - employee.id=1
     * - employee.name=John Doe
     * - employee.salary=50000
     * - employee.department=IT
     */
    @PostMapping("/saveEmployee")
    public String saveEmployee(Employee employee) {
        System.out.println("✓ Employé reçu: " + employee);
        return "Employé sauvegardé: " + employee.getName() + 
               " (ID: " + employee.getId() + ", Salaire: " + employee.getSalary() + ")";
    }

    /**
     * Test 2: Sauvegarder un département
     * Format des paramètres HTTP:
     * - department.id=10
     * - department.name=Engineering
     * - department.location=Paris
     */
    @PostMapping("/saveDepartment")
    public String saveDepartment(Department department) {
        System.out.println("✓ Département reçu: " + department);
        return "Département sauvegardé: " + department.getName() + 
               " situé à " + department.getLocation();
    }

    /**
     * Test 3: Sauvegarder plusieurs employés (tableau)
     * Format des paramètres HTTP:
     * - employees[0].id=1
     * - employees[0].name=John Doe
     * - employees[0].salary=50000
     * - employees[0].department=IT
     * - employees[1].id=2
     * - employees[1].name=Jane Smith
     * - employees[1].salary=60000
     * - employees[1].department=HR
     */
    @PostMapping("/saveEmployees")
    public String saveEmployees(Employee[] employees) {
        System.out.println("✓ " + employees.length + " employé(s) reçu(s)");
        StringBuilder result = new StringBuilder("Employés sauvegardés:\n");
        
        for (int i = 0; i < employees.length; i++) {
            System.out.println("  [" + i + "] " + employees[i]);
            result.append((i + 1)).append(". ")
                  .append(employees[i].getName())
                  .append(" - Salaire: ").append(employees[i].getSalary())
                  .append("\n");
        }
        
        return result.toString();
    }

    /**
     * Test 4: Sauvegarder plusieurs employés ET un département
     * Format des paramètres HTTP:
     * - employees[0].id=1
     * - employees[0].name=John Doe
     * - employees[0].salary=50000
     * - employees[1].id=2
     * - employees[1].name=Jane Smith
     * - employees[1].salary=60000
     * - department.id=10
     * - department.name=IT
     * - department.location=Paris
     */
    @PostMapping("/saveEmployeesAndDepartment")
    public String saveEmployeesAndDepartment(Employee[] employees, Department department) {
        System.out.println("✓ " + employees.length + " employé(s) et 1 département reçu(s)");
        StringBuilder result = new StringBuilder();
        result.append("Département: ").append(department.getName())
              .append(" (").append(department.getLocation()).append(")\n");
        result.append("Employés assignés:\n");
        
        for (int i = 0; i < employees.length; i++) {
            System.out.println("  [" + i + "] " + employees[i]);
            result.append((i + 1)).append(". ")
                  .append(employees[i].getName())
                  .append(" - ").append(employees[i].getDepartment())
                  .append("\n");
        }
        
        return result.toString();
    }

    /**
     * Test 5: Sauvegarder plusieurs employés, plusieurs départements ET un nom de company
     * Format des paramètres HTTP:
     * - employees[0].id=1
     * - employees[0].name=John Doe
     * - employees[0].salary=50000
     * - departments[0].id=10
     * - departments[0].name=IT
     * - departments[0].location=Paris
     * - departments[1].id=20
     * - departments[1].name=HR
     * - departments[1].location=Lyon
     * - companyName=TechCorp
     */
    @PostMapping("/saveCompleteCompany")
    public String saveCompleteCompany(Employee[] employees, Department[] departments, String companyName) {
        System.out.println("✓ Company data reçue pour " + companyName);
        StringBuilder result = new StringBuilder();
        result.append("=== Données de l'entreprise: ").append(companyName).append(" ===\n");
        result.append("\nDépartements: ").append(departments.length).append("\n");
        
        for (int i = 0; i < departments.length; i++) {
            System.out.println("  Department[" + i + "] " + departments[i]);
            result.append("  - ").append(departments[i].getName())
                  .append(" (").append(departments[i].getLocation()).append(")\n");
        }
        
        result.append("\nEmployés: ").append(employees.length).append("\n");
        for (int i = 0; i < employees.length; i++) {
            System.out.println("  Employee[" + i + "] " + employees[i]);
            result.append("  - ").append(employees[i].getName())
                  .append(" (").append(employees[i].getSalary()).append(")\n");
        }
        
        return result.toString();
    }

    /**
     * Test 6: Sauvegarder un projet avec tous les types de données
     * Démontre le casting automatique des types (int, double, boolean, String)
     * Format des paramètres HTTP:
     * - project.id=100
     * - project.title=New Project
     * - project.description=A great project
     * - project.budget=150000.50
     * - project.active=true
     */
    @PostMapping("/saveProject")
    public String saveProject(Project project) {
        System.out.println("✓ Projet reçu: " + project);
        return "Projet sauvegardé:\n" +
               "  ID: " + project.getId() + "\n" +
               "  Titre: " + project.getTitle() + "\n" +
               "  Description: " + project.getDescription() + "\n" +
               "  Budget: " + project.getBudget() + "€\n" +
               "  Actif: " + (project.isActive() ? "Oui" : "Non");
    }

    /**
     * Test 7: Sauvegarder plusieurs projets et une équipe
     * Format des paramètres HTTP:
     * - projects[0].id=100
     * - projects[0].title=Project A
     * - projects[0].budget=100000
     * - projects[0].active=true
     * - projects[1].id=101
     * - projects[1].title=Project B
     * - projects[1].budget=200000
     * - projects[1].active=false
     * - teamLeader=John Manager
     */
    @PostMapping("/saveProjectsWithTeam")
    public String saveProjectsWithTeam(Project[] projects, String teamLeader) {
        System.out.println("✓ " + projects.length + " projet(s) pour l'équipe de " + teamLeader);
        StringBuilder result = new StringBuilder();
        result.append("Équipe dirigée par: ").append(teamLeader).append("\n");
        result.append("Projets assignés:\n");
        
        for (int i = 0; i < projects.length; i++) {
            System.out.println("  Project[" + i + "] " + projects[i]);
            result.append("  ").append((i + 1)).append(". ")
                  .append(projects[i].getTitle())
                  .append(" - Budget: ").append(projects[i].getBudget())
                  .append("€ - Actif: ").append(projects[i].isActive() ? "Oui" : "Non")
                  .append("\n");
        }
        
        return result.toString();
    }

    /**
     * Test 8: Cas complexe - Sauvegarder tout en même temps
     * Démontre la capacité du framework à gérer plusieurs paramètres complexes simultanément
     */
    @PostMapping("/saveEverything")
    public String saveEverything(Employee[] employees, Department[] departments, 
                                 Project[] projects, String companyName) {
        System.out.println("✓ Données complètes reçues pour " + companyName);
        StringBuilder result = new StringBuilder();
        result.append("====== DONNÉES COMPLÈTES: ").append(companyName).append(" ======\n\n");
        
        result.append("EMPLOYÉS (").append(employees.length).append("):\n");
        for (int i = 0; i < employees.length; i++) {
            result.append("  ").append((i + 1)).append(". ").append(employees[i].getName())
                  .append(" - Salaire: ").append(employees[i].getSalary()).append("€\n");
        }
        
        result.append("\nDÉPARTEMENTS (").append(departments.length).append("):\n");
        for (int i = 0; i < departments.length; i++) {
            result.append("  ").append((i + 1)).append(". ").append(departments[i].getName())
                  .append(" - ").append(departments[i].getLocation()).append("\n");
        }
        
        result.append("\nPROJETS (").append(projects.length).append("):\n");
        for (int i = 0; i < projects.length; i++) {
            result.append("  ").append((i + 1)).append(". ").append(projects[i].getTitle())
                  .append(" - Budget: ").append(projects[i].getBudget()).append("€\n");
        }
        
        return result.toString();
    }

    /**
     * Test 9: Vérification des types null et valeurs par défaut
     * Teste le comportement du framework quand certains paramètres sont manquants
     */
    @PostMapping("/saveWithOptional")
    public String saveWithOptional(Employee employee, Department department) {
        System.out.println("✓ Données optionnelles reçues");
        StringBuilder result = new StringBuilder();
        
        if (employee != null && employee.getName() != null && !employee.getName().isEmpty()) {
            result.append("Employé: ").append(employee.getName()).append("\n");
        } else {
            result.append("Aucun employé fourni\n");
        }
        
        if (department != null && department.getName() != null && !department.getName().isEmpty()) {
            result.append("Département: ").append(department.getName()).append("\n");
        } else {
            result.append("Aucun département fourni\n");
        }
        
        return result.toString();
    }
}
