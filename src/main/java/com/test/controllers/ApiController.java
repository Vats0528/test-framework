package com.test.controllers;

import com.framework.annotation.Controller;
import com.framework.annotation.GetMapping;
import com.framework.annotation.RequestParam;
import com.framework.annotations.API;
import com.framework.annotations.Get;
import com.framework.annotations.Json;
import com.test.models.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Sprint 9 - API REST
 *
 * Démontre les annotations @API, @Json et @Get : la valeur retournée est
 * sérialisée en JSON (enveloppe ApiResponse) au lieu d'être rendue en HTML.
 *
 * Toutes les URLs sont sous /api/ (mappé sur la FrontServlet dans web.xml).
 */
@Controller
public class ApiController {

    /** Jeu de données en mémoire pour la démo */
    private static final List<Employee> EMPLOYEES = new ArrayList<>(List.of(
            new Employee(1, "John Doe", 50000.0, "IT"),
            new Employee(2, "Jane Smith", 60000.0, "HR"),
            new Employee(3, "Bob Martin", 55000.0, "IT")
    ));

    /**
     * Liste complète : retourne une List → la réponse JSON contient "count"
     * Routage assuré par @Get (annotation Sprint 9)
     */
    @Get("/api/employees")
    public List<Employee> listEmployees() {
        return EMPLOYEES;
    }

    /**
     * Un seul employé, retrouvé par paramètre d'URL dynamique.
     * @Json force la sérialisation JSON sur un mapping classique @GetMapping.
     */
    @GetMapping("/api/employees/{id}")
    @Json
    public Employee getEmployee(@RequestParam("id") int id) {
        for (Employee e : EMPLOYEES) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null; // → enveloppe d'erreur JSON (code 404)
    }

    /**
     * Filtrage par département via query string.
     * @API marque la méthode comme endpoint REST.
     */
    @GetMapping("/api/search")
    @API
    public List<Employee> searchByDepartment(@RequestParam("department") String department) {
        List<Employee> found = new ArrayList<>();
        for (Employee e : EMPLOYEES) {
            if (department != null && department.equals(e.getDepartment())) {
                found.add(e);
            }
        }
        return found;
    }

    /**
     * Retour d'un type simple (String) sérialisé en JSON
     */
    @GetMapping("/api/ping")
    @Json
    public String ping() {
        return "pong";
    }
}
