package com.hemendra.llmsql.repository.employee;

import com.hemendra.llmsql.entity.employee.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, String> {
}
