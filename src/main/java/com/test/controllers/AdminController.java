package com.test.controllers;

import com.framework.annotation.Controller;
import com.framework.annotation.Url;

@Controller("/admin") // URL de base différente
public class AdminController {

    @Url("/dashboard")
    public String dashboard() {
        return "Tableau de bord administrateur ";
    }

    @Url("/users")
    public String users() {
        return "Gestion des utilisateurs ";
    }
}