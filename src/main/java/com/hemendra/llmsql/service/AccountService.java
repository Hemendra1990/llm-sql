package com.hemendra.llmsql.service;

import com.hemendra.llmsql.entity.gen.Account;
import com.hemendra.llmsql.util.AccountDataGenerator;
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
public class AccountService {
    private final AccountDataGenerator accountDataGenerator;

    @Autowired
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 10000;

    public String generateAndSaveAccount(Integer count) {
        long start = System.currentTimeMillis();
        List<Account> accounts = accountDataGenerator.generateFakeAccount(count);
        long end = System.currentTimeMillis();
        log.info("Generated {} leads in {} ms", accounts.size(), end - start);
        //leadRepository.saveAll(leads);
        saveLeadsInBatch(accounts);
        return "Total Leads created: " + accounts.size();
    }

    @Transactional
    public void saveLeadsInBatch(List<Account> accounts) {
        int count = 0;
        for (Account account : accounts) {
            entityManager.merge(account);
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
