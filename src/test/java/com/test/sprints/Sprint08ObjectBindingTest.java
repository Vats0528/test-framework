package com.test.sprints;

import com.test.support.HttpTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sprints 8, 8 bis & 8 ter - Binding automatique d'objets (bout en bout)
 *
 * Les paramètres HTTP "employee.name", "employees[0].name", ... sont liés
 * automatiquement aux objets et tableaux d'objets des méthodes de contrôleur,
 * sans annotation.
 */
@DisplayName("Sprint 8 - Binding d'objets et de tableaux via HTTP")
class Sprint08ObjectBindingTest {

    private final HttpTest client = HttpTest.newClient();

    @Test
    @DisplayName("Objet simple : employee.* -> Employee")
    void bindSimpleObject() {
        HttpTest.Response response = client.postForm("/saveEmployee", HttpTest.form(
                "employee.id", "1",
                "employee.name", "John Doe",
                "employee.salary", "50000.5",
                "employee.department", "IT"));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Employé sauvegardé: John Doe"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("ID: 1"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("Salaire: 50000.5"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Objet simple : department.* -> Department")
    void bindOtherSimpleObject() {
        HttpTest.Response response = client.postForm("/saveDepartment", HttpTest.form(
                "department.id", "10",
                "department.name", "Engineering",
                "department.location", "Paris"));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Département sauvegardé: Engineering situé à Paris"),
                "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Tableau d'objets : employees[i].* -> Employee[]")
    void bindArrayOfObjects() {
        HttpTest.Response response = client.postForm("/saveEmployees", HttpTest.form(
                "employees[0].id", "1",
                "employees[0].name", "John Doe",
                "employees[0].salary", "50000",
                "employees[1].id", "2",
                "employees[1].name", "Jane Smith",
                "employees[1].salary", "60000"));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("1. John Doe"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("2. Jane Smith"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Tableau + objet simple dans la même méthode")
    void bindArrayAndObjectTogether() {
        HttpTest.Response response = client.postForm("/saveEmployeesAndDepartment", HttpTest.form(
                "employees[0].id", "1",
                "employees[0].name", "John Doe",
                "employees[0].department", "IT",
                "employees[1].id", "2",
                "employees[1].name", "Jane Smith",
                "employees[1].department", "HR",
                "department.id", "10",
                "department.name", "IT",
                "department.location", "Paris"));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Département: IT (Paris)"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("John Doe"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("Jane Smith"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Deux tableaux + une String dans la même méthode")
    void bindTwoArraysAndAString() {
        HttpTest.Response response = client.postForm("/saveCompleteCompany", HttpTest.form(
                "employees[0].id", "1",
                "employees[0].name", "John Doe",
                "employees[0].salary", "50000",
                "departments[0].id", "10",
                "departments[0].name", "IT",
                "departments[0].location", "Paris",
                "departments[1].id", "20",
                "departments[1].name", "HR",
                "departments[1].location", "Lyon",
                "companyName", "TechCorp"));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("TechCorp"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("Départements: 2"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("IT (Paris)"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("HR (Lyon)"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("Employés: 1"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Types mixtes : int, String, double, boolean")
    void bindMixedTypes() {
        HttpTest.Response response = client.postForm("/saveProject", HttpTest.form(
                "project.id", "100",
                "project.title", "New Project",
                "project.description", "A great project",
                "project.budget", "150000.5",
                "project.active", "true"));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("ID: 100"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("Titre: New Project"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("Budget: 150000.5"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("Actif: Oui"), "le boolean devrait être converti : " + response.body());
    }

    @Test
    @DisplayName("Tableau d'objets + String : projects[] et teamLeader")
    void bindArrayOfProjectsAndTeamLeader() {
        HttpTest.Response response = client.postForm("/saveProjectsWithTeam", HttpTest.form(
                "projects[0].id", "100",
                "projects[0].title", "Project A",
                "projects[0].budget", "100000",
                "projects[0].active", "true",
                "projects[1].id", "101",
                "projects[1].title", "Project B",
                "projects[1].budget", "200000",
                "projects[1].active", "false",
                "teamLeader", "John Manager"));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Équipe dirigée par: John Manager"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("Project A"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("Actif: Non"), "projects[1].active=false attendu : " + response.body());
    }

    @Test
    @DisplayName("Trois tableaux + une String simultanément")
    void bindEverythingAtOnce() {
        HttpTest.Response response = client.postForm("/saveEverything", HttpTest.form(
                "employees[0].name", "John Doe",
                "employees[0].salary", "50000",
                "departments[0].name", "IT",
                "departments[0].location", "Paris",
                "projects[0].title", "Project A",
                "projects[0].budget", "100000",
                "companyName", "TechCorp"));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("EMPLOYÉS (1)"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("DÉPARTEMENTS (1)"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("PROJETS (1)"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Paramètres manquants : objet instancié avec valeurs par défaut")
    void bindWithMissingParameters() {
        HttpTest.Response response = client.postForm("/saveWithOptional", HttpTest.form(
                "employee.name", "John Doe"));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Employé: John Doe"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("Aucun département fourni"),
                "un objet sans paramètre doit rester vide, pas planter : " + response.body());
    }

    @Test
    @DisplayName("Tableau vide quand aucun index n'est fourni")
    void bindEmptyArray() {
        HttpTest.Response response = client.postForm("/saveEmployees", HttpTest.form());

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Employés sauvegardés"),
                "un tableau vide ne doit pas provoquer d'erreur : " + response.body());
    }

    @Test
    @DisplayName("Index non séquentiels : employees[0] et employees[2]")
    void bindArrayWithNonSequentialIndices() {
        HttpTest.Response response = client.postForm("/saveEmployees", HttpTest.form(
                "employees[0].id", "1",
                "employees[0].name", "John",
                "employees[0].salary", "1000",
                "employees[2].id", "3",
                "employees[2].name", "Charlie",
                "employees[2].salary", "3000"));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("John"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("Charlie"), "corps inattendu : " + response.body());
    }
}
