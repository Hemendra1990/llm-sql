package com.hemendra.llmsql.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {
    /*private final OllamaAiService ollamaAiService;

    @GetMapping("/generate-sql")
    public String generateSql(@RequestParam String query) {
        String sql = ollamaAiService.generateSql(query);
        return sql;
    }

    @GetMapping("/generate-sql-json")
    public LLMResponseDto generateSqlJson(@RequestParam String query) {
        LLMResponseDto llmResponseDto = ollamaAiService.generateSqlJson(query);
        return llmResponseDto;
    }

    @GetMapping("/generate-sql-result")
    public LLMResponseDto generateSqlJsonResult(@RequestParam String query) {
        LLMResponseDto llmResponseDto = ollamaAiService.generateSqlJsonResult(query);
        return llmResponseDto;
    }*/


}
