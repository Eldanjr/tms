package com.bassure.applicantservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillRequest {

    @NotBlank(message = "skill name cannot be blank")
    private String name;

    @NotNull(message = "skill description cannot be blank")
    private String description;

}
