package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.gen.Campaign;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CampaignDataGenerator {

    public List<Campaign> generateFakeCampaign(int count) {
        Faker faker = new Faker();
        List<Campaign> campaigns = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Campaign campaign = new Campaign();
            campaign.setCreatedBy(faker.name().username());
            campaign.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
            campaign.setOrganisationId(UUID.randomUUID().toString());
            campaign.setUpdatedBy(faker.name().username());
            campaign.setUpdatedOn(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1));
            campaign.setActualCost((long) faker.number().numberBetween(1000, 10000));
            campaign.setBudgetedCost((long) faker.number().numberBetween(10000, 50000));
            campaign.setDescription(faker.lorem().sentence());
            campaign.setEndDate(OffsetDateTime.now(ZoneOffset.UTC).plusMonths(1));
            campaign.setExpectedResponse((long) faker.number().numberBetween(100, 1000));
            campaign.setExpectedRevenue((long) faker.number().numberBetween(10000, 100000));
            campaign.setIsActive(faker.bool().bool());
            campaign.setName(faker.company().name());
            campaign.setStartDate(OffsetDateTime.now(ZoneOffset.UTC));
            campaign.setStatus(faker.options().option("Planned", "In Progress", "Completed", "Aborted"));
            campaign.setType(faker.options().option(
                    "Webinar",
                    "Conference",
                    "Trade Show",
                    "Public Relations",
                    "Partners",
                    "Referral Program",
                    "Advertisement",
                    "Banner Ads",
                    "Direct Mail",
                    "Email",
                    "Telemarketing",
                    "Other"
            ));
            campaign.setIsDeleted(faker.bool().bool());
            campaigns.add(campaign);
        }

        return campaigns;
    }
}
