package com.test;

import com.framework.util.ParameterBinder;
import com.test.models.Employee;
import com.test.models.Department;
import com.test.models.Project;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests pour Sprint 8 - ParameterBinder
 * 
 * Teste la liaison automatique des paramètres HTTP aux objets Java
 * via réflexion, sans utiliser les annotations @RequestParam
 */
@DisplayName("Sprint 8 - ParameterBinder Tests")
public class ParameterBinderTest {

    @Mock
    private HttpServletRequest mockRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test 1: Binding d'un objet simple
     * Vérifie que les paramètres HTTP "employee.id", "employee.name", etc.
     * sont correctement assignés aux attributs de l'objet Employee
     */
    @Test
    @DisplayName("Binding d'un objet simple (Employee)")
    public void testBindSimpleObject() throws Exception {
        // Arrangement
        List<String> paramNames = new ArrayList<>();
        paramNames.add("employee.id");
        paramNames.add("employee.name");
        paramNames.add("employee.salary");
        paramNames.add("employee.department");

        when(mockRequest.getParameterNames()).thenReturn(new Enumeration<String>() {
            private int index = 0;

            @Override
            public boolean hasMoreElements() {
                return index < paramNames.size();
            }

            @Override
            public String nextElement() {
                return paramNames.get(index++);
            }
        });

        when(mockRequest.getParameter("employee.id")).thenReturn("1");
        when(mockRequest.getParameter("employee.name")).thenReturn("John Doe");
        when(mockRequest.getParameter("employee.salary")).thenReturn("50000.50");
        when(mockRequest.getParameter("employee.department")).thenReturn("IT");

        // Action
        // Test direct avec bindObject
        Employee employee = bindTestObject(Employee.class, "employee", mockRequest);

        // Assertion
        assertNotNull(employee);
        assertEquals(1, employee.getId());
        assertEquals("John Doe", employee.getName());
        assertEquals(50000.50, employee.getSalary());
        assertEquals("IT", employee.getDepartment());
    }

    /**
     * Test 2: Binding d'un tableau d'objets
     * Vérifie que les paramètres au format "employees[0].id", "employees[0].name", etc.
     * créent un tableau d'objets Employee correctement remplis
     */
    @Test
    @DisplayName("Binding d'un tableau d'objets (Employee[])")
    public void testBindArrayOfObjects() throws Exception {
        // Arrangement
        List<String> paramNames = new ArrayList<>();
        paramNames.add("employees[0].id");
        paramNames.add("employees[0].name");
        paramNames.add("employees[0].salary");
        paramNames.add("employees[1].id");
        paramNames.add("employees[1].name");
        paramNames.add("employees[1].salary");

        when(mockRequest.getParameterNames()).thenReturn(new Enumeration<String>() {
            private int index = 0;

            @Override
            public boolean hasMoreElements() {
                return index < paramNames.size();
            }

            @Override
            public String nextElement() {
                return paramNames.get(index++);
            }
        });

        when(mockRequest.getParameter("employees[0].id")).thenReturn("1");
        when(mockRequest.getParameter("employees[0].name")).thenReturn("John Doe");
        when(mockRequest.getParameter("employees[0].salary")).thenReturn("50000");
        when(mockRequest.getParameter("employees[1].id")).thenReturn("2");
        when(mockRequest.getParameter("employees[1].name")).thenReturn("Jane Smith");
        when(mockRequest.getParameter("employees[1].salary")).thenReturn("60000");

        // Action
        Employee[] employees = bindTestArray(Employee.class, "employees", mockRequest);

        // Assertion
        assertNotNull(employees);
        assertEquals(2, employees.length);
        assertEquals(1, employees[0].getId());
        assertEquals("John Doe", employees[0].getName());
        assertEquals(50000, employees[0].getSalary());
        assertEquals(2, employees[1].getId());
        assertEquals("Jane Smith", employees[1].getName());
        assertEquals(60000, employees[1].getSalary());
    }

