package com.hemendra.llmsql.repository;

import com.hemendra.llmsql.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignationRepository extends JpaRepository<Designation, String> {
}
