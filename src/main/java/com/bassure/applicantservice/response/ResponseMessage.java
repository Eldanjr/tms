package com.bassure.applicantservice.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ats.response")
@EnableConfigurationProperties
@Getter
@Setter
public class ResponseMessage {

    private String success;
    private String error;
    private String errorKey;
    private String noDataFound;
    private String skillNotFound;
    private String serverError;
    private String addMessage;
    private String editMessage;
    private String deleteMessage;
    private String alreadyExists;
    private String message;
    private String reschedule;
    private String updationFailed;
    private String noInterviewScheduledPreviously;
    private String applicationServiceContactUrl;
    private String applicantServiceSingleEmpUrl;
    private String applicantServiceBaseEmailUrl;
    private String otpUrl;
    private String tenantBaseUrl;

}
