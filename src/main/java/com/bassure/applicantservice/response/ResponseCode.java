package com.bassure.applicantservice.response;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ats.codes")
@EnableConfigurationProperties
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCode {

    private String success;
    private String failed;
    private String alreadyExists;
    private String notValid;
    private String notFound;
    private String serverError;
    private String constraintError;
    private String noChangeRequired;

}
