package com.project.budgoal.auth;

import com.project.budgoal.dtos.LoginRequest;
import com.project.budgoal.dtos.RegisterDto;
import com.project.budgoal.exceptions.BudgoalException;
import com.project.budgoal.response.ApiResponse;
import com.project.budgoal.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budgoal/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @SneakyThrows
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> signup (@RequestBody RegisterDto signupRequest)  {

        return authService.registerUser(signupRequest);
    }

    @PostMapping ("/login")

    public ResponseEntity<ApiResponse<AuthResponse>> login (@RequestBody LoginRequest loginRequest){

        return authService.loginUser(loginRequest);
    }

    @PostMapping("/confirm-email-address")

    public ResponseEntity<ApiResponse<String>> confirmEmail(@RequestParam String token){

        var response =  authService.confirmEmail(token);

        return new ResponseEntity<>(response, response.getCode());
    }
}
