package com.bassure.applicantservice.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ats.tenentmanagement")
@EnableConfigurationProperties
@Getter
@Setter
public class TenentServerRequestUrl {

    private String tenantServiceUrl;
    private String viewOneEmployee;
    private String getIdByRole;
    private String getEmpByRole;
    private String role;
    private String recruiter;
    private String interviewer;
    private String getEmployeebyIds;
    private String ids;

}
