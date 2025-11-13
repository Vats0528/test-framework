package com.test.controllers;

import com.framework.annotation.*;
import com.framework.model.ModelView;

@Controller
public class TestControllerJsp {

    @Url("/page")
    public ModelView page() {
        return new ModelView("test.jsp");
    }
}
