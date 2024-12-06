package com.hemendra.llmsql.service;

import com.hemendra.llmsql.entity.gen.OpportunityProduct;
import com.hemendra.llmsql.util.OpportunityProductDataGenerator;
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
public class OpportunityProductService {
    private final OpportunityProductDataGenerator opportunityProductDataGenerator;

    @Autowired
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 10000;

    public String generateAndSaveOpportunityProduct(Integer count) {
        long start = System.currentTimeMillis();
        List<OpportunityProduct> opportunityProducts = opportunityProductDataGenerator.generateFakeOpportunityProduct(count);
        long end = System.currentTimeMillis();
        log.info("Generated {} opportunity products in {} ms", opportunityProducts.size(), end - start);
        saveOpportunityProductsInBatch(opportunityProducts);
        return "Total OpportunityProducts created: " + opportunityProducts.size();
    }

    @Transactional
    public void saveOpportunityProductsInBatch(List<OpportunityProduct> opportunityProducts) {
        int count = 0;
        for (OpportunityProduct opportunityProduct : opportunityProducts) {
            entityManager.merge(opportunityProduct);
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