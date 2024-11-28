package com.hemendra.llmsql.service;

import com.hemendra.llmsql.entity.gen.Opportunity;
import com.hemendra.llmsql.util.OpportunityDataGenerator;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OpportunityService {
    private final OpportunityDataGenerator opportunityDataGenerator;

    @Autowired
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 10000;

    public String generateAndSaveOpportunity(Integer count) {
        long start = System.currentTimeMillis();
        List<Opportunity> opportunities = opportunityDataGenerator.generateFakeOpportunity(count);
        long end = System.currentTimeMillis();
        log.info("Generated {} leads in {} ms", opportunities.size(), end - start);
        //leadRepository.saveAll(leads);
        saveOpportunitiesInBatch(opportunities);
        return "Total Leads created: " + opportunities.size();
    }

    @Transactional
    public void saveOpportunitiesInBatch(List<Opportunity> opportunities) {
        int count = 0;
        for (Opportunity opportunity : opportunities) {
            entityManager.merge(opportunity);
            count++;

            if (count % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        // Flush remaining entities
        entityManager.flush();
        entityManager.clear();
        log.info("Record saved {}", count);
    }
}
