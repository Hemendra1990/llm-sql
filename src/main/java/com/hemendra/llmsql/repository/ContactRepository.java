package com.hemendra.llmsql.repository;


import com.hemendra.llmsql.entity.gen.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
