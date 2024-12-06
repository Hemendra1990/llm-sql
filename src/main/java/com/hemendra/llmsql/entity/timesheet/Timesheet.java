package com.hemendra.llmsql.entity.timesheet;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hemendra.llmsql.entity.BaseEntity;
import com.hemendra.llmsql.entity.User;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "ts_timesheet")
@Getter
@Setter
public class Timesheet extends BaseEntity {
    @Id
    @Tsid
    @Column(length = 50)
    private String id;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;

    @OneToMany(mappedBy = "timesheet", cascade = CascadeType.ALL)
    private List<TimeEntry> dailyTimeEntries;

    @ManyToOne
    @JoinColumn(name = "submitted_by_id")
    private User submittedBy;

    //@JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "ts_timesheet_approver_xref",
            joinColumns = @JoinColumn(name = "timesheet_id"),
            inverseJoinColumns = @JoinColumn(name = "approver_id")
    )
    private List<User> approverList;

    private String status;
    private Double submittedHours;
    private Double approvedHours;
    private String approveComment;
    private OffsetDateTime submittedDate;

}
