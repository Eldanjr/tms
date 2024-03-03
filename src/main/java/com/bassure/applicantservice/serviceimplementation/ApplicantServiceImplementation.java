package com.bassure.applicantservice.serviceimplementation;

import com.bassure.applicantservice.model.*;
import com.bassure.applicantservice.model.applicantModel.*;
import com.bassure.applicantservice.repo.*;
import com.bassure.applicantservice.request.ApplicantAssociate;
import com.bassure.applicantservice.request.ApplicantDetailUpdateRequest;
import com.bassure.applicantservice.request.ApplicantDetailsRequest;
import com.bassure.applicantservice.request.ApplicantRequest;
import com.bassure.applicantservice.response.*;
import com.bassure.applicantservice.service.ApplicantService;
import com.bassure.applicantservice.util.ServiceCall;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ApplicantServiceImplementation implements ApplicantService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicantServiceImplementation.class);

    @Autowired
    private ResponseCode code;

    @Autowired
    private ResponseMessage message;

    @Autowired
    ApplicantDetailsRepository applicantDetailsRepository;

    @Autowired
    ApplicantRepository applicantRepository;

    @Autowired
    ApplicantAddressRepository applicantAddresRepo;

    @Autowired
    ApplicantEducationalRepo applicantEduRep;

    @Autowired
    ApplicantExperienceRepo applicantExRepo;

    @Autowired
    ApplicantAddressRepository applicantAddress;

    @Autowired
    SkillRepository skillRepo;

    @Autowired
    OtpRepository otpRepository;

    @Autowired
    ServiceCall serviceCall;

    @Autowired
    JobRecruiterRepository jobReqRep;

    @Autowired
    ApplicantResponeRepository appRespRep;

    @Autowired
    JobPostingRepository jobResRep;

    @Autowired
    InterviewScheduleRepository interviewScheduleRepo;

    @Autowired
    ApplicantHistoryRepository applicantHistoryRepo;

    WebClient client = WebClient.create("http://localhost:8083/Application-tracking-system/email/api");

    @Override
    public ApiResponse viewAllApplicant(Pageable pageable) {
        try {
//            List<ApplicantDetails> applicantdetails = applicantDetailsRepository.findAll();
            List<ApplicantResponsesResponse> appResList = new ArrayList<>();
            List<Applicant> applicants = applicantRepository.findActiveApplicant1(pageable);
//            applicants.forEach(i -> System.out.println(i.getApplicantId()));
            for (Applicant app : applicants) {
                ApplicantResponsesResponse appRes = new ApplicantResponsesResponse();
//                BeanUtils.copyProperties(applicants, app);
                BeanUtils.copyProperties(app, appRes);
                ApplicantResponse apRs = appRespRep.findApplicantResByAppId(app.getApplicantId());
                appRes.setJobTitle(apRs.getJobPostingId().getTitle());
                ApplicantDetails appDetails = applicantDetailsRepository.findByApplicantResponseId(apRs.getApplicantResponseId());
                if (appDetails == null) {
                    appResList.add(appRes);
                } else {
                    appRes.setApplicantdetailsId(appDetails.getApplicantDetailsId());
                    WebClient webClient = WebClient.create();
                    ApiResponse apigetResp = webClient.get().uri(message.getTenantBaseUrl() + "/view-single-employee/" + appDetails.getAddedBy()).retrieve().bodyToMono(ApiResponse.class).block();
                    EmployeeRespone emp = new EmployeeRespone();
                    Object employee = (Object) apigetResp.getValue();
                    if (employee == null) {
                        appRes.setJobRecruiterName(null);
                        appResList.add(appRes);
                    } else {
                        ObjectMapper mapper = new ObjectMapper();
                        Map<String, Object> employeeMapped = mapper.convertValue(employee, Map.class);
                        emp.setEmployeeId((int) employeeMapped.get("employeeId"));
                        emp.setFirstName((String) employeeMapped.get("firstName"));
                        appRes.setJobRecruiterName(emp.getFirstName());
//                        appRes.setRecruiterId(appDetails.getJobRecruiterId().getId());
                        appResList.add(appRes);
                    }
                }
            }
            return new ApiResponse("2200", appResList, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ApiResponse("2254", null, Map.of(message.getServerError(), ex.getLocalizedMessage()));
        }
    }

    @Override
    public ApiResponse addApplicantDetails(ApplicantDetailsRequest applicant) {
        try {
            List<Skill> applicantSkill = new ArrayList<>();
//            if (applicant.getContactNo1() == null) {
            for (int i = 0; i < applicant.getSkill().size(); i++) {
                applicantSkill.add(skillRepo.findById(applicant.getSkill().get(i)).get());
            }
            Applicant appmob = applicantRepository.findByContactNo(applicant.getContactNo());
            appmob.setApplicantFlag(Boolean.TRUE);
            applicantRepository.save(appmob);
            ApplicantResponse apRespId = appRespRep.findByApplicantId(appmob);
            ApplicantDetails temp = new ApplicantDetails();
            BeanUtils.copyProperties(applicant, temp);
            temp.setSkill(applicantSkill);
            JobRecruiter jobRecruiter = jobReqRep.findByJobPostingIdAndRecruiterId(apRespId.getJobPostingId().getId(), applicant.getAddedBy());
//                temp.setApplicantId(appmob);
            temp.setApplicantResponseId(apRespId);
            temp.setJobRecruiterId(jobRecruiter);
            if (applicant.getCurrentAddress().getState().isEmpty() || applicant.getCurrentAddress().getPinCode().isEmpty()) {
                temp.setCurrentAddress(null);
            } else {
                temp.setCurrentAddress(applicant.getCurrentAddress());
            }
            ApplicantDetails appAdd = applicantDetailsRepository.save(temp);
            ApplicantHistory applicantHistory = new ApplicantHistory();
            applicantHistory.setApplicantDetailsId(appAdd);
            applicantHistory.setDescription("Applicant newly added");
            applicantHistory.setCreatedBy(appAdd.getAddedBy());
            applicantHistory.setDate(Timestamp.from(Instant.now()));
            applicantHistoryRepo.save(applicantHistory);
            if (applicant.getApplicantEducationalDetailses().get(0).getInstituteOrSchoolName().isEmpty() != true) {
                for (ApplicantEducationalDetails aed : applicant.getApplicantEducationalDetailses()) {
                    aed.setApplicantId(appAdd);
                    applicantEduRep.save(aed);
                }
            }
            if (applicant.getApplicantExperienceDetailses().get(0).getOccupationName().isEmpty() != true) {
                for (ApplicantExperienceDetails aep : applicant.getApplicantExperienceDetailses()) {
                    aep.setApplicantId(appAdd);
                    applicantExRepo.save(aep);
                }
            }
            return new ApiResponse("2200", "Employee added successfully", null);
//            }
//            else {
//                WebClient webClient = WebClient.create();
//                ApiResponse apigetResp = webClient.get().uri(message.getTenantBaseUrl() + "/view-single-employee-mobileno?num=" + applicant.getContactNo1()).retrieve().bodyToMono(ApiResponse.class).block();
//                EmployeeRespone emp = new EmployeeRespone();
//                Object employee = (Object) apigetResp.getValue();
//                if (employee == null) {
//                    emp.setEmployeeId(0);
//                    Applicant appmob = applicantRepository.findByContactNo(applicant.getContactNo());
//                    appmob.setApplicantFlag(Boolean.TRUE);
//                    applicantRepository.save(appmob);
//                    ApplicantResponse apRespId = appRespRep.findByApplicantId(appmob);
//                    ApplicantDetails temp = new ApplicantDetails();
//                    BeanUtils.copyProperties(applicant, temp);
//                    temp.setReferenceBy("o");
//                    for (int i = 0; i < applicant.getSkill().size(); i++) {
//                        applicantSkill.add(skillRepo.findById(applicant.getSkill().get(i)).get());
//                    }
//                    temp.setSkill(applicantSkill);
////                    temp.setApplicantId(appmob);
//                    JobRecruiter jobRecruiter = jobReqRep.findByJobPostingIdAndRecruiterId(apRespId.getJobPostingId().getId(), applicant.getAddedBy());
//                    temp.setApplicantResponseId(apRespId);f
//                    temp.setJobRecruiterId(jobRecruiter);
//                    ApplicantDetails appAdd = applicantDetailsRepository.save(temp);
//                    ApplicantHistory applicantHistory = new ApplicantHistory();
//                    applicantHistory.setApplicantDetailsId(appAdd);
//                    applicantHistory.setDescription("Applicant newly added");
//                    applicantHistory.setCreatedBy(appAdd.getAddedBy());
//                    applicantHistory.setDate(Timestamp.from(Instant.now()));
//                    applicantHistoryRepo.save(applicantHistory);
//                    for (ApplicantEducationalDetails aed : applicant.getApplicantEducationalDetailses()) {
//                        aed.setApplicantId(appAdd);
//                        applicantEduRep.save(aed);
//                    }
//                    if (applicant.getApplicantExperienceDetailses().get(0).getOccupationName().isEmpty() != true) {
//
//                        for (ApplicantExperienceDetails aep : applicant.getApplicantExperienceDetailses()) {
//                            aep.setApplicantId(appAdd);
//                            applicantExRepo.save(aep);
//                        }
//                    }
//                } else {
//                    ObjectMapper mapper = new ObjectMapper();
//                    Map<String, Object> employeeMapped = mapper.convertValue(employee, Map.class);
//                    emp.setEmployeeId((int) employeeMapped.get("employeeId"));
//                    Applicant appmob = applicantRepository.findByContactNo(applicant.getContactNo());
//                    appmob.setApplicantFlag(Boolean.TRUE);
//                    applicantRepository.save(appmob);
//                    ApplicantResponse apRespId = appRespRep.findByApplicantId(appmob);
//                    ApplicantDetails temp = new ApplicantDetails();
//                    BeanUtils.copyProperties(applicant, temp);
//                    temp.setReferenceBy("emp");
//                    for (int i = 0; i < applicant.getSkill().size(); i++) {
//                        applicantSkill.add(skillRepo.findById(applicant.getSkill().get(i)).get());
//                    }
//                    temp.setSkill(applicantSkill);
////                    temp.setApplicantId(appmob);
//                    JobRecruiter jobRecruiter = jobReqRep.findByJobPostingIdAndRecruiterId(apRespId.getJobPostingId().getId(), applicant.getAddedBy());
//                    temp.setApplicantResponseId(apRespId);
//                    temp.setJobRecruiterId(jobRecruiter);
//                    ApplicantDetails appAdd = applicantDetailsRepository.save(temp);
//                    ApplicantHistory applicantHistory = new ApplicantHistory();
//                    applicantHistory.setApplicantDetailsId(appAdd);
//                    applicantHistory.setDescription("Applicant newly added");
//                    applicantHistory.setCreatedBy(appAdd.getAddedBy());
//                    applicantHistory.setDate(Timestamp.from(Instant.now()));
//                    applicantHistoryRepo.save(applicantHistory);
//                    for (ApplicantEducationalDetails aed : applicant.getApplicantEducationalDetailses()) {
//                        aed.setApplicantId(appAdd);
//                        applicantEduRep.save(aed);
//                    }
//                    if (applicant.getApplicantExperienceDetailses().get(0).getOccupationName().isEmpty() != true) {
//                        for (ApplicantExperienceDetails aep : applicant.getApplicantExperienceDetailses()) {
//                            aep.setApplicantId(appAdd);
//                            applicantExRepo.save(aep);
//                        }
//                    }
//                }
//                return new ApiResponse("2200", "Employee added successfully", null);
//            }
        } catch (Exception ex) {
            return new ApiResponse("2254", null, Map.of("err", ex.getLocalizedMessage()));
        }
    }

    @Override
    public ApiResponse updateApplicant(ApplicantDetailUpdateRequest applicant, int id) {
        try {
            ApplicantDetails appDetailsRes = applicantDetailsRepository.findById(id).get();
            BeanUtils.copyProperties(applicant, appDetailsRes);
//            appDetailsRes.setYearOfExperience(applicant.getYearOfExperience());
            ApplicantResponse apRs = appRespRep.findApplicantResByAppId(applicant.getApplicantId());
            appDetailsRes.setApplicantResponseId(apRs);
            List<Skill> applicantSkill = new ArrayList<>();
            for (int i = 0; i < applicant.getSkill().size(); i++) {
                applicantSkill.add(skillRepo.findById(applicant.getSkill().get(i)).get());
            }
            appDetailsRes.setSkill(applicantSkill);
            ApplicantDetails appAdd = applicantDetailsRepository.save(appDetailsRes);
            ApplicantHistory applicantHistory = new ApplicantHistory();
            applicantHistory.setApplicantDetailsId(appAdd);
            applicantHistory.setDescription("Applicant details edited");
            applicantHistory.setCreatedBy(appAdd.getAddedBy());
            applicantHistory.setDate(Timestamp.from(Instant.now()));
            applicantHistoryRepo.save(applicantHistory);
            return new ApiResponse("2200", "Applicant updated successfully", null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ApiResponse("2254", null, Map.of("err", ex.getLocalizedMessage()));
        }
    }

    @Override
    public ApiResponse viewSingleApplicant(int id) {
        try {
            ApplicantDetailsResponse applicantsDetails = new ApplicantDetailsResponse();
            Applicant applicants = applicantRepository.findById(id).get();
            ApplicantResponse apRs = appRespRep.findApplicantResByAppId(applicants.getApplicantId());
            ApplicantDetails appDetails = applicantDetailsRepository.findByApplicantResponseId(apRs.getApplicantResponseId());
            BeanUtils.copyProperties(applicants, applicantsDetails);
            BeanUtils.copyProperties(appDetails, applicantsDetails);
            return new ApiResponse("2200", applicantsDetails, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ApiResponse("2254", null, Map.of(message.getErrorKey(), ex.getLocalizedMessage()));

        }
    }

    @Override
    public ApiResponse applicantStatusChange(int id, String status) {
        try {
            Optional<Applicant> tem = applicantRepository.findById(id);
            if (tem == null) {
                return new ApiResponse(code.getFailed(), null, Map.of("err", message.getAlreadyExists()));
            } else {
                switch (status) {
                    case "NEW":
                        tem.get().setApplicantStatus(ApplicantStatus.NEW);
                        break;
                    case "OFFERACCEPTED":
                        tem.get().setApplicantStatus(ApplicantStatus.OFFERACCEPTED);
                        break;
                    case "HIRED":
                        tem.get().setApplicantStatus(ApplicantStatus.HIRED);
                        break;
                    case "OFFERED":
                        tem.get().setApplicantStatus(ApplicantStatus.OFFERED);
                        break;
                    case "OFFERREJECTED":
                        tem.get().setApplicantStatus(ApplicantStatus.OFFERREJECTED);
                        break;
                    case "INPROCESS":
                        tem.get().setApplicantStatus(ApplicantStatus.INPROCESS);
                        break;
                    case "REJECTED":
                        tem.get().setApplicantStatus(ApplicantStatus.REJECTED);
                        break;
                    case "MOVETONEXTROUND":
                        tem.get().setApplicantStatus(ApplicantStatus.MOVETONEXTROUND);
                        break;
                    default:
                        tem.get().setApplicantStatus(ApplicantStatus.DECLINED);
                        break;
                }
                applicantRepository.save(tem.get());

                return new ApiResponse("2200", "employee status changed successfully", null);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ApiResponse("2254", null, Map.of(message.getServerError(), ex.getLocalizedMessage()));
        }

    }

    @Transactional
    public ApiResponse sendLink(Applicant info) {
        String path = "/Send-email";
        String link = message.getOtpUrl() + info.getEmail();
        String subject = "Regarding form Fill up";
        OtpInfo otpInfo = new OtpInfo();

        try {
            String otp = generateOTP();
            otpInfo.setApplicant(info);
            otpInfo.setOtp(otp);
            otpInfo = otpRepository.save(otpInfo);
            String body = "Hii " + info.getFirstName() + ",\n \n Please Fill in the form from the below link \n \n" + link + "\n \n  The otp for you is" + otpInfo.getOtp();
            logger.info(body);
            logger.info(subject);
            logger.info(String.valueOf(info.getCreatedBy()));
            MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
            queryParams.add("to", info.getEmail());
            queryParams.add("createdBy", String.valueOf(info.getCreatedBy()));
            queryParams.add("subject", subject);
            queryParams.add("body", body);
            final OtpInfo OTP = otpInfo;
            client.post().uri(uriBuilder -> uriBuilder.path(path)
                    .queryParams(queryParams)
                    .build())
                    .retrieve().bodyToMono(ApiResponse.class)
                    .onErrorResume(ex -> {
                        otpRepository.delete(OTP);
                        return Mono.just(new ApiResponse(code.getServerError(), null, Map.of(message.getErrorKey(), "Sorry Mail Not Sent....")));

                    }).timeout(Duration.ofSeconds(12))
                    .flatMap(value -> {
                        return Mono.just(new ApiResponse(code.getSuccess(), value, null));
                    }).block();

        } catch (Exception ex) {
            otpRepository.delete(otpInfo);
            ex.printStackTrace();
            return new ApiResponse(code.getServerError(), null, Map.of(message.getErrorKey(), "Sorry Mail Not Sent...."));
        }
        return null;
    }

    @Override
    public ApiResponse verifyApplicant(String email, String token) {
        logger.info(email, "email {}");
        logger.info(token, "otp {}");
        logger.info(code.getNotFound(), "{}");
        OtpInfo otp = otpRepository.findByOtp(token);
        if (otp == null) {
            logger.info("otp is not in db");
            return new ApiResponse("2125", null, Map.of(message.getErrorKey(), message.getNoDataFound()));
        } else {
            if (otp.getOtp().equals(token) && otp.getApplicant().getEmail().equalsIgnoreCase(email)) {
                logger.info("otp is correct");
                return new ApiResponse(code.getSuccess(), "otp verified successfully...!", null);
            }
            logger.info("otp is not correct");
            return new ApiResponse(code.getNotValid(), null, Map.of(message.getErrorKey(), "Enter a valid OTP...!,"));
        }

    }

    private String generateOTP() {
        int otpLength = 6;
        Random random = new Random();

        StringBuilder otpBuilder = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            otpBuilder.append(random.nextInt(10));
        }
        return otpBuilder.toString();
    }

    @Override
    public ApiResponse applicantStatusCount(int id) {
//        ApplicantStatusCount ac = new ApplicantStatusCount();
//        ac.setAccepted(applicantDetailsRepository.findAcceptedSts(id));
//        ac.setHired(applicantDetailsRepository.findHiredSts(id));
//        ac.setMovedToNextRound(applicantDetailsRepository.findMovedToNextRoundSts(id));
//        ac.setDeclined(applicantDetailsRepository.findDeclinedSts(id));
//        ac.setJoined(applicantDetailsRepository.findJoinedSts(id));
//        ac.setOfferMade(applicantDetailsRepository.findOfferMadeSts(id));
        return null;
    }

    @Override
    @Transactional
    public ApiResponse addApplicant(ApplicantRequest applicantRequest, boolean email) {
        Applicant appmob = applicantRepository.findByContactNo(applicantRequest.getContactNo());
        Applicant appEmail = applicantRepository.findByEmail(applicantRequest.getEmail());
        try {
            if (!email) {
                if (appmob != null) {
                    return new ApiResponse("2256", null, Map.of("err", "applicantAlreadyExist"));
                } else {
                    if (appEmail != null) {
                        return new ApiResponse("2256", null, Map.of("err", "applicantAlreadyExist"));
                    } else {
                        Applicant app = new Applicant();
                        BeanUtils.copyProperties(applicantRequest, app);
                        Applicant appId = applicantRepository.save(app);
                        JobPosting jp = jobResRep.findById(applicantRequest.getJobPostingId()).get();
                        ApplicantResponse apres = new ApplicantResponse();
                        apres.setApplicantId(appId);
                        apres.setInterested(applicantRequest.getInterested());
                        if (applicantRequest.getInterested() == true) {
                            apres.setApplicantResponseStatus(ApplicantResponseStatus.NEW);
                            apres.setJobPostingId(jp);
                            appRespRep.save(apres);
                            return new ApiResponse("2200", app, null);
                        } else {
                            apres.setApplicantResponseStatus(ApplicantResponseStatus.DECLINED);
                            apres.setJobPostingId(jp);
                            appRespRep.save(apres);
                            return new ApiResponse("2200", app, null);
                        }
                    }
                }
            } else {

                if (appEmail != null) {
                    System.out.println("Called email....");
                    return new ApiResponse("2256", null, Map.of(message.getErrorKey(), "applicantAlreadyExist"));
                } else {
                    Applicant app = new Applicant();
                    BeanUtils.copyProperties(applicantRequest, app);
                    JobPosting jp = jobResRep.findById(applicantRequest.getJobPostingId()).get();
                    Applicant appId = applicantRepository.save(app);

                    try {
                        ApplicantResponse apres = new ApplicantResponse();
                        apres.setApplicantId(appId);
                        apres.setInterested(applicantRequest.getInterested());
                        apres.setApplicantResponseStatus(ApplicantResponseStatus.NEW);
                        apres.setJobPostingId(jp);
                        ApiResponse mailResponse = sendLink(appId);
                        appRespRep.save(apres);

                        if (mailResponse.getStatusCode().equals(code.getSuccess())) {
                            System.out.println(mailResponse + "...Mail");
                            System.out.println("in success block....");
                            return new ApiResponse(code.getSuccess(), app, null);
                        } else {
                            System.out.println("in else block");
                            applicantRepository.delete(appId);
                            return new ApiResponse(code.getServerError(), null, Map.of(message.getErrorKey(), message.getServerError()));
                        }
                    } catch (Exception ex) {
                        applicantRepository.delete(appId);
                        return new ApiResponse(code.getFailed(), null, Map.of(message.getErrorKey(), message.getError()));
                    }

                }
            }

        } catch (Exception ex) {
            return new ApiResponse("2254", null, Map.of(message.getErrorKey(), ex.getLocalizedMessage()));
        }
    }

    @Override
    public ApiResponse updateApplicantBasic(ApplicantRequest applicant, int id) {
        try {
            Applicant temp = new Applicant();
            BeanUtils.copyProperties(applicant, temp);
            temp.setApplicantId(id);
            applicantRepository.save(temp);
            return new ApiResponse("2200", "Applicant updated successfully", null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ApiResponse("2254", null, Map.of("err", ex.getLocalizedMessage()));
        }
    }

    @Override
    public ApiResponse findRecruiterJob(int id) {
        logger.info("{}", id);
        List<Map<Integer, String>> jobsforRecruiter = jobReqRep.findByrecruiterJob(id);
        if (!jobsforRecruiter.isEmpty()) {
            return new ApiResponse(code.getSuccess(), jobsforRecruiter, null);
        }
        return new ApiResponse(code.getNotFound(), null, Map.of("errors", "No jobs for this recruiter"));
    }

    @Override
    public ApiResponse findApplicantByStatus(String sts) {
        try {
            List<Applicant> applicant = applicantRepository.findApplicantByStatus(sts);
            List<ApplicantResponsesResponse> applicantResp = new ArrayList<>();
            for (Applicant app : applicant) {
                ApplicantResponsesResponse applicantRes = new ApplicantResponsesResponse();
                ApplicantResponse applicantResponse = appRespRep.findApplicantResByAppId(app.getApplicantId());
                applicantRes.setJobTitle(applicantResponse.getJobPostingId().getTitle());
                BeanUtils.copyProperties(app, applicantRes);
                ApplicantDetails applicantDetails = applicantDetailsRepository.findByApplicantResponseId(applicantResponse.getApplicantResponseId());
                if (applicantDetails == null) {
                    applicantRes.setJobRecruiterName(null);
                    applicantResp.add(applicantRes);
                } else {
                    WebClient webClient = WebClient.create();
                    ApiResponse apigetResp = webClient.get().uri(message.getTenantBaseUrl() + "/view-single-employee/" + applicantDetails.getAddedBy()).retrieve().bodyToMono(ApiResponse.class).block();
                    EmployeeRespone emp = new EmployeeRespone();
                    Object employee = (Object) apigetResp.getValue();
                    if (employee == null) {
                        applicantRes.setJobRecruiterName(null);
                        applicantResp.add(applicantRes);
                    } else {
                        applicantRes.setApplicantdetailsId(applicantDetails.getApplicantDetailsId());
                        ObjectMapper mapper = new ObjectMapper();
                        Map<String, Object> employeeMapped = mapper.convertValue(employee, Map.class);
                        emp.setEmployeeId((int) employeeMapped.get("employeeId"));
                        emp.setFirstName((String) employeeMapped.get("firstName"));
                        applicantRes.setJobRecruiterName(emp.getFirstName());
                        applicantResp.add(applicantRes);
                    }

                }
            }
            return new ApiResponse("2200", applicantResp, null);
        } catch (Exception ex) {
            return new ApiResponse(code.getNotFound(), null, Map.of("errors", "No applicant details are available"));
        }
    }

    @Override
    public ApiResponse findApplicantByMobile(String mobile) {
        try {
            Applicant applicant = applicantRepository.findByContactNo(mobile);
            ApplicantResponse applicantResponse = appRespRep.findApplicantResByAppId(applicant.getApplicantId());
            ApplicantResponsesResponse applicantResp = new ApplicantResponsesResponse();
            applicantResp.setJobTitle(applicantResponse.getJobPostingId().getTitle());
            BeanUtils.copyProperties(applicant, applicantResp);
            if (applicantResponse != null) {
                applicantResp.setJobTitle(applicantResponse.getJobPostingId().getTitle());
                ApplicantDetails appDetails = applicantDetailsRepository.findByApplicantResponseId(applicantResponse.getApplicantResponseId());
                if (appDetails == null) {
                    applicantResp.setJobRecruiterName(null);
                } else {
                    WebClient webClient = WebClient.create();
                    ApiResponse apigetResp = webClient.get().uri(message.getTenantBaseUrl() + "/view-single-employee/" + appDetails.getAddedBy()).retrieve().bodyToMono(ApiResponse.class).block();
                    EmployeeRespone emp = new EmployeeRespone();
                    Object employee = (Object) apigetResp.getValue();
                    if (employee == null) {
                        applicantResp.setJobRecruiterName(null);
                    } else {
                        applicantResp.setApplicantdetailsId(appDetails.getApplicantDetailsId());
                        ObjectMapper mapper = new ObjectMapper();
                        Map<String, Object> employeeMapped = mapper.convertValue(employee, Map.class);
                        emp.setEmployeeId((int) employeeMapped.get("employeeId"));
                        emp.setFirstName((String) employeeMapped.get("firstName"));
                        applicantResp.setJobRecruiterName(emp.getFirstName());
                    }
                }
            }
            List<ApplicantResponsesResponse> listApplicant = List.of(applicantResp);
            return new ApiResponse("2200", listApplicant, null);
        } catch (Exception ex) {
            return new ApiResponse(code.getNotFound(), null, Map.of("errors", "No applicant details are available"));
        }
    }

    @Override
    public ApiResponse findApplicantByJob(int job) {
        try {
            List<ApplicantResponsesResponse> appResList = new ArrayList<>();
            List<Applicant> applicants = applicantRepository.findActiveApplicant();
            for (Applicant app : applicants) {
                ApplicantResponsesResponse appRes = new ApplicantResponsesResponse();
                appRes.setApplicantId(app.getApplicantId());
                appRes.setFirstName(app.getFirstName());
                appRes.setMiddleName(app.getMiddleName());
                appRes.setLastName(app.getLastName());
                appRes.setEmail(app.getEmail());
                appRes.setContactNo(app.getContactNo());
                appRes.setApplicantFlag(app.getApplicantFlag());
                appRes.setApplicantStatus(app.getApplicantStatus());
                ApplicantResponse apRs = appRespRep.findApplicantResByAppJob(app.getApplicantId(), job);
                if (apRs != null) {
                    appRes.setJobTitle(apRs.getJobPostingId().getTitle());
                    ApplicantDetails appDetails = applicantDetailsRepository.findByApplicantResponseId(apRs.getApplicantResponseId());
                    if (appDetails == null) {
                        appResList.add(appRes);
                    } else {
                        WebClient webClient = WebClient.create();
                        ApiResponse apigetResp = webClient.get().uri(message.getTenantBaseUrl() + "/view-single-employee/" + appDetails.getAddedBy()).retrieve().bodyToMono(ApiResponse.class).block();
                        EmployeeRespone emp = new EmployeeRespone();
                        Object employee = (Object) apigetResp.getValue();
                        if (employee == null) {
                            appRes.setJobRecruiterName(null);
                            appResList.add(appRes);
                        } else {
                            appRes.setApplicantdetailsId(appDetails.getApplicantDetailsId());
                            ObjectMapper mapper = new ObjectMapper();
                            Map<String, Object> employeeMapped = mapper.convertValue(employee, Map.class);
                            emp.setEmployeeId((int) employeeMapped.get("employeeId"));
                            emp.setFirstName((String) employeeMapped.get("firstName"));
                            appRes.setJobRecruiterName(emp.getFirstName());
                            appResList.add(appRes);
                        }
                    }
                }
            }
            return new ApiResponse("2200", appResList, null);
        } catch (Exception ex) {
//            ex.printStackTrace();
            return new ApiResponse("2254", null, Map.of(message.getServerError(), ex.getLocalizedMessage()));
        }
    }

    @Override
    public ApiResponse findApplicantByJob1() {
        try {
            List<JobPosting> job = jobResRep.findAll();
            return new ApiResponse(code.getSuccess(), job, null);
        } catch (Exception ex) {
            return new ApiResponse(code.getNotFound(), null, Map.of("errors", "No jobs for this recruiter"));
        }
    }

    @Override
    public ApiResponse findApplicantByEmail(String email) {
        try {
            Applicant applicant = applicantRepository.findByEmail(email);
            List<Applicant> listApplicant = List.of(applicant);
            return new ApiResponse("2200", listApplicant, null);
        } catch (Exception ex) {
            return new ApiResponse(code.getNotFound(), null, Map.of("errors", "No applicant details are available"));
        }
    }

    @Override
    public ApiResponse viewSingleApplicants(int id) {
        try {
            Applicant applicant = applicantRepository.findById(id).get();
            return new ApiResponse("2200", applicant, null);
        } catch (Exception ex) {
            return new ApiResponse(code.getNotFound(), null, Map.of("errors", "No applicant details are available"));
        }
    }

    @Override
    public ApiResponse findApplicantBySkill(int id) {
        List<ApplicantResponsesResponse> appResList = new ArrayList<>();
        List<Object[]> applicantDetails = applicantDetailsRepository.findBySkillId(id);
        for (Object[] obj : applicantDetails) {
            ApplicantResponsesResponse appRes = new ApplicantResponsesResponse();
            appRes.setApplicantId((int) obj[0]);
            appRes.setFirstName((String) obj[1]);
            appRes.setMiddleName((String) obj[2]);
            appRes.setLastName((String) obj[3]);
            appRes.setContactNo((String) obj[4]);
            appRes.setEmail((String) obj[5]);
            String sts = ((String) obj[6]);
            appRes.setApplicantStatus(ApplicantStatus.valueOf(sts));
            boolean flag = (byte) obj[7] == 1 ? true : false;
            appRes.setApplicantFlag(flag);
            JobPosting jobTitle = jobResRep.findById((int) obj[8]).get();
            WebClient webClient = WebClient.create();
            ApiResponse apigetResp = webClient.get().uri(message.getTenantBaseUrl() + "/view-single-employee/" + ((int) obj[9])).retrieve().bodyToMono(ApiResponse.class).block();
            appRes.setApplicantdetailsId((int) obj[10]);
            EmployeeRespone emp = new EmployeeRespone();
            Object employee = (Object) apigetResp.getValue();
            if (employee == null) {
                appRes.setJobRecruiterName(null);
                Skill applicantSkill = skillRepo.findById(id).get();
                appRes.setSkillName(applicantSkill.getName());
                appResList.add(appRes);
            } else {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> employeeMapped = mapper.convertValue(employee, Map.class);
                emp.setEmployeeId((int) employeeMapped.get("employeeId"));
                emp.setFirstName((String) employeeMapped.get("firstName"));
                appRes.setJobRecruiterName(emp.getFirstName());
                appRes.setJobTitle(jobTitle.getTitle());
                Skill applicantSkill = skillRepo.findById(id).get();
                appRes.setSkillName(applicantSkill.getName());
                appResList.add(appRes);
            }
        }
        return new ApiResponse("2200", appResList, null);

    }

    @Override
    public ApiResponse findApplicantsCount() {
        ApplicantStatusCount applicantCount = new ApplicantStatusCount();
        applicantCount.setTotalcount(applicantRepository.findActiveApplicantsCount());
        return new ApiResponse("2200", applicantCount, null);
    }

    @Override
    public ApiResponse associate(ApplicantAssociate applicant) {
        try {
            Applicant appId = applicantRepository.findById(applicant.getApplicantId()).get();
            appId.setApplicantStatus(ApplicantStatus.ASSOCIATE);
            applicantRepository.save(appId);
            JobPosting jobId = jobResRep.findById(applicant.getJobId()).get();
            ApplicantResponse apres = new ApplicantResponse();
            apres.setApplicantId(appId);
            apres.setInterested(Boolean.TRUE);
            apres.setApplicantResponseStatus(ApplicantResponseStatus.NEW);
            apres.setJobPostingId(jobId);
            ApplicantResponse applicantRes = appRespRep.save(apres);
            List<Object[]> applicantDetails = applicantDetailsRepository.findByAppId(applicant.getApplicantDetailsId());
            ApplicantDetails applicantDetail = new ApplicantDetails();
            for (Object[] obj : applicantDetails) {
                ApplicantDetails appDetails = new ApplicantDetails();
                String gender = ((String) obj[0]);
                appDetails.setGender(Gender.valueOf(gender));
                appDetails.setExpectedCtc((String) obj[1]);
                appDetails.setDateOfBirth((Date) obj[2]);
                appDetails.setAddedBy((int) obj[3]);
                appDetails.setAddedAt((Timestamp) obj[4]);
                appDetails.setSource((String) obj[5]);
                appDetails.setReferenceBy((String) obj[6]);
                appDetails.setCurrentCtc((String) obj[7]);
                appDetails.setCurrentCompany((String) obj[8]);
                appDetails.setReasonForChange((String) obj[9]);
                boolean flag = (byte) obj[10] == 1 ? true : false;
                appDetails.setHoldingOffer(flag);
                String marital = ((String) obj[11]);
                appDetails.setMaritalStatus(MaritalStatus.valueOf(marital));
                ApplicantAddress adrs = applicantAddress.findById((int) obj[12]).get();
                appDetails.setCurrentAddress(adrs);
                appDetails.setResumePath((String) obj[13]);
                appDetails.setYearOfExperience((String) obj[14]);
                JobRecruiter jobRec = jobReqRep.findById((int) obj[15]).get();
                appDetails.setJobRecruiterId(jobRec);
//                ApplicantResponse applicantResponse = appRespRep.findById((int) obj[16]).get();
                appDetails.setApplicantResponseId(applicantRes);
                BeanUtils.copyProperties(appDetails, applicantDetail);
            }
            ApplicantDetails applicantDt = applicantDetailsRepository.save(applicantDetail);
            List<ApplicantEducationalDetails> applicantEducation = applicantEduRep.findByApplicantId(applicant.getApplicantDetailsId());
            for (int i = 0; i < applicantEducation.size(); i++) {
                ApplicantEducationalDetails appEducation = applicantEduRep.findById(applicantEducation.get(i).getApplicantEducationalDetailsId()).get();
                appEducation.setApplicantId(applicantDt);
                applicantEduRep.save(appEducation);
            }
            List<ApplicantExperienceDetails> applicantExperience = applicantExRepo.findByApplicantId(applicant.getApplicantDetailsId());
            for (int i = 0; i < applicantExperience.size(); i++) {
                ApplicantExperienceDetails appExp = applicantExRepo.findById(applicantExperience.get(i).getApplicantExpDetailsId()).get();
                appExp.setApplicantId(applicantDt);
                applicantExRepo.save(appExp);
            }
            ApplicantHistory applicantHistory = new ApplicantHistory();
            applicantHistory.setApplicantDetailsId(applicantDt);
            applicantHistory.setDescription("New job is assigned");
            applicantHistory.setCreatedBy(applicantDt.getAddedBy());
            applicantHistory.setDate(Timestamp.from(Instant.now()));
            applicantHistoryRepo.save(applicantHistory);
            return new ApiResponse("2200", "Applicant Associated Succesfully", null);
        } catch (Exception ex) {
            return new ApiResponse("2254", null, Map.of(message.getServerError(), ex.getLocalizedMessage()));
        }
    }

    @Override
    public ApiResponse applicantStat(int jobRecruiterId) {
        List<Object[]> applicantStatusfromJrId = applicantDetailsRepository.getApplicantStatusfromJrId(jobRecruiterId);
        List<ApplicantStatResponse> applicantStatResponseList = new ArrayList<>();
        for (Object[] applicant : applicantStatusfromJrId) {
            ApplicantStatResponse applicantStatResponse = new ApplicantStatResponse();
            applicantStatResponse.setFirstName((String) applicant[0]);
            applicantStatResponse.setLevel((String) applicant[1]);
            applicantStatResponse.setApplicantResponse((String) applicant[2]);
            applicantStatResponse.setResumePath((String) applicant[3]);
            applicantStatResponseList.add(applicantStatResponse);
        }
        if (applicantStatResponseList.isEmpty()) {
            return new ApiResponse(code.getNotFound(), null, Map.of(message.getErrorKey(), message.getNoDataFound()));
        } else {
            return new ApiResponse(code.getSuccess(), applicantStatResponseList, null);
        }
    }

}
