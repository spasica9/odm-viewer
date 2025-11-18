package com.example.odm_viewer_backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

    @RequestMapping(value = "/")
    public String forwardRoot() {
        return "forward:/index.html";
    }
}
