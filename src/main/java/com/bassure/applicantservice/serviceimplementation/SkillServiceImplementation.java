package com.bassure.applicantservice.serviceimplementation;

import com.bassure.applicantservice.model.Skill;
import com.bassure.applicantservice.repo.SkillRepository;
import com.bassure.applicantservice.request.SkillRequest;
import com.bassure.applicantservice.response.ApiResponse;
import com.bassure.applicantservice.response.ResponseCode;
import com.bassure.applicantservice.response.ResponseMessage;
import com.bassure.applicantservice.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SkillServiceImplementation implements SkillService {

    @Autowired
    private ResponseCode code;
    @Autowired
    private ResponseMessage message;
    @Autowired
    private SkillRepository skillRepository;

    @Override
    public ApiResponse getSkills() {
        try {
            List<Skill> listOfSkills = skillRepository.findAll();
            if (listOfSkills.isEmpty())
                return new ApiResponse(code.getFailed(), null, Map.of(message.getErrorKey(), message.getError()));
            return new ApiResponse(code.getSuccess(), listOfSkills, null);
        } catch (Exception exception) {
            return new ApiResponse(code.getServerError(), null, Map.of(message.getErrorKey(), exception.getMessage()));
        }
    }


}
