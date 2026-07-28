package com.test.controllers;

import com.framework.annotation.*;
import com.framework.model.ModelView;

@Controller
public class TestControllerJsp {

    // Sprint 5 : ancienne annotation @Url (compatibilité).
    // URL distincte de /page (TestController) : deux mappings identiques
    // rendaient la route dépendante de l'ordre de scan des classes.
    @Url("/page5")
    public ModelView page() {
        ModelView mv = new ModelView("test.jsp");
        mv.addAttribute("titre", "Bienvenue");
        mv.addAttribute("message", "Ceci est un message depuis le Sprint 5");
        return mv;
    }
}