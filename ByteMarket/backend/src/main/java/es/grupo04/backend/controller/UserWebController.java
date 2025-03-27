package es.grupo04.backend.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.grupo04.backend.dto.NewUserDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserWebController {
   @Autowired
   private UserService userService;

   public UserWebController() {
   }

   @ModelAttribute
   public void addAttributes(Model model, HttpServletRequest request) {

      Principal principal = request.getUserPrincipal();

      if (principal != null) {

         UserBasicDTO user = userService.findByMail(principal.getName()).get();

         model.addAttribute("logged", true);
         model.addAttribute("userName", user.name());
         model.addAttribute("admin", request.isUserInRole("ADMIN"));
         model.addAttribute("user", user);

      } else {
         model.addAttribute("logged", false);
      }
   }

   @GetMapping("/login")
   public String login() {
      return "login_template";
   }

   @GetMapping("/loginerror")
   public String loginerror(Model model) {
      model.addAttribute("message", "El usuario o la contraseña son incorrectos");
      return "error";
   }

   @GetMapping("/signin")
   public String signin(Model model) {
      model.addAttribute("user", new NewUserDTO(null, null, null, null));
      return "signin_template";
   }

   @PostMapping("/signin")
   public String createUser(Model model, @ModelAttribute NewUserDTO user, String confirmPassword) {
      if (userService.validateUser(user)) {

         if (userService.createAccount(user).isEmpty()) { // If the user already has an account
            model.addAttribute("message", "El usuario ya tiene una cuenta");
            return "error";
         } else {
            return "login_template";
         }

      } else {
         model.addAttribute("message", "Error en la validación de los datos");
         return "error";
      }
   }

   @PostMapping("/deleteAccount/{userId}")
   public String deleteAccount(@PathVariable Long userId, HttpServletRequest request, Model model) {
      Optional<UserBasicDTO> userOptional = userService.findById(userId);
      if (!userOptional.isPresent()) {
         model.addAttribute("message", "Usuario no encontrado");
         return "error";
      }
      UserBasicDTO userToDelete = userOptional.get();
      if (request.isUserInRole("ADMIN")) {
         if (userToDelete != null && !userToDelete.name().equals(request.getUserPrincipal().getName())) {
            userService.delete(userToDelete);
            return "redirect:/";
         }else{
            model.addAttribute("message", "Este usuario no puede ser borrado");
            return "error";  
         }
      }else{
         model.addAttribute("message", "No tienes permisos para realizar esta operación");
         return "error";
      }
   }

}