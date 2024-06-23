package com.project.budgoal.services;

import com.project.budgoal.dtos.request.BudgetRequest;
import com.project.budgoal.dtos.request.TransactionRequest;
import com.project.budgoal.entites.Budget;
import com.project.budgoal.entites.Users;
import com.project.budgoal.enums.TransactionCatgory;
import com.project.budgoal.repository.BudgetRepo;
import com.project.budgoal.repository.UserRepository;
import com.project.budgoal.dtos.response.ApiResponse;
import com.project.budgoal.dtos.response.BudgetResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepo budgetRepo;

    private final UserRepository userRepo;


    public ApiResponse<BudgetResponse> createBudget(BudgetRequest budgetRequest, Long userId){

        var user = userRepo.findUsersById(userId);

            List<Users> usersList = new ArrayList<>();

            Budget newBudget = new Budget();

            newBudget.setBudgetAmount(budgetRequest.amount());
            newBudget.setBudgetName(budgetRequest.name());
            newBudget.setBudgetCategory(budgetRequest.budgetCategory());
            usersList.add(user);
            newBudget.setUsersList(usersList);
            newBudget.setBudgetMembers(usersList.size());
            newBudget.setCreatedDate(LocalDate.now());
            budgetRepo.save(newBudget);

           BudgetResponse budgetResponse= new  BudgetResponse(newBudget.getBudgetName(), newBudget.getBudgetAmount(), newBudget.getTransactions(), newBudget.getBudgetMembers());

            return   new ApiResponse<>(newBudget.getBudgetName() + " Budget has been created", HttpStatus.CREATED, budgetResponse);


    }

    public ApiResponse<List<BudgetResponse>> addBudgetMembers (Long userId, Long newUserId, Long budgetId){

        var user = userRepo.findById(userId);
        List<BudgetResponse> listOfMembers = new ArrayList<>();
        if(user.isPresent() && userRepo.existsById(newUserId)) {
            var newUSer = userRepo.findUsersById(newUserId);
            var budget = budgetRepo.findBudgetById(budgetId);

            if (!budget.getUsersList().contains(newUSer)) {
               budget.addUsersToUserList(newUSer);
                budgetRepo.save(budget);
                BudgetResponse budgetResponse = new BudgetResponse(budget.getBudgetName(), budget.getBudgetAmount(), budget.getTransactions(), budget.getBudgetMembers());
                listOfMembers.add(budgetResponse);
                return new ApiResponse<>("User added successfully", HttpStatus.OK, listOfMembers);

            } else {
                return new ApiResponse<>("This User already exists in the Budget", HttpStatus.BAD_REQUEST);

            }
        }else {
            return new ApiResponse<>("Kindly ensure the user is a registered user", HttpStatus.BAD_REQUEST);

        }

    }

    public ApiResponse<List<BudgetResponse>> allUsersBudget(Long userId){

        var user = userRepo.findUsersById(userId);

        if (user != null) {
            var budgets = budgetRepo.findAllByUsersList(user);
            List<BudgetResponse> listOfBudgets = new ArrayList<>();
            for (Budget budget : budgets) {
                BudgetResponse budgetResponse = new BudgetResponse(budget.getBudgetName(), budget.getBudgetAmount(), budget.getTransactions(), budget.getBudgetMembers());
                listOfBudgets.add(budgetResponse);
            }
            if (!listOfBudgets.isEmpty()) {
                return  new ApiResponse<>("The list of budgets", HttpStatus.OK, listOfBudgets);

            }
        }

        return new ApiResponse<>("This user has no budget", HttpStatus.BAD_REQUEST);

    }





    public ApiResponse<BudgetResponse> editBudget( Long budgetId,BudgetRequest budgetRequest){

        var budget = budgetRepo.findBudgetById(budgetId);

        budget.setBudgetAmount(budgetRequest.amount());
        budget.setBudgetName(budgetRequest.name());
        budget.setUpdatedDate(LocalDateTime.now());
        budgetRepo.save(budget);

        return new ApiResponse<>("Budget edited Successfully", HttpStatus.OK);



    }

    public ApiResponse<BudgetResponse> addTransaction(Long budgetId, TransactionRequest request) {
        var budget1 = budgetRepo.findById(budgetId);

        if (budget1.isPresent()) {
            Budget budget = budgetRepo.findBudgetById(budgetId);

            if (request.category() == TransactionCatgory.INCOME) {
                budget.setBudgetAmount(budget.getBudgetAmount() + request.amount());


                budget.addTransactionToTransactionsList(request.category().toString(), request.amount());

                budgetRepo.save(budget);
                BudgetResponse budgetResponse = new  BudgetResponse(budget.getBudgetName(), budget.getBudgetAmount(), budget.getTransactions(), budget.getBudgetMembers());

                return new ApiResponse<>("An Income has been added", HttpStatus.OK, budgetResponse);


            } else if (request.category() == TransactionCatgory.EXPENSES && request.amount() < budget.getBudgetAmount()) {
                budget.setBudgetAmount(budget.getBudgetAmount() - request.amount());

                budget.addTransactionToTransactionsList(request.category().toString(), request.amount());
                budgetRepo.save(budget);
                BudgetResponse budgetResponse = new BudgetResponse(budget.getBudgetName(), budget.getBudgetAmount(), budget.getTransactions(), budget.getBudgetMembers());
                return new ApiResponse<>("An expense has been added", HttpStatus.OK, budgetResponse);

            } else if (request.amount() > budget.getBudgetAmount()) {
                 return new ApiResponse<>("This is beyond your budget. Please let us try and stick to the money available", HttpStatus.BAD_REQUEST);

            }else {
                 return new ApiResponse<>("This category is not allowed", HttpStatus.BAD_REQUEST);

            }


        }else {
            return  new ApiResponse<>("This budget does not exist", HttpStatus.BAD_REQUEST);


        }

    }


    public ApiResponse<Map<String, Long>> viewTransactions (Long budgetId){
        var budget =  budgetRepo.findById(budgetId);

        if (budget.isPresent()){
            Budget budgets = budgetRepo.findBudgetById(budgetId);

            Map<String, Long> transactions = budgets.getTransactions();

             BudgetResponse budgetResponse = new  BudgetResponse("The following are the transactions in " + budgets.getBudgetName(),budgets.getBudgetAmount(), transactions, budgets.getBudgetMembers());
             return new ApiResponse<>(budgetResponse.name(),HttpStatus.OK, transactions);

        } else {
             return new ApiResponse<>("This budget does not exist", HttpStatus.BAD_REQUEST);

        }
    }
}

