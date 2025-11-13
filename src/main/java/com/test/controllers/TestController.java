package com.test.controllers;

import com.framework.annotation.*;


@Controller
public class TestController {

    @Url("/test")
    public String hello() {
        return "Bonjour depuis TestController.hello()";
    }

    @Url("/time")
    public String time() {
        return "Heure actuelle : " + java.time.LocalTime.now();
    }
}
