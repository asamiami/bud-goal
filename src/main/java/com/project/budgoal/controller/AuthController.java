package com.project.budgoal.controller;

import com.project.budgoal.services.AuthServ;
import com.project.budgoal.services.implementation.AuthService;
import com.project.budgoal.dtos.request.LoginRequest;
import com.project.budgoal.dtos.request.RegisterDto;
import com.project.budgoal.dtos.response.ApiResponse;
import com.project.budgoal.dtos.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/budgoal/auth")
@RequiredArgsConstructor
public class AuthController {


        private final AuthServ authService;

        @PostMapping("/register")
        public ResponseEntity<ApiResponse<String>> signup (@RequestBody RegisterDto signupRequest)  {
            var response =  authService.registerUser(signupRequest);
            return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
        }

        @PostMapping ("/login")

        public ResponseEntity<ApiResponse<AuthResponse>> login (@RequestBody LoginRequest loginRequest){
            var response = authService.loginUser(loginRequest);
            return  new ResponseEntity<>(response, response.getCode());
        }

    }

