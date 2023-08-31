package com.ravi.BlogApplication.controller;
import com.ravi.BlogApplication.entity.User;
import com.ravi.BlogApplication.service.PostService;
import com.ravi.BlogApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage() {
        return "login";
    }

    @GetMapping("/showRegisterForm")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "registerForm";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, @RequestParam("ConfirmPassword") String confirmPassword,Model model) {
        if(confirmPassword.equals(user.getPassword())==false) {
            model.addAttribute("error","both password are not same");
            model.addAttribute("user", new User());

            return "registerForm";
        }
        else {
            String password=user.getPassword();
            user.setPassword("{noop}"+password);

            userService.saveUser(user);
        }
        return "redirect:/showMyLoginPage";
    }
}
