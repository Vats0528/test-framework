package com.test.controllers;

import com.framework.annotation.*;
import com.framework.model.ModelView;

@Controller
public class TestController {

    // GET - Affiche la page par défaut
    @GetMapping("/page")
    public ModelView page() {
        ModelView mv = new ModelView("test.jsp");
        mv.addAttribute("titre", "Bienvenue");
        mv.addAttribute("message", "Default message");
        return mv;
    }

    // GET - Récupère les paramètres de la requête
    @GetMapping("/pageWithParams")
    public ModelView pageWithParams(@RequestParam("titre") String titre, @RequestParam("message") String message) {
        ModelView mv = new ModelView("test.jsp");
        mv.addAttribute("titre", titre != null ? titre : "Bienvenue");
        mv.addAttribute("message", message != null ? message : "Default");
        return mv;
    }

    // POST - Additionner deux nombres
    @PostMapping("/calcul")
    public String postCalcul(@RequestParam("nombre1") int nombre1, @RequestParam("nombre2") int nombre2) {
        int resultat = nombre1 + nombre2;
        return "Résultat : " + nombre1 + " + " + nombre2 + " = " + resultat;
    }

    // GET - Récupère un paramètre depuis l'URL
    @GetMapping("/zavatra/{valeur}")
    public String getZavatra(@RequestParam("valeur") String valeur) {
        return "Valeur reçue : " + valeur;
    }

    // GET - Récupérer un utilisateur
    @GetMapping("/user/{id}")
    public String getUser(@RequestParam("id") int id) {
        return "Utilisateur récupéré : ID=" + id;
    }

    // POST - Créer une ressource (exemple)
    @PostMapping("/user/{id}")
    public String createUser(@RequestParam("id") int id, @RequestParam("nom") String nom) {
        return "Utilisateur créé : ID=" + id + ", Nom=" + nom;
    }

    // PUT - Mettre à jour une ressource
    @PutMapping("/user/{id}")
    public String updateUser(@RequestParam("id") int id, @RequestParam("nom") String nom) {
        return "Utilisateur modifié : ID=" + id + ", Nouveau nom=" + nom;
    }

    // DELETE - Supprimer une ressource
    @DeleteMapping("/user/{id}")
    public String deleteUser(@RequestParam("id") int id) {
        return "Utilisateur supprimé : ID=" + id;
    }

    // RequestMapping - utilisation générale avec spécification de la méthode
    @RequestMapping(value = "/custom", method = HttpMethod.GET)
    public String custom() {
        return "Endpoint personnalisé avec @RequestMapping";
    }
}