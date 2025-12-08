<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test 7: Projets + Équipe - Sprint 8</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        .container {
            max-width: 700px;
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
            margin-bottom: 12px;
            padding-bottom: 8px;
            border-bottom: 1px solid #ddd;
            font-weight: bold;
        }
        .form-group {
            margin-bottom: 12px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #333;
            font-weight: bold;
            font-size: 0.9em;
        }
        input, select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 0.9em;
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
            <h1>Test 7: Projets + Équipe</h1>
            <p>Binding d'un tableau de projets avec un String (équipe)</p>
        </div>
        
        <div class="info-box">
            <p><strong>Format:</strong> projects[index].attribut + teamName (String)</p>
            <p><strong>Cas d'usage:</strong> Sauvegarder plusieurs projets avec le nom de l'équipe</p>
        </div>
        
        <form action="<%= request.getContextPath() %>/sprint8/saveProjectsWithTeam" method="POST">
            <div class="section-title">👥 Informations Équipe</div>
            <div class="form-group">
                <label for="team">Nom Équipe:</label>
                <input type="text" id="team" name="teamName" value="DevTeam Alpha" required>
            </div>
            
            <div class="section-title">📊 Projets</div>
            
            <div class="section-title" style="margin-top: 10px; border: none; font-size: 0.95em;">📋 Projet 1</div>
            <div class="form-group">
                <label>ID:</label>
                <input type="number" name="projects[0].id" value="101" required>
            </div>
            <div class="form-group">
                <label>Titre:</label>
                <input type="text" name="projects[0].title" value="Frontend Redesign" required>
            </div>
            <div class="form-group">
                <label>Description:</label>
                <input type="text" name="projects[0].description" value="Refonte complète du design" required>
            </div>
            <div class="form-group">
                <label>Budget:</label>
                <input type="number" name="projects[0].budget" value="75000" step="0.01" required>
            </div>
            <div class="form-group">
                <label>Actif:</label>
                <select name="projects[0].active" required>
                    <option value="true">✓ Actif</option>
                    <option value="false">✗ Inactif</option>
                </select>
            </div>
            
            <div class="section-title" style="margin-top: 10px; border: none; font-size: 0.95em;">📋 Projet 2</div>
            <div class="form-group">
                <label>ID:</label>
                <input type="number" name="projects[1].id" value="102" required>
            </div>
            <div class="form-group">
                <label>Titre:</label>
                <input type="text" name="projects[1].title" value="API Integration" required>
            </div>
            <div class="form-group">
                <label>Description:</label>
                <input type="text" name="projects[1].description" value="Intégration d'API externes" required>
            </div>
            <div class="form-group">
                <label>Budget:</label>
                <input type="number" name="projects[1].budget" value="120000" step="0.01" required>
            </div>
            <div class="form-group">
                <label>Actif:</label>
                <select name="projects[1].active" required>
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
