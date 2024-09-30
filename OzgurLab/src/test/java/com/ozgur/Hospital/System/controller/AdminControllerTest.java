package com.ozgur.Hospital.System.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozgur.Hospital.System.model.LaborantRequest;
import com.ozgur.Hospital.System.model.Role;
import com.ozgur.Hospital.System.repository.AdminRepository;
import com.ozgur.Hospital.System.service.AdminService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(AdminController.class)
@ContextConfiguration(classes = {AdminController.class})
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;



    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldGetAllLaborantRequests() throws Exception {

        LaborantRequest laborantRequest1 = LaborantRequest.builder()
                .id(1L)
                .name("Test")
                .surname("Test")
                .username("Test")
                .password("Test")
                .department("Test")
                .build();
        LaborantRequest laborantRequest2 = LaborantRequest.builder()
                .id(2L)
                .name("Test")
                .surname("Test")
                .username("Test")
                .password("Test")
                .department("Test")
                .build();
        List<LaborantRequest> laborantRequestList = new ArrayList<>(List.of(laborantRequest1,laborantRequest2));

        BDDMockito.given(adminService.getAllLaborantRequests()).willReturn(laborantRequestList);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/v1/admin/laborantRequests")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(laborantRequestList))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L));
    }

    @AfterEach
    void tearDown() {
        
    }
}
