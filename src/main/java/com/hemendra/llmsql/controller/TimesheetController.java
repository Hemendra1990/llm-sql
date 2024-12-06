package com.hemendra.llmsql.controller;

import com.hemendra.llmsql.service.TimesheetConfigService;
import com.hemendra.llmsql.service.TimesheetEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/timesheet")
@RequiredArgsConstructor
public class TimesheetController {
    private final TimesheetConfigService timesheetConfigService;
    private final TimesheetEntryService timesheetEntryService;
    @PostMapping("/config")
    public ResponseEntity setupTimesheetConfig(@RequestParam int count) {
        timesheetConfigService.setupConfig(count);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/entry")
    public ResponseEntity generateWeeklyTimesheets(@RequestParam(defaultValue = "0") int weeksBack) {
        timesheetEntryService.generateWeeklyTimesheets(weeksBack);
        return ResponseEntity.ok().build();
    }
}
