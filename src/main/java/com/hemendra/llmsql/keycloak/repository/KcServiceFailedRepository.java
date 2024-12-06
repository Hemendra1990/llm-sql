package com.hemendra.llmsql.keycloak.repository;

import com.hemendra.llmsql.keycloak.model.KcServiceFailed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KcServiceFailedRepository extends JpaRepository<KcServiceFailed, String> {
}
