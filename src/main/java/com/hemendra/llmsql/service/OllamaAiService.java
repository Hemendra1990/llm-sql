/*
package com.hemendra.llmsql.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hemendra.llmsql.dto.LLMResponseDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OllamaAiService {
    private final ChatClient.Builder chatClientBuilder;
    private final DbMetadataService dbMetadataService;
    private final ChatClient chatClient;
    private String databaseSchemaJson;

    private final JdbcClient jdbcClient;

    public OllamaAiService(ChatClient.Builder chatClientBuilder, DbMetadataService dbMetadataService, JdbcClient jdbcClient) {
        this.chatClientBuilder = chatClientBuilder;
        this.dbMetadataService = dbMetadataService;
        this.jdbcClient = jdbcClient;

        this.chatClient = this.chatClientBuilder
                .defaultSystem(systemPrompt)
                .build();
    }

    String systemPrompt = """
            You are an expert database assistant and SQL generator. Your task is to:
            1. Understand the schema of a relational database based on the provided schema description.
            2. Convert natural language queries into accurate and efficient SQL queries that match the schema.
            3. Ensure the generated SQL is optimized and safe, avoiding destructive operations like `DROP`, `DELETE`, or `UPDATE` unless explicitly specified.
            4. If the user asks for a result summary, summarize the query results in simple terms.
            
            Schema information will be provided to you before the query. Assume the database is PostgreSQL.
            
            When you generate SQL queries:
            - Use valid PostgreSQL syntax.
            - Avoid returning excessively large datasets by including reasonable `LIMIT` clauses where applicable.
            - Include filters or conditions based on the user's query.
            
            When summarizing query results:
            - Focus on key insights from the data.
            - Keep the explanation concise and easy to understand.
            
            Respond only in JSON format with the following structure:
            {
              "sqlQuery": "<Generated SQL Query>",
              "summary": "<Optional Summary or Result Processing Instructions>"
            }
            
            """;

    @PostConstruct
    public void init() {
        this.databaseSchemaJson = dbMetadataService.generateDatabaseSchemaJson();

        */
/*try {
            String dbSchema14 = dbMetadataService.generate14DatabaseSchemaJson();
            FileOutputStream fileOutputStream = new FileOutputStream(new File("dbSchema14.json"));
            fileOutputStream.write(dbSchema14.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.close();
            log.info(dbSchema14);
            log.info("Initialization complete.");
        } catch (SQLException e) {
            log.error(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*//*

        log.info("Database schema json: {}", databaseSchemaJson);
    }

    public String generateSql(String query) {
        String content = this.chatClient.prompt()
                .user("Schema: " + this.databaseSchemaJson + " Query: " + query).call().content();

        return content;
    }

    public LLMResponseDto generateSqlJson(String query) {
        String content = this.chatClient.prompt()
                .user("Schema: " + this.databaseSchemaJson + " Query: " + query).call().content();
        ObjectMapper mapper = new ObjectMapper();
        try {
            LLMResponseDto llmResponseDto = mapper.readValue(content, LLMResponseDto.class);
            return llmResponseDto;
        } catch (JsonProcessingException e) {
        }

        return null;
    }

    public LLMResponseDto generateSqlJsonResult(String query) {
        String content = this.chatClient.prompt()
                .user("Schema: " + this.databaseSchemaJson + " Query: " + query).call().content();
        ObjectMapper mapper = new ObjectMapper();
        try {
            LLMResponseDto llmResponseDto = mapper.readValue(content, LLMResponseDto.class);

            String sqlQuery = llmResponseDto.getSqlQuery();
            log.info("SQL query: {}", sqlQuery);
            List<Map<String, Object>> listedOfRows = jdbcClient.sql(sqlQuery).query().listOfRows();
            llmResponseDto.setData(listedOfRows);

            return llmResponseDto;
        } catch (JsonProcessingException e) {
        }

        return null;
    }




}
*/
