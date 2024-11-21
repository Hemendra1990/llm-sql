package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.Lead;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class LeadDataGenerator {

    public List<Lead> generateLeads(int count) {
        List<Lead> leads = new ArrayList<>();
        Faker faker = new Faker();
        Random random = new Random();
        for(int i = 0; i < count; i++) {
            Lead lead = new Lead();
            lead.setFirstName(faker.name().firstName());
            lead.setLastName(faker.name().lastName());
            lead.setEmail(faker.internet().emailAddress());
            lead.setPhone(faker.phoneNumber().phoneNumber());
            lead.setAddress(faker.address().streetAddress());
            lead.setCity(faker.address().city());
            lead.setState(faker.address().state());
            lead.setCountry(faker.address().country());
            lead.setZipCode(faker.address().zipCode());
            lead.setCompanyName(faker.company().name());
            lead.setJobTitle(faker.job().title());
            lead.setIndustry(faker.company().industry());
            lead.setWebsite(faker.internet().url());
            lead.setLeadSource(faker.options().option("Website", "Referral", "Event", "Email", "Whatsapp", "Telegram"));
            lead.setLeadStatus(faker.options().option("New", "Contacted", "Qualified"));
            lead.setRating(faker.options().option("Hot", "Warm", "Cold"));
            lead.setAnnualRevenue(random.nextDouble() * 500000 + 50000);
            lead.setNumberOfEmployees(random.nextInt(500) + 1);
            lead.setNotes(faker.lorem().sentence());
            lead.setCreatedDate(LocalDateTime.now().minusDays(random.nextInt(30)));
            lead.setUpdatedDate(LocalDateTime.now().minusDays(random.nextInt(30)));
            lead.setOptIn(random.nextBoolean());
            lead.setPreferredLanguage(faker.options().option("English", "Hindi", "French", "Spanish", "German", "Odia", "Tamil", "Japanese", "Telugu"));
            lead.setTimezone(faker.options().option("IST", "PST", "UTC", "CST", "EST"));
            lead.setMarketingChannel(faker.options().option("Email", "Phone Call", "Social Media"));
            lead.setReferralSource(faker.options().option("Google Ads", "Event Booth", "Friend"));
            lead.setCampaignName(faker.options().option("Google Campaign", "Event Booth"));
            lead.setPriority(faker.options().option("High", "Medium", "Low"));
            lead.setAssignedTo(faker.name().name());

            leads.add(lead);
        }

        return leads;
    }
}
