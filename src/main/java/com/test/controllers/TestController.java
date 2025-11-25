package com.test.controllers;

import com.framework.annotation.*;
import com.framework.model.ModelView;

@Controller
public class TestController {

    @Url("/page")
    public ModelView page() {
        ModelView mv = new ModelView("test.jsp");
        mv.addAttribute("titre", "Bienvenue");
        mv.addAttribute("message", "Default message");
        return mv;
    }

    @Url("/pageWithParams")
    public ModelView pageWithParams(@RequestParam("titre") String titre, @RequestParam("message") String message) {
        ModelView mv = new ModelView("test.jsp");
        mv.addAttribute("titre", titre != null ? titre : "Bienvenue");
        mv.addAttribute("message", message != null ? message : "Default");
        return mv;
    }

    @Url("/calcul")
    public String calcul(@RequestParam("nombre1") int nombre1, @RequestParam("nombre2") int nombre2) {
        int resultat = nombre1 + nombre2;
        return "Résultat : " + nombre1 + " + " + nombre2 + " = " + resultat;
    }

    @Url("/zavatra/{valeur}")
    public String getZavatra(@RequestParam("valeur") String valeur) {
        return "Valeur reçue : " + valeur;
    }
}