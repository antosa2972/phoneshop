package com.es.phoneshop.web.controller.pages.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/login")
public class LoginPageController {

    @Autowired
    private Environment environment;

    @GetMapping
    public String getLoginPage(@RequestParam(required = false) String error, Authentication authentication,
                        Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/orders";
        }
        if (error != null) {
            model.addAttribute("error", environment.getProperty("invalidCredentials"));
        }
        return "login";
    }
}
