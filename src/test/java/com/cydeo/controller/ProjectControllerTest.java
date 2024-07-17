package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Gender;
import com.cydeo.enums.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mvc;

    static String token;

    static UserDTO manager;
    static ProjectDTO project;

    @BeforeAll
    static void setUp() {
        token = "Bearer " + "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJXM3FqNUxVUXBIbVN3U1B4UnMtb3kxYUtHTEUwMU1HU2VxMXlCVFhKbUNvIn0.eyJleHAiOjE3MjEyNjk2NzUsImlhdCI6MTcyMTI1MTY3NSwianRpIjoiZTJmNTU1NjctZjI0OC00MzViLWI1YzMtZjM5MzhjMzRjZjI5IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL2N5ZGVvLWRldiIsImF1ZCI6WyJ0cmFpbmluZyIsImFjY291bnQiXSwic3ViIjoiNjA2NTg3MzQtZjVmNS00YWMwLTlmZjQtYzVmNzViOWQyNGRlIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidGlja2V0aW5nLWFwcCIsInNlc3Npb25fc3RhdGUiOiJmYTU4NjE5ZC1kZWJiLTQwYWMtOTBlZC1jYmU0MjY3MmU0MzAiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6ODA4MSJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1jeWRlby1kZXYiXX0sInJlc291cmNlX2FjY2VzcyI6eyJ0aWNrZXRpbmctYXBwIjp7InJvbGVzIjpbIk1hbmFnZXIiXX0sInRyYWluaW5nIjp7InJvbGVzIjpbIkFETUlOIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwic2lkIjoiZmE1ODYxOWQtZGViYi00MGFjLTkwZWQtY2JlNDI2NzJlNDMwIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsInByZWZlcnJlZF91c2VybmFtZSI6Im96enkifQ.DwR17zr9oxsMlCvTSifaF9WhjHviAY-EESAAlKnPhQM3uoecp74ew-moNVi0Rf3lDrYhWFBnEOxtSOjlrnhlDPrdLdI9Ls1pGUxDgdWJikz4gjo2KE8ueyUnX1cDWJreOwbEHlrjNhIzMhVNnLhKo_75Y7EFCPXbDTCq7_Euua0aNDsKHORknxXz2QT0lYQZKWOqn3I-kZyEsOp5xWQySA4DnrxgIgE8Aji0TZVnE2YYlyzJcVClOEyxtY2Kilj6PqDglksDgmSe-0kc-rQcvDWz_ohe3aSddiVuZM5W6IzlFpEq3_Kub6EAQFYE1gB7NKOldy6sBlqfHzTt_QM1EA";
        manager = new UserDTO(2L,
                "",
                "",
                "ozzy",
                "abc1",
                "",
                true,
                "",
                new RoleDTO(2L, "Manager"), Gender.MALE);

        project = new ProjectDTO(
                "API Project",
                "PR001",
                manager,
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                "some details",
                Status.OPEN
        );

    }

    @Test
    void givenNoToken_getProjects() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/project"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenToken_getProjects() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/project")
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].projectCode").exists())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").exists())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").isNotEmpty())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").isString())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").value("ozzy"));
    }

    @Test
    void givenToken_createProject() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/project")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJsonString(project)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("project is created"));
    }

    @Test
    void givenToken_updateProject() throws Exception {
        project.setProjectName("API Project-2");

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/project")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJsonString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("project updated"));
    }

    @Test
    void givenToken_deleteProject() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/project/" + project.getProjectCode())
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("project is deleted"));
    }

    private String toJsonString(final Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);

    }


}