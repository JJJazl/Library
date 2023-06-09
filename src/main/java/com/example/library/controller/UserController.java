package com.example.library.controller;

import com.example.library.dto.user.UserCreateDto;
import com.example.library.dto.user.UserLoginDto;
import com.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model, UserLoginDto userDto) {
        model.addAttribute("userLoginDto", userDto);
        return "user/login";
    }

    @GetMapping("/register")
    public String newUser(UserCreateDto userDto, Model model) {
        model.addAttribute("userCreateDto", userDto);
        return "user/registration";
    }


    @PostMapping("/post/register")
    public String addUser(@ModelAttribute("userDto") UserCreateDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return "user/registration";
        }
        userService.addUser(userDto);
        return "redirect:/book/list";
    }
}

