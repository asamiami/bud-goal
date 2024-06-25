package com.project.budgoal.controller;

import com.project.budgoal.dtos.request.SavingsRequest;
import com.project.budgoal.dtos.request.TransactionRequest;
import com.project.budgoal.dtos.response.ApiResponse;
import com.project.budgoal.dtos.response.SavingsResponse;
import com.project.budgoal.dtos.response.UserResponse;
import com.project.budgoal.services.SavingsServ;
import com.project.budgoal.services.implementation.SavingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/budgoal/savings")
@RequiredArgsConstructor
public class SavingsController {

    private final SavingsServ savingsService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<SavingsResponse>> createSavings (@RequestBody SavingsRequest savingsRequest, @RequestParam Long user){
        var response = savingsService.createSavings(savingsRequest,user);
        return new ResponseEntity<>(response, response.getCode());
    }


    @PutMapping("/add-user/{user}")
    public ResponseEntity<ApiResponse<List<SavingsResponse>>> addMember (@RequestParam Long newUser, @RequestParam Long savingsId, @PathVariable Long user){
        var response = savingsService.addMembers(user,newUser,savingsId);
        return new ResponseEntity<>(response, response.getCode());
    }

    @GetMapping("/all-savings")
    public ResponseEntity<ApiResponse<List<SavingsResponse>>> allSavings ( @RequestParam Long userId){
       var response = savingsService.viewAllSavings(userId);
       return new ResponseEntity<>(response,response.getCode());
    }

    @GetMapping("/all-members")
    public ResponseEntity<ApiResponse<List<UserResponse>>> allMembers (@RequestParam Long savingsId){
        var response = savingsService.viewMembers(savingsId);
        return new ResponseEntity<>(response,response.getCode());
    }

    @PostMapping("/add-transaction")
    public ResponseEntity<ApiResponse<SavingsResponse>> addTransaction(@RequestParam Long savingsId, @RequestBody TransactionRequest transactionRequest, @RequestParam Long userId){
        var response = savingsService.addTransaction(savingsId, transactionRequest, userId);
        return new ResponseEntity<>(response, response.getCode());
    }

    @GetMapping("/view-transactions")
    public ResponseEntity<ApiResponse<Map<String, Long>>> viewTransactions (@RequestParam Long savingsId){
        var response = savingsService.viewTransaction(savingsId);
        return new ResponseEntity<>(response, response.getCode());
    }

    }

