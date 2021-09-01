package com.spring.msreport.service;


import com.spring.msreport.entity.BankAccount;
import com.spring.msreport.entity.Card;
import com.spring.msreport.entity.CreditTransaction;
import com.spring.msreport.entity.Customer;
import com.spring.msreport.entity.DebitCardTransaction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReportService {

	Mono<Customer> findCustomer(String idcustomer);
	
	Flux<Card> findCards(String idcustomer);
	
	Flux<BankAccount> findAccounts(String idcustomer);
	
	Flux<CreditTransaction> findByCreditCreditCardCustomerId(String idcustomer);
	
	Flux<DebitCardTransaction> findByDebitCardCustomerId(String idcustomer);
}
