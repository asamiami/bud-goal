package com.project.budgoal.controller;

import com.project.budgoal.event.RegistrationCompleteEvent;
import com.project.budgoal.services.AuthServ;
import com.project.budgoal.dtos.request.LoginRequest;
import com.project.budgoal.dtos.request.RegisterDto;
import com.project.budgoal.dtos.response.ApiResponse;
import com.project.budgoal.dtos.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budgoal/auth")
@RequiredArgsConstructor
public class AuthController {


        private final AuthServ authService;

        private final ApplicationEventPublisher publisher;
        @PostMapping("/register")
        public ApiResponse<String> signup (@RequestBody RegisterDto signupRequest)  {
            ApiResponse<String> response =  authService.registerUser(signupRequest);

            return new ApiResponse<>(response.getMessage(), response.getCode(), response.getData());
        }
        @PostMapping ("/login")

        public ApiResponse<AuthResponse> login (@RequestBody LoginRequest loginRequest){
            ApiResponse<AuthResponse> response = authService.loginUser(loginRequest);
            return new ApiResponse<>(response.getMessage(), response.getCode(), response.getData());
        }

        @PostMapping("/verify-email")
        public ApiResponse<String> verifyEmail (@RequestParam String email, @RequestParam String otp){
            ApiResponse<String> response = authService.verifyEmail(email, otp);
            return new ApiResponse<>(response.getMessage(), response.getCode());
        }
    }

