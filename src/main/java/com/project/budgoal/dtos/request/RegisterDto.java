package com.project.budgoal.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.budgoal.enums.Roles;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;



@Builder
public record RegisterDto(


        String firstName,

        String lastName,

        String email,

        String nickname,

        String password


) {
}
