package com.bassure.applicantservice.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedBackResponse {
    private int interviewerId;
    private Map<String, Integer> questionWithRating;


}
