package com.project.budgoal.event.listener;

import com.project.budgoal.entites.Users;
import com.project.budgoal.event.RegistrationCompleteEvent;
import com.project.budgoal.services.implementation.AuthService;
import com.project.budgoal.services.implementation.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class RegistationCompleteListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final EmailService emailService;
    private  Users theUser;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        theUser = event.getUsers();
        String token = event.getOtp();

        try {
            sendVerificationEmail(theUser.getEmail(), token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public void sendVerificationEmail(String email, String otp){
       String subject = "Email Verification";
       String body = "<p> Hi, " + theUser.getNickName() + " , </p>" +
               "<p> Thank you for registering with us at Budgoal. </p>" +
               "<p> Please use this otp: <b> "+otp +"</b> </p>"+
               "<p> Thank you. Can't wait for u to achieve all your Budget and Saving Goals</p>";
       emailService.sendMessage(email, subject, body);
    }
}
