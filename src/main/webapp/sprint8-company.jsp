<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test 5: Entreprise Complète - Sprint 8</title>
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
        input {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 0.9em;
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
            <h1>Test 5: Entreprise Complète</h1>
            <p>Binding complexe avec tableaux multiples et type primitif</p>
        </div>
        
        <div class="info-box">
            <p><strong>Format:</strong> employees[], departments[], companyName (String)</p>
            <p><strong>Cas d'usage:</strong> Sauvegarder toute une structure d'entreprise</p>
        </div>
        
        <form action="<%= request.getContextPath() %>/sprint8/saveCompleteCompany" method="POST">
            <div class="section-title">🏢 Informations Entreprise</div>
            <div class="form-group">
                <label>Nom Entreprise:</label>
                <input type="text" name="companyName" value="TechCorp Inc" required>
            </div>
            
            <div class="section-title">👥 Employés</div>
            <div class="section-title" style="margin-top: 10px; border: none; font-size: 0.95em;">📋 Employé 1</div>
            <div class="form-group">
                <label>ID:</label>
                <input type="number" name="employees[0].id" value="1" required>
            </div>
            <div class="form-group">
                <label>Nom:</label>
                <input type="text" name="employees[0].name" value="Alex Dupont" required>
            </div>
            <div class="form-group">
                <label>Salaire:</label>
                <input type="number" name="employees[0].salary" value="52000" step="0.01" required>
            </div>
            <div class="form-group">
                <label>Département:</label>
                <input type="text" name="employees[0].department" value="IT" required>
            </div>
            
            <div class="section-title" style="margin-top: 10px; border: none; font-size: 0.95em;">📋 Employé 2</div>
            <div class="form-group">
                <label>ID:</label>
                <input type="number" name="employees[1].id" value="2" required>
            </div>
            <div class="form-group">
                <label>Nom:</label>
                <input type="text" name="employees[1].name" value="Sophie Laurent" required>
            </div>
            <div class="form-group">
                <label>Salaire:</label>
                <input type="number" name="employees[1].salary" value="58000" step="0.01" required>
            </div>
            <div class="form-group">
                <label>Département:</label>
                <input type="text" name="employees[1].department" value="IT" required>
            </div>
            
            <div class="section-title">🏢 Départements</div>
            <div class="section-title" style="margin-top: 10px; border: none; font-size: 0.95em;">📋 Département 1</div>
            <div class="form-group">
                <label>ID:</label>
                <input type="number" name="departments[0].id" value="10" required>
            </div>
            <div class="form-group">
                <label>Nom:</label>
                <input type="text" name="departments[0].name" value="IT Department" required>
            </div>
            <div class="form-group">
                <label>Localisation:</label>
                <input type="text" name="departments[0].location" value="Paris" required>
            </div>
            
            <div class="section-title" style="margin-top: 10px; border: none; font-size: 0.95em;">📋 Département 2</div>
            <div class="form-group">
                <label>ID:</label>
                <input type="number" name="departments[1].id" value="20" required>
            </div>
            <div class="form-group">
                <label>Nom:</label>
                <input type="text" name="departments[1].name" value="HR Department" required>
            </div>
            <div class="form-group">
                <label>Localisation:</label>
                <input type="text" name="departments[1].location" value="Lyon" required>
            </div>
            
            <div class="btn-group">
                <button type="submit" class="btn-submit">✓ Soumettre</button>
                <button type="reset" class="btn-reset">↺ Réinitialiser</button>
            </div>
        </form>
    </div>
</body>
</html>
