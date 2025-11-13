package com.test.controllers;

import com.framework.annotation.*;


<<<<<<< Updated upstream
    @Url("/home") // Devient /front/home
    public String home() {
        return "Bienvenue sur la page d'accueil 🏠 - Controller: " + this.getClass().getSimpleName();
    }

    @Url("/about") // Devient /front/about
    public String about() {
        return "À propos du framework 🚀 - Controller: " + this.getClass().getSimpleName();
=======
@Controller
public class TestController {

    @Url("/test")
    public String hello() {
        return "Bonjour depuis TestController.hello()";
>>>>>>> Stashed changes
    }

    @Url("/time")
    public String time() {
        return "Heure actuelle : " + java.time.LocalTime.now();
    }
}
