package com.spring.msreport.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.spring.msreport.entity.ReportConsolidated;

public interface ReportRepository extends ReactiveMongoRepository<ReportConsolidated, String>{

}
