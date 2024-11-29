package com.hemendra.llmsql.entity.employee;

import com.hemendra.llmsql.entity.User;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "em_employee_info", schema = "client1")
@Getter
@Setter
public class EmployeeInfo {

    @Id
    @Tsid
    @Column(length = 50)
    private String id;

    @Column(name = "employee_id", unique = true)
    private String employeeId;

    @Column(name = "employee_name")
    private String name;

    @Column(name = "employee_contact")
    private String contact;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "employeeInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Experience> experiences = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
