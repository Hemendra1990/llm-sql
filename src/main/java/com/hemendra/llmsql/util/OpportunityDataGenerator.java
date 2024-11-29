package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.gen.Account;
import com.hemendra.llmsql.entity.gen.Campaign;
import com.hemendra.llmsql.entity.gen.Opportunity;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Component
public class OpportunityDataGenerator {

    private static final List<String> CAMPAIGN_IDS = Arrays.asList(
            "0J1813EK4WWQK", "0J1813ENCWWQX", "0J1813ENGWWQ0", "0J1813ENGWWQ1", "0J1813ENGWWQ2",
            "0J1813ENGWWQ3", "0J1813ENMWWQJ", "0J1813ENMWWQK", "0J1813ENMWWQM", "0J1813ENMWWQN",
            "0J181PZ64WWQV", "0J181PZ6MWWQJ", "0J181PZ6MWWQK", "0J181PZ6RWWQJ", "0J181PZ6RWWQK",
            "0J181PZ6WWWQT", "0J181PZ70WWQQ", "0J181PZ74WWQW", "0J181PZ74WWQX", "0J181PZ74WWQY",
            "0J181PZD0WWQM", "0J181PZD4WWQQ", "0J181PZD4WWQR", "0J181PZD4WWQS", "0J181PZD4WWQT",
            "0J181PZDCWWQW", "0J181PZDCWWQX", "0J181PZDGWWQG", "0J181PZDMWWQV", "0J181PZDMWWQW",
            "0J181PZDRWWQJ", "0J181PZDWWWQQ", "0J181PZE0WWQR", "0J181PZE0WWQS", "0J181PZE4WWQY",
            "0J181PZE4WWQZ", "0J181PZE8WWQZ", "0J181PZE8WWR0", "0J181PZECWWQX", "0J181PZECWWQY",
            "0J181PZEGWWQR", "0J181PZEMWWQJ", "0J181PZEMWWQK", "0J181PZERWWQQ", "0J181PZERWWQR",
            "0J181PZEWWWQV", "0J181PZEWWWQW", "0J181PZEWWWQX", "0J181PZEWWWQY", "0J181PZF0WWQZ",
            "0J181PZF0WWR0", "0J181PZF0WWR1", "0J181PZF8WWQY", "0J181PZFGWWQY", "0J181PZFMWWQK",
            "0J181PZFMWWQM", "0J181PZFRWWQJ", "0J181PZFRWWQK", "0J181PZG8WWQR", "0J181PZGCWWQW",
            "0J181PZGCWWQX", "0J181PZGGWWQK", "0J181PZGGWWQM", "0J181PZGRWWQG", "0J181PZKRWWQW",
            "0J181PZKWWWQQ", "0J181PZM0WWQM", "0J181PZM0WWQN", "0J181PZM8WWQS", "0J181PZM8WWQT",
            "0J181PZMCWWQX", "0J181PZMGWWQP", "0J181PZMGWWQQ", "0J181PZMMWWQG", "0J181PZMRWWQG",
            "0J181PZMRWWQH", "0J181PZMWWWQX", "0J181PZMWWWQY", "0J181PZMWWWQZ", "0J181PZN4WWQJ",
            "0J181PZN4WWQK", "0J181PZN4WWQM", "0J181PZN4WWQN", "0J181PZN4WWQP", "0J181PZN8WWQG",
            "0J181PZN8WWQH", "0J181PZN8WWQJ", "0J181PZN8WWQK", "0J181PZNCWWQJ", "0J181PZNCWWQK",
            "0J181PZNCWWQM", "0J181PZNCWWQN", "0J181PZNCWWQP", "0J181PZNCWWQQ", "0J181PZNCWWQR",
            "0J181PZNCWWQS", "0J181PZNGWWQM", "0J181PZNGWWQN", "0J181PZNGWWQP", "0J181PZNMWWQG",
            "0J181PZNMWWQH", "0J181PZNMWWQJ", "0J181PZNRWWQY", "0J181PZNRWWQZ", "0J181PZNRWWR0",
            "0J181PZNRWWR1", "0J181PZNWWWQT", "0J181PZNWWWQV", "0J181PZNWWWQW", "0J181PZNWWWQX",
            "0J181PZP0WWQJ", "0J181PZP0WWQK", "0J181PZP0WWQM", "0J181PZP0WWQN", "0J181PZP4WWQZ",
            "0J181PZP4WWR0", "0J181PZP4WWR1", "0J181PZP4WWR2", "0J181PZP4WWR3", "0J181PZP8WWQZ",
            "0J181PZP8WWR0", "0J181PZP8WWR1", "0J181PZPCWWQP", "0J181PZPCWWQQ", "0J181PZPCWWQR",
            "0J181PZPGWWQZ", "0J181PZPRWWQZ", "0J181PZPWWWQK", "0J181PZQ4WWQG", "0J181PZQ4WWQH",
            "0J181PZQ4WWQJ", "0J181PZQ8WWQH", "0J181PZQ8WWQJ", "0J181PZQ8WWQK", "0J181PZQ8WWQM",
            "0J181PZQCWWQZ", "0J181PZQCWWR0", "0J181PZQGWWQP", "0J181PZQGWWQQ", "0J181PZQGWWQR",
            "0J181PZQGWWQS", "0J181PZQMWWQP", "0J181PZQMWWQQ", "0J181PZQMWWQR", "0J181PZQMWWQS",
            "0J181PZQMWWQT", "0J181PZQRWWQN", "0J181PZQRWWQP", "0J181PZQRWWQQ", "0J181PZQWWWQZ",
            "0J181PZQWWWR0", "0J181PZQWWWR1", "0J181PZQWWWR2", "0J181PZQWWWR3", "0J181PZR0WWQV",
            "0J181PZR0WWQW", "0J181PZR0WWQX", "0J181PZR0WWQY", "0J181PZR0WWQZ", "0J181PZR0WWR0",
            "0J181PZR4WWQW", "0J181PZR4WWQX", "0J181PZR4WWQY", "0J181PZR8WWQK", "0J181PZR8WWQM",
            "0J181PZRCWWQM", "0J181PZRCWWQN", "0J181PZRCWWQP", "0J181PZRCWWQQ", "0J181PZRGWWQN",
            "0J181PZRGWWQP", "0J181PZRMWWQK", "0J181PZRMWWQM", "0J181PZRMWWQN", "0J181PZRMWWQP",
            "0J181PZRMWWQQ", "0J181PZRRWWQG", "0J181PZRRWWQH"
    );

