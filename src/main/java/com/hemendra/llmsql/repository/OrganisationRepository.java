package com.hemendra.llmsql.repository;

import com.hemendra.llmsql.entity.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganisationRepository extends JpaRepository<Organisation, String> {
    Optional<Organisation> findByNameIgnoreCase(String name);
}
