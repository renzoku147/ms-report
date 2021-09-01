package com.spring.msreport.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DebitCard implements Card{

    private String id;

    @NotEmpty
    private String cardNumber;
    
    @NotNull
    private Customer customer;
    
    @NotEmpty
    private List<Accounts> accounts;

    @NotNull
    private LocalDate expirationDate;

    private LocalDateTime date;
    
}
