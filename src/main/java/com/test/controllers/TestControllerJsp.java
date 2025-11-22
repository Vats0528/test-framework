package com.test.controllers;

import com.framework.annotation.*;
import com.framework.model.ModelView;

@Controller
public class TestControllerJsp {

    @Url("/page")
    public ModelView page() {
        ModelView mv = new ModelView("test.jsp");
        mv.addAttribute("titre", "Bienvenue");
        mv.addAttribute("message", "Ceci est un message depuis le Sprint 5");
        return mv;
    }
}