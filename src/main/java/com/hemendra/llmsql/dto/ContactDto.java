package com.hemendra.llmsql.dto;

import com.hemendra.llmsql.entity.Lead;
import lombok.Data;

@Data
public class ContactDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private Lead lead;
}