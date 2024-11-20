package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class LeadDataInserter {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/mydatabase";
    private static final String USERNAME = "myusername";
    private static final String PASSWORD = "mypassword";

    public static void main(String[] args) {
        insertFakeLeads(100); // Generate and insert 100 records
    }

    public static void insertFakeLeads(int count) {
        Faker faker = new Faker();
        Random random = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String sql = "INSERT INTO lead (first_name, last_name, email, phone, address, city, state, country, zip_code, " +
                "company_name, job_title, industry, website, lead_source, lead_status, rating, annual_revenue, " +
                "number_of_employees, notes, created_date, updated_date, opt_in, preferred_language, timezone, " +
                "marketing_channel, referral_source, campaign_name, priority, assigned_to, last_contacted_date, " +
                "custom_field_1, custom_field_2) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < count; i++) {
                preparedStatement.setString(1, faker.name().firstName());
                preparedStatement.setString(2, faker.name().lastName());
                preparedStatement.setString(3, faker.internet().emailAddress());
                preparedStatement.setString(4, faker.phoneNumber().cellPhone());
                preparedStatement.setString(5, faker.address().streetAddress());
                preparedStatement.setString(6, faker.address().city());
                preparedStatement.setString(7, faker.address().stateAbbr());
                preparedStatement.setString(8, faker.address().country());
                preparedStatement.setString(9, faker.address().zipCode());
                preparedStatement.setString(10, faker.company().name());
                preparedStatement.setString(11, faker.job().title());
                preparedStatement.setString(12, faker.company().industry());
                preparedStatement.setString(13, faker.internet().url());
                preparedStatement.setString(14, faker.options().option("Website", "Referral", "Event", "Email"));
                preparedStatement.setString(15, faker.options().option("New", "Contacted", "Qualified"));
                preparedStatement.setString(16, faker.options().option("Hot", "Warm", "Cold"));
                preparedStatement.setDouble(17, random.nextDouble() * 500000 + 50000); // Random annual revenue
                preparedStatement.setInt(18, random.nextInt(500) + 1); // Random number of employees
                preparedStatement.setString(19, faker.lorem().sentence());
                preparedStatement.setString(20, LocalDateTime.now().minusDays(random.nextInt(30)).format(formatter));
                preparedStatement.setString(21, LocalDateTime.now().format(formatter));
                preparedStatement.setBoolean(22, random.nextBoolean());
                preparedStatement.setString(23, faker.options().option("English", "Spanish", "French"));
                preparedStatement.setString(24, faker.options().option("EST", "PST", "CST"));
                preparedStatement.setString(25, faker.options().option("Email", "Phone Call", "Social Media"));
                preparedStatement.setString(
