package com.spring.msreport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.msreport.entity.CreditTransaction;
import com.spring.msreport.entity.Customer;
import com.spring.msreport.entity.DebitCardTransaction;
import com.spring.msreport.entity.ReportConsolidated;
import com.spring.msreport.service.ReportService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RefreshScope
@RestController
@Slf4j
@RequestMapping("/report")
public class ReportController {

	@Autowired
	ReportService reportService;
	
	@GetMapping("/customers/{idcustomer}")
	public Mono<ReportConsolidated> findCustomer(@PathVariable String idcustomer){
		return reportService.findCustomer(idcustomer)
				.flatMap(customer -> reportService.findCards(idcustomer)
									.collectList()
									.flatMap(cards -> reportService.findAccounts(idcustomer)
													.collectList()
													.map(accounts -> ReportConsolidated.builder()
																	.customer(customer)
																	.cards(cards)
																	.bankAccounts(accounts)
																	.build())));
	}
	

	@GetMapping("/debitCard/{idcustomer}")
	public Flux<DebitCardTransaction> findByDebitCardCustomerId(@PathVariable String idcustomer){
		return reportService.findByDebitCardCustomerId(idcustomer);
	}
	
	@GetMapping("/creditCard/{idcustomer}")
	public Flux<CreditTransaction> findByCreditCreditCardCustomerId(@PathVariable String idcustomer){
		return reportService.findByCreditCreditCardCustomerId(idcustomer);
	}
	
	
}
