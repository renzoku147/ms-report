package com.spring.msreport.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.reactive.function.client.WebClient;

import com.spring.msreport.entity.BankAccount;
import com.spring.msreport.entity.Card;
import com.spring.msreport.entity.CreditCard;
import com.spring.msreport.entity.CreditTransaction;
import com.spring.msreport.entity.CurrentAccount;
import com.spring.msreport.entity.Customer;
import com.spring.msreport.entity.DebitCard;
import com.spring.msreport.entity.DebitCardTransaction;
import com.spring.msreport.entity.FixedTerm;
import com.spring.msreport.entity.SavingAccount;
import com.spring.msreport.repository.ReportRepository;
import com.spring.msreport.service.ReportService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReportServiceImpl implements ReportService{

	@Autowired
	ReportRepository reportRepository;
	
	WebClient webClientCustomer = WebClient.create("http://localhost:8887/ms-customer/customer");
	
	WebClient webClientCreditCard = WebClient.create("http://localhost:8887/ms-creditcard/creditCard");
	
	WebClient webClientCreditCardTransaction = WebClient.create("http://localhost:8887/ms-credit-charge/creditCharge");
	
	WebClient webClientDebitCard = WebClient.create("http://localhost:8887/ms-debitcard/debitCard");
	
	WebClient webClientDebitCardTransaction = WebClient.create("http://localhost:8887/ms-debitcard-transaction/debitCardTransaction");

	WebClient webClientCurrent = WebClient.create("http://localhost:8887/ms-current-account/currentAccount");

    WebClient webClientFixed = WebClient.create("http://localhost:8887/ms-fixed-term/fixedTerm");

    WebClient webClientSaving = WebClient.create("http://localhost:8887/ms-saving-account/savingAccount");
	
	@Override
	public Mono<Customer> findCustomer(String id) {
		return webClientCustomer.get().uri("/find/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Customer.class);
	}

	@Override
	public Flux<Card> findCards(String idcustomer) {
		return Flux.merge(
				webClientCreditCard.get().uri("/findCreditCards/{id}", idcustomer)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(CreditCard.class)
                .map(cc -> (Card)cc),
					webClientDebitCard.get().uri("/findByCustomerId/{idcustomer}", idcustomer)
	                .accept(MediaType.APPLICATION_JSON)
	                .retrieve()
	                .bodyToFlux(DebitCard.class)
	                .map(dc -> (Card)dc));
	}

	@Override
	public Flux<BankAccount> findAccounts(String idcustomer) {
		return Flux.merge(webClientCurrent.get().uri("/findAccountByCustomerId/{idcustomer}", idcustomer)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(CurrentAccount.class)
                .map(cc -> (BankAccount)cc),
                
	                webClientFixed.get().uri("/findByCustomerId/{idcustomer}", idcustomer)
	                .accept(MediaType.APPLICATION_JSON)
	                .retrieve()
	                .bodyToFlux(FixedTerm.class)
	                .map(ft -> (BankAccount)ft),
	                
	                	webClientSaving.get().uri("/findByCustomerId/{idcustomer}", idcustomer)
		                .accept(MediaType.APPLICATION_JSON)
		                .retrieve()
		                .bodyToFlux(SavingAccount.class)
		                .map(sa -> (BankAccount)sa)
				);
	}

	@Override
	public Flux<CreditTransaction> findByCreditCreditCardCustomerId(String idcustomer) {
		
		return webClientCreditCardTransaction.get().uri("/findByCreditCardCustomerId/{idcustomer}", idcustomer)
	        .accept(MediaType.APPLICATION_JSON)
	        .retrieve()
	        .bodyToFlux(CreditTransaction.class)
	        .sort((a,b)->b.getTransactionDateTime().compareTo(a.getTransactionDateTime()))
	        .limitRate(10);
	}
	
	@Override
	public Flux<DebitCardTransaction> findByDebitCardCustomerId(String idcustomer) {
		
		return webClientDebitCardTransaction.get().uri("/findByDebitCardCustomerId/{idcustomer}", idcustomer)
	        .accept(MediaType.APPLICATION_JSON)
	        .retrieve()
	        .bodyToFlux(DebitCardTransaction.class)
	        .sort((a,b)->b.getTransactionDateTime().compareTo(a.getTransactionDateTime()))
	        .limitRate(10);
	}
}
