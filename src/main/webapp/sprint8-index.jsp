<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sprint 8 - Tests de Binding Automatique</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
        }
        
        .header {
            text-align: center;
            color: white;
            margin-bottom: 40px;
            animation: slideDown 0.6s ease-out;
        }
        
        .header h1 {
            font-size: 2.5em;
            margin-bottom: 10px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }
        
        .header p {
            font-size: 1.1em;
            opacity: 0.9;
        }
        
        @keyframes slideDown {
            from {
                transform: translateY(-30px);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }
        
        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        
        .card {
            background: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.2);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            animation: fadeIn 0.6s ease-out forwards;
            opacity: 0;
        }
        
        .card:nth-child(1) { animation-delay: 0.1s; }
        .card:nth-child(2) { animation-delay: 0.2s; }
        .card:nth-child(3) { animation-delay: 0.3s; }
        .card:nth-child(4) { animation-delay: 0.4s; }
        .card:nth-child(5) { animation-delay: 0.5s; }
        .card:nth-child(6) { animation-delay: 0.6s; }
        .card:nth-child(7) { animation-delay: 0.7s; }
        .card:nth-child(8) { animation-delay: 0.8s; }
        .card:nth-child(9) { animation-delay: 0.9s; }
        
        @keyframes fadeIn {
            to {
                opacity: 1;
            }
        }
        
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.3);
        }
        
        .card h3 {
            color: #667eea;
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .card-badge {
            display: inline-block;
            background: #667eea;
            color: white;
            padding: 3px 8px;
            border-radius: 20px;
            font-size: 0.8em;
            font-weight: bold;
        }
        
        .card p {
            color: #666;
            margin-bottom: 15px;
            font-size: 0.95em;
            line-height: 1.5;
        }
        
        .btn {
            display: inline-block;
            padding: 10px 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-decoration: none;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
            font-weight: bold;
            width: 100%;
            text-align: center;
        }
        
        .btn:hover {
            transform: scale(1.02);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
        }
        
        .btn:active {
            transform: scale(0.98);
        }
        
        .feature-list {
            background: #f5f5f5;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 15px;
            font-size: 0.9em;
        }
        
        .feature-list ul {
            list-style: none;
            padding-left: 0;
        }
        
        .feature-list li {
            padding: 5px 0;
            padding-left: 25px;
            position: relative;
            color: #555;
        }
        
        .feature-list li:before {
            content: "✓";
            position: absolute;
            left: 0;
            color: #667eea;
            font-weight: bold;
        }
        
        .difficulty {
            font-size: 0.85em;
            color: #999;
            margin-top: 10px;
            padding-top: 10px;
            border-top: 1px solid #eee;
        }
        
        .difficulty.easy { color: #27ae60; font-weight: bold; }
        .difficulty.medium { color: #f39c12; font-weight: bold; }
        .difficulty.hard { color: #e74c3c; font-weight: bold; }
        
        .footer {
            text-align: center;
            color: white;
            padding: 30px 20px;
            border-top: 1px solid rgba(255,255,255,0.2);
            margin-top: 40px;
        }
        
        .footer p {
            margin-bottom: 10px;
        }
        
        .stats {
            display: flex;
            justify-content: center;
            gap: 30px;
            margin-top: 20px;
            flex-wrap: wrap;
        }
        
        .stat {
            text-align: center;
        }
        
        .stat-number {
            font-size: 2em;
            font-weight: bold;
            display: block;
        }
        
        .stat-label {
            font-size: 0.9em;
            opacity: 0.9;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🚀 Sprint 8 - Binding Automatique avec Réflexion</h1>
            <p>Testez les 9 endpoints de binding d'objets, tableaux et types mixtes</p>
        </div>
        
        <div class="grid">
            <!-- Test 1: Simple Object -->
            <div class="card">
                <h3><span class="card-badge">1</span> Employé Simple</h3>
                <div class="feature-list">
                    <ul>
                        <li>Binding d'un objet</li>
                        <li>Conversion automatique</li>
                        <li>Paramètres: employee.id, employee.name, etc.</li>
                    </ul>
                </div>
                <p>Testez le binding d'un simple objet Employee avec tous ses attributs</p>
                <a href="sprint8-employee.jsp" class="btn">Tester</a>
                <div class="difficulty easy">⭐ Difficultés: Facile</div>
            </div>
            
            <!-- Test 2: Department -->
            <div class="card">
                <h3><span class="card-badge">2</span> Département Simple</h3>
                <div class="feature-list">
                    <ul>
                        <li>Binding d'un objet</li>
                        <li>Types String et int</li>
                        <li>Paramètres: department.id, department.name, etc.</li>
                    </ul>
                </div>
                <p>Testez le binding d'un objet Department simple</p>
                <a href="sprint8-department.jsp" class="btn">Tester</a>
                <div class="difficulty easy">⭐ Difficultés: Facile</div>
            </div>
            
            <!-- Test 3: Array of Employees -->
            <div class="card">
                <h3><span class="card-badge">3</span> Plusieurs Employés</h3>
                <div class="feature-list">
                    <ul>
                        <li>Binding de tableau</li>
                        <li>Format: object[index].attribute</li>
                        <li>Création dynamique du tableau</li>
                    </ul>
                </div>
                <p>Testez le binding d'un tableau d'employés avec indices</p>
                <a href="sprint8-employees.jsp" class="btn">Tester</a>
                <div class="difficulty medium">⭐⭐ Difficultés: Moyen</div>
            </div>
            
            <!-- Test 4: Employees and Department -->
            <div class="card">
                <h3><span class="card-badge">4</span> Employés + Département</h3>
                <div class="feature-list">
                    <ul>
                        <li>Binding mixte (tableau + objet)</li>
                        <li>Paramètres multiples</li>
                        <li>Gestion de plusieurs objets</li>
                    </ul>
                </div>
                <p>Testez le binding combiné d'un tableau et d'un objet</p>
                <a href="sprint8-employees-dept.jsp" class="btn">Tester</a>
                <div class="difficulty medium">⭐⭐ Difficultés: Moyen</div>
            </div>
            
            <!-- Test 5: Complete Company -->
            <div class="card">
                <h3><span class="card-badge">5</span> Entreprise Complète</h3>
                <div class="feature-list">
                    <ul>
                        <li>Plusieurs tableaux</li>
                        <li>Types primitifs (String)</li>
                        <li>Binding complexe</li>
                    </ul>
                </div>
                <p>Testez le binding complet avec employés, départements et nom d'entreprise</p>
                <a href="sprint8-company.jsp" class="btn">Tester</a>
                <div class="difficulty hard">⭐⭐⭐ Difficultés: Difficile</div>
            </div>
            
            <!-- Test 6: Project -->
            <div class="card">
                <h3><span class="card-badge">6</span> Projet Simple</h3>
                <div class="feature-list">
                    <ul>
                        <li>Binding d'objet</li>
                        <li>Types mixtes (int, double, boolean)</li>
                        <li>Conversion de booléen</li>
                    </ul>
                </div>
                <p>Testez le binding d'un objet Project avec types variés</p>
                <a href="sprint8-project.jsp" class="btn">Tester</a>
                <div class="difficulty medium">⭐⭐ Difficultés: Moyen</div>
            </div>
            
            <!-- Test 7: Projects with Team -->
            <div class="card">
                <h3><span class="card-badge">7</span> Projets + Équipe</h3>
                <div class="feature-list">
                    <ul>
                        <li>Tableau de projets</li>
                        <li>String (nom d'équipe)</li>
                        <li>Binding mixte</li>
                    </ul>
                </div>
                <p>Testez le binding de plusieurs projets avec le nom de l'équipe</p>
                <a href="sprint8-projects-team.jsp" class="btn">Tester</a>
                <div class="difficulty hard">⭐⭐⭐ Difficultés: Difficile</div>
            </div>
            
            <!-- Test 8: Everything -->
            <div class="card">
                <h3><span class="card-badge">8</span> Tout Combiné</h3>
                <div class="feature-list">
                    <ul>
                        <li>Tous les types à la fois</li>
                        <li>Binding ultra-complexe</li>
                        <li>Gestion complète</li>
                    </ul>
                </div>
                <p>Testez le binding de TOUS les paramètres combinés (max test)</p>
                <a href="sprint8-everything.jsp" class="btn">Tester</a>
                <div class="difficulty hard">⭐⭐⭐ Difficultés: Très Difficile</div>
            </div>
            
            <!-- Test 9: Optional Parameters -->
            <div class="card">
                <h3><span class="card-badge">9</span> Paramètres Optionnels</h3>
                <div class="feature-list">
                    <ul>
                        <li>Paramètres manquants</li>
                        <li>Valeurs par défaut</li>
                        <li>Gestion gracieuse</li>
                    </ul>
                </div>
                <p>Testez le binding avec des paramètres manquants ou vides</p>
                <a href="sprint8-optional.jsp" class="btn">Tester</a>
                <div class="difficulty easy">⭐ Difficultés: Facile</div>
            </div>
        </div>
        
        <div class="footer">
            <h2>📊 Informations</h2>
            <div class="stats">
                <div class="stat">
                    <span class="stat-number">9</span>
                    <span class="stat-label">Tests disponibles</span>
                </div>
                <div class="stat">
                    <span class="stat-number">10</span>
                    <span class="stat-label">Tests unitaires (100% ✅)</span>
                </div>
                <div class="stat">
                    <span class="stat-number">3</span>
                    <span class="stat-label">Types d'objets</span>
                </div>
                <div class="stat">
                    <span class="stat-number">∞</span>
                    <span class="stat-label">Cas d'usage</span>
                </div>
            </div>
            <p style="margin-top: 30px; opacity: 0.8;">
                Binding Automatique avec Réflexion Java | Sprint 8 | Décembre 2025
            </p>
        </div>
    </div>
</body>
</html>
