package com.taskmaster.service;

import com.taskmaster.dto.RegisterRequest;
import com.taskmaster.dto.UserResponse;
import com.taskmaster.entity.User;
import com.taskmaster.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.taskmaster.dto.LoginRequest;
import com.taskmaster.security.JwtService;
import com.taskmaster.dto.ProfileResponse;
import com.taskmaster.dto.UpdateProfileRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {

    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
}
    
    public UserResponse register(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create User Entity
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Save to database
        User savedUser = userRepository.save(user);

        // Create Response DTO
        UserResponse response = new UserResponse();

        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());

        return response;
    }

   public String login(LoginRequest request) 
   {

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid email or password"));

    boolean isPasswordCorrect =
            passwordEncoder.matches(request.getPassword(), user.getPassword());

    if (!isPasswordCorrect) 
    {
        throw new RuntimeException("Invalid email or password");
    }

    return jwtService.generateToken(user.getEmail());
    }

    public ProfileResponse getProfile()
     {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    User user = (User) authentication.getPrincipal();

    ProfileResponse response = new ProfileResponse();

    response.setId(user.getId());
    response.setName(user.getName());
    response.setEmail(user.getEmail());

    return response;
   }

   public ProfileResponse updateProfile(UpdateProfileRequest request) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    User user = (User) authentication.getPrincipal();

    user.setName(request.getName());
    user.setEmail(request.getEmail());

    user.setPassword(
            passwordEncoder.encode(request.getPassword())
    );

    User updatedUser = userRepository.save(user);

    ProfileResponse response = new ProfileResponse();

    response.setId(updatedUser.getId());
    response.setName(updatedUser.getName());
    response.setEmail(updatedUser.getEmail());

    return response;
    }

}