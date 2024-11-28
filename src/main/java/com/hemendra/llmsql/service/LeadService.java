package com.hemendra.llmsql.service;

import com.hemendra.llmsql.entity.Lead;
import com.hemendra.llmsql.repository.LeadRepository;
import com.hemendra.llmsql.util.LeadDataGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LeadService {
    private final LeadRepository leadRepository;
    private final LeadDataGenerator leadDataGenerator;

    @Autowired
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 10000;

    public List<Lead> findAll() {
        return leadRepository.findAll();
    }

    public Lead findById(Long id) {
        return leadRepository.findById(id).orElse(null);
    }

    public Lead save(Lead lead) {
        return leadRepository.save(lead);
    }

    public List<Lead> saveAll(List<Lead> leads) {
        return leadRepository.saveAll(leads);
    }

    public String generateAndSaveLead(Integer count) {
        long start = System.currentTimeMillis();
        List<Lead> leads = leadDataGenerator.generateLeads(count);
        long end = System.currentTimeMillis();
        log.info("Generated {} leads in {} ms", leads.size(), end - start);
        //leadRepository.saveAll(leads);
        saveLeadsInBatch(leads);
        return "Total Leads created: " + leads.size();
    }

    @Transactional
    public void saveLeadsInBatch(List<Lead> leads) {
        int count = 0;
        for (Lead lead : leads) {
            entityManager.merge(lead);
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
