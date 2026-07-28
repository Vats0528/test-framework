package com.test.sprints;

import com.framework.util.ParameterBinder;
import com.test.models.Department;
import com.test.models.Employee;
import com.test.models.Project;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Sprint 8 - Tests unitaires du VRAI ParameterBinder du framework
 *
 * Contrairement à un test qui réimplémenterait la logique de binding, ces tests
 * appellent com.framework.util.ParameterBinder.bindParameters() : ils échouent
 * donc si le framework régresse.
 *
 * Les Parameter[] proviennent de la réflexion sur les méthodes de la classe
 * interne Signatures (compilées avec -parameters pour conserver les noms).
 */
@DisplayName("Sprint 8 - ParameterBinder (tests unitaires du framework)")
class Sprint08ParameterBinderTest {

    /** Signatures de référence : seuls leurs noms/types de paramètres comptent */
    @SuppressWarnings("unused")
    static class Signatures {
        void simpleObject(Employee employee) {
        }

        void arrayOfObjects(Employee[] employees) {
        }

        void mixed(Employee[] employees, Department department, String companyName) {
        }

        void primitives(int departmentId, double ratio, boolean active, String companyName) {
        }

        void projectWithMixedTypes(Project project) {
        }
    }

    @Test
    @DisplayName("Objet simple : employee.* est lié aux champs de Employee")
    void bindsSimpleObject() {
        HttpServletRequest request = requestWith(
                "employee.id", "1",
                "employee.name", "John Doe",
                "employee.salary", "50000.50",
                "employee.department", "IT");

        Object[] args = ParameterBinder.bindParameters(parametersOf("simpleObject"), request);

        assertEquals(1, args.length);
        Employee employee = assertInstanceOf(Employee.class, args[0]);
        assertEquals(1, employee.getId());
        assertEquals("John Doe", employee.getName());
        assertEquals(50000.50, employee.getSalary());
        assertEquals("IT", employee.getDepartment());
    }

    @Test
    @DisplayName("Tableau d'objets : employees[i].* devient un Employee[]")
    void bindsArrayOfObjects() {
        HttpServletRequest request = requestWith(
                "employees[0].id", "1",
                "employees[0].name", "John Doe",
                "employees[0].salary", "50000",
                "employees[1].id", "2",
                "employees[1].name", "Jane Smith",
                "employees[1].salary", "60000");

        Object[] args = ParameterBinder.bindParameters(parametersOf("arrayOfObjects"), request);

        Employee[] employees = assertInstanceOf(Employee[].class, args[0]);
        assertEquals(2, employees.length);
        assertEquals("John Doe", employees[0].getName());
        assertEquals(50000.0, employees[0].getSalary());
        assertEquals("Jane Smith", employees[1].getName());
        assertEquals(2, employees[1].getId());
    }

    @Test
    @DisplayName("Index non séquentiels : tableau dimensionné sur l'index max, trous remplis")
    void bindsArrayWithNonSequentialIndices() {
        HttpServletRequest request = requestWith(
                "employees[0].name", "John",
                "employees[2].name", "Charlie",
                "employees[5].name", "Frank");

        Object[] args = ParameterBinder.bindParameters(parametersOf("arrayOfObjects"), request);

        Employee[] employees = assertInstanceOf(Employee[].class, args[0]);
        assertEquals(6, employees.length);
        assertEquals("John", employees[0].getName());
        assertEquals("Charlie", employees[2].getName());
        assertEquals("Frank", employees[5].getName());

        // les index absents reçoivent un objet vide, pas null : un contrôleur qui
        // parcourt le tableau ne doit pas planter à cause d'un trou dans l'entrée
        assertNotNull(employees[1], "les index absents doivent être des objets vides");
        assertNull(employees[1].getName());
        assertEquals(0, employees[1].getId());
    }

    @Test
    @DisplayName("Aucun paramètre indexé -> tableau vide (jamais null)")
    void bindsEmptyArray() {
        HttpServletRequest request = requestWith();

        Object[] args = ParameterBinder.bindParameters(parametersOf("arrayOfObjects"), request);

        Employee[] employees = assertInstanceOf(Employee[].class, args[0]);
        assertEquals(0, employees.length);
    }

    @Test
    @DisplayName("Tableau + objet + String liés en une seule passe")
    void bindsMixedParameters() {
        HttpServletRequest request = requestWith(
                "employees[0].name", "John Doe",
                "employees[1].name", "Jane Smith",
                "department.id", "10",
                "department.name", "IT",
                "department.location", "Paris",
                "companyName", "TechCorp");

        Object[] args = ParameterBinder.bindParameters(parametersOf("mixed"), request);

        assertEquals(3, args.length);
        Employee[] employees = assertInstanceOf(Employee[].class, args[0]);
        assertEquals(2, employees.length);

        Department department = assertInstanceOf(Department.class, args[1]);
        assertEquals(10, department.getId());
        assertEquals("Paris", department.getLocation());

        assertEquals("TechCorp", args[2]);
    }