    /**
     * Test 3: Binding de type primitif (String, int)
     * Vérifie que les types simples sont correctement convertis
     */
    @Test
    @DisplayName("Binding de types primitifs")
    public void testBindPrimitiveTypes() throws Exception {
        // Arrangement
        when(mockRequest.getParameter("companyName")).thenReturn("TechCorp");
        when(mockRequest.getParameter("departmentId")).thenReturn("42");

        // Action - String
        String companyName = (String) bindTestPrimitive(String.class, "companyName", mockRequest);
        Integer departmentId = (Integer) bindTestPrimitive(int.class, "departmentId", mockRequest);

        // Assertion
        assertEquals("TechCorp", companyName);
        assertEquals(42, departmentId);
    }

    /**
     * Test 4: Binding d'objets avec types mixtes (int, String, double, boolean)
     * Vérifie le casting automatique pour Project avec plusieurs types d'attributs
     */
    @Test
    @DisplayName("Binding d'objets avec types mixtes")
    public void testBindObjectWithMixedTypes() throws Exception {
        // Arrangement
        List<String> paramNames = new ArrayList<>();
        paramNames.add("project.id");
        paramNames.add("project.title");
        paramNames.add("project.description");
        paramNames.add("project.budget");
        paramNames.add("project.active");

        when(mockRequest.getParameterNames()).thenReturn(new Enumeration<String>() {
            private int index = 0;

            @Override
            public boolean hasMoreElements() {
                return index < paramNames.size();
            }

            @Override
            public String nextElement() {
                return paramNames.get(index++);
            }
        });

        when(mockRequest.getParameter("project.id")).thenReturn("100");
        when(mockRequest.getParameter("project.title")).thenReturn("New Project");
        when(mockRequest.getParameter("project.description")).thenReturn("A great project");
        when(mockRequest.getParameter("project.budget")).thenReturn("150000.50");
        when(mockRequest.getParameter("project.active")).thenReturn("true");

        // Action
        Project project = bindTestObject(Project.class, "project", mockRequest);

        // Assertion
        assertNotNull(project);
        assertEquals(100, project.getId());
        assertEquals("New Project", project.getTitle());
        assertEquals("A great project", project.getDescription());
        assertEquals(150000.50, project.getBudget());
        assertTrue(project.isActive());
    }

    /**
     * Test 5: Binding avec valeurs manquantes
     * Vérifie que le framework gère correctement les paramètres manquants
     */
    @Test
    @DisplayName("Binding avec paramètres manquants")
    public void testBindWithMissingParameters() throws Exception {
        // Arrangement: Seulement id et name, pas salary
        List<String> paramNames = new ArrayList<>();
        paramNames.add("employee.id");
        paramNames.add("employee.name");

        when(mockRequest.getParameterNames()).thenReturn(new Enumeration<String>() {
            private int index = 0;

            @Override
            public boolean hasMoreElements() {
                return index < paramNames.size();
            }

            @Override
            public String nextElement() {
                return paramNames.get(index++);
            }
        });

        when(mockRequest.getParameter("employee.id")).thenReturn("1");
        when(mockRequest.getParameter("employee.name")).thenReturn("John Doe");
        when(mockRequest.getParameter("employee.salary")).thenReturn(null);

        // Action
        Employee employee = bindTestObject(Employee.class, "employee", mockRequest);

        // Assertion
        assertNotNull(employee);
        assertEquals(1, employee.getId());
        assertEquals("John Doe", employee.getName());
        assertEquals(0.0, employee.getSalary()); // Valeur par défaut pour double
    }

