// TestController.java (version mise à jour)
package com.test.controllers;

import com.framework.annotation.Controller;
import com.framework.annotation.Url;

@Controller("/front") // URL de base pour toutes les méthodes de ce contrôleur
public class TestController {

    @Url("/home") // Devient /front/home
    public String home() {
        return "Bienvenue sur la page d'accueil 🏠 - Controller: " + this.getClass().getSimpleName();
    }

    @Url("/about") // Devient /front/about
    public String about() {
        return "À propos du framework 🚀 - Controller: " + this.getClass().getSimpleName();
    }

    // Méthode sans annotation - ne sera pas mappée
    public String notMapped() {
        return "Cette méthode ne sera jamais appelée via URL";
    }
}