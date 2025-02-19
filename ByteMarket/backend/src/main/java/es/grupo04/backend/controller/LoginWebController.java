package es.grupo04.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginWebController {
   public LoginWebController() {
   }
   @GetMapping("/login")
   public String login(){
      return "login_template";
   }

   @GetMapping("/loginerror")
   public String loginerror() {
      return "loginerror";
   }

   @GetMapping("/signin")
   public String signin(){
      return "signin_template";
   }
}
