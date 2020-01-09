package com.github.hairless.plink.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping(value = {"/", "/page/**"})
    public String index() {
        return "index.html";
    }

}
