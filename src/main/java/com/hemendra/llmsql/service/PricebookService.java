package com.hemendra.llmsql.service;

import com.hemendra.llmsql.entity.gen.Pricebook;
import com.hemendra.llmsql.util.PricebookDataGenerator;
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
public class PricebookService {
    private final PricebookDataGenerator pricebookDataGenerator;

    @Autowired
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 10000;

    public String generateAndSaveAccount(Integer count) {
        long start = System.currentTimeMillis();
        List<Pricebook> pricebooks = pricebookDataGenerator.generateFakePricebook(count);
        long end = System.currentTimeMillis();
        log.info("Generated {} leads in {} ms", pricebooks.size(), end - start);
        //leadRepository.saveAll(leads);
        saveLeadsInBatch(pricebooks);
        return "Total Leads created: " + pricebooks.size();
    }

    @Transactional
    public void saveLeadsInBatch(List<Pricebook> pricebooks) {
        int count = 0;
        for (Pricebook pricebook : pricebooks) {
            entityManager.merge(pricebook);
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
