package com.hemendra.llmsql.controller;

import com.hemendra.llmsql.service.CampaignService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/campaign")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PostMapping
    public String addCampaign(@RequestParam int count) {
        campaignService.generateAndSaveLead(count);
        return "success, created campaign with " + count + " records";
    }
}
