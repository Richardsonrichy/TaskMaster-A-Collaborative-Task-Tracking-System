package com.taskmaster.controller;

import com.taskmaster.dto.RegisterRequest;
import com.taskmaster.dto.UserResponse;
import com.taskmaster.service.UserService;
import jakarta.validation.Valid;
import com.taskmaster.dto.ProfileResponse;
import org.springframework.web.bind.annotation.*;
import com.taskmaster.dto.LoginRequest;
import com.taskmaster.dto.UpdateProfileRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {

        return userService.register(request);

    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request)
     {
    return userService.login(request);
     }

     @GetMapping("/profile")
     public ProfileResponse getProfile() 
     {

    return userService.getProfile();
      }

    @PutMapping("/profile")
    public ProfileResponse updateProfile(@Valid @RequestBody UpdateProfileRequest request)
     {

    return userService.updateProfile(request);
     }

     @PostMapping("/logout")
     public ResponseEntity<?> logout() {

    SecurityContextHolder.clearContext();

    return ResponseEntity.ok(
            Map.of("message", "Logout successful. Please discard the JWT token on the client side.")
    );
}
}