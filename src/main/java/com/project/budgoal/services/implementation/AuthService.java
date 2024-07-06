package com.project.budgoal.services.implementation;

import com.project.budgoal.dtos.request.LoginRequest;
import com.project.budgoal.dtos.request.RegisterDto;
import com.project.budgoal.entites.Users;
import com.project.budgoal.enums.AccountStatus;
import com.project.budgoal.enums.Roles;
import com.project.budgoal.event.RegistrationCompleteEvent;
import com.project.budgoal.exceptions.BudgoalException;
import com.project.budgoal.repository.UserRepository;
import com.project.budgoal.dtos.response.ApiResponse;
import com.project.budgoal.dtos.response.AuthResponse;
import com.project.budgoal.security.JwtService;

import com.project.budgoal.services.AuthServ;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthServ {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher publisher;




    public ApiResponse<String> registerUser(RegisterDto registerDto) {
        var user = userRepository.findByEmail(registerDto.email());

        if (!user.isPresent()) {
            Users newUser = Users.builder()
                    .nickName(registerDto.nickname())
                    .firstName(registerDto.firstName())
                    .lastName(registerDto.lastName())
                    .email(registerDto.email())
                    .password(passwordEncoder.encode(registerDto.password()))
                    .userRoles(Roles.MEMBER)
                    .accountStatus(AccountStatus.SUSPENDED)
                    .build();
            String otp = generateOTP();
            newUser.setOtp(otp);
            userRepository.save(newUser);
            publisher.publishEvent(new RegistrationCompleteEvent(newUser, otp));



            return new ApiResponse<>(
                    "Check your email for OTP verification",
                    "Successfully created account", HttpStatus.valueOf(HttpStatus.OK.value()));

        } else {
            return new ApiResponse<>("User already exists", HttpStatus.valueOf(400));

        }
    }


    public ApiResponse<AuthResponse> loginUser(LoginRequest login){

        Optional<Users> user = userRepository.findByEmail(login.email());
        if(user.isPresent() && user.get().getAccountStatus().equals(AccountStatus.ACTIVE)){
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.email(), login.password()));
            String token = jwtService.generateToken(user.get());

            SecurityContextHolder.getContext().setAuthentication(auth);

            AuthResponse authResponse = new AuthResponse(
                    token,
                    user.get().getFirstName(),
                    user.get().getLastName(),
                    user.get().getEmail()
            );

            return new ApiResponse<>("User logged in successfully",HttpStatus.valueOf(200), authResponse);




        }else {

            return new ApiResponse<>("Email is not registered kindly Sign Up", HttpStatus.valueOf(401));


        }

    }
    public ApiResponse<String> verifyEmail(String email, String otp){
        Optional<Users> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            throw new BudgoalException("User not found");
        } else if (user.get().getAccountStatus().equals(AccountStatus.ACTIVE)) {
            throw new BudgoalException("User is already verified");
        } else if (otp.equals(user.get().getOtp())) {
            user.get().setAccountStatus(AccountStatus.ACTIVE);
            userRepository.save(user.get());
            return new ApiResponse<>(user.get().getNickName() + " Your email is successfully verified", HttpStatus.valueOf(200));

        }else return new ApiResponse<>("Internal Server Error", HttpStatus.valueOf(500));
    }

    private String generateOTP(){
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);
        return otp;
    }




    }







