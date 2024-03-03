package com.bassure.applicantservice.response;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ApiResponse {

    private String statusCode;
    private Object value;
    private Map<String, String> errors;

}
