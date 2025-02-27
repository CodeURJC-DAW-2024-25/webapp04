package es.grupo04.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.grupo04.backend.model.User;
import es.grupo04.backend.service.UserService;

@Controller
public class UserWebController {
   @Autowired
   private UserService userService;

   public UserWebController() {
   }

   @GetMapping("/login")
   public String login() {
      System.out.println("Página de login");
      return "login_template";
   }

   @GetMapping("/loginerror")
   public String loginerror() {
      return "loginerror";
   }

   @GetMapping("/signin")
   public String signin(Model model) {
      model.addAttribute("user", new User());
      return "signin_template";
   }

   @PostMapping("/signin")
   public String createUser(Model model, @ModelAttribute User user, String confirmPassword) {
      if(userService.validateUser(user, confirmPassword)){
         if(userService.createAccount(user)){   //If the user already has an account
            model.addAttribute("message", "El usuario ya tiene una cuenta");
            return "error";
         }else {          
            return "login_template";
         }
      }else{
         model.addAttribute("message", "Error en la validación de los datos");
         return "error";
      }
   }

}