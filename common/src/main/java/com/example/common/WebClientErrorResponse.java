package com.example.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebClientErrorResponse {
    private String statusCode;
    private String message;
    private List<String> errors;
}
