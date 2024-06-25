package com.project.budgoal.services;

import com.project.budgoal.dtos.request.LoginRequest;
import com.project.budgoal.dtos.request.RegisterDto;
import com.project.budgoal.dtos.response.ApiResponse;
import com.project.budgoal.dtos.response.AuthResponse;

public interface AuthServ {

    public ApiResponse<String> registerUser(RegisterDto registerDto);

    public ApiResponse<AuthResponse> loginUser(LoginRequest login);
}
