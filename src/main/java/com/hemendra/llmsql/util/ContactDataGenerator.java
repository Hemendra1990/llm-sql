package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.Contact;
import com.hemendra.llmsql.entity.Lead;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class ContactDataGenerator {
    public List<Contact> generateContacts(int count) {
        List<Contact> contacts = new ArrayList<>();
        Faker faker = new Faker();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            Contact contact = new Contact();
            contact.setFirstName(faker.name().firstName());
            contact.setLastName(faker.name().lastName());
            contact.setEmail(faker.internet().emailAddress());
            contact.setPhone(faker.phoneNumber().phoneNumber());
            Lead lead = new Lead();
            lead.setId(random.nextLong(100)+1);
            contact.setLead(lead);

            contacts.add(contact);
        }

        return contacts;
    }
}
