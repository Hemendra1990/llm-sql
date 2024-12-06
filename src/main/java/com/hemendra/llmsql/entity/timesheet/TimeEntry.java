package com.hemendra.llmsql.entity.timesheet;

import com.hemendra.llmsql.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "ts_time_entry")
@Getter
@Setter
public class TimeEntry extends BaseEntity {
    @Id
    @Tsid
    @Column(length = 50)
    private String id;
    private OffsetDateTime date;

    @OneToMany(mappedBy = "timeEntry", cascade = CascadeType.ALL)
    private List<JobEntry> taskEntries;
    private String status;

    @ManyToOne
    @JoinColumn(name = "timesheet_id")
    private Timesheet timesheet;
}
