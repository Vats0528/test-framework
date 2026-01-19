package com.test.controllers;

import com.framework.annotation.*;
import com.framework.model.ModelView;
import com.framework.util.CustomSession;
import com.framework.util.Auth;
import com.test.models.User;

/**
 * Sprint 11 & 11 bis: Controller de démonstration pour la gestion des sessions et la sécurité
 */
@Controller
public class SessionController {

    /**
     * SPRINT 11: Démonstration de l'ajout de données dans la session
     */
    @GetMapping("/session/login")
    @AllowAnonymous
    public ModelView login(String username, String password, @Session CustomSession session) {
        ModelView mv = new ModelView("sprint11-result");
        
        // Validation basique (pour la démo)
        if (username == null || username.isEmpty()) {
            mv.addAttribute("message", "Veuillez fournir un nom d'utilisateur");
            mv.addAttribute("success", false);
            return mv;
        }
        
        // Créer un utilisateur selon les credentials
        User user;
        if ("admin".equals(username) && "admin123".equals(password)) {
            user = new User(username, password, "ADMIN", username + "@example.com");
        } else {
            user = new User(username, password, "USER", username + "@example.com");
        }
        
        // Ajouter l'utilisateur dans la session
        session.set("auth", user);
        session.set("username", username);
        session.set("loginTime", System.currentTimeMillis());
        
        mv.addAttribute("message", "Connexion réussie ! Bienvenue " + username);
        mv.addAttribute("user", user);
        mv.addAttribute("success", true);
        
        return mv;
    }

    /**
     * SPRINT 11: Récupération de données depuis la session avec @Session
     */
    @GetMapping("/session/profile")
    public ModelView profile(@Session String username, @Session("loginTime") Long loginTime) {
        ModelView mv = new ModelView("sprint11-result");
        
        if (username == null) {
            mv.addAttribute("message", "Vous n'êtes pas connecté");
            mv.addAttribute("success", false);
        } else {
            long duration = System.currentTimeMillis() - (loginTime != null ? loginTime : 0);
            mv.addAttribute("message", "Profil de " + username);
            mv.addAttribute("username", username);
            mv.addAttribute("sessionDuration", duration / 1000 + " secondes");
            mv.addAttribute("success", true);
        }
        
        return mv;
    }

    /**
     * SPRINT 11: Injection de l'objet CustomSession complet
     */
    @GetMapping("/session/info")
    public ModelView sessionInfo(@Session CustomSession session) {
        ModelView mv = new ModelView("sprint11-result");
        
        mv.addAttribute("sessionId", session.getId());
        mv.addAttribute("sessionData", session.getAll());
        mv.addAttribute("message", "Informations de session");
        mv.addAttribute("success", true);
        
        return mv;
    }

    /**
     * SPRINT 11: Modification de données dans la session
     */
    @PostMapping("/session/update")
    public ModelView updateSession(String email, @Session CustomSession session) {
        ModelView mv = new ModelView("sprint11-result");
        
        User user = session.get("auth", User.class);
        if (user == null) {
            mv.addAttribute("message", "Vous devez être connecté");
            mv.addAttribute("success", false);
        } else {
            user.setEmail(email);
            session.set("auth", user);
            mv.addAttribute("message", "Email mis à jour : " + email);
            mv.addAttribute("user", user);
            mv.addAttribute("success", true);
        }
        
        return mv;
    }

    /**
     * SPRINT 11: Suppression de données de la session
     */
    @GetMapping("/session/logout")
    public ModelView logout(@Session CustomSession session) {
        ModelView mv = new ModelView("sprint11-result");
        
        String username = (String) session.get("username");
        session.clear();
        
        mv.addAttribute("message", "Déconnexion réussie" + (username != null ? " (" + username + ")" : ""));
        mv.addAttribute("success", true);
        
        return mv;
    }

    /**
     * SPRINT 11 BIS: Méthode accessible uniquement aux utilisateurs authentifiés
     */
    @GetMapping("/session/dashboard")
    @Authenticated
    public ModelView dashboard(@Session User auth) {
        ModelView mv = new ModelView("sprint11-result");
        
        mv.addAttribute("message", "Bienvenue sur votre tableau de bord, " + auth.getUsername());
        mv.addAttribute("user", auth);
        mv.addAttribute("success", true);
        
        return mv;
    }

    /**
     * SPRINT 11 BIS: Méthode accessible uniquement aux ADMIN
     */
    @GetMapping("/session/admin")
    @RequiresRole("ADMIN")
    public ModelView adminPanel(@Session User auth) {
        ModelView mv = new ModelView("sprint11-result");
        
        mv.addAttribute("message", "Panneau d'administration - Accès réservé aux ADMIN");
        mv.addAttribute("user", auth);
        mv.addAttribute("success", true);
        
        return mv;
    }

    /**
     * SPRINT 11 BIS: Méthode accessible uniquement aux MODERATOR
     */
    @GetMapping("/session/moderate")
    @RequiresRole("MODERATOR")
    public ModelView moderatePanel(@Session User auth) {
        ModelView mv = new ModelView("sprint11-result");
        
        mv.addAttribute("message", "Panneau de modération - Accès réservé aux MODERATOR");
        mv.addAttribute("user", auth);
        mv.addAttribute("success", true);
        
        return mv;
    }

    /**
     * SPRINT 11 BIS: Méthode publique (explicitement)
     */
    @GetMapping("/session/public")
    @AllowAnonymous
    public ModelView publicPage() {
        ModelView mv = new ModelView("sprint11-result");
        
        mv.addAttribute("message", "Page publique - accessible à tous");
        mv.addAttribute("success", true);
        
        return mv;
    }
}
