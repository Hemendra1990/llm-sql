package com.hemendra.llmsql.controller;

import com.hemendra.llmsql.service.ai.langchain.LangchainAiAssistantService;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/langchain-ai")
@RequiredArgsConstructor
public class LangChainAiController {
    private final LangchainAiAssistantService langchainAiAssistantService;

    @GetMapping
    public String getSql(@RequestParam String query) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        //"jdbc:postgresql://14.141.154.146:5433/44_crm", "postgres", "B6afIH?C+#W6Wrl@ED1@"
        hikariDataSource.setJdbcUrl("jdbc:postgresql://14.141.154.146:5433/44_crm");
        hikariDataSource.setUsername("postgres");
        hikariDataSource.setPassword("B6afIH?C+#W6Wrl@ED1@");

        String chat = langchainAiAssistantService.chat(query);
        return chat;
    }
}
