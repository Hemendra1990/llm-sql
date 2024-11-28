package com.hemendra.llmsql.service;

import com.hemendra.llmsql.entity.gen.Contact;
import com.hemendra.llmsql.repository.ContactRepository;
import com.hemendra.llmsql.util.ContactDataGenerator;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ContactService {
    private final ContactDataGenerator contactDataGenerator;

    @Autowired
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 10000;

    public void addContact(int count) {
        long start = System.currentTimeMillis();
        List<Contact> contacts = contactDataGenerator.generateContacts(count);
        long end = System.currentTimeMillis();
        log.info("Generated {} contacts in {} ms", contacts.size(), end - start);
        saveContactsInBatch(contacts);
        log.info("Saved {} contacts in {} ms", contacts.size(), System.currentTimeMillis() - end);
    }

    @Transactional
    public void saveContactsInBatch(List<Contact> contacts) {
        int count = 0;
        for (Contact contact : contacts) {
            entityManager.persist(contact);
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
