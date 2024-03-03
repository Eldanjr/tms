package com.bassure.applicantservice.serviceimplementation;

import com.bassure.applicantservice.repo.JobPostingRepository;
import com.bassure.applicantservice.repo.JobRecruiterRepository;
import com.bassure.applicantservice.response.ApiResponse;
import com.bassure.applicantservice.response.ResponseCode;
import com.bassure.applicantservice.response.ResponseMessage;
import com.bassure.applicantservice.service.GetRecruiterService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GetRecruiterServiceImplementation implements GetRecruiterService {

    @Autowired
    ResponseCode code;
    @Autowired
    ResponseMessage message;
    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private JobRecruiterRepository jobRecruiterRepository;

    private static final Logger logger = LoggerFactory.getLogger(GetRecruiterServiceImplementation.class);


    @Override
    public ApiResponse getRecruiterProgress(int id) {
        List<Object[]> obj = jobPostingRepository.getRecruiterProgress(id);
        if (obj.isEmpty()) return new ApiResponse(code.getNotFound(), null, Map.of("error", message.getNoDataFound()));

        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Object[] data : obj) {
            Map<String, Object> resultMap = new HashMap<>();

            resultMap.put("postingId", data[0]);
            resultMap.put("jobTitle", data[1]);
            resultMap.put("assignedCount", data[2]);
            resultMap.put("hiredCount", data[3]);

            resultList.add(resultMap);
        }

        return new ApiResponse(code.getSuccess(), resultList, null);
    }


    @Override
    public ApiResponse getRecruiterProgressOnJd(int jobId, int recruiterId) {
        List<Object[]> objects = jobPostingRepository.getRecruiterProgressOnJd(jobId, recruiterId);
        System.out.println(jobId + "job id " + recruiterId + "this is rec id");
        if (objects.isEmpty())
            return new ApiResponse(code.getNotFound(), null, Map.of("error", message.getNoDataFound()));
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Object[] data : objects) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("statusCount", data[1]);
            resultMap.put("applicantStatus", data[0]);

            resultList.add(resultMap);
        }

        return new ApiResponse(code.getSuccess(), resultList, null);
    }

    @Override
    public ApiResponse getJobByRecruiterId(int recruiterId) {
        List<Object[]> objects = jobPostingRepository.getJobByRecruiterId(recruiterId);
        if (objects.isEmpty())
            return new ApiResponse(code.getFailed(), null, Map.of("error", message.getNoDataFound()));

        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Object[] data : objects) {
            Map<String, Object> resultMap = new HashMap<>();

            resultMap.put("title", data[0]);
            resultMap.put("no_of_vacancies", data[1]);
            resultMap.put("type", data[2]);
            resultMap.put("status", data[3]);
            resultMap.put("location", data[4]);
            resultMap.put("open_date", data[5]);
            resultMap.put("close_date", data[6]);
            resultMap.put("created_by", data[7]);
            resultMap.put("jobRecruiterId", data[8]);
            resultMap.put("jobPostingId", data[9]);
            resultMap.put("recruiterId", data[10]);

            resultList.add(resultMap);
        }
        return new ApiResponse(code.getSuccess(), resultList, null);
    }

    @Override
    public ApiResponse getCandidatesByRecruiterIdAndJobId(int jobId, int recruiterId) {
        List<Object[]> candidateList = jobPostingRepository.getCandidatesByRecruiterIdAndJobId(jobId, recruiterId);
        logger.info("data: {}", candidateList);
        if (candidateList.isEmpty())
            return new ApiResponse(code.getFailed(), null, Map.of(message.getErrorKey(), message.getNoDataFound()));

        List<Map<String, Object>> candidateData = new ArrayList<>();

        for (Object[] data : candidateList) {
            Map<String, Object> resultData = new HashMap<>();

            resultData.put("applicantName", data[0]);
            resultData.put("applicantStatus", data[1]);
            resultData.put("resumePath", data[2]);
            resultData.put("jobRecruiterId", data[3]);
            resultData.put("jobPostingId", data[4]);
            resultData.put("recruiterId", data[5]);
            resultData.put("applicantDetailsId", data[6]);
            resultData.put("levelOfInterview", data[7]);
            resultData.put("feedback", data[8]);
            resultData.put("applicantId", data[9]);
            resultData.put("interviewId", data[10]);

            candidateData.add(resultData);
        }

        return new ApiResponse(code.getSuccess(), candidateData, null);
    }

    @Override
    public ApiResponse searchByNameandStatus(int recruiter, String status, String title) {
        List<Object[]> jobDetails = jobRecruiterRepository.searchByNameandStatus(recruiter, status, title);
        logger.info("data: {}", jobDetails);

        if (jobDetails.isEmpty())
            return new ApiResponse(code.getFailed(), null, Map.of(message.getErrorKey(), message.getNoDataFound()));
        List<Map<String, Object>> jobPostingDetails = new ArrayList<>();
        for (Object[] data : jobDetails) {
            Map<String, Object> jobdata = new HashMap<>();

            jobdata.put("title", data[0]);
            jobdata.put("no_of_vacancies", data[1]);
            jobdata.put("type", data[2]);
            jobdata.put("status", data[3]);
            jobdata.put("location", data[4]);
            jobdata.put("open_date", data[5]);
            jobdata.put("close_date", data[6]);
            jobdata.put("created_by", data[7]);
            jobdata.put("id ", data[8]);
            jobdata.put("jobRecruiterId", data[9]);
            jobdata.put("jobPostingId", data[10]);
            jobdata.put("recruiterId", data[11]);

            jobPostingDetails.add(jobdata);

        }
        return new ApiResponse(code.getSuccess(), jobPostingDetails, null);
    }


}

