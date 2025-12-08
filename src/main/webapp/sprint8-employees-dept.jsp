<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test 4: Employés + Département - Sprint 8</title>
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
            font-size: 1.2em;
            margin-top: 25px;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 1px solid #ddd;
            font-weight: bold;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: bold;
            font-size: 0.95em;
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
            <h1>Test 4: Employés + Département</h1>
            <p>Binding mixte d'un tableau et d'un objet</p>
        </div>
        
        <div class="info-box">
            <p><strong>Format de binding:</strong> <code>employees[index].attribut=valeur</code> + <code>department.attribut=valeur</code></p>
            <p><strong>Cas d'usage:</strong> Sauvegarder plusieurs employés et leur département en une seule requête</p>
        </div>
        
        <form action="<%= request.getContextPath() %>/sprint8/saveEmployeesAndDepartment" method="POST">
            <div class="section-title">👥 Employés (Tableau)</div>
            
            <div class="section-title" style="margin-top: 15px; border: none;">📋 Employé 1</div>
            <div class="form-group">
                <label>ID:</label>
                <input type="number" name="employees[0].id" value="1" required>
            </div>
            <div class="form-group">
                <label>Nom:</label>
                <input type="text" name="employees[0].name" value="Alice Martin" required>
            </div>
            <div class="form-group">
                <label>Salaire:</label>
                <input type="number" name="employees[0].salary" value="55000" step="0.01" required>
            </div>
            <div class="form-group">
                <label>Département:</label>
                <input type="text" name="employees[0].department" value="IT" required>
            </div>
            
            <div class="section-title" style="margin-top: 15px; border: none;">📋 Employé 2</div>
            <div class="form-group">
                <label>ID:</label>
                <input type="number" name="employees[1].id" value="2" required>
            </div>
            <div class="form-group">
                <label>Nom:</label>
                <input type="text" name="employees[1].name" value="Bob Wilson" required>
            </div>
            <div class="form-group">
                <label>Salaire:</label>
                <input type="number" name="employees[1].salary" value="60000" step="0.01" required>
            </div>
            <div class="form-group">
                <label>Département:</label>
                <input type="text" name="employees[1].department" value="IT" required>
            </div>
            
            <div class="section-title">🏢 Département</div>
            <div class="form-group">
                <label>ID Département:</label>
                <input type="number" name="department.id" value="10" required>
            </div>
            <div class="form-group">
                <label>Nom Département:</label>
                <input type="text" name="department.name" value="Information Technology" required>
            </div>
            <div class="form-group">
                <label>Localisation:</label>
                <input type="text" name="department.location" value="Paris" required>
            </div>
            
            <div class="btn-group">
                <button type="submit" class="btn-submit">✓ Soumettre</button>
                <button type="reset" class="btn-reset">↺ Réinitialiser</button>
            </div>
        </form>
    </div>
</body>
</html>
