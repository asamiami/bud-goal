package com.project.budgoal.services;

import com.project.budgoal.dtos.BudgetRequest;
import com.project.budgoal.dtos.TransactionRequest;
import com.project.budgoal.entites.Budget;
import com.project.budgoal.entites.Transaction;
import com.project.budgoal.entites.Users;
import com.project.budgoal.enums.TransactionCatgory;
import com.project.budgoal.repository.BudgetRepo;
import com.project.budgoal.repository.TransactionsRepo;
import com.project.budgoal.repository.UserRepository;
import com.project.budgoal.response.ApiResponse;
import com.project.budgoal.response.BudgetResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepo budgetRepo;

    private final UserRepository userRepo;

    private final TransactionsRepo transactionsRepo;

    public ResponseEntity createBudget(BudgetRequest budgetRequest, Long userId){

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

            BudgetResponse budgetResponse = new BudgetResponse(newBudget.getBudgetName(), newBudget.getBudgetAmount(), newBudget.getTransaction(), newBudget.getBudgetMembers());

        ApiResponse response =  new ApiResponse<>(newBudget.getBudgetName() + " Budget has been created", HttpStatus.CREATED, budgetResponse);

        return new ResponseEntity<>(response, response.getCode());
    }

    public ResponseEntity<ApiResponse<BudgetResponse>> addBudgetMembers (Long userId, Long newUserId, Long budgetId){
        Users existingUser = userRepo.findUsersById(newUserId);
        var budget = budgetRepo.findBudgetById(budgetId);
        var user = userRepo.findById(userId);
        var newMember = userRepo.findById(newUserId);

        if(user.isPresent() && !newMember.isPresent() && budgetRepo.existsById(budgetId)){
            List<Users> usersList = new ArrayList<>();
            usersList.add(existingUser);
            budget.setBudgetMembers(usersList.size());
            budget.setUsersList(usersList);
            budgetRepo.save(budget);

            BudgetResponse budgetResponse = new BudgetResponse(budget.getBudgetName(),budget.getBudgetAmount(), budget.getTransaction(), budget.getBudgetMembers());

            ApiResponse response = new ApiResponse<>("User added succesfully", HttpStatus.OK, budgetResponse);

            return new ResponseEntity<>(response, response.getCode());
        }else{
            ApiResponse response = new ApiResponse<>("Kindly ensure the user is a registered user", HttpStatus.BAD_REQUEST);
            return  new ResponseEntity<>(response, response.getCode());
        }

    }


    public ResponseEntity<ApiResponse<List<BudgetResponse>>> allUsersBudget(Long userId){

        var user = userRepo.findUsersById(userId);

        if (user != null) {
            var budgets = budgetRepo.findAllByUsersList(user);
            List<BudgetResponse> listOfBudgets = new ArrayList<>();
            for (Budget budget : budgets) {
                BudgetResponse budgetResponse = new BudgetResponse(budget.getBudgetName(), budget.getBudgetAmount(), budget.getTransaction(), budget.getBudgetMembers());
                listOfBudgets.add(budgetResponse);
            }
            if (!listOfBudgets.isEmpty()) {
                ApiResponse<List<BudgetResponse>> response = new ApiResponse<>("The list of budgets", HttpStatus.OK, listOfBudgets);
                return new ResponseEntity<>(response, response.getCode());
            }
        }

        ApiResponse apiResponse = new ApiResponse<>("This user has no budget", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiResponse, apiResponse.getCode());
    }







    public ResponseEntity<ApiResponse<BudgetResponse>> editBudget( Long budgetId,BudgetRequest budgetRequest){

        var budget = budgetRepo.findBudgetById(budgetId);

        budget.setBudgetAmount(budgetRequest.amount());
        budget.setBudgetName(budgetRequest.name());
        budget.setUpdatedDate(LocalDateTime.now());
        budgetRepo.save(budget);

        ApiResponse response = new ApiResponse<>("Budget edited Successfully", HttpStatus.OK);

        return new ResponseEntity<>(response, response.getCode());

    }

    public ResponseEntity<ApiResponse<BudgetResponse>> addTransaction(Long budgetId, TransactionRequest request) {
        var budget = budgetRepo.findBudgetById(budgetId);
        if (budget == null) {
            ApiResponse apiResponse = new ApiResponse<>("Budget not found", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(apiResponse, apiResponse.getCode());
        }

        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();

        if (request.category() == TransactionCatgory.INCOME) {
            budget.setBudgetAmount(budget.getBudgetAmount() + request.amount());
            transaction.setAmount(request.amount());
            transaction.setUserBudget(budget);
            transaction.setCategory(request.category());
            transactions.add(transaction);
            budget.setTransaction(transactions);
            transactionsRepo.save(transaction);
            budgetRepo.save(budget);

            BudgetResponse budgetResponse = new BudgetResponse(budget.getBudgetName(), budget.getBudgetAmount(), budget.getTransaction(), budget.getBudgetMembers());
            ApiResponse response = new ApiResponse<>("An income has been added", HttpStatus.OK, budgetResponse);
            return new ResponseEntity<>(response, response.getCode());
        }

        if (request.category() == TransactionCatgory.EXPENSES) {
            if (request.amount() > budget.getBudgetAmount()) {
                ApiResponse response = new ApiResponse<>("This expense is beyond your budget", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, response.getCode());
            }

            budget.setBudgetAmount(budget.getBudgetAmount() - request.amount());
            transaction.setAmount(request.amount());
            transaction.setUserBudget(budget);
            transaction.setCategory(request.category());
            transactions.add(transaction);
            budget.setTransaction(transactions);
            budget.setTransaction(transactions);
            budgetRepo.save(budget);

            BudgetResponse budgetResponse = new BudgetResponse(budget.getBudgetName(), budget.getBudgetAmount(), budget.getTransaction(), budget.getBudgetMembers());
            ApiResponse response = new ApiResponse<>("An expense has been added", HttpStatus.OK, budgetResponse);
            return new ResponseEntity<>(response, response.getCode());
        }

        ApiResponse response = new ApiResponse<>("Invalid transaction category", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getCode());
    }


}
