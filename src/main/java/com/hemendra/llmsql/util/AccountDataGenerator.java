package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.gen.Account;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AccountDataGenerator {
    public List<Account> generateFakeAccount(int count) {
        Faker faker = new Faker();
        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Account account = new Account();

            account.setCreatedBy(faker.name().username());
            account.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
            account.setOrganisationId(UUID.randomUUID().toString());
            account.setUpdatedBy(faker.name().username());
            account.setUpdatedOn(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1));
            account.setAccountNumber(faker.number().numberBetween(100000000L, 999999999L));
            account.setAccountSite(faker.company().name() + " Site");
            account.setAnnualRevenue(faker.number().numberBetween(1000000L, 50000000L));
            account.setCity(faker.address().city());
            account.setCountry(faker.address().country());
            account.setPostalCode(faker.address().zipCode());
            account.setState(faker.address().state());
            account.setStreet(faker.address().streetAddress());
            account.setContactIsInactive(faker.bool().bool());
            account.setContactRole(faker.job().title());
            account.setDescription(faker.lorem().sentence());
            account.setEmailAddress(faker.internet().emailAddress());
            account.setFax(faker.phoneNumber().phoneNumber());

            // Using the specified industry options
            account.setIndustry(faker.options().option("Agriculture", "Apparel", "Banking", "Biotechnology", "Chemicals"));

            account.setName(faker.company().name());
            account.setNoOfEmployee(faker.number().numberBetween(10, 5000));

            // Using the specified ownership options
            account.setOwnership(faker.options().option("Public", "Private", "Subsidiary", "Other"));

            account.setPhoneNumber(faker.phoneNumber().phoneNumber());

            // Using the specified rating options
            account.setRating(faker.options().option("Hot", "Warm", "Cold"));

            account.setShippingAddressCity(faker.address().city());
            account.setShippingAddressCountry(faker.address().country());
            account.setShippingAddressPostalCode(faker.address().zipCode());
            account.setShippingAddressState(faker.address().state());
            account.setShippingAddressStreet(faker.address().streetAddress());
            account.setSicCode(faker.code().isbn10());
            account.setTickerSymbol(faker.stock().nyseSymbol());

            // Using the specified type options
            account.setType(faker.options().option(
                    "Prospect",
                    "Customer-Direct",
                    "Customer-Channel",
                    "Channel Partner/Reseller",
                    "Installation Partner",
                    "Technology Partner",
                    "Other"
            ));

            account.setWebsite(faker.internet().url());
            account.setIsActive(faker.bool().bool());
            account.setIsDeleted(faker.bool().bool());

            accounts.add(account);
        }


        return accounts;
    }
}