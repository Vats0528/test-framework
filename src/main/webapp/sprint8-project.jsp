<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test 6: Projet Simple - Sprint 8</title>
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
        input, select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1em;
            transition: border-color 0.3s;
        }
        input:focus, select:focus {
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
            <h1>Test 6: Projet Simple</h1>
            <p>Binding d'un objet Project avec types mixtes</p>
        </div>
        
        <div class="info-box">
            <p><strong>Format de binding:</strong> <code>project.attribut=valeur</code></p>
            <p><strong>Types:</strong> int, String, double, boolean</p>
            <p><strong>Cas d'usage:</strong> Sauvegarder les données d'un projet</p>
        </div>
        
        <form action="<%= request.getContextPath() %>/sprint8/saveProject" method="POST">
            <div class="form-group">
                <label for="id">ID Projet:</label>
                <input type="number" id="id" name="project.id" value="100" required>
            </div>
            
            <div class="form-group">
                <label for="title">Titre:</label>
                <input type="text" id="title" name="project.title" value="Application Web 2024" required>
            </div>
            
            <div class="form-group">
                <label for="description">Description:</label>
                <input type="text" id="description" name="project.description" value="Développement d'une plateforme e-commerce" required>
            </div>
            
            <div class="form-group">
                <label for="budget">Budget (€):</label>
                <input type="number" id="budget" name="project.budget" value="150000" step="0.01" required>
            </div>
            
            <div class="form-group">
                <label for="active">Statut:</label>
                <select id="active" name="project.active" required>
                    <option value="true">✓ Actif</option>
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
