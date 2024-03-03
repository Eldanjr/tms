//package com.bassure.applicantserviceTest;
//
//import com.bassure.applicantservice.model.Skill;
//import com.bassure.applicantservice.repo.SkillRepository;
//import com.bassure.applicantservice.response.ApiResponse;
//import com.bassure.applicantservice.response.ResponseCode;
//import com.bassure.applicantservice.response.ResponseMessage;
//import com.bassure.applicantservice.service.SkillService;
//import com.bassure.applicantservice.serviceimplementation.SkillServiceImplementation;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class SkillServiceTest {
//    @InjectMocks
//    private SkillServiceImplementation skillService;
//
//    @Mock
//    private ResponseMessage message;
//    @Mock
//    private ResponseCode code;
//    @Mock
//    private SkillRepository skillRepository;
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetSkillsWhenSkillsExist() {
//        List<Skill> mockSkills = Collections.singletonList(new Skill("Java"));
//
//        when(skillRepository.findAll()).thenReturn(mockSkills);
//        when(code.getSuccess()).thenReturn("2200");
//        when(message.getErrorKey()).thenReturn("errors");
//        when(message.getError()).thenReturn("failed");
//
//        ApiResponse response = skillService.getSkills();
//
//        Mockito.verify(skillRepository, times(1)).findAll();
//
//        assertEquals(code.getSuccess(), response.getStatusCode());
//        assertEquals(mockSkills, response.getValue());
//        assertEquals(null, response.getErrors());
//    }
//
//    @Test
//    void testGetSkillsNegative() {
//        List<Skill> skills = new ArrayList<>();
//
//        when(skillRepository.findAll()).thenReturn(skills);
//        when(code.getFailed()).thenReturn("Failed");
//        when(message.getErrorKey()).thenReturn("errorKey");
//        when(message.getError()).thenReturn("Error message");
//
//        ApiResponse response = skillService.getSkills();
//
//        assertEquals("Failed", response.getStatusCode());
//        assertEquals(null, response.getValue());
//        assertEquals(Map.of("errorKey", "Error message"), response.getErrors());
//    }
//}