    public Campaign getRandomCampaign() {
        Random random = new Random();
        String randomCampaignId = CAMPAIGN_IDS.get(random.nextInt(CAMPAIGN_IDS.size()));

        Campaign campaign = new Campaign();
        campaign.setId(randomCampaignId);

        return campaign;
    }

    private static final List<String> ACCOUNT_IDS = Arrays.asList(
            "0J18HJGDFJGZD", "0J18HJGDKJGWQ", "0J18HJGDQJGX8", "0J18HJGDZJGXF", "0J18HJGE7JGWS",
            "0J18HJGEBJGY2", "0J18HJGEKJGYD", "0J18HJGEQJGW2", "0J18HJGEVJGR0", "0J18HJGEZJGSM",
            "0J18HJGF3JGVJ", "0J18HJGF7JGYR", "0J18HJGFBJGZJ", "0J18HJGFFJGTM", "0J18HJGFKJGW3",
            "0J18HJGFKJGW4", "0J18HJGFQJGV6", "0J18HJGFVJGVW", "0J18HJGFZJGSR", "0J18HJGG3JGX5",
            "0J18HJGG3JGX6", "0J18HJGNFJGS2", "0J18HJGNQJGWN", "0J18HJGNQJGWP", "0J18HJGNVJGRP",
            "0J18HJGNVJGRQ", "0J18HJGNZJGYG", "0J18HJGP3JGVR", "0J18HJGP3JGVS", "0J18HJGPBJGZF",
            "0J18HJGPBJGZG", "0J18HJGPFJGTV", "0J18HJGPKJGWZ", "0J18HJGPKJGX0", "0J18HJGPQJGS3",
            "0J18HJGPQJGS4", "0J18HJGPQJGS5", "0J18HJGPQJGS6", "0J18HJGPVJGZR", "0J18HJGPVJGZS",
            "0J18HJGPVJGZT", "0J18HJGPVJGZV", "0J18HJGPVJGZW", "0J18HJGPZJGXZ", "0J18HJGPZJGY0",
            "0J18HJGPZJGY1", "0J18HJGPZJGY2", "0J18HJGPZJGY3", "0J18HJGQ3JGTG", "0J18HJGQ3JGTH",
            "0J18HJGQ3JGTJ", "0J18HJGQ3JGTK", "0J18HJGQ7JGZM", "0J18HJGQ7JGZN", "0J18HJGQ7JGZP",
            "0J18HJGQ7JGZQ", "0J18HJGQ7JGZR", "0J18HJGQBJGS5", "0J18HJGQBJGS6", "0J18HJGQFJGVF",
            "0J18HJGQKJGRJ", "0J18HJGQQJGS3", "0J18HJGQQJGS4", "0J18HJGQZJGZD", "0J18HJGTQJGSF",
            "0J18HJGTVJGY9", "0J18HJGTVJGYA", "0J18HJGTVJGYB", "0J18HJGTVJGYC", "0J18HJGTVJGYD",
            "0J18HJGTVJGYE", "0J18HJGTZJGW5", "0J18HJGTZJGW6", "0J18HJGTZJGW7", "0J18HJGTZJGW8",
            "0J18HJGTZJGW9", "0J18HJGTZJGWA", "0J18HJGV3JGY1", "0J18HJGV3JGY2", "0J18HJGV7JGRZ",
            "0J18HJGV7JGS0", "0J18HJGV7JGS1", "0J18HJGVBJGV0", "0J18HJGVBJGV1", "0J18HJGVBJGV2",
            "0J18HJGVBJGV3", "0J18HJGVBJGV4", "0J18HJGVFJGZY", "0J18HJGVFJGZZ", "0J18HJGVFJH00",
            "0J18HJGVFJH01", "0J18HJGVFJH02", "0J18HJGVKJGVA", "0J18HJGVKJGVB", "0J18HJGVQJGRN",
            "0J18HJGVVJGRX", "0J18HJGVVJGRY", "0J18HJGVZJGX5", "0J18HJGVZJGX6", "0J18HJGVZJGX7",
            "0J18HJGVZJGX8", "0J18HJGVZJGX9", "0J18HJGVZJGXA", "0J18HJGW3JGXV", "0J18HJGW3JGXW",
            "0J18HJGW3JGXX", "0J18HJGW3JGXY", "0J18HJGW3JGXZ", "0J18HJGW3JGY0", "0J18HJGW7JGVC",
            "0J18HJGW7JGVD", "0J18HJGW7JGVE", "0J18HJGWBJGX1", "0J18HJGWBJGX2", "0J18HJGWBJGX3",
            "0J18HJGWFJGST", "0J18HJGWFJGSV", "0J18HJGWKJGTM", "0J18HJGWKJGTN", "0J18HJGWQJGZZ",
            "0J18HJGWQJH00", "0J18HJGWQJH01", "0J18HJGWVJGZK", "0J18HJGWVJGZM", "0J18HJGWVJGZN",
            "0J18HJGWVJGZP", "0J18HJGWZJGTM", "0J18HJGX3JGZ6", "0J18HJGX7JGY5", "0J18HJGX7JGY6",
            "0J18HJGXBJGS8", "0J18HJGXBJGS9", "0J18HJGXFJGXR", "0J18HJGXFJGXS", "0J18HJGXFJGXT",
            "0J18HJGXFJGXV", "0J18HJGXFJGXW", "0J18HJGXKJGVE", "0J18HJGXQJGXW", "0J18HJGXQJGXX",
            "0J18HJGXVJGVK", "0J18HJGXVJGVM", "0J18HJGXVJGVN", "0J18HJGXVJGVP", "0J18HJGXVJGVQ",
            "0J18HJGXVJGVR", "0J18HJGXZJGW5", "0J18HJGXZJGW6", "0J18HJGXZJGW7", "0J18HJGXZJGW8",
            "0J18HJGXZJGW9", "0J18HJGY3JGZJ", "0J18HJGY3JGZK", "0J18HJGY3JGZM", "0J18HJGY3JGZN",
            "0J18HJGY3JGZP", "0J18HJGY7JGXJ", "0J18HJGY7JGXK", "0J18HJGYBJGX9", "0J18HJGYBJGXA",
            "0J18HJGYBJGXB", "0J18HJGYFJGV3", "0J18HJGYFJGV4", "0J18HJGYFJGV5", "0J18HJGYKJGXX",
            "0J18HJGYKJGXY", "0J18HJGYKJGXZ", "0J18HJGYQJGVJ", "0J18HJGYQJGVK", "0J18HJGYQJGVM",
            "0J18HJGYVJGXK", "0J18HJGYVJGXM", "0J18HJGYVJGXN", "0J18HJGZ3JGW5", "0J18HJGZ3JGW6",
            "0J18HJGZ3JGW7", "0J18HJGZ3JGW8", "0J18HJGZ3JGW9", "0J18HJGZ7JGXH", "0J18HJGZ7JGXJ",
            "0J18HJGZ7JGXK", "0J18HJGZ7JGXM", "0J18HJGZ7JGXN", "0J18HJGZBJGTS"

    );

    public Account getRandomAccount() {
        Random random = new Random();
        String randomAccountId = ACCOUNT_IDS.get(random.nextInt(ACCOUNT_IDS.size()));

        Account account = new Account();
        account.setId(randomAccountId);

        return account;
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
            opportunity.setAccount(getRandomAccount());

            opportunities.add(opportunity);
        }

        return opportunities;
    }

}