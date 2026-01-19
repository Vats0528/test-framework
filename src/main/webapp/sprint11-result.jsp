<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.test.models.User" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultat - Sprint 11</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .result {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .success {
            color: #28a745;
            font-size: 24px;
            margin-bottom: 20px;
        }
        .error {
            color: #dc3545;
            font-size: 24px;
            margin-bottom: 20px;
        }
        .info {
            background-color: #e9ecef;
            padding: 15px;
            border-radius: 5px;
            margin: 15px 0;
        }
        .back-link {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        .back-link:hover {
            background-color: #0056b3;
        }
        pre {
            background-color: #f8f9fa;
            padding: 15px;
            border-left: 4px solid #007bff;
            overflow-x: auto;
        }
    </style>
</head>
<body>
    <div class="result">
        <%
            Boolean success = (Boolean) request.getAttribute("success");
            String message = (String) request.getAttribute("message");
            User user = (User) request.getAttribute("user");
            String username = (String) request.getAttribute("username");
            String sessionId = (String) request.getAttribute("sessionId");
            String sessionDuration = (String) request.getAttribute("sessionDuration");
            Map<String, Object> sessionData = (Map<String, Object>) request.getAttribute("sessionData");
        %>
        
        <div class="<%= (success != null && success) ? "success" : "error" %>">
            <%= success != null && success ? "" : "" %> <%= message %>
        </div>
        
        <% if (user != null) { %>
            <div class="info">
                <h3>Informations utilisateur :</h3>
                <p><strong>Nom d'utilisateur :</strong> <%= user.getUsername() %></p>
                <p><strong>Rôle :</strong> <%= user.getRole() %></p>
                <p><strong>Email :</strong> <%= user.getEmail() != null ? user.getEmail() : "Non renseigné" %></p>
            </div>
        <% } %>
        
        <% if (username != null) { %>
            <div class="info">
                <p><strong>Username (depuis session) :</strong> <%= username %></p>
            </div>
        <% } %>
        
        <% if (sessionDuration != null) { %>
            <div class="info">
                <p><strong>Durée de la session :</strong> <%= sessionDuration %></p>
            </div>
        <% } %>
        
        <% if (sessionId != null) { %>
            <div class="info">
                <p><strong>Session ID :</strong> <%= sessionId %></p>
            </div>
        <% } %>
        
        <% if (sessionData != null && !sessionData.isEmpty()) { %>
            <div class="info">
                <h3>Données de session :</h3>
                <pre><%= sessionData %></pre>
            </div>
        <% } %>
        
        <a href="<%= request.getContextPath() %>/front/sprint11-index.jsp" class="back-link">← Retour aux tests</a>
    </div>
</body>
</html>
