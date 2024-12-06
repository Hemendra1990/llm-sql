package com.hemendra.llmsql.entity.timesheet;

import com.hemendra.llmsql.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "ts_job_entry", schema = "client1")
@Getter
@Setter
public class JobEntry extends BaseEntity {
    @Id
    @Tsid
    @Column(length = 50)
    private String id;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Double hoursSpent;
    @ManyToOne
    @JoinColumn(name = "time_entry_id")
    private TimeEntry timeEntry;
    @ManyToOne
    @JoinColumn(name = "job_id")
    private TimesheetJob timesheetJob;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private String comment;
    @Column(columnDefinition = "text")
    private String workItem;
    private String billableStatus;
    private String status;
}
