package com.project.budgoal.controller;

import com.project.budgoal.services.AuthService;
import com.project.budgoal.dtos.request.LoginRequest;
import com.project.budgoal.dtos.request.RegisterDto;
import com.project.budgoal.dtos.response.ApiResponse;
import com.project.budgoal.dtos.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/budgoal/auth")
@RequiredArgsConstructor
public class AuthController {


        private final AuthService authService;

        @PostMapping("/register")
        public ResponseEntity<ApiResponse<String>> signup (@RequestBody RegisterDto signupRequest)  {

            return authService.registerUser(signupRequest);
        }

        @PostMapping ("/login")

        public ResponseEntity<ApiResponse<AuthResponse>> login (@RequestBody LoginRequest loginRequest){

            return authService.loginUser(loginRequest);
        }

    }