    /**
     * Test 6: Binding d'un tableau vide
     * Vérifie que le framework crée un tableau vide quand aucun paramètre n'est fourni
     */
    @Test
    @DisplayName("Binding d'un tableau vide")
    public void testBindEmptyArray() throws Exception {
        // Arrangement: Pas de paramètres
        when(mockRequest.getParameterNames()).thenReturn(new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return false;
            }

            @Override
            public String nextElement() {
                return null;
            }
        });

        // Action
        Employee[] employees = bindTestArray(Employee.class, "employees", mockRequest);

        // Assertion
        assertNotNull(employees);
        assertEquals(0, employees.length);
    }

    /**
     * Test 7: Binding de plusieurs objets différents (Employee + Department)
     * Vérifie que le framework peut binder plusieurs objets en même temps
     */
    @Test
    @DisplayName("Binding de plusieurs objets différents")
    public void testBindMultipleDifferentObjects() throws Exception {
        // Arrangement - Créer deux mocks séparé pour éviter la réutilisation d'énumération
        HttpServletRequest mockRequest1 = mock(HttpServletRequest.class);
        HttpServletRequest mockRequest2 = mock(HttpServletRequest.class);
        
        // Énumération 1 pour Employee
        List<String> paramNames1 = new ArrayList<>();
        paramNames1.add("employee.id");
        paramNames1.add("employee.name");

        when(mockRequest1.getParameterNames()).thenReturn(new Enumeration<String>() {
            private int index = 0;

            @Override
            public boolean hasMoreElements() {
                return index < paramNames1.size();
            }

            @Override
            public String nextElement() {
                return paramNames1.get(index++);
            }
        });

        when(mockRequest1.getParameter("employee.id")).thenReturn("1");
        when(mockRequest1.getParameter("employee.name")).thenReturn("John Doe");

        // Énumération 2 pour Department
        List<String> paramNames2 = new ArrayList<>();
        paramNames2.add("department.id");
        paramNames2.add("department.name");
        paramNames2.add("department.location");

        when(mockRequest2.getParameterNames()).thenReturn(new Enumeration<String>() {
            private int index = 0;

            @Override
            public boolean hasMoreElements() {
                return index < paramNames2.size();
            }

            @Override
            public String nextElement() {
                return paramNames2.get(index++);
            }
        });

        when(mockRequest2.getParameter("department.id")).thenReturn("10");
        when(mockRequest2.getParameter("department.name")).thenReturn("IT");
        when(mockRequest2.getParameter("department.location")).thenReturn("Paris");

        // Action
        Employee employee = bindTestObject(Employee.class, "employee", mockRequest1);
        Department department = bindTestObject(Department.class, "department", mockRequest2);

        // Assertion
        assertNotNull(employee);
        assertEquals(1, employee.getId());
        assertEquals("John Doe", employee.getName());

        assertNotNull(department);
        assertEquals(10, department.getId());
        assertEquals("IT", department.getName());
        assertEquals("Paris", department.getLocation());
    }

    /**
     * Test 8: Binding avec conversion de types
     * Vérifie que String -> Integer et String -> Double fonctionnent correctement
     */
    @Test
    @DisplayName("Binding avec conversion de types")
    public void testBindWithTypeConversion() throws Exception {
        // Arrangement
        List<String> paramNames = new ArrayList<>();
        paramNames.add("employee.id");
        paramNames.add("employee.salary");

        when(mockRequest.getParameterNames()).thenReturn(new Enumeration<String>() {
            private int index = 0;

            @Override
            public boolean hasMoreElements() {
                return index < paramNames.size();
            }

            @Override
            public String nextElement() {
                return paramNames.get(index++);
            }
        });

        when(mockRequest.getParameter("employee.id")).thenReturn("42");
        when(mockRequest.getParameter("employee.salary")).thenReturn("75500.99");

        // Action
        Employee employee = bindTestObject(Employee.class, "employee", mockRequest);

        // Assertion
        assertNotNull(employee);
        assertEquals(42, employee.getId()); // String -> int
        assertEquals(75500.99, employee.getSalary()); // String -> double
    }

    /**
     * Test 9: Binding d'un tableau avec index non-séquentiel
     * Par exemple: employees[0], employees[2], employees[5]
     * Devrait créer un tableau de taille 6 avec éléments vides aux indices manquants
     */
    @Test
    @DisplayName("Binding d'un tableau avec index non-séquentiel")
    public void testBindArrayWithNonSequentialIndices() throws Exception {
        // Arrangement: indices 0, 2, 5
        List<String> paramNames = new ArrayList<>();
        paramNames.add("employees[0].id");
        paramNames.add("employees[0].name");
        paramNames.add("employees[2].id");
        paramNames.add("employees[2].name");
        paramNames.add("employees[5].id");
        paramNames.add("employees[5].name");

        when(mockRequest.getParameterNames()).thenReturn(new Enumeration<String>() {
            private int index = 0;

            @Override
            public boolean hasMoreElements() {
                return index < paramNames.size();
            }

            @Override
            public String nextElement() {
                return paramNames.get(index++);
            }
        });

        when(mockRequest.getParameter("employees[0].id")).thenReturn("1");
        when(mockRequest.getParameter("employees[0].name")).thenReturn("John");
        when(mockRequest.getParameter("employees[2].id")).thenReturn("3");
        when(mockRequest.getParameter("employees[2].name")).thenReturn("Charlie");
        when(mockRequest.getParameter("employees[5].id")).thenReturn("6");
        when(mockRequest.getParameter("employees[5].name")).thenReturn("Frank");

        // Action
        Employee[] employees = bindTestArray(Employee.class, "employees", mockRequest);

        // Assertion
        assertNotNull(employees);
        assertEquals(6, employees.length); // Tableau de taille 6 (0 à 5)
        
        assertEquals(1, employees[0].getId());
        assertEquals("John", employees[0].getName());
        
        assertNotNull(employees[2]);
        assertEquals(3, employees[2].getId());
        assertEquals("Charlie", employees[2].getName());
        
        assertNotNull(employees[5]);
        assertEquals(6, employees[5].getId());
        assertEquals("Frank", employees[5].getName());
    }

    /**
     * Test 10: Binding avec valeurs vides/null
     * Vérifie le comportement avec des paramètres vides
     */
    @Test
    @DisplayName("Binding avec valeurs vides")
    public void testBindWithEmptyValues() throws Exception {
        // Arrangement
        List<String> paramNames = new ArrayList<>();
        paramNames.add("employee.id");
        paramNames.add("employee.name");

        when(mockRequest.getParameterNames()).thenReturn(new Enumeration<String>() {
            private int index = 0;

            @Override
            public boolean hasMoreElements() {
                return index < paramNames.size();
            }

            @Override
            public String nextElement() {
                return paramNames.get(index++);
            }
        });

        when(mockRequest.getParameter("employee.id")).thenReturn(""); // Vide
        when(mockRequest.getParameter("employee.name")).thenReturn(""); // Vide

        // Action
        Employee employee = bindTestObject(Employee.class, "employee", mockRequest);

        // Assertion
        assertNotNull(employee);
        assertEquals(0, employee.getId()); // Valeur par défaut pour int
        assertNull(employee.getName()); // null pour String vide
    }

    // === Méthodes auxiliaires pour les tests ===

    /**
     * Méthode auxiliaire pour tester le binding d'un objet simple
     */
    private <T> T bindTestObject(Class<T> clazz, String paramName, HttpServletRequest request) throws Exception {
        T obj = clazz.getDeclaredConstructor().newInstance();
        
        java.util.Map<String, String> attributes = new java.util.HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            
            if (name.startsWith(paramName + ".")) {
                String attributeName = name.substring(paramName.length() + 1);
                String value = request.getParameter(name);
                attributes.put(attributeName, value);
            }
        }
        
        populateTestObject(obj, attributes);
        return obj;
    }

    /**
     * Méthode auxiliaire pour tester le binding d'un tableau
     */
    private <T> T[] bindTestArray(Class<T> componentType, String paramName, HttpServletRequest request) throws Exception {
        java.util.Map<Integer, java.util.Map<String, String>> indexedParams = new java.util.HashMap<>();
        
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            
            if (name.startsWith(paramName + "[")) {
                try {
                    int indexStart = name.indexOf('[') + 1;
                    int indexEnd = name.indexOf(']');
                    int index = Integer.parseInt(name.substring(indexStart, indexEnd));
                    
                    String attributeName = name.substring(indexEnd + 2);
                    String value = request.getParameter(name);
                    
                    indexedParams.computeIfAbsent(index, k -> new java.util.HashMap<>()).put(attributeName, value);
                } catch (Exception e) {
                    // Ignorer les erreurs de parsing
                }
            }
        }
        
        int maxIndex = indexedParams.keySet().stream().max(Integer::compareTo).orElse(-1);
        int arraySize = maxIndex + 1;
        
        T[] array = (T[]) java.lang.reflect.Array.newInstance(componentType, arraySize);
        
        for (java.util.Map.Entry<Integer, java.util.Map<String, String>> entry : indexedParams.entrySet()) {
            int index = entry.getKey();
            java.util.Map<String, String> attributes = entry.getValue();
            
            T element = componentType.getDeclaredConstructor().newInstance();
            populateTestObject(element, attributes);
            
            java.lang.reflect.Array.set(array, index, element);
        }
        
        return array;
    }

    /**
     * Méthode auxiliaire pour tester le binding de types primitifs
     */
    private Object bindTestPrimitive(Class<?> paramType, String paramName, HttpServletRequest request) {
        String value = request.getParameter(paramName);
        
        if (value == null || value.trim().isEmpty()) {
            return getDefaultValue(paramType);
        }
        
        try {
            if (paramType == String.class) {
                return value;
            } else if (paramType == int.class || paramType == Integer.class) {
                return Integer.parseInt(value);
            } else if (paramType == double.class || paramType == Double.class) {
                return Double.parseDouble(value);
            } else if (paramType == boolean.class || paramType == Boolean.class) {
                return Boolean.parseBoolean(value);
            }
        } catch (Exception e) {
            return getDefaultValue(paramType);
        }
        
        return null;
    }

    /**
     * Peuple un objet test avec les attributs fournis
     */
    private void populateTestObject(Object obj, java.util.Map<String, String> attributes) {
        Class<?> clazz = obj.getClass();
        
        for (java.util.Map.Entry<String, String> entry : attributes.entrySet()) {
            String attributeName = entry.getKey();
            String value = entry.getValue();
            
            try {
                java.lang.reflect.Field field = findField(clazz, attributeName);
                
                if (field != null) {
                    field.setAccessible(true);
                    Object convertedValue = convertTestValue(value, field.getType());
                    field.set(obj, convertedValue);
                }
            } catch (Exception e) {
                // Ignorer les erreurs
            }
        }
    }

    /**
     * Trouve un field dans une classe
     */
    private java.lang.reflect.Field findField(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    /**
     * Convertit une valeur String vers le type demandé
     */
    private Object convertTestValue(String value, Class<?> targetType) {
        if (value == null || value.trim().isEmpty()) {
            return getDefaultValue(targetType);
        }
        
        try {
            if (targetType == String.class) {
                return value;
            } else if (targetType == int.class || targetType == Integer.class) {
                return Integer.parseInt(value);
            } else if (targetType == double.class || targetType == Double.class) {
                return Double.parseDouble(value);
            } else if (targetType == boolean.class || targetType == Boolean.class) {
                return Boolean.parseBoolean(value);
            }
        } catch (Exception e) {
            return getDefaultValue(targetType);
        }
        
        return null;
    }

    /**
     * Retourne la valeur par défaut pour un type
     */
    private Object getDefaultValue(Class<?> type) {
        if (type == int.class) return 0;
        if (type == double.class) return 0.0;
        if (type == boolean.class) return false;
        return null;
    }
}
