package com.bassure.applicantservice.SecurityConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String ADMIN = "admin";
    public static final String RECRUITER = "recruiter";
    public static final String INTERVIEWER = "interviewer";

    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configure(http));
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/applicant-service/api/admin/**").hasAuthority(ADMIN)
                .requestMatchers("/applicant-service/api/recruiter/**").hasAuthority(RECRUITER)
                .requestMatchers("/applicant-service/api/interviewers/**").hasAuthority(INTERVIEWER)
                .requestMatchers("/applicant-service/api/employee/all/**").hasAnyAuthority(ADMIN, RECRUITER, INTERVIEWER)
                .requestMatchers("/applicant-service/api/employee/all/view-my-profile/**").hasAnyAuthority(ADMIN, RECRUITER, INTERVIEWER)
                .requestMatchers("/applicant-service/api/employee/ar/**").hasAnyAuthority(ADMIN, RECRUITER)
                .requestMatchers("/applicant-service/api/employee/ai/**").hasAnyAuthority(ADMIN, INTERVIEWER)
                .requestMatchers("/applicant-service/api/employee/ri/**").hasAnyAuthority(RECRUITER, INTERVIEWER)
                .requestMatchers("/applicant-service/api/update-draft/{id}").hasAuthority(ADMIN)
                .anyRequest().authenticated()
        );
//        http.csrf(csrf -> csrf.ignoringRequestMatchers("/applicant-service/api/update-draft/{id}"));
        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                        .jwtAuthenticationConverter(jwtAuthConverter)
                )
        );

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        return http.build();

    }



}
