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
import com.project.budgoal.services.EmailService;
import com.project.budgoal.util.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final EmailService emailService;

    public ResponseEntity<ApiResponse<String>> registerUser(RegisterDto registerDto) {
        var user = userRepository.findByEmail(registerDto.email());

        if (!user.isPresent()) {
            Users newUser = new Users();

            newUser.setEmail(registerDto.email());
            newUser.setLastName(registerDto.lastName());
            newUser.setFirstName(registerDto.firstName());
            newUser.setPassword(passwordEncoder.encode(registerDto.password()));
            newUser.setNickName(registerDto.nickname());


            userRepository.save(newUser);
            String otp = generateOTP();


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

//            SecurityContextHolder.getContext().setAuthentication(auth);

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


    private String generateOTP(){

        Random random = new Random();

        int OTPValue = 10000 + random.nextInt(90000);
        return String.valueOf(OTPValue);
    }

    private void sendVerificationEmail(Users users, String token){
        emailService.sendEmail(users);
    }

    @SneakyThrows
    public ApiResponse<String> confirmEmail(String token) {

        String email = jwtService.extractEmailAddressFromToken(token);

        if (email != null){

            if(jwtService.isExpired(token)){
                throw new BudgoalException("Your token is Expired");
            }else {
                var user = userRepository.findByEmail(email);
                if (user.isPresent() || !user.get().isEnabled()) {
                    var update = user.get();
                    update.setAccountStatus(AccountStatus.ACTIVE);
                    userRepository.save(update);
                    return new ApiResponse<>("Email is confirmed",HttpStatus.valueOf(201));

                }else{
                    throw new BudgoalException("Email doesn't exist");
                }
            }

        }return new ApiResponse<>("Email is empty", HttpStatus.valueOf(400));
    }


}
