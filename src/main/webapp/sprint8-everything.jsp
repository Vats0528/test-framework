<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test 8: Tout Combiné - Sprint 8</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            max-height: 90vh;
            overflow-y: auto;
        }
        .header {
            color: #667eea;
            margin-bottom: 30px;
            border-bottom: 2px solid #667eea;
            padding-bottom: 15px;
        }
        .header h1 { font-size: 1.8em; margin-bottom: 5px; }
        .header p { color: #666; font-size: 0.95em; }
        .back-link {
            display: inline-block;
            color: #667eea;
            text-decoration: none;
            margin-bottom: 20px;
            font-size: 0.9em;
        }
        .back-link:hover { text-decoration: underline; }
        .info-box {
            background: #f5f5f5;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            border-left: 4px solid #e74c3c;
        }
        .info-box p {
            color: #555;
            font-size: 0.9em;
            line-height: 1.6;
        }
        .info-box strong { color: #e74c3c; }
        .section-title {
            color: #667eea;
            font-size: 1.05em;
            margin-top: 15px;
            margin-bottom: 10px;
            padding-bottom: 8px;
            border-bottom: 1px solid #ddd;
            font-weight: bold;
        }
        .form-group {
            margin-bottom: 10px;
        }
        label {
            display: block;
            margin-bottom: 4px;
            color: #333;
            font-weight: bold;
            font-size: 0.85em;
        }
        input, select {
            width: 100%;
            padding: 6px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 0.85em;
            transition: border-color 0.3s;
        }
        input:focus, select:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 5px rgba(102, 126, 234, 0.3);
        }
        .btn-group {
            display: flex;
            gap: 10px;
            margin-top: 30px;
            position: sticky;
            bottom: 0;
            background: white;
            padding-top: 20px;
        }
        button {
            flex: 1;
            padding: 12px;
            border: none;
            border-radius: 5px;
            font-size: 1em;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
            font-weight: bold;
        }
        .btn-submit {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        .btn-reset {
            background: #eee;
            color: #333;
        }
        .btn-reset:hover { background: #ddd; }
    </style>
</head>
<body>
    <div class="container">
        <a href="sprint8-index.jsp" class="back-link">← Retour à l'index</a>
        
        <div class="header">
            <h1>Test 8: Tout Combiné</h1>
            <p>Le test maximal avec TOUS les types combinés</p>
        </div>
        
        <div class="info-box">
            <p><strong>⚠️ Test Ultra-Complexe!</strong></p>
            <p>Format: employees[], departments[], projects[], description (String)</p>
            <p><strong>Cas d'usage:</strong> Tester les limites du binding avec tous les types</p>
        </div>
        
        <form action="<%= request.getContextPath() %>/sprint8/saveEverything" method="POST">
            <div class="section-title">📝 Description</div>
            <div class="form-group">
                <label>Description:</label>
                <input type="text" name="description" value="Test complet du binding" required>
            </div>
            
            <div class="section-title">👥 Employés</div>
            <div class="section-title" style="margin-top: 8px; border: none; font-size: 0.9em;">Employé 1</div>
            <div class="form-group">
                <label>ID:</label>
                <input type="number" name="employees[0].id" value="1" required>
            </div>
            <div class="form-group">
                <label>Nom:</label>
                <input type="text" name="employees[0].name" value="Alice" required>
            </div>
            <div class="form-group">
                <label>Salaire:</label>
                <input type="number" name="employees[0].salary" value="50000" step="0.01" required>
            </div>
            <div class="form-group">
                <label>Département:</label>
                <input type="text" name="employees[0].department" value="IT" required>
            </div>
            
            <div class="section-title">🏢 Départements</div>
            <div class="section-title" style="margin-top: 8px; border: none; font-size: 0.9em;">Département 1</div>
            <div class="form-group">
                <label>ID:</label>
                <input type="number" name="departments[0].id" value="10" required>
            </div>
            <div class="form-group">
                <label>Nom:</label>
                <input type="text" name="departments[0].name" value="IT" required>
            </div>
            <div class="form-group">
                <label>Localisation:</label>
                <input type="text" name="departments[0].location" value="Paris" required>
            </div>
            
            <div class="section-title">📊 Projets</div>
            <div class="section-title" style="margin-top: 8px; border: none; font-size: 0.9em;">Projet 1</div>
            <div class="form-group">
                <label>ID:</label>
                <input type="number" name="projects[0].id" value="100" required>
            </div>
            <div class="form-group">
                <label>Titre:</label>
                <input type="text" name="projects[0].title" value="Web App" required>
            </div>
            <div class="form-group">
                <label>Description:</label>
                <input type="text" name="projects[0].description" value="Appli web" required>
            </div>
            <div class="form-group">
                <label>Budget:</label>
                <input type="number" name="projects[0].budget" value="100000" step="0.01" required>
            </div>
            <div class="form-group">
                <label>Actif:</label>
                <select name="projects[0].active" required>
                    <option value="true" selected>✓ Actif</option>
                    <option value="false">✗ Inactif</option>
                </select>
            </div>
            
            <div class="btn-group">
                <button type="submit" class="btn-submit">✓ Soumettre</button>
                <button type="reset" class="btn-reset">↺ Réinitialiser</button>
            </div>
        </form>
    </div>
</body>
</html>
