package com.project.budgoal.auth;

import com.project.budgoal.dtos.LoginRequest;
import com.project.budgoal.dtos.RegisterDto;
import com.project.budgoal.entites.Users;
import com.project.budgoal.enums.AccountStatus;
import com.project.budgoal.enums.Roles;
import com.project.budgoal.exceptions.BudgoalException;
import com.project.budgoal.repository.UserRepository;
import com.project.budgoal.response.ApiResponse;
import com.project.budgoal.response.AuthResponse;
import com.project.budgoal.security.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public ResponseEntity<ApiResponse<String>> registerUser(RegisterDto registerDto) {
        var user = userRepository.findByEmail(registerDto.email());

        if (!user.isPresent()) {
            Users newUser = Users.builder()
                    .nickName(registerDto.nickname())
                    .firstName(registerDto.firstName())
                    .lastName(registerDto.lastName())
                    .email(registerDto.email())
                    .password(passwordEncoder.encode(registerDto.password()))
                    .userRoles(Roles.MEMBER)
                    .build();
            userRepository.save(newUser);



            ApiResponse<String> response = new ApiResponse<>(
                    "Check your email for OTP verification",
                    "Successfully created account", HttpStatus.valueOf(HttpStatus.OK.value()));
            return new ResponseEntity<>(response, response.getCode());
        } else {
            ApiResponse apiResponse = new ApiResponse<>("User already exists", HttpStatus.valueOf(400));
            return new ResponseEntity<>(apiResponse, apiResponse.getCode());
        }
    }


    public ResponseEntity<ApiResponse<AuthResponse>> loginUser(LoginRequest login){

        var user = userRepository.findByEmail(login.email());

        if(user.isPresent()){
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.email(), login.password()));
            String token = jwtService.generateToken(user.get());

            SecurityContextHolder.getContext().setAuthentication(auth);

            AuthResponse authResponse = new AuthResponse(
                    token,
                    user.get().getFirstName(),
                    user.get().getLastName(),
                    user.get().getEmail()
            );

            ApiResponse response = new ApiResponse<>("User logged in successfully",HttpStatus.valueOf(200), authResponse);


            return new ResponseEntity<>(response, response.getCode());

        }else {

            ApiResponse apiResponse = new ApiResponse<>("Email is not registered kindly Sign Up", HttpStatus.valueOf(401));

            return new ResponseEntity<>(apiResponse, HttpStatusCode.valueOf(apiResponse.getCode().value()));
        }

    }




}
