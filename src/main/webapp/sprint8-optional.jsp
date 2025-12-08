<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test 9: Paramètres Optionnels - Sprint 8</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
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
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: bold;
        }
        input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1em;
            transition: border-color 0.3s;
        }
        input:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 5px rgba(102, 126, 234, 0.3);
        }
        .info-box {
            background: #f5f5f5;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            border-left: 4px solid #667eea;
        }
        .info-box p {
            color: #555;
            font-size: 0.9em;
            line-height: 1.6;
        }
        .section-title {
            color: #667eea;
            font-size: 1.1em;
            margin-top: 20px;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 1px solid #ddd;
            font-weight: bold;
        }
        .optional-notice {
            background: #fff3cd;
            padding: 10px;
            border-radius: 5px;
            margin-top: 8px;
            font-size: 0.85em;
            color: #856404;
            border-left: 4px solid #ffc107;
        }
        .btn-group {
            display: flex;
            gap: 10px;
            margin-top: 30px;
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
            <h1>Test 9: Paramètres Optionnels</h1>
            <p>Tester le binding avec des paramètres manquants</p>
        </div>
        
        <div class="info-box">
            <p><strong>Objectif:</strong> Vérifier que le binding gère gracieusement les paramètres manquants ou vides</p>
            <p><strong>Cas d'usage:</strong> Les utilisateurs ne soumettent pas toujours tous les champs</p>
        </div>
        
        <form action="<%= request.getContextPath() %>/sprint8/saveWithOptional" method="POST">
            <div class="section-title">👤 Employé (Facultatif)</div>
            <div class="form-group">
                <label for="emp-id">ID Employé:</label>
                <input type="number" id="emp-id" name="employee.id" placeholder="Laissez vide pour ommettre">
                <div class="optional-notice">💡 Optionnel - laissez vide pour tester les valeurs par défaut</div>
            </div>
            
            <div class="form-group">
                <label for="emp-name">Nom:</label>
                <input type="text" id="emp-name" name="employee.name" placeholder="Laissez vide pour ommettre">
                <div class="optional-notice">💡 Optionnel</div>
            </div>
            
            <div class="form-group">
                <label for="emp-salary">Salaire:</label>
                <input type="number" id="emp-salary" name="employee.salary" placeholder="Laissez vide pour ommettre" step="0.01">
                <div class="optional-notice">💡 Optionnel</div>
            </div>
            
            <div class="form-group">
                <label for="emp-dept">Département:</label>
                <input type="text" id="emp-dept" name="employee.department" placeholder="Laissez vide pour ommettre">
                <div class="optional-notice">💡 Optionnel</div>
            </div>
            
            <div class="section-title">🏢 Département (Facultatif)</div>
            <div class="form-group">
                <label for="dept-id">ID Département:</label>
                <input type="number" id="dept-id" name="department.id" placeholder="Laissez vide pour ommettre">
                <div class="optional-notice">💡 Optionnel - laissez vide pour tester les valeurs par défaut</div>
            </div>
            
            <div class="form-group">
                <label for="dept-name">Nom:</label>
                <input type="text" id="dept-name" name="department.name" placeholder="Laissez vide pour ommettre">
                <div class="optional-notice">💡 Optionnel</div>
            </div>
            
            <div class="form-group">
                <label for="dept-location">Localisation:</label>
                <input type="text" id="dept-location" name="department.location" placeholder="Laissez vide pour ommettre">
                <div class="optional-notice">💡 Optionnel</div>
            </div>
            
            <div class="section-title">🧪 Cas de Test</div>
            <div class="info-box">
                <p><strong>Essayez:</strong></p>
                <p>1. Soumettre avec TOUS les champs vides</p>
                <p>2. Soumettre avec seulement employee.id</p>
                <p>3. Soumettre avec employee.name vide mais id rempli</p>
                <p>4. Soumettre avec tous les paramètres remplis</p>
            </div>
            
            <div class="btn-group">
                <button type="submit" class="btn-submit">✓ Soumettre</button>
                <button type="reset" class="btn-reset">↺ Réinitialiser</button>
            </div>
        </form>
    </div>
</body>
</html>
