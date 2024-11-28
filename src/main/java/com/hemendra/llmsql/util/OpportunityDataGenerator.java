package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.gen.Campaign;
import com.hemendra.llmsql.entity.gen.Opportunity;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Component
public class OpportunityDataGenerator {

    private static final List<String> CAMPAIGN_IDS = Arrays.asList(
            "0J17YNMGPCMQY", "0J17YNMHPCMQR", "0J17YNMHPCMQS", "0J17YNMHPCMQT",
            "0J17YNMHPCMQV", "0J17YNMHPCMQW", "0J17YNMHPCMQX", "0J17YNMHPCMQY",
            "0J17YNMHTCMQZ", "0J17YNMHTCMR0", "0J17ZWV2TCMTG", "0J17ZWV2TCMTH",
            "0J17ZWV2YCMTS", "0J17ZWV2YCMTT", "0J17ZWV2YCMTV", "0J17ZWV2YCMTW",
            "0J17ZWV2YCMTX", "0J17ZWV2YCMTY", "0J17ZWV2YCMTZ", "0J17ZWV32CMRK",
            "0J17ZWV3JCMTN", "0J17ZWV3JCMTP", "0J17ZWV3JCMTQ", "0J17ZWV3JCMTR",
            "0J17ZWV3JCMTS", "0J17ZWV3JCMTT", "0J17ZWV3JCMTV", "0J17ZWV3JCMTW",
            "0J17ZWV3PCMWZ", "0J17ZWV3PCMX0", "0J17ZWV3PCMX1", "0J17ZWV3PCMX2",
            "0J17ZWV3PCMX3", "0J17ZWV3PCMX4", "0J17ZWV3PCMX5", "0J17ZWV3PCMX6",
            "0J17ZWV3PCMX7", "0J17ZWV3TCMSG", "0J17ZWV3TCMSH", "0J17ZWV3TCMSJ",
            "0J17ZWV3TCMSK", "0J17ZWV3TCMSM", "0J17ZWV3TCMSN", "0J17ZWV3TCMSP",
            "0J17ZWV3TCMSQ", "0J17ZWV3TCMSR", "0J17ZWV3YCMTY", "0J17ZWV3YCMTZ",
            "0J17ZWV3YCMV0", "0J17ZWV3YCMV1", "0J17ZWV3YCMV2", "0J17ZWV3YCMV3",
            "0J17ZWV3YCMV4", "0J17ZWV3YCMV5", "0J17ZWV3YCMV6", "0J17ZWV3YCMV7",
            "0J17ZWV3YCMV8", "0J17ZWV42CMYN", "0J17ZWV42CMYP", "0J17ZWV42CMYQ",
            "0J17ZWV42CMYR", "0J17ZWV42CMYS", "0J17ZWV42CMYT", "0J17ZWV46CMZM",
            "0J17ZWV4ECMZV", "0J17ZWV4ECMZW", "0J17ZWV4ECMZX", "0J17ZWV4JCMSM",
            "0J17ZWV4JCMSN", "0J17ZWV4JCMSP", "0J17ZWV4JCMSQ", "0J17ZWV4JCMSR",
            "0J17ZWV4JCMSS", "0J17ZWV4JCMST", "0J17ZWV4JCMSV", "0J17ZWV4JCMSW",
            "0J17ZWV4JCMSX", "0J17ZWV4JCMSY", "0J17ZWV4JCMSZ", "0J17ZWV4JCMT0",
            "0J17ZWV4PCMSK", "0J17ZWV4PCMSM", "0J17ZWV4PCMSN", "0J17ZWV4PCMSP",
            "0J17ZWV4PCMSQ", "0J17ZWV4PCMSR", "0J17ZWV4PCMSS", "0J17ZWV4PCMST",
            "0J17ZWV4PCMSV", "0J17ZWV4PCMSW", "0J17ZWV4PCMSX", "0J17ZWV4PCMSY",
            "0J17ZWV4PCMSZ", "0J17ZWV4TCMST", "0J17ZWV4TCMSV", "0J17ZWV4TCMSW",
            "0J17ZWV4TCMSX", "0J17ZWV4TCMSY", "0J17ZWV4TCMSZ", "0J17ZWV4TCMT0",
            "0J17ZWV4TCMT1", "0J17ZWV4TCMT2", "0J17ZWV4TCMT3", "0J17ZWV4TCMT4",
            "0J17ZWV4TCMT5", "0J17ZWV4TCMT6", "0J17ZWV4YCMZK", "0J17ZWV4YCMZM",
            "0J17ZWV4YCMZN", "0J17ZWV4YCMZP", "0J17ZWV4YCMZQ", "0J17ZWV4YCMZR",
            "0J17ZWV4YCMZS", "0J17ZWV4YCMZT", "0J17ZWV4YCMZV", "0J17ZWV4YCMZW",
            "0J17ZWV4YCMZX", "0J17ZWV4YCMZY", "0J17ZWV4YCMZZ", "0J17ZWV4YCN00",
            "0J17ZWV4YCN01", "0J17ZWV52CMXK", "0J17ZWV52CMXM", "0J17ZWV52CMXN"
    );

    public Campaign getRandomCampaign() {
        Random random = new Random();
        String randomCampaignId = CAMPAIGN_IDS.get(random.nextInt(CAMPAIGN_IDS.size()));

        Campaign campaign = new Campaign();
        campaign.setId(randomCampaignId);

        return campaign;
    }

    public List<Opportunity> generateFakeOpportunity(int count) {
        Faker faker = new Faker();
        List<Opportunity> opportunities = new ArrayList<>();
        for(int i = 0; i < count; i++) {

            Opportunity opportunity = new Opportunity();

            opportunity.setCreatedBy(faker.name().username());
            opportunity.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
            opportunity.setOrganisationId(UUID.randomUUID().toString());
            opportunity.setUpdatedBy(faker.name().username());
            opportunity.setUpdatedOn(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1));
            opportunity.setAmount(faker.number().randomDouble(2, 1000, 100000));
            opportunity.setAmountConverted(faker.number().randomDouble(2, 1000, 100000));
            opportunity.setAmountWeightedConverted(faker.number().randomDouble(2, 500, 50000));
            opportunity.setCloseDate(OffsetDateTime.now(ZoneOffset.UTC).plusDays(faker.number().numberBetween(30, 180)));
            opportunity.setDescription(faker.lorem().sentence());

            // Using specified options for leadSource
            opportunity.setLeadSource(faker.options().option(
                    "Web",
                    "Phone Inquiry",
                    "Partner Referral",
                    "Purchased List",
                    "Other"
            ));

            opportunity.setName(faker.company().name());
            opportunity.setNextStep(faker.lorem().sentence());
            opportunity.setProbability(faker.number().numberBetween(0, 100));

            // Using specified options for stage
            opportunity.setStage(faker.options().option(
                    "Prospecting",
                    "Qualification",
                    "Needs Analysis",
                    "Value Proposition",
                    "Id. Decision Maker",
                    "Perception Analysis",
                    "Proposal/Price Quote",
                    "Negotiation/Review",
                    "Closed Won",
                    "Closed Lost"
            ));

            // Using specified options for type
            opportunity.setType(faker.options().option(
                    "Existing Customer-Upgrade",
                    "Existing Customer-Replacement",
                    "Existing Customer-Downgrade",
                    "New Customer"
            ));

            opportunity.setIsActive(faker.bool().bool());
            opportunity.setIsDeleted(faker.bool().bool());

            opportunity.setCampaign(getRandomCampaign());

            opportunities.add(opportunity);
        }

        return opportunities;
    }

}