    @Test
    @DisplayName("Types primitifs convertis depuis les paramètres de requête")
    void bindsPrimitiveTypes() {
        HttpServletRequest request = requestWith(
                "departmentId", "42",
                "ratio", "1.5",
                "active", "true",
                "companyName", "TechCorp");

        Object[] args = ParameterBinder.bindParameters(parametersOf("primitives"), request);

        assertEquals(42, args[0]);
        assertEquals(1.5, args[1]);
        assertEquals(true, args[2]);
        assertEquals("TechCorp", args[3]);
    }

    @Test
    @DisplayName("Types mixtes dans un objet : int, String, double, boolean")
    void bindsObjectWithMixedTypes() {
        HttpServletRequest request = requestWith(
                "project.id", "100",
                "project.title", "New Project",
                "project.description", "A great project",
                "project.budget", "150000.50",
                "project.active", "true");

        Object[] args = ParameterBinder.bindParameters(parametersOf("projectWithMixedTypes"), request);

        Project project = assertInstanceOf(Project.class, args[0]);
        assertEquals(100, project.getId());
        assertEquals("New Project", project.getTitle());
        assertEquals("A great project", project.getDescription());
        assertEquals(150000.50, project.getBudget());
        assertTrue(project.isActive());
    }

    @Test
    @DisplayName("Paramètres manquants : valeurs par défaut, objet non nul")
    void bindsWithMissingParameters() {
        HttpServletRequest request = requestWith(
                "employee.id", "1",
                "employee.name", "John Doe");

        Object[] args = ParameterBinder.bindParameters(parametersOf("simpleObject"), request);

        Employee employee = assertInstanceOf(Employee.class, args[0]);
        assertEquals(1, employee.getId());
        assertEquals("John Doe", employee.getName());
        assertEquals(0.0, employee.getSalary(), "double non fourni -> 0.0");
        assertNull(employee.getDepartment(), "String non fournie -> null");
    }

    @Test
    @DisplayName("Valeurs vides : défaut pour les primitifs, null pour String")
    void bindsEmptyValues() {
        HttpServletRequest request = requestWith(
                "employee.id", "",
                "employee.name", "");

        Object[] args = ParameterBinder.bindParameters(parametersOf("simpleObject"), request);

        Employee employee = assertInstanceOf(Employee.class, args[0]);
        assertEquals(0, employee.getId());
        assertNull(employee.getName());
    }

    @Test
    @DisplayName("Valeur non convertible : retour à la valeur par défaut, sans exception")
    void invalidNumberFallsBackToDefault() {
        HttpServletRequest request = requestWith(
                "employee.id", "pas-un-nombre",
                "employee.name", "John");

        Object[] args = ParameterBinder.bindParameters(parametersOf("simpleObject"), request);

        Employee employee = assertInstanceOf(Employee.class, args[0]);
        assertEquals(0, employee.getId());
        assertEquals("John", employee.getName());
    }

    @Test
    @DisplayName("Attribut inconnu dans la requête : ignoré silencieusement")
    void unknownAttributeIsIgnored() {
        HttpServletRequest request = requestWith(
                "employee.name", "John",
                "employee.champInexistant", "peu importe");

        Object[] args = ParameterBinder.bindParameters(parametersOf("simpleObject"), request);

        Employee employee = assertInstanceOf(Employee.class, args[0]);
        assertEquals("John", employee.getName());
    }

    // === helpers ===

    /** Parameter[] de la méthode nommée dans Signatures */
    private static Parameter[] parametersOf(String methodName) {
        for (Method method : Signatures.class.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                Parameter[] parameters = method.getParameters();
                assertNotNull(parameters);
                assertTrue(parameters[0].isNamePresent(),
                        "compiler-plugin doit être configuré avec <parameters>true</parameters>");
                return parameters;
            }
        }
        throw new IllegalArgumentException("méthode inconnue : " + methodName);
    }

    /**
     * Requête simulée : getParameterNames() renvoie une NOUVELLE énumération à
     * chaque appel (le binder l'appelle une fois par paramètre de méthode).
     */
    private static HttpServletRequest requestWith(String... keyValues) {
        Map<String, String> parameters = new LinkedHashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            parameters.put(keyValues[i], keyValues[i + 1]);
        }

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameterNames())
                .thenAnswer(invocation -> Collections.enumeration(parameters.keySet()));
        when(request.getParameter(anyString()))
                .thenAnswer(invocation -> parameters.get(invocation.getArgument(0, String.class)));
        return request;
    }
}
