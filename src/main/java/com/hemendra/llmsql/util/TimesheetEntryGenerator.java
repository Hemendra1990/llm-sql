package com.hemendra.llmsql.util;

import com.github.javafaker.Faker;
import com.hemendra.llmsql.entity.BaseEntity;
import com.hemendra.llmsql.entity.User;
import com.hemendra.llmsql.entity.timesheet.*;
import com.hemendra.llmsql.repository.UserRepository;
import com.hemendra.llmsql.repository.timesheet.JobCategoryRepository;
import com.hemendra.llmsql.repository.timesheet.ProjectRepository;
import com.hemendra.llmsql.repository.timesheet.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimesheetEntryGenerator {
    private final Faker faker = new Faker();
    private final Random random = new Random();

    private final ProjectRepository projectRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final UserRepository userRepository;
    private final TimesheetRepository timesheetRepository;

    private static final List<String> TIMESHEET_STATUSES = Arrays.asList(
            "Draft", "Submitted", "Approved", "Rejected");
    private static final List<String> BILLABLE_STATUSES = Arrays.asList(
            "Billable", "NonBillable");

    // Helper method to populate base entity fields
    // Helper method to populate base entity fields
    private void populateBaseEntity(BaseEntity entity, User user) {
        entity.setCreatedBy(user.getId());
        entity.setUpdatedBy(user.getId());

        // For Timesheet entity
        if (entity instanceof Timesheet timesheet) {
            entity.setCreatedOn(timesheet.getStartDate());
            entity.setUpdatedOn(timesheet.getEndDate());
        }
        // For TimeEntry entity
        else if (entity instanceof TimeEntry timeEntry) {
            entity.setCreatedOn(timeEntry.getTimesheet().getStartDate());
            entity.setUpdatedOn(timeEntry.getTimesheet().getEndDate());
        }
        // For JobEntry entity
        else if (entity instanceof JobEntry jobEntry) {
            entity.setCreatedOn(jobEntry.getTimeEntry().getTimesheet().getStartDate());
            entity.setUpdatedOn(jobEntry.getTimeEntry().getTimesheet().getEndDate());
        }

        entity.setIsActive(true);
        entity.setIsDeleted(false);
    }

    // Main method to generate weekly timesheets for all users
    public List<Timesheet> generateWeeklyTimesheets(List<User> users, int weeksBack) {
        // Get active projects and job categories
        List<Project> activeProjects = projectRepository.findAllByProjectStatus("Active");
        List<JobCategory> jobCategories = jobCategoryRepository.findAllByStatus(true);

        if (activeProjects.isEmpty() || jobCategories.isEmpty()) {
            throw new IllegalStateException("No active projects or job categories found.");
        }

        // Calculate week start and end dates
        OffsetDateTime weekStart = OffsetDateTime.now()
                .minusWeeks(weeksBack)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        OffsetDateTime weekEnd = weekStart.plusDays(6)
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);

        // Check for existing timesheets in this week
        List<User> usersWithoutTimesheet = new ArrayList<>();
        for (User user : users) {
            boolean hasTimesheet = hasTimesheetInWeek(user, weekStart, weekEnd);
            if (!hasTimesheet) {
                usersWithoutTimesheet.add(user);
            }
        }

        log.info("Found {} users without timesheet for week starting {}",
                usersWithoutTimesheet.size(),
                weekStart.toLocalDate());

        // Generate timesheet for eligible users
        return usersWithoutTimesheet.stream()
                .map(user -> generateUserTimesheet(user, weekStart, weekEnd, activeProjects, jobCategories))
                .collect(Collectors.toList());
    }

    private boolean hasTimesheetInWeek(User user, OffsetDateTime weekStart, OffsetDateTime weekEnd) {
        boolean exists = timesheetRepository.existsBySubmittedByAndDateBetween(
                user.getId(),
                weekStart,
                weekEnd);
        return exists;
    }

    private Timesheet generateUserTimesheet(User submitter, OffsetDateTime startDate,
                                            OffsetDateTime endDate, List<Project> projects, List<JobCategory> jobCategories) {
        Timesheet timesheet = new Timesheet();
        //timesheet.setId(faker.numerify("TS###"));
        timesheet.setStartDate(startDate);
        timesheet.setEndDate(endDate);
        timesheet.setSubmittedBy(submitter);

        // Set approvers from user's project managers
        /*List<User> projectManagers = projects.stream()
                .map(Project::getManager)
                .distinct()
                .limit(2)
                .collect(Collectors.toList());
        timesheet.setApproverList(projectManagers);*/

        // Set initial status as Draft or Submitted
        timesheet.setStatus(random.nextBoolean() ? "Draft" : "Submitted");

        // If submitted, randomly set as Approved or Rejected
        if (timesheet.getStatus().equals("Submitted") && random.nextBoolean()) {
            timesheet.setStatus(random.nextBoolean() ? "Approved" : "Rejected");
            timesheet.setApproveComment(faker.lorem().sentence());
        }

        timesheet.setSubmittedHours(0.0); // Will be calculated from entries
        timesheet.setApprovedHours(0.0); // Will be calculated from entries

        // Set submitted date based on status
        if (!timesheet.getStatus().equals("Draft")) {
            timesheet.setSubmittedDate(endDate.plusDays(random.nextInt(3))); // Submit within 3 days after week ends
        }

        // Generate daily entries
        List<TimeEntry> dailyEntries = generateWeeklyTimeEntries(timesheet, projects, jobCategories);
        timesheet.setDailyTimeEntries(dailyEntries);

        // Now set approvers based on the projects user has worked on
        setApproversBasedOnTimeEntries(timesheet);

        // Calculate total hours
        double totalHours = dailyEntries.stream()
                .flatMap(te -> te.getTaskEntries().stream())
                .mapToDouble(JobEntry::getHoursSpent)
                .sum();

        if (totalHours < 40.0) {
            // Add additional hours to reach minimum 40
            addAdditionalHours(timesheet, 40.0 - totalHours, projects, jobCategories);
            totalHours = 40.0;
        }

        timesheet.setSubmittedHours(totalHours);
        timesheet.setApprovedHours(timesheet.getStatus().equals("Approved") ? totalHours : 0.0);

        //populateBaseEntity(timesheet);
        populateBaseEntity(timesheet, submitter);
        return timesheet;
    }

    private void addAdditionalHours(Timesheet timesheet, double hoursNeeded, List<Project> projects, List<JobCategory> jobCategories) {
        // Get user's projects
        User submitter = timesheet.getSubmittedBy();
        List<Project> userProjects = projects.stream()
                .filter(project -> project.getTeamMembers().contains(submitter))
                .collect(Collectors.toList());

        // Find the last weekday time entry or create a new one if needed
        TimeEntry lastEntry = timesheet.getDailyTimeEntries().stream()
                .filter(entry -> entry.getDate().getDayOfWeek() != DayOfWeek.SATURDAY
                        && entry.getDate().getDayOfWeek() != DayOfWeek.SUNDAY)
                .reduce((first, second) -> second)
                .orElseGet(() -> {
                    // Create new time entry for Friday if no weekday entry exists
                    OffsetDateTime lastWeekday = timesheet.getEndDate();
                    while (lastWeekday.getDayOfWeek() == DayOfWeek.SATURDAY
                            || lastWeekday.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        lastWeekday = lastWeekday.minusDays(1);
                    }
                    return generateTimeEntry(timesheet, lastWeekday, userProjects, jobCategories);
                });

        // Calculate remaining hours available in the day
        double currentDayHours = lastEntry.getTaskEntries().stream()
                .mapToDouble(JobEntry::getHoursSpent)
                .sum();

        if (currentDayHours + hoursNeeded > 8.0) {
            // Need to distribute hours across multiple days
            distributeRemainingHours(timesheet, hoursNeeded, userProjects, jobCategories);
        } else {
            // Can add all remaining hours to the last entry
            JobEntry additionalEntry = generateJobEntry(lastEntry, userProjects, jobCategories, hoursNeeded);
            lastEntry.getTaskEntries().add(additionalEntry);
        }
    }

    private void distributeRemainingHours(Timesheet timesheet, double hoursNeeded,
                                          List<Project> userProjects, List<JobCategory> jobCategories) {
        // Get all weekday entries ordered by date
        List<TimeEntry> weekdayEntries = timesheet.getDailyTimeEntries().stream()
                .filter(entry -> entry.getDate().getDayOfWeek() != DayOfWeek.SATURDAY
                        && entry.getDate().getDayOfWeek() != DayOfWeek.SUNDAY)
                .sorted(Comparator.comparing(TimeEntry::getDate))
                .collect(Collectors.toList());

        double remainingHours = hoursNeeded;

        for (TimeEntry entry : weekdayEntries) {
            if (remainingHours <= 0) break;

            // Calculate how many hours can be added to this day
            double currentDayHours = entry.getTaskEntries().stream()
                    .mapToDouble(JobEntry::getHoursSpent)
                    .sum();

            double availableHours = Math.min(8.0 - currentDayHours, remainingHours);

            if (availableHours > 0) {
                // Add a job entry for the available hours
                JobEntry additionalEntry = generateJobEntry(entry, userProjects, jobCategories, availableHours);
                entry.getTaskEntries().add(additionalEntry);
                remainingHours -= availableHours;
            }
        }

        // If still have remaining hours and need more days
        if (remainingHours > 0) {
            // Create new entries for remaining workdays if needed
            OffsetDateTime currentDate = timesheet.getStartDate();
            while (remainingHours > 0 && !currentDate.isAfter(timesheet.getEndDate())) {
                if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY
                        && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    // Check if entry already exists for this date
                    OffsetDateTime finalCurrentDate = currentDate;
                    boolean entryExists = weekdayEntries.stream()
                            .anyMatch(e -> e.getDate().toLocalDate().equals(finalCurrentDate.toLocalDate()));

                    if (!entryExists) {
                        TimeEntry newEntry = generateTimeEntry(timesheet, currentDate, userProjects, jobCategories);
                        double hoursForDay = Math.min(8.0, remainingHours);
                        JobEntry jobEntry = generateJobEntry(newEntry, userProjects, jobCategories, hoursForDay);
                        newEntry.setTaskEntries(List.of(jobEntry));
                        timesheet.getDailyTimeEntries().add(newEntry);
                        remainingHours -= hoursForDay;
                    }
                }
                currentDate = currentDate.plusDays(1);
            }
        }
    }
    private void setApproversBasedOnTimeEntries(Timesheet timesheet) {
        // Get unique projects from all job entries
        List<Project> projectsWorkedOn = timesheet.getDailyTimeEntries().stream()
                .flatMap(timeEntry -> timeEntry.getTaskEntries().stream())
                .map(JobEntry::getProject)
                .distinct()
                .collect(Collectors.toList());

        // Get managers of these projects as approvers
        List<User> approvers = projectsWorkedOn.stream()
                .map(Project::getManager)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        timesheet.setApproverList(approvers);
    }

    private List<TimeEntry> generateWeeklyTimeEntries(Timesheet timesheet,
                                                      List<Project> projects, List<JobCategory> jobCategories) {
        List<TimeEntry> entries = new ArrayList<>();
        OffsetDateTime currentDate = timesheet.getStartDate();

        while (!currentDate.isAfter(timesheet.getEndDate())) {
            // Skip weekends
            if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                // 90% chance of having entries for a workday
                if (random.nextDouble() <= 0.9) {
                    TimeEntry entry = generateTimeEntry(timesheet, currentDate, projects, jobCategories);
                    entries.add(entry);
                }
            }
            currentDate = currentDate.plusDays(1);
        }

        return entries;
    }

    private TimeEntry generateTimeEntry(Timesheet timesheet, OffsetDateTime date,
                                        List<Project> projects, List<JobCategory> jobCategories) {
        TimeEntry entry = new TimeEntry();
        //entry.setId(faker.numerify("TE###"));
        entry.setDate(date);
        entry.setStatus(timesheet.getStatus());
        entry.setTimesheet(timesheet);

        // Generate 1-3 job entries per day
        int numEntries = faker.number().numberBetween(1, 4);
        List<JobEntry> jobEntries = new ArrayList<>();

        // Track remaining hours for the day
        double remainingHours = 8.0; // Standard 8-hour workday

        for (int i = 0; i < numEntries && remainingHours > 0; i++) {
            JobEntry jobEntry = generateJobEntry(entry, projects, jobCategories, remainingHours);
            remainingHours -= jobEntry.getHoursSpent();
            jobEntries.add(jobEntry);
        }

        entry.setTaskEntries(jobEntries);
        populateBaseEntity(entry, timesheet.getSubmittedBy());
        return entry;
    }

    private JobEntry generateJobEntry(TimeEntry timeEntry, List<Project> projects,
                                      List<JobCategory> jobCategories, double maxHours) {
        JobEntry entry = new JobEntry();
        //entry.setId(faker.numerify("JE###"));

        // Validate projects list
        if (projects == null || projects.isEmpty()) {
            throw new IllegalStateException("No active projects available for job entry creation");
        }

        // Set work hours between 9 AM and 5 PM
        int startHour = faker.number().numberBetween(9, 16); // Leave room for duration
        OffsetDateTime startTime = timeEntry.getDate()
                .withHour(startHour)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        // Calculate duration (between 1 and maxHours, rounded to 0.5)
        double duration = Math.min(
                Math.round(faker.number().randomDouble(1, 1, (int) maxHours) * 2) / 2.0,
                maxHours
        );

        entry.setStartTime(startTime);
        entry.setEndTime(startTime.plusMinutes((long)(duration * 60)));
        entry.setHoursSpent(duration);

        // Get projects where user is a team member
        User submitter = timeEntry.getTimesheet().getSubmittedBy();
        List<Project> userProjects = projects.stream()
                .filter(p -> p.getTeamMembers().contains(submitter))
                .collect(Collectors.toList());
        Project project;
        if (userProjects.isEmpty()) {
            // Assign user to random projects (2-3 projects)
            int numProjects = faker.number().numberBetween(2, 4);
            List<Project> assignedProjects = new ArrayList<>();

            for (int i = 0; i < numProjects && i < projects.size(); i++) {
                // Get random project
                Project randomProject = projects.get(random.nextInt(projects.size()));
                if (!assignedProjects.contains(randomProject)) {
                    // Add user to project's team members
                    List<User> teamMembers = new ArrayList<>(randomProject.getTeamMembers());
                    teamMembers.add(submitter);
                    randomProject.setTeamMembers(teamMembers);

                    // Save updated project
                    projectRepository.save(randomProject);
                    assignedProjects.add(randomProject);

                    log.info("Assigned user {} to project {}", submitter.getUsername(), randomProject.getName());
                }
            }

            // Use one of the newly assigned projects
            project = assignedProjects.get(0);
            log.info("User {} was not assigned to any projects. Assigned to {} projects",
                    submitter.getUsername(), assignedProjects.size());
        } else {
            // Use existing project assignment
            project = userProjects.get(random.nextInt(userProjects.size()));
        }
        JobCategory category = jobCategories.get(random.nextInt(jobCategories.size()));

        // Get a random job from the category's job list
        List<TimesheetJob> categoryJobs = category.getJobList();
        TimesheetJob job = categoryJobs.get(random.nextInt(categoryJobs.size()));

        entry.setTimeEntry(timeEntry);
        entry.setTimesheetJob(job);
        entry.setProject(project);

        // Generate work details
        entry.setComment(generateWorkComment(project, job));
        entry.setWorkItem(generateWorkDescription(project, job));

        // Set billable status based on project type
        entry.setBillableStatus(project.getProjectType().equals("Billable") ? "Billable" : "NonBillable");
        entry.setStatus(timeEntry.getStatus());

        populateBaseEntity(entry, timeEntry.getTimesheet().getSubmittedBy());
        return entry;
    }

    private String generateWorkComment(Project project, TimesheetJob job) {
        List<String> commentTemplates = Arrays.asList(
                "Working on %s for %s",
                "Completed %s tasks for %s",
                "Progress on %s activities in %s",
                "%s work for %s project"
        );
        String template = commentTemplates.get(random.nextInt(commentTemplates.size()));
        return String.format(template, job.getName(), project.getName());
    }

    private String generateWorkDescription(Project project, TimesheetJob job) {
        StringBuilder description = new StringBuilder();
        description.append(faker.lorem().paragraph(1))
                .append("\n\nKey activities:\n");

        // Add 2-3 random activities
        int activities = faker.number().numberBetween(2, 4);
        for (int i = 0; i < activities; i++) {
            description.append("- ").append(faker.lorem().sentence()).append("\n");
        }

        return description.toString();
    }
}