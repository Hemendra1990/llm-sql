package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.gen.Campaign;
import com.hemendra.llmsql.entity.gen.Opportunity;
import com.hemendra.llmsql.entity.gen.OpportunityProduct;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Component
public class OpportunityProductDataGenerator {

    private final EntityManager entityManager;

    public OpportunityProductDataGenerator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private static final List<String> OPPORTUNITY_IDS = Arrays.asList(
          "0J1FKJ4VBK8Q0", "0J1FKJ4XFK8QB", "0J1FKJ4XQK8Q0", "0J1FKJ4XZK8QB", "0J1FKJ4YBK8QZ",
                "0J1FKJ4YKK8Q1", "0J1FKJ4YVK8QM", "0J1FKJ4YZK8QD", "0J1FKJ4Z7K8QR", "0J1FKJ4ZFK8Q2",
                "0J1FKJ4ZQK8QY", "0J1FKJ4ZZK8QJ", "0J1FKJ507K8QM", "0J1FKJ50BK8Q6", "0J1FKJ50KK8QY",
                "0J1FKJ50QK8QF", "0J1FKJ50VK8QZ", "0J1FKJ517K8QR", "0J1FKJ51BK8QM", "0J1FKJ51FK8QP",
                "0J1FKJ51KK8QJ", "0J1FKJ52QK8QW", "0J1FKJ52ZK8QS", "0J1FKJ537K8QJ", "0J1FKJ53FK8QG",
                "0J1FKJ53QK8QD", "0J1FKJ53VK8QT", "0J1FKJ543K8Q4", "0J1FKJ54BK8QX", "0J1FKJ54FK8QV",
                "0J1FKJ54QK8QY", "0J1FKJ54VK8QR", "0J1FKJ54ZK8QN", "0J1FKJ553K8QT", "0J1FKJ55BK8QP",
                "0J1FKJ55KK8QW", "0J1FKJ55QK8Q1", "0J1FKJ55ZK8QH", "0J1FKJ563K8QS", "0J1FKJ56BK8QQ",
                "0J1FKJ56FK8QF", "0J1FKJ56QK8QQ", "0J1FKJ573K8QH", "0J1FKJ57BK8QP", "0J1FKJ57BK8QQ",
                "0J1FKJ57KK8Q6", "0J1FKJ57VK8QF", "0J1FKJ57ZK8QC", "0J1FKJ587K8Q1", "0J1FKJ58BK8QD",
                "0J1FKJ58KK8QX", "0J1FKJ58KK8QY", "0J1FKJ58VK8QR", "0J1FKJ58ZK8QC", "0J1FKJ597K8QC",
                "0J1FKJ59BK8QA", "0J1FKJ59BK8QB", "0J1FKJ59FK8Q3", "0J1FKJ59KK8QM", "0J1FKJ59QK8Q1",
                "0J1FKJ59VK8Q4", "0J1FKJ59ZK8QV", "0J1FKJ5A3K8QB", "0J1FKJ5AFK8Q1", "0J1FKJ5B3K8QS",
                "0J1FKJ5BBK8Q7", "0J1FKJ5BFK8QT", "0J1FKJ5BFK8QV", "0J1FKJ5BQK8QX", "0J1FKJ5BVK8QM",
                "0J1FKJ5BZK8QX", "0J1FKJ5C3K8Q9", "0J1FKJ5C7K8QN", "0J1FKJ5C7K8QP", "0J1FKJ5CBK8Q8",
                "0J1FKJ5CBK8Q9", "0J1FKJ5CFK8QH", "0J1FKJ5CQK8QS", "0J1FKJ5CVK8QD", "0J1FKJ5CVK8QE",
                "0J1FKJ5CZK8QT", "0J1FKJ5CZK8QV", "0J1FKJ5D3K8Q4", "0J1FKJ5D7K8QY", "0J1FKJ5D7K8QZ",
                "0J1FKJ5DBK8QY", "0J1FKJ5DFK8QT", "0J1FKJ5DQK8QA", "0J1FKJ5DQK8QB", "0J1FKJ5DVK8Q4",
                "0J1FKJ5DZK8Q7", "0J1FKJ5DZK8Q8", "0J1FKJ5E3K8QQ", "0J1FKJ5E3K8QR", "0J1FKJ5E7K8QH",
                "0J1FKJ5EBK8Q7", "0J1FKJ5EFK8QF", "0J1FKJ5EKK8Q8", "0J1FKJ5EKK8Q9", "0J1FKJ5EVK8QC",
                "0J1FKJ5EZK8QX", "0J1FKJ5F3K8QG", "0J1FKJ5F7K8QX", "0J1FKJ5F7K8QY", "0J1FKJ5FFK8Q7",
                "0J1FKJ5FFK8Q8", "0J1FKJ5FFK8Q9", "0J1FKJ5FFK8QA", "0J1FKJ5FKK8Q2", "0J1FKJ5FQK8QV",
                "0J1FKJ5FVK8Q1", "0J1FKJ5FVK8Q2", "0J1FKJ5FVK8Q3", "0J1FKJ5FVK8Q4", "0J1FKJ5FVK8Q5",
                "0J1FKJ5FZK8QW", "0J1FKJ5G3K8QD", "0J1FKJ5G7K8QD", "0J1FKJ5GBK8QF", "0J1FKJ5GBK8QG",
                "0J1FKJ5GBK8QH", "0J1FKJ5GFK8Q4", "0J1FKJ5GFK8Q5", "0J1FKJ5GFK8Q6", "0J1FKJ5GFK8Q7",
                "0J1FKJ5GFK8Q8", "0J1FKJ5GKK8Q1", "0J1FKJ5GQK8Q0", "0J1FKJ5GZK8Q2", "0J1FKJ5GZK8Q3",
                "0J1FKJ5GZK8Q4", "0J1FKJ5GZK8Q5", "0J1FKJ5GZK8Q6", "0J1FKJ5GZK8Q7", "0J1FKJ5H3K8QY",
                "0J1FKJ5H3K8QZ", "0J1FKJ5H7K8QP", "0J1FKJ5H7K8QQ", "0J1FKJ5H7K8QR", "0J1FKJ5HBK8Q9",
                "0J1FKJ5HBK8QA", "0J1FKJ5HFK8Q9", "0J1FKJ5HFK8QA", "0J1FKJ5HKK8Q6", "0J1FKJ5HKK8Q7",
                "0J1FKJ5HKK8Q8", "0J1FKJ5HKK8Q9", "0J1FKJ5HQK8QN", "0J1FKJ5HQK8QP", "0J1FKJ5HVK8QV",
                "0J1FKJ5HVK8QW", "0J1FKJ5HZK8QN", "0J1FKJ5HZK8QP", "0J1FKJ5HZK8QQ", "0J1FKJ5J3K8Q2",
                "0J1FKJ5J3K8Q3", "0J1FKJ5J3K8Q4", "0J1FKJ5J7K8QJ"
    );

