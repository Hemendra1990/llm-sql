package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.User;
import com.hemendra.llmsql.entity.employee.EmployeeInfo;
import com.hemendra.llmsql.entity.employee.Experience;
import com.hemendra.llmsql.entity.employee.Position;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class EmployeeDataGenerator {
    private final Faker faker;
    private final Random random;

    public EmployeeDataGenerator() {
        this.faker = new Faker();
        this.random = new Random();
    }

    public List<Position> generatePredefinedPositions() {
        List<Position> positions = new ArrayList<>();
        String[] positionNames = {
            "Software Engineer", 
            "Senior Software Engineer",
            "Tech Lead",
            "Project Manager",
            "Product Manager",
            "Business Analyst",
            "Quality Analyst",
            "DevOps Engineer",
            "System Architect",
            "Data Scientist"
        };

        for (String positionName : positionNames) {
            Position position = new Position();
            position.setName(positionName);
            positions.add(position);
        }

        return positions;
    }

    public EmployeeInfo generateEmployeeInfo(User user, List<Position> availablePositions) {
        EmployeeInfo employeeInfo = new EmployeeInfo();
        
        // Set basic info from user
        employeeInfo.setEmployeeId("EMP" + faker.number().digits(6));
        employeeInfo.setName(user.getFirstName() + " " + user.getLastName());
        employeeInfo.setContact(user.getMobileNumber());
        employeeInfo.setEmailId(user.getEmail());
        employeeInfo.setIsActive(true);
        employeeInfo.setUser(user);

        // Generate 1-4 experiences
        int numberOfExperiences = random.nextInt(3) + 1;
        List<Experience> experiences = generateExperiences(numberOfExperiences, employeeInfo, availablePositions);
        employeeInfo.setExperiences(experiences);

        return employeeInfo;
    }

    private List<Experience> generateExperiences(int count, EmployeeInfo employeeInfo, List<Position> availablePositions) {
        List<Experience> experiences = new ArrayList<>();
        LocalDateTime currentDate = LocalDateTime.now();
        
        for (int i = 0; i < count; i++) {
            Experience experience = new Experience();
            experience.setEmployeeInfo(employeeInfo);
            experience.setCompanyName(faker.company().name());
            experience.setPosition(availablePositions.get(random.nextInt(availablePositions.size())));
            
            // Generate description
            String responsibilities = String.join(". ", 
                faker.job().keySkills(),
                faker.job().keySkills(),
                faker.job().keySkills()
            );
            experience.setDescription("Worked on " + faker.commerce().productName() + 
                ". Responsibilities included: " + responsibilities);
            
            // Calculate dates
            LocalDateTime startDate = currentDate.minusYears(random.nextInt(2) + 1)
                                               .minusMonths(random.nextInt(11));
            
            // For the most recent experience (i==0), end date might be null
            LocalDateTime endDate = (i == 0 && random.nextBoolean()) ? 
                null : 
                startDate.plusMonths(random.nextInt(20) + 4);
            
            // Update currentDate for next iteration
            currentDate = startDate;
            
            experience.setStartDate(OffsetDateTime.of(startDate, ZoneOffset.UTC));
            if (endDate != null) {
                experience.setEndDate(OffsetDateTime.of(endDate, ZoneOffset.UTC));
            }
            
            experience.setSortOrder((double) (count - i));  // Reverse order for sorting
            
            experiences.add(experience);
        }
        
        return experiences;
    }
}