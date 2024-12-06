package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.BaseEntity;
import com.hemendra.llmsql.entity.User;
import com.hemendra.llmsql.entity.timesheet.JobCategory;
import com.hemendra.llmsql.entity.timesheet.Project;
import com.hemendra.llmsql.entity.timesheet.ProjectCategory;
import com.hemendra.llmsql.entity.timesheet.TimesheetJob;
import com.hemendra.llmsql.repository.UserRepository;
import com.hemendra.llmsql.repository.timesheet.TimesheetJobRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimesheetConfigGenerator {
    private final Faker faker = new Faker();
    private final Random random = new Random();

    private final UserRepository userRepository;

    private final TimesheetJobRepository timesheetJobRepository;

    private User adminUser; // Store admin user as a field
    
    private static final List<String> PROJECT_TYPES = Arrays.asList(
            "Billable", "NonBillable", "Internal", "Administration");
    private static final List<String> PROJECT_STATUSES = Arrays.asList(
            "Active", "InActive", "OnHold");



    private void initAdminUser() {
        // Fetch admin user once during initialization
        this.adminUser = userRepository.findFirstByRole_RoleName("Super Admin")
                .orElseThrow(() -> new IllegalStateException("No admin user found to create configuration data"));
        log.info("Initialized TimesheetConfigGenerator with admin user: {}", adminUser.getUsername());
    }

    // Helper method to generate Base Entity fields
    private void populateBaseEntity(BaseEntity entity) {
        entity.setCreatedBy(adminUser.getId());
        entity.setUpdatedBy(adminUser.getId());
        entity.setCreatedOn(OffsetDateTime.now().minusDays(faker.number().numberBetween(1, 100)));
        entity.setUpdatedOn(OffsetDateTime.now());
        entity.setIsActive(true);
        entity.setIsDeleted(false);
    }

    // Generate Project Categories
    public List<ProjectCategory> generateProjectCategories(int count) {
        initAdminUser();
        return IntStream.range(0, count)
                .mapToObj(i -> {
                    ProjectCategory category = new ProjectCategory();
                    //category.setId(faker.numerify("PC###"));
                    category.setName(faker.commerce().department());
                    category.setDisplayName(faker.commerce().department());
                    category.setStatus(true);
                    category.setDescription(faker.lorem().paragraph());
                    return category;
                })
                .collect(Collectors.toList());
    }

    // Generate Job Categories with Jobs
    public List<JobCategory> generateJobCategories(int count) {
        initAdminUser();
        return IntStream.range(0, count)
                .mapToObj(i -> {
                    JobCategory category = new JobCategory();
                    //category.setId(faker.numerify("JC###"));
                    category.setName(faker.job().field());
                    category.setDisplayName(faker.job().field());
                    category.setStatus(true);
                    
                    // Generate 3-5 jobs per category
                    List<TimesheetJob> jobs = IntStream.range(0, faker.number().numberBetween(3, 6))
                            .mapToObj(j -> generateTimesheetJob(category))
                            .collect(Collectors.toList());
                    category.setJobList(jobs);
                    
                    populateBaseEntity(category);
                    return category;
                })
                .collect(Collectors.toList());
    }

    private TimesheetJob generateTimesheetJob(JobCategory category) {
        initAdminUser();
        TimesheetJob job = new TimesheetJob();
        //job.setId(faker.numerify("TSJ###"));
        job.setName(faker.job().position());
        job.setDisplayName(faker.job().position());
        job.setStatus(true);
        job.setJobCategory(category);
        job.setIsSystemJob(faker.bool().bool());
        populateBaseEntity(job);
        return job;
    }

    // Generate Projects
    public List<Project> generateProjects(int count, List<ProjectCategory> categories) {
        initAdminUser();
        List<User> allUsers = userRepository.findByRole_RoleNameNot("Super Admin");
        List<TimesheetJob> allJobs = timesheetJobRepository.findAllByStatusTrue();

        if (allUsers.isEmpty()) {
            throw new IllegalStateException("No active users found. Please set up users first.");
        }

        if (allJobs.isEmpty()) {
            throw new IllegalStateException("No active jobs found. Please set up jobs first.");
        }
        return IntStream.range(0, count)
                .mapToObj(i -> {
                    Project project = new Project();
                    //project.setId(faker.numerify("PROJ###"));
                    project.setName(faker.app().name());
                    project.setDisplayName(faker.app().name());
                    project.setDescription(faker.lorem().paragraph());
                    project.setStartDate(OffsetDateTime.now().minusMonths(faker.number().numberBetween(1, 12)));
                    project.setEndDate(OffsetDateTime.now().plusMonths(faker.number().numberBetween(1, 12)));
                    project.setProjectStatus(PROJECT_STATUSES.get(random.nextInt(PROJECT_STATUSES.size())));
                    project.setCustomerName(faker.company().name());
                    project.setCustomerLocation(faker.address().fullAddress());
                    project.setModule(faker.app().version());
                    project.setProjectType(PROJECT_TYPES.get(random.nextInt(PROJECT_TYPES.size())));
                    project.setProjectCategory(categories.get(random.nextInt(categories.size())));
                    project.setIsSystemProject(faker.bool().bool());

                    // Assign manager - randomly select from users
                    project.setManager(allUsers.get(random.nextInt(allUsers.size())));

                    // Assign team members (3-7 random users)
                    int teamSize = faker.number().numberBetween(3, 8);
                    List<User> teamMembers = new ArrayList<>();
                    Set<Integer> selectedUserIndices = new HashSet<>();

                    // Always include the manager in the team
                    teamMembers.add(project.getManager());

                    // Add random team members
                    while (teamMembers.size() < teamSize) {
                        int randomIndex = random.nextInt(allUsers.size());
                        if (selectedUserIndices.add(randomIndex)) {
                            User teamMember = allUsers.get(randomIndex);
                            if (!teamMembers.contains(teamMember)) {
                                teamMembers.add(teamMember);
                            }
                        }
                    }
                    project.setTeamMembers(teamMembers);

                    // Assign jobs (2-5 random jobs)
                    int jobCount = faker.number().numberBetween(2, 6);
                    List<TimesheetJob> projectJobs = new ArrayList<>();
                    Set<Integer> selectedJobIndices = new HashSet<>();

                    while (projectJobs.size() < jobCount) {
                        int randomIndex = random.nextInt(allJobs.size());
                        if (selectedJobIndices.add(randomIndex)) {
                            TimesheetJob job = allJobs.get(randomIndex);
                            projectJobs.add(job);
                        }
                    }
                    project.setJobList(projectJobs);

                    populateBaseEntity(project);
                    return project;
                })
                .collect(Collectors.toList());
    }
}