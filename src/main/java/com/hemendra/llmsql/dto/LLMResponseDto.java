package com.hemendra.llmsql.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LLMResponseDto {
    private String sqlQuery;private String summary;private List<Map<String, Object>> data;
}