    public Opportunity getRandomOpportunity() {
        Random random = new Random();
        String randomOpportunityId = OPPORTUNITY_IDS.get(random.nextInt(OPPORTUNITY_IDS.size()));

        Opportunity opportunity = new Opportunity();
        opportunity.setId(randomOpportunityId);

        return opportunity;
    }

    public List<OpportunityProduct> generateFakeOpportunityProduct(int count) {
        Faker faker = new Faker();
        List<OpportunityProduct> opportunityProducts = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            OpportunityProduct opportunityProduct = new OpportunityProduct();

            // Set basic audit fields
            opportunityProduct.setCreatedBy(faker.name().username());
            opportunityProduct.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
            //opportunityProduct.setOrganisationId(UUID.randomUUID().toString());
            opportunityProduct.setUpdatedBy(faker.name().username());
            opportunityProduct.setUpdatedOn(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1));

            // Set product-specific fields
            opportunityProduct.setDate(OffsetDateTime.now(ZoneOffset.UTC).plusDays(faker.number().numberBetween(1, 30)));
            opportunityProduct.setDescription(faker.commerce().productName() + " - " + faker.lorem().sentence());
            opportunityProduct.setListPrice(faker.commerce().price());
            opportunityProduct.setName(faker.commerce().productName());
            opportunityProduct.setPricebookEntriesId(UUID.randomUUID().toString());
            opportunityProduct.setProductCode("PRD-" + faker.number().numberBetween(1000, 9999));
            opportunityProduct.setProductId(UUID.randomUUID().toString());
            opportunityProduct.setProductName(faker.commerce().productName());
            opportunityProduct.setQuantity(faker.number().numberBetween(1, 100));
            
            // Calculate prices
            double salePrice = faker.number().randomDouble(2, 100, 10000);
            opportunityProduct.setSalePrice(salePrice);
            opportunityProduct.setTotalPrice(salePrice * opportunityProduct.getQuantity());

            // Set status flags
            opportunityProduct.setIsActive(faker.bool().bool());
            opportunityProduct.setIsDeleted(false);

            // Set random opportunity from database
            opportunityProduct.setOpportunity(getRandomOpportunity());

            opportunityProducts.add(opportunityProduct);
        }

        return opportunityProducts;
    }
}