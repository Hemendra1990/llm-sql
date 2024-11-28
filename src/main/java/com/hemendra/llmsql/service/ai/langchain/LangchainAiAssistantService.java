package com.hemendra.llmsql.service.ai.langchain;


import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface LangchainAiAssistantService {
    @SystemMessage("You are a polite assistant")
    String chat(String userMessage);
}
