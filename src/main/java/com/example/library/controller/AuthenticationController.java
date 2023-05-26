package com.example.library.controller;

import com.example.library.dto.AuthenticationRequestDto;
import com.example.library.dto.user.UserLoginDto;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import com.example.library.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserRepository userRepository,
                                    JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/login")
    public String authenticateGet(Model model, UserLoginDto loginDto) {
        model.addAttribute("userLoginDto", loginDto);
        return "user/login";
    }

    @PostMapping("/post/login")
    public String authenticatePost(@ModelAttribute("loginDto") UserLoginDto loginDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                    loginDto.getPassword()));
            User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(
                    () -> new UsernameNotFoundException("Not found " + loginDto.getEmail()));
            String token = jwtTokenProvider.createToken(loginDto.getEmail(), user.getRole().name());
            Map<Object, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("email", user.getEmail());
            response.put("role", user.getRole().name());
            response.put("token", token);
            return "redirect:/book/list";
        } catch (AuthenticationException e) {
            return "user/register";
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}
