package com.hemendra.llmsql.repository.timesheet;

import com.hemendra.llmsql.entity.User;
import com.hemendra.llmsql.entity.timesheet.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, String> {
    @Query(value = """
            SELECT 
                CASE 
                    WHEN COUNT(*) > 0 THEN true 
                    ELSE false 
                END
            FROM ts_timesheet 
            WHERE submitted_by_id = :userId 
            AND DATE(start_date AT TIME ZONE 'UTC') 
                BETWEEN DATE(:startDate AT TIME ZONE 'UTC') 
                AND DATE(:endDate AT TIME ZONE 'UTC')
            """, nativeQuery = true)
    boolean existsBySubmittedByAndDateBetween(
            @Param("userId") String userId,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate);
}