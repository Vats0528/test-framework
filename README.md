# Test Framework - Sprint 8

## 📋 Vue d'ensemble

Ce dossier contient l'application web de test pour valider les fonctionnalités du framework Sprint 8 - Binding Automatique avec Réflexion.

## 📁 Structure du Projet

```
test-framework/
├── src/
│   ├── main/
│   │   ├── java/com/test/
│   │   │   ├── controllers/
│   │   │   │   ├── TestController.java          # Tests des fonctionnalités basiques
│   │   │   │   ├── AdminController.java         # Tests avec @RequestParam
│   │   │   │   ├── Sprint8Controller.java       # 9 tests d'intégration Sprint 8
│   │   │   │   └── TestControllerJsp.java       # Tests JSP
│   │   │   └── models/
│   │   │       ├── Employee.java                # Modèle test
│   │   │       ├── Department.java              # Modèle test
│   │   │       └── Project.java                 # Modèle test
│   │   └── webapp/
│   │       ├── sprint8.html                     # Interface web de test (7 formulaires)
│   │       ├── index.jsp
│   │       └── test.jsp
│   └── test/
│       └── java/com/test/
│           └── ParameterBinderTest.java         # 10 tests unitaires
├── lib/
│   └── framework-1.0-SNAPSHOT.jar              # JAR du framework
├── pom.xml                                      # Dépendances (JUnit5, Mockito)
├── mvnw                                         # Maven Wrapper
└── README.md
```

## 🚀 Démarrage Rapide

### 1. Compiler le Framework

```bash
cd ../framework
mvn clean package
```

### 2. Copier le JAR du Framework

```bash
cp ../framework/target/framework-1.0-SNAPSHOT.jar lib/
```

### 3. Compiler et Tester

```bash
mvn clean test
```

### 4. Compiler le WAR

```bash
mvn package
```

## 🧪 Tests

### Tests Unitaires (ParameterBinderTest)

10 tests JUnit 5 couvrant le binding automatique:

```bash
mvn test -Dtest=ParameterBinderTest
```

**Tests inclus:**

1. ✅ Binding d'un objet simple
2. ✅ Binding d'un tableau d'objets
3. ✅ Binding de types primitifs
4. ✅ Binding avec types mixtes (int, String, double, boolean)
5. ✅ Binding avec paramètres manquants
6. ✅ Binding d'un tableau vide
7. ✅ Binding de plusieurs objets différents
8. ✅ Binding avec conversion de types
9. ✅ Binding d'un tableau avec index non-séquentiel
10. ✅ Binding avec valeurs vides

**Résumé du Résultat:**
```
Tests run: 10, Failures: 0, Errors: 0
BUILD SUCCESS
```

### Tests d'Intégration (Sprint8Controller)

9 endpoints REST testables via interface web HTML:

| # | Endpoint | Méthode | Paramètres |
|---|----------|---------|------------|
| 1 | `/sprint8/saveEmployee` | POST | `employee` (objet simple) |
| 2 | `/sprint8/saveDepartment` | POST | `department` (objet simple) |
| 3 | `/sprint8/saveEmployees` | POST | `employees[]` (tableau) |
| 4 | `/sprint8/saveEmployeesAndDepartment` | POST | `employees[]` + `department` |
| 5 | `/sprint8/saveCompleteCompany` | POST | `employees[]` + `departments[]` + `companyName` |
| 6 | `/sprint8/saveProject` | POST | `project` (types mixtes) |
| 7 | `/sprint8/saveProjectsWithTeam` | POST | `projects[]` + `teamLeader` |
| 8 | `/sprint8/saveEverything` | POST | Tous les types combinés |
| 9 | `/sprint8/saveWithOptional` | POST | Paramètres optionnels |

## 📝 Modèles de Données

### Employee.java

```java
public class Employee {
    private int id;
    private String name;
    private double salary;
    private String department;
    
    public Employee() {} // Requis
    // Getters et setters...
}
```

**Utilisation:**
```
POST /sprint8/saveEmployee
employee.id=1
employee.name=John Doe
employee.salary=50000
employee.department=IT
```

### Department.java

```java
public class Department {
    private int id;
    private String name;
    private String location;
    
    public Department() {} // Requis
    // Getters et setters...
}
```

**Utilisation:**
```
POST /sprint8/saveDepartment
department.id=10
department.name=Engineering
department.location=Paris
```

### Project.java

```java
public class Project {
    private int id;
    private String title;
    private String description;
    private double budget;
    private boolean active;
    
    public Project() {} // Requis
    // Getters et setters...
}
```

**Utilisation:**
```
POST /sprint8/saveProject
project.id=100
project.title=New Project
project.description=A great project
project.budget=150000.50
project.active=true
```

## 🌐 Interface Web de Test

Une interface HTML complète est fournie dans `src/main/webapp/sprint8.html`

### Caractéristiques

✅ Design responsive
✅ 7 formulaires interactifs
✅ Affichage des formats HTTP
✅ Documentation intégrée
✅ Exemples de code
✅ Styles modernes CSS

### Accès

```
http://localhost:8080/test-project-1.0-SNAPSHOT/sprint8.html
```

## 🔍 Exécution Manuelle des Tests

### Test 1: Employé Simple

```bash
curl -X POST "http://localhost:8080/test-project-1.0-SNAPSHOT/front/sprint8/saveEmployee" \
  --data-urlencode "employee.id=1" \
  --data-urlencode "employee.name=John Doe" \
  --data-urlencode "employee.salary=50000" \
  --data-urlencode "employee.department=IT"
```

**Réponse Attendue:**
```
Employé sauvegardé: John Doe (ID: 1, Salaire: 50000.0)
```

## 🛠️ Déploiement

### Avec Tomcat

```bash
# 1. Compiler
mvn clean package

# 2. Copier le WAR
cp target/test-project-1.0-SNAPSHOT.war /path/to/tomcat/webapps/

# 3. Redémarrer Tomcat
cd /path/to/tomcat/bin
./catalina.sh restart

# 4. Accéder
# http://localhost:8080/test-project-1.0-SNAPSHOT/
```

## 📊 Dépendances

**Dépendances de Compilation:**
- `jakarta.servlet:jakarta.servlet-api:6.0.0`
- `com.framework:framework:1.0-SNAPSHOT`

**Dépendances de Test:**
- `org.junit.jupiter:junit-jupiter-api:5.9.3`
- `org.junit.jupiter:junit-jupiter-engine:5.9.3`
- `org.mockito:mockito-core:5.3.1`
- `org.mockito:mockito-junit-jupiter:5.3.1`

## ✅ Résumé

Le test-framework fournit:

✅ 10 tests unitaires JUnit 5 (100% passage)
✅ 9 tests d'intégration dans le contrôleur
✅ 7 formulaires HTML interactifs
✅ 3 modèles de données complets
✅ Documentation complète et commentée

**Prêt à tester le framework Sprint 8! 🚀**

---

**Version:** 1.0  
**Dernière modification:** Décembre 2025  
**Auteur:** Sprint 8 Team
