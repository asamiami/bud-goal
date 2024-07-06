package com.project.budgoal.event;

import com.project.budgoal.entites.Users;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private Users users;
    private String  otp;

    public RegistrationCompleteEvent(Users users, String otp) {
        super(users);
        this.users = users;
        this.otp = otp;
    }
}
