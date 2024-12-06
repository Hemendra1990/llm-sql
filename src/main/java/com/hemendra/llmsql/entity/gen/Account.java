package com.hemendra.llmsql.entity.gen;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Tsid
    private String id;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_on")
    private OffsetDateTime createdOn;

    @Column(name = "organisation_id", length = 50)
    private String organisationId;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "updated_on")
    private OffsetDateTime updatedOn;

    @Column(name = "account_number")
    private Long accountNumber;

    @Column(name = "account_site")
    private String accountSite;

    @Column(name = "annual_revenue")
    private Long annualRevenue;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "state")
    private String state;

    @Column(name = "street")
    private String street;

    @Column(name = "contact_is_inactive")
    private Boolean contactIsInactive;

    @Column(name = "contact_role")
    private String contactRole;

    @Column(name = "description")
    private String description;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "fax")
    private String fax;

    @Column(name = "industry")
    private String industry;

    @Column(name = "name")
    private String name;

    @Column(name = "no_of_employee")
    private Integer noOfEmployee;

    @Column(name = "ownership")
    private String ownership;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "rating")
    private String rating;

    @Column(name = "shipping_address_city")
    private String shippingAddressCity;

    @Column(name = "shipping_address_country")
    private String shippingAddressCountry;

    @Column(name = "shipping_address_postal_code")
    private String shippingAddressPostalCode;

    @Column(name = "shipping_address_state")
    private String shippingAddressState;

    @Column(name = "shipping_address_street")
    private String shippingAddressStreet;

    @Column(name = "sic_code")
    private String sicCode;

    @Column(name = "ticker_symbol")
    private String tickerSymbol;

    @Column(name = "type")
    private String type;

    @Column(name = "website")
    private String website;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}