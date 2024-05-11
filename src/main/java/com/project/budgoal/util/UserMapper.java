package com.project.budgoal.util;

import com.project.budgoal.dtos.BudgetTransactionRequest;
import com.project.budgoal.dtos.RegisterDto;
import com.project.budgoal.entites.BudgetTransaction;
import com.project.budgoal.entites.Users;

public class UserMapper {
    public static RegisterDto userToDto(Users users){

        return  RegisterDto.builder()
                .email(users.getEmail())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .nickname(users.getNickName())
                .password(users.getPassword())
                .build();

    }

    public static Users registerDtoToUser(RegisterDto registerDto){

        return Users.builder()
                .email(registerDto.email())
                .firstName(registerDto.firstName())
                .lastName(registerDto.lastName())
                .password(registerDto.password())
                .nickName(registerDto.email())

                .build();
    }


    public static BudgetTransaction requestToTransaction (BudgetTransactionRequest request){
        return BudgetTransaction.builder()
                .category(request.category())
                .amount(request.amount())
                .build();
    }
}
