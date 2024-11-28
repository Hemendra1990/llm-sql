package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.gen.Pricebook;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class PricebookDataGenerator {

    public List<Pricebook> generateFakePricebook(int count) {
        Faker faker = new Faker();
        List<Pricebook> pricebooks = new ArrayList<>();
        for (int i = 0; i < count; i++) {

            Pricebook pricebook = new Pricebook();

            pricebook.setCreatedBy(faker.name().username());
            pricebook.setCreatedOn(OffsetDateTime.now(ZoneOffset.UTC));
            pricebook.setOrganisationId(UUID.randomUUID().toString());
            pricebook.setUpdatedBy(faker.name().username());
            pricebook.setUpdatedOn(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1));
            pricebook.setDescription(faker.lorem().sentence());
            pricebook.setIsActive(faker.bool().bool());
            pricebook.setIsDeleted(faker.bool().bool());
            pricebook.setIsStandardPriceBook(faker.bool().bool());
            pricebook.setName(faker.commerce().productName());

            pricebooks.add(pricebook);
        }


        return pricebooks;
    }

}