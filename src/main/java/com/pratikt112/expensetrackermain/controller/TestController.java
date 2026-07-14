package com.pratikt112.expensetrackermain.controller;


import com.pratikt112.expensetrackermain.DTO.ExpenseDTO;
import com.pratikt112.expensetrackermain.DTO.ProratedExpenseDTO;
import com.pratikt112.expensetrackermain.model.User;
import com.pratikt112.expensetrackermain.service.ProratedExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final ProratedExpenseService proratedExpenseService;

    public TestController(ProratedExpenseService proratedExpenseService) {
        this.proratedExpenseService = proratedExpenseService;
    }

    @PostMapping("/calculateMonthlyComponents")
    public ResponseEntity<List<ExpenseDTO>> calculateMonthlyExpenseComponents(@RequestBody ProratedExpenseDTO dto){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(proratedExpenseService.calculateMonthlyExpenseComponents(dto));
    }
}
