package com.bassure.applicantservice.serviceimplementation;

import com.bassure.applicantservice.repo.ApplicantDetailsRepository;
import com.bassure.applicantservice.repo.JobRecruiterRepository;
import com.bassure.applicantservice.response.ApiResponse;
import com.bassure.applicantservice.response.ResponseCode;
import com.bassure.applicantservice.response.ResponseMessage;
import com.bassure.applicantservice.response.TenentServerRequestUrl;
import com.bassure.applicantservice.service.TenentEmployeeService;
import com.bassure.applicantservice.util.ServiceCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TenentEmployeeServiceImplementation implements TenentEmployeeService {

    @Autowired
    ResponseMessage message;

    @Autowired
    ServiceCall client;

    @Autowired
    ResponseCode code;

    @Autowired
    TenentServerRequestUrl tenantUrl;

    @Autowired
    JobRecruiterRepository jobReqRep;

    @Autowired
    ApplicantDetailsRepository applicantDetailsRepository;

    @Override
    public ApiResponse viewProfileByEmployeeId(int employeeId) {
        ApiResponse apiResponse = new ApiResponse();
        ApiResponse apiGetResponse = null;
        Map<String, String> errors = new HashMap<>();
        try {
            apiGetResponse = client.performGetRequestWithOutQueryParams(tenantUrl.getTenantServiceUrl(), tenantUrl.getViewOneEmployee() + "/" + employeeId, ApiResponse.class);
        } catch (Exception e) {
            errors.put(message.getMessage(), code.getServerError());
            apiResponse.setStatusCode(code.getServerError());
            apiResponse.setErrors(errors);

            return apiResponse;

        }
        if (apiGetResponse == null) {
            errors.put(message.getMessage(), message.getNoDataFound());
            apiResponse.setStatusCode(code.getNotFound());
            apiResponse.setErrors(errors);
            return apiResponse;
        }

        apiResponse.setStatusCode(code.getSuccess());

        apiResponse.setValue(apiGetResponse.getValue());

        return apiResponse;

    }

    public Object getGroupOfEmployeesByIds(List<Integer> ids) {
        ApiResponse apiGetResponse = null;
        try {
            apiGetResponse = client.performGetRequestWithObjectQueryParam(message.getTenantBaseUrl(), tenantUrl.getGetEmployeebyIds(), tenantUrl.getIds(), ids, ApiResponse.class);
        } catch (Exception e) {
            return null;
        }
        return apiGetResponse.getValue();
    }

    @Override
    public Object getRecuriterIdByBranch(int branchId) {
        ApiResponse apiResponse = new ApiResponse();
        ApiResponse apiGetResponse = new ApiResponse();
        Map<String, String> errors = new HashMap<>();
        List<Integer> resp = new ArrayList<>();
        MultiValueMap queryParams = new LinkedMultiValueMap();
        queryParams.add(tenantUrl.getRole(), tenantUrl.getRecruiter());

        try {
            apiGetResponse = client.performGetRequestWithQueryParams(tenantUrl.getTenantServiceUrl(), tenantUrl.getGetIdByRole() + "/" + branchId, queryParams, ApiResponse.class);
        } catch (Exception e) {
            errors.put(message.getMessage(), message.getServerError());
            apiResponse.setStatusCode(code.getServerError());
            apiResponse.setErrors(errors);
        }
        return apiGetResponse.getValue();
    }

    @Override
    public ApiResponse viewRecuriterByBranchId(int branchId) {
        ApiResponse apiResponse = new ApiResponse();
        ApiResponse apiGetResponse = null;
        Map<String, String> errors = new HashMap<>();
        MultiValueMap queryParams = new LinkedMultiValueMap();
        queryParams.add(tenantUrl.getRole(), tenantUrl.getRecruiter());
        try {
            apiGetResponse = client.performGetRequestWithQueryParams(tenantUrl.getTenantServiceUrl(), tenantUrl.getGetEmpByRole() + "/" + branchId, queryParams, ApiResponse.class);

        } catch (Exception e) {
            errors.put(message.getMessage(), message.getServerError());
            apiResponse.setStatusCode(code.getServerError());
            apiResponse.setErrors(errors);
            return apiResponse;
        }
        apiResponse.setStatusCode(code.getSuccess());
        apiResponse.setValue(apiGetResponse.getValue());
        return apiResponse;
    }

    @Override
    public ApiResponse viewInterviewerByBranchId(int branchId) {
        String recruiterPath = tenantUrl.getGetEmpByRole() + "/" + branchId;
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("role", "Interviewer");
        ApiResponse apiResponse = new ApiResponse();
        ApiResponse apiGetResponse = null;
        Map<String, String> errors = new HashMap<>();
        try {
            apiGetResponse = client.performGetRequestWithQueryParams(tenantUrl.getTenantServiceUrl(), recruiterPath, queryParams, ApiResponse.class);
            apiResponse.setStatusCode(code.getSuccess());
            apiResponse.setValue(apiGetResponse.getValue());
            return apiResponse;
        } catch (Exception e) {
            errors.put(message.getErrorKey(), message.getServerError());
            apiResponse.setStatusCode(code.getServerError());
            apiResponse.setErrors(errors);
            return apiResponse;
        }

    }

    @Override
    public ApiResponse viewInterviewerById(int id) {
        ApiResponse apiResponse = new ApiResponse();
        ApiResponse apiGetResponse = null;
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(tenantUrl.getRole(), tenantUrl.getInterviewer());
        Map<String, String> errors = new HashMap<>();
        try {

            apiGetResponse = client.performGetRequestWithQueryParams(tenantUrl.getTenantServiceUrl(), tenantUrl.getViewOneEmployee() + "/" + id, queryParams, ApiResponse.class);
            apiResponse.setStatusCode("1500");
            apiResponse.setValue(apiGetResponse.getValue());
            return apiResponse;
        } catch (Exception e) {
            errors.put("message", "Connection Failure.");
            apiResponse.setStatusCode("1540");
            apiResponse.setErrors(errors);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse viewInterviewer(int id) {
        ApiResponse apiResponse = new ApiResponse();
        ApiResponse apiGetResponse = null;
        Map<String, String> errors = new HashMap<>();
        try {
            apiGetResponse = client.performGetRequestWithOutQueryParams(tenantUrl.getTenantServiceUrl(), tenantUrl.getViewOneEmployee() + "/" + id, ApiResponse.class);
            apiResponse.setStatusCode("1500");
            apiResponse.setValue(apiGetResponse.getValue());
            return apiResponse;
        } catch (Exception e) {
            errors.put("message", "Connection Failure.");
            apiResponse.setStatusCode("1540");
            apiResponse.setErrors(errors);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse getRecruiterJobCount(int id) {
        ApiResponse apiResponse = new ApiResponse();
        Map<String, String> errors = new HashMap<>();
        try {
            int total = 0;
            int hiredcount = 0;
            List<Integer> jobRecruiterId = jobReqRep.findByEmployeeId(id);
            List<Integer> recruiterAssignedCounts = jobReqRep.findByEmployeeTaskCount(id);
            if (recruiterAssignedCounts.isEmpty()) {
                total = 0;
            } else {
                for (Integer assignedCount : recruiterAssignedCounts) {
                    total = total + assignedCount;
                }
                for (Integer jobRecId : jobRecruiterId) {
                    List<Integer> rejectedCount = applicantDetailsRepository.findByJobRecruiterId(jobRecId);
                    hiredcount = hiredcount + rejectedCount.get(0);
                }
                total = total - hiredcount;
            }
            apiResponse.setStatusCode(code.getSuccess());
            apiResponse.setValue(total);
            return apiResponse;
        } catch (Exception ex) {
            apiResponse.setStatusCode(code.getServerError());
            apiResponse.setErrors(errors);
            return apiResponse;
        }
    }

}
