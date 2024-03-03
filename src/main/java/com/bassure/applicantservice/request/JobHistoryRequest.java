/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bassure.applicantservice.request;

import com.bassure.applicantservice.model.JobPostingStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobHistoryRequest {

    @NotNull(message = "id is mandatory")
    private int id;
     
    @NotNull(message = "status is mandatory")
    @Enumerated(EnumType.STRING)
    private JobPostingStatus status;
   
    @NotNull(message = "modified by is mandatory")
    private int modifiedBy;
          
   @NotNull(message = "modified at is mandatory")
    private Timestamp modifiedAt;
    
   
   




}
