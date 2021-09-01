package com.spring.msreport.entity;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportConsolidated {
	Customer customer;
	
	List<Card> cards;
	
	List<BankAccount> bankAccounts;
}
