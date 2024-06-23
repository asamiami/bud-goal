package com.project.budgoal.dtos.response;

import lombok.Builder;

@Builder
public record AuthResponse
    (String token,

    String firstName,
    String lastName,
    String email){

}
