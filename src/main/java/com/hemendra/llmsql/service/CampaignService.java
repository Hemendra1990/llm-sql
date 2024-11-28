package com.hemendra.llmsql.service;

import com.hemendra.llmsql.entity.gen.Campaign;
import com.hemendra.llmsql.util.CampaignDataGenerator;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CampaignService {
    private final CampaignDataGenerator generateLeads;

    @Autowired
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 10000;

    public String generateAndSaveLead(Integer count) {
        long start = System.currentTimeMillis();
        List<Campaign> campaigns = generateLeads.generateFakeCampaign(count);
        long end = System.currentTimeMillis();
        log.info("Generated {} leads in {} ms", campaigns.size(), end - start);
        //leadRepository.saveAll(leads);
        saveLeadsInBatch(campaigns);
        return "Total Leads created: " + campaigns.size();
    }

    @Transactional
    public void saveLeadsInBatch(List<Campaign> campaigns) {
        int count = 0;
        for (Campaign campaign : campaigns) {
            entityManager.merge(campaign);
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
