package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LogoutController {
    
     @PostMapping("/logout")
    public String logout(Model model,HttpSession session) {
     session.invalidate();
        return "redirect:/login";
    }
    
}
