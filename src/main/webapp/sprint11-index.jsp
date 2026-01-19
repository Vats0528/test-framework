<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sprint 11 & 11 bis - Sessions et Sécurité</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 1200px;
            margin: 20px auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        h1 {
            color: #333;
            border-bottom: 3px solid #007bff;
            padding-bottom: 10px;
        }
        h2 {
            color: #555;
            margin-top: 30px;
            border-bottom: 2px solid #28a745;
            padding-bottom: 8px;
        }
        .section {
            background: white;
            padding: 20px;
            margin: 20px 0;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .test-link {
            display: inline-block;
            margin: 10px 10px 10px 0;
            padding: 12px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s;
        }
        .test-link:hover {
            background-color: #0056b3;
        }
        .test-link.secure {
            background-color: #28a745;
        }
        .test-link.secure:hover {
            background-color: #218838;
        }
        .test-link.admin {
            background-color: #dc3545;
        }
        .test-link.admin:hover {
            background-color: #c82333;
        }
        .test-link.public {
            background-color: #6c757d;
        }
        .test-link.public:hover {
            background-color: #5a6268;
        }
        .form-section {
            background: #e9ecef;
            padding: 15px;
            margin: 15px 0;
            border-radius: 5px;
        }
        input[type="text"], input[type="password"], input[type="email"] {
            padding: 8px;
            margin: 5px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
            width: 250px;
        }
        button {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-left: 10px;
        }
        button:hover {
            background-color: #0056b3;
        }
        .note {
            background-color: #fff3cd;
            border-left: 4px solid #ffc107;
            padding: 15px;
            margin: 15px 0;
        }
        .code {
            background-color: #f8f9fa;
            padding: 10px;
            border-left: 4px solid #007bff;
            font-family: 'Courier New', monospace;
            margin: 10px 0;
        }
    </style>
</head>
<body>
    <h1>🎯 Sprint 11 & 11 bis - Sessions et Sécurité</h1>
    
    <div class="section">
        <h2>📋 Sprint 11 : Gestion des Sessions</h2>
        
        <div class="note">
            <strong>Objectif :</strong> Gérer les sessions HTTP via Controller et FrontServlet
            <ul>
                <li>Ajouter / récupérer / supprimer des données dans la session</li>
                <li>La session est une Map&lt;String, Object&gt;</li>
                <li>Utiliser l'annotation @Session pour les paramètres</li>
            </ul>
        </div>

        <h3>1. Connexion (ajout dans la session)</h3>
        <div class="form-section">
            <form action="<%= request.getContextPath() %>/front/session/login" method="GET">
                <input type="text" name="username" placeholder="Username" required>
                <input type="password" name="password" placeholder="Password" required>
                <button type="submit">Se connecter</button>
            </form>
            <div class="code">
                Testez avec :<br>
                - admin / admin123 → rôle ADMIN<br>
                - john / pass → rôle USER
            </div>
        </div>

        <h3>2. Récupération depuis la session</h3>
        <a href="<%= request.getContextPath() %>/front/session/profile" class="test-link">Voir mon profil (@Session)</a>
        <a href="<%= request.getContextPath() %>/front/session/info" class="test-link">Infos session complète</a>

        <h3>3. Modification de la session</h3>
        <div class="form-section">
            <form action="<%= request.getContextPath() %>/front/session/update" method="POST">
                <input type="email" name="email" placeholder="Nouvel email" required>
                <button type="submit">Mettre à jour l'email</button>
            </form>
        </div>

        <h3>4. Suppression de la session</h3>
        <a href="<%= request.getContextPath() %>/front/session/logout" class="test-link">Se déconnecter (clear session)</a>
    </div>

    <div class="section">
        <h2>🔒 Sprint 11 bis : Sécurité et Contrôle d'Accès</h2>
        
        <div class="note">
            <strong>Objectif :</strong> Sécuriser l'accès aux méthodes du Controller
            <ul>
                <li>@AllowAnonymous : accessible à tous</li>
                <li>@Authenticated : accessible seulement aux utilisateurs connectés</li>
                <li>@RequiresRole("ROLE") : accessible selon le rôle</li>
                <li>Vérification AVANT l'invocation de la méthode</li>
            </ul>
        </div>

        <h3>1. Page publique (@AllowAnonymous)</h3>
        <a href="<%= request.getContextPath() %>/front/session/public" class="test-link public">Page publique (accessible à tous)</a>

        <h3>2. Page protégée (@Authenticated)</h3>
        <a href="<%= request.getContextPath() %>/front/session/dashboard" class="test-link secure">Dashboard (nécessite authentification)</a>
        <div class="code">
             Retourne 401 si non connecté
        </div>

        <h3>3. Page Admin (@RequiresRole("ADMIN"))</h3>
        <a href="<%= request.getContextPath() %>/front/session/admin" class="test-link admin">Panneau Admin (rôle ADMIN requis)</a>
        <div class="code">
             Retourne 401 si non connecté<br>
             Retourne 403 si rôle insuffisant
        </div>

        <h3>4. Page Modérateur (@RequiresRole("MODERATOR"))</h3>
        <a href="<%= request.getContextPath() %>/front/session/moderate" class="test-link admin">Panneau Modération (rôle MODERATOR requis)</a>
        <div class="code">
             Doit retourner 403 car aucun utilisateur n'a ce rôle dans nos tests
        </div>
    </div>

</body>
</html>